//api key AIzaSyAR_8KK_Pr3k6imEa4OHvlnroD_1x_5rdQ

//https://translation.googleapis.com/language/translate/v2?target=es&key=AIzaSyAR_8KK_Pr3k6imEa4OHvlnroD_1x_5rdQ&q="People%20have%20the%20right%20to%20disagree%20with%20your%20opinions%20and%20to%20dissent."

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientSynchronous4 {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        // Frase a traducir
        String text = "hola soy nat y tengo un gato llamado socrates";

        String apiKey = "AIzaSyDyFDn7L2OEP5xsQbdk80qXwQhCfNDi1D8";

        // Idiomas a los que traducir
        String[] targets = {"en", "ja"}; // inglés, japonés

        for (String lang : targets) {
            String url = "https://translation.googleapis.com/language/translate/v2"
                    + "?target=" + lang
                    + "&key=" + apiKey
                    + "&q=" + text.replace(" ", "%20");

            System.out.println("\n----------traduccion al idioma: " + lang + " ----------");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .setHeader("User-Agent", "Java 11 HttpClient Bot")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            HttpHeaders headers = response.headers();
            headers.map().forEach((k, v) -> System.out.println(k + ": " + v));

            System.out.println("\nCódigo de estado: " + response.statusCode());
            System.out.println("Cuerpo de la respuesta:");
            System.out.println(response.body());
        }
    }
}

