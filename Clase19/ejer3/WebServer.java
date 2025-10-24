/*
 *  MIT License
 *  Copyright (c) 2019 Michael Pogrebinsky
 */

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class WebServer {
    private static final String TASK_ENDPOINT = "/task";
    private static final String STATUS_ENDPOINT = "/status";

    private final int port;
    private HttpServer server;

    public static void main(String[] args) {
        int serverPort = 8080;                 // por defecto 8080
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]);
        }
        WebServer webServer = new WebServer(serverPort);
        webServer.startServer();
        System.out.println("Servidor escuchando en el puerto " + serverPort);
    }

    public WebServer(int port) { this.port = port; }

    public void startServer() {
        try {
            // Escucha en 0.0.0.0:port (todas las interfaces)
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        HttpContext statusContext = server.createContext(STATUS_ENDPOINT);
        HttpContext taskContext = server.createContext(TASK_ENDPOINT);

        statusContext.setHandler(this::handleStatusCheckRequest);
        taskContext.setHandler(this::handleTaskRequest);

        // 8 hilos para atender solicitudes (ajustable)
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    private void handleTaskRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendError(405, "Método no permitido", exchange);
            return;
        }

        Headers headers = exchange.getRequestHeaders();

        // Modo prueba opcional
        if ("true".equalsIgnoreCase(headers.getFirst("X-Test"))) {
            sendResponse("123\n".getBytes(StandardCharsets.UTF_8), exchange);
            return;
        }

        boolean isDebugMode = "true".equalsIgnoreCase(headers.getFirst("X-Debug"));

        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String body = new String(requestBytes, StandardCharsets.UTF_8).trim();

        long t0 = System.nanoTime();
        String response;
        try {
            response = processPayload(body);
        } catch (IllegalArgumentException ex) {
            sendError(400, "Payload inválido. Usa: N,CADENA (ej. 17576000,IPN). " + ex.getMessage(), exchange);
            return;
        }
        long t1 = System.nanoTime();
        long nanos = t1 - t0;

        if (isDebugMode) {
            // Para tu cliente: valor en nanosegundos
            exchange.getResponseHeaders().put("X-Debug", Arrays.asList(String.valueOf(nanos)));
            // Mensaje legible (compatibilidad)
            exchange.getResponseHeaders().put("X-Debug-Info",
                    Arrays.asList(String.format("La operación tomó %d nanosegundos", nanos)));
        }

        sendResponse((response + "\n").getBytes(StandardCharsets.UTF_8), exchange);
    }

    /**
     * Procesa el payload "N,CADENA" y simula recorrer N tokens
     * contando coincidencias exactas con CADENA sin almacenar los N tokens.
     */
    private String processPayload(String body) {
        String[] parts = body.split(",");
        if (parts.length != 2) throw new IllegalArgumentException("(faltan partes)");
        long N;
        try {
            N = Long.parseLong(parts[0].trim());
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("(N no es número)");
        }
        if (N < 0) throw new IllegalArgumentException("(N debe ser >= 0)");
        String needle = parts[1].trim();

        // Generador determinista de tokens para simular un stream largo.
        // Aquí colocamos la cadena 'needle' cada K posiciones (K=97 por defecto).
        final int K = 97; // ajusta la densidad de coincidencias si lo necesitas
        long matches = 0;
        for (long i = 0; i < N; i++) {
            // token sintetizado:
            String token = (i % K == 0) ? needle : "OTRO";
            if (token.equals(needle)) matches++;
        }

        return String.format("Cadena buscada: %s\nTokens procesados: %d\nCoincidencias: %d",
                needle, N, matches);
    }

    private void handleStatusCheckRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendError(405, "Método no permitido", exchange);
            return;
        }
        sendResponse("El servidor está vivo\n".getBytes(StandardCharsets.UTF_8), exchange);
    }

    private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
            os.flush();
        } finally {
            exchange.close();
        }
    }

    private void sendError(int code, String message, HttpExchange exchange) throws IOException {
        byte[] bytes = (message + "\n").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        } finally {
            exchange.close();
        }
    }
}
