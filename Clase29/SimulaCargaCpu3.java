import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.Random;

public class SimulaCargaCpu3 {
    
    // Clase interna que implementa la logica de carga de CPU (del SimulaCargaCpu2)
    static class CargaCpuRunnable implements Runnable {
        private final int porcentajeCpu;
        private final int tiempoSegundos;
        
        public CargaCpuRunnable(int porcentajeCpu, int tiempoSegundos) {
            this.porcentajeCpu = porcentajeCpu;
            this.tiempoSegundos = tiempoSegundos;
        }

        @Override
        public void run() {
            //1 s = 1,000,000,000 ns
            final long CICLO_NS = 1_000_000; // 1 milisegundo en nanosegundos

            // tiempo total de ejecucion
            long tiempoFinTotal = System.currentTimeMillis() + (long)tiempoSegundos * 1000;
            Random ran = new Random();

            while (System.currentTimeMillis() < tiempoFinTotal) {
                // bucle que simula un ciclo de 1 segundo (1000 iteraciones de 1 ms)
                for (int i = 0; i < 1000; i++) {
                    
                    // determinar los tiempos de trabajo y descanso dentro de este 1ms
                    long trabajo_ns = (long)porcentajeCpu * 10_000; // porcentaje * 10,000 = nanosegundos de trabajo
                    
                    long inicio_ciclo = System.nanoTime();

                    // fase de trabajo (Consumir CPU)
                    long fin_trabajo = inicio_ciclo + trabajo_ns;
                    while (System.nanoTime() < fin_trabajo) {
                        // Carga de CPU
                        Math.sqrt(ran.nextInt(2147483647)); 
                    }

                    // fase de descanso (Liberar el CPU)
                    long tiempo_ejecutado_ns = System.nanoTime() - inicio_ciclo;
                    long tiempo_restante_ns = CICLO_NS - tiempo_ejecutado_ns;

                    if (tiempo_restante_ns > 0) {
                        // convertir nanosegundos restantes a milisegundos para sleep
                        long sleep_ms = tiempo_restante_ns / 1_000_000;
                        int sleep_ns = (int) (tiempo_restante_ns % 1_000_000);
                        
                        try {
                            // solo descansar si hay tiempo restante. 
                            if (sleep_ms > 0 || sleep_ns > 100000) { // Si queda un tiempo significativo
                                 Thread.sleep(sleep_ms, sleep_ns);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
        }
    }
    
    // Manejador HTTP para el endpoint /cpu
    static class CpuLoadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            /*Los parametros se pasan como Query Parameters: 
            /cpu?p= porcentaje
            &t= tiempo
            &n= nucleos */
            String query = exchange.getRequestURI().getQuery();
            
            // 
            if (query == null) {
                String response = "Error: Faltan parametros.";
                sendResponse(exchange, 400, response);
                return;
            }
            
            // Parsear los parametros p (porcentaje), t (tiempo), n (nucleos)
            int porcentajeCpu = Integer.parseInt(getQueryParam(query, "p"));
            int tiempoSegundos = Integer.parseInt(getQueryParam(query, "t"));
            int numNucleos = Integer.parseInt(getQueryParam(query, "n"));

            System.out.printf("Se va a simular un uso de CPU del %d%% durante %d seg usando %d nucleos.%n", 
                              porcentajeCpu, tiempoSegundos, numNucleos);

            // Crear y arrancar los hilos
            Thread[] hilos = new Thread[numNucleos];
            for (int i = 0; i < numNucleos; i++) {
                hilos[i] = new Thread(new CargaCpuRunnable(porcentajeCpu, tiempoSegundos));
                hilos[i].start();
            }
            
            // Responder inmediatamente al cliente (no bloqueante)
            String response = String.format("Carga de CPU iniciada: %d%% por %d segundos en %d nucleos.", 
                                   porcentajeCpu, tiempoSegundos, numNucleos);
            sendResponse(exchange, 200, response);
            
            // Los hilos continuan ejecutandose en segundo plano
            new Thread(() -> {
                try {
                    for (Thread hilo : hilos) {
                        hilo.join();
                    }
                    System.out.println("Simulacion finalizada.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
        
        // Funcion para extraer un parametro de la URL
        private String getQueryParam(String query, String paramName) {
            if (query == null) return null;
            for (String pair : query.split("&")) {
                String[] parts = pair.split("=");
                if (parts.length == 2 && parts[0].equalsIgnoreCase(paramName)) {
                    return parts[1];
                }
            }
            return null;
        }

        // Funcion para enviar la respuesta HTTP
        private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
            exchange.sendResponseHeaders(statusCode, message.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(message.getBytes());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int puerto = 8080; // Puerto del servidor
        HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);
        
        server.createContext("/cpu", new CpuLoadHandler()); 
        
        // pool de hilos para manejar multiples peticiones 
        server.setExecutor(Executors.newCachedThreadPool()); 
        
        server.start();
        System.out.println("Servidor HTTP iniciado en puerto " + puerto);
        System.out.println("Listo para recibir peticiones a /cpu?p=<porcentaje>&t=<tiempo>&n=<nucleos>");
    }
}