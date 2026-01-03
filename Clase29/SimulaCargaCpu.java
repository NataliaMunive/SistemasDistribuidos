import java.util.Random;

public class SimulaCargaCpu {

    public static void main(String[] args) throws InterruptedException {
        
        if (args.length != 2) {
            System.out.println("java SimulaCargaCpu <porcentaje_cpu> <tiempo_segundos>");
            return;
        }

        int porcentajeCpu = Integer.parseInt(args[0]);
        int tiempoSegundos = Integer.parseInt(args[1]);

        System.out.printf("Se va a simular un uso de CPU del %d%% durante %d seg.%n", porcentajeCpu, tiempoSegundos);

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
                long descanso_ns = CICLO_NS - trabajo_ns;      // nanosegundos de descanso
                
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
                    
                    // solo descansar si hay tiempo restante. 
                    if (sleep_ms > 0 || sleep_ns > 100000) { // Si queda un tiempo significativo
                         Thread.sleep(sleep_ms, sleep_ns);
                    }
                }
            }
        }
        System.out.println("Simulacion finalizada.");
    }
}