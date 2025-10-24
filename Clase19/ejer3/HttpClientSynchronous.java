import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class HttpClientSynchronous {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        // Usa las IPs EXTERNAS actuales de tus 3 VMs
        List<String> urls = List.of(
            "http://136.113.168.213:8080/task", // servidor-1
            "http://35.188.69.6:8080/task",     // servidor-2
            "http://35.239.106.108:8080/task"   // servidor-3
        );

        // Payload que pide el profe
        final String payload = "17576000,IPN";

        try (PrintWriter csv = new PrintWriter("resultados.csv")) {
            csv.println("Servidor,TiempoServidor(ms),TiempoCliente(ms),HTTP");

            int idx = 1;
            for (String url : urls) {
                System.out.println("\n==> Enviando a: " + url);

                HttpRequest request = HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(payload))
                        .uri(URI.create(url))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot")
                        .setHeader("Content-Type", "text/plain;charset=UTF-8")
                        .setHeader("X-Debug", "true")   // pide tiempo del lado servidor
                        .build();

                long t0 = System.currentTimeMillis();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                long t1 = System.currentTimeMillis();

                HttpHeaders headers = response.headers();
                String xdebugNs = headers.firstValue("X-Debug").orElse("");
                Long serverMs = null;
                if (!xdebugNs.isEmpty()) {
                    try { serverMs = Long.parseLong(xdebugNs) / 1_000_000L; } catch (NumberFormatException ignored) {}
                }

                System.out.println("HTTP: " + response.statusCode());
                System.out.println(response.body());
                if (serverMs != null) System.out.println("Tiempo servidor: " + serverMs + " ms");
                System.out.println("Tiempo cliente:  " + (t1 - t0) + " ms");

                csv.printf("Servidor %d,%s,%d,%d%n",
                        idx++,
                        serverMs == null ? "" : serverMs.toString(),
                        (t1 - t0),
                        response.statusCode()
                );

                // Pausa pequeña entre solicitudes (opcional)
                Thread.sleep(1500);
            }
        }

        System.out.println("\nse guardo resultados.csv");
    }
}
