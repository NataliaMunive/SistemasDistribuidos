/* 
 * Munive hernandez Erika Natalia
 * 7CM4
 */

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class citas {
    
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        // sacamos 3 citas
        String quotesUrl = "https://api.breakingbadquotes.xyz/v1/quotes/3";

        HttpRequest quotesReq = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(quotesUrl))
                .setHeader("User-Agent", "Java HttpClient")
                .build();

        HttpResponse<String> quotesRes = httpClient.send(quotesReq, HttpResponse.BodyHandlers.ofString());
        HttpHeaders qHeaders = quotesRes.headers();
        /*
        qHeaders.map().forEach((k, v) -> System.out.println(k + ":" + v));
        
        System.out.println("" + quotesRes.statusCode());
        System.out.println("Cita: " + quotesRes.body());
        System.out.println(quotesRes.body());
        System.out.println("------------------------------------------------------------");
        */

        if (quotesRes.statusCode() != 200) {
            System.err.println("Error No se pudo obtener citas" + quotesRes.statusCode());
            return;
        }

        // convertir texto JSON en objetos Java 
        JsonArray quotesArray;
        try {
            quotesArray = JsonParser.parseString(quotesRes.body()).getAsJsonArray();
        } catch (Exception ex) {
            System.err.println("[Error] JSON de citas invalido: " + ex.getMessage());
            return;
        }

        // traduccion
        String apiKey = "AIzaSyDyFDn7L2OEP5xsQbdk80qXwQhCfNDi1D8";

        // procesar cada cita
        for (int i = 0; i < quotesArray.size(); i++) {
            JsonObject elemento = quotesArray.get(i).getAsJsonObject();
            String quote  = elemento.has("quote")  ? elemento.get("quote").getAsString()  : "(sin quote)";
            String author = elemento.has("author") ? elemento.get("author").getAsString() : "(desconocido)";

            // preparar solicitud de traduccion
            String encodedQ = URLEncoder.encode(quote, StandardCharsets.UTF_8);
            String translateUrl = "https://translation.googleapis.com/language/translate/v2"
                    + "?target=es"
                    + "&key=" + apiKey 
                    + "&q=" + encodedQ;

            System.out.println("\n---------- Cita #" + (i + 1) + " ----------");
            
            // crear solicitud de traduccion
            HttpRequest trReq = HttpRequest.newBuilder()
                    .uri(URI.create(translateUrl))
                    .setHeader("User-Agent", "Java 11 HttpClient Bot")
                    .GET()
                    .build();

            // enviar solicitud de traduccion
            HttpResponse<String> trRes = httpClient.send(trReq, HttpResponse.BodyHandlers.ofString());
            HttpHeaders tHeaders = trRes.headers();
            /*t 
            Headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
            
            System.out.println("\n " + trRes.statusCode());
            System.out.println("Cita traducida:");
            System.out.println(trRes.body());
            */
            
            // procesar respuesta de traduccion
            String translated = "no hay traduccion";
            if (trRes.statusCode() == 200) {
                try {
                    JsonObject responseJson = JsonParser.parseString(trRes.body()).getAsJsonObject();
                    JsonObject data = responseJson.getAsJsonObject("data");
                    JsonArray translations = data.getAsJsonArray("translations");
                    translated = translations.get(0).getAsJsonObject().get("translatedText").getAsString();
                } catch (Exception parseEx) {
                    translated = "error: " + parseEx.getMessage();
                }
            } else {
                translated = "Error " + trRes.statusCode() + " al traducir";
            }

            System.out.println("\nCita (en): " + quote);
            System.out.println("Autor    : " + author);
            System.out.println("Cita (es): " + translated);
        }
    }
}

/* 
 * Munive hernandez Erika Natalia
 * 7CM4
 */
