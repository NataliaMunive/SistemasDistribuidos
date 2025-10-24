import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientSynchronous3 {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        // Cuerpo JSON a enviar
        String json = """
        {
          "userId": 1,
          "title": "7CM4-EQ7",
          "body": "Natalia"
        }
        """;

        // Crear solicitud POST con JSON
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // Cabecera opcional
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Enviar solicitud y obtener respuesta
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Mostrar encabezados
        HttpHeaders headers = response.headers();
        headers.map().forEach((k, v) -> System.out.println(k + ": " + v));

        // Código de estado
        System.out.println("\nCódigo de estado: " + response.statusCode());

        // Cuerpo de respuesta
        System.out.println("Cuerpo de la respuesta:");
        System.out.println(response.body());
    }
}
