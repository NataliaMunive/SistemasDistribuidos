import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GCPClient {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {
        
        // Cambia estas IPs por las IPs INTERNAS de tus VMs en GCP
        String frontendIP = "10.128.0.2";  // IP interna del frontend
        String backendIP = "10.128.0.3";   // IP interna del backend
        
        System.out.println("=== PRUEBA DE CONECTIVIDAD GCP ===");
        
        // 1. Probar status del frontend
        System.out.println("\n1. Probando servidor FRONTEND:");
        testServer("http://" + frontendIP + ":80/status");
        
        // 2. Probar task del frontend
        System.out.println("\n2. Probando task FRONTEND:");
        testTask("http://" + frontendIP + ":80/task", "10,20,30");
        
        // 3. Probar status del backend
        System.out.println("\n3. Probando servidor BACKEND:");
        testServer("http://" + backendIP + ":8080/status");
        
        // 4. Probar task del backend  
        System.out.println("\n4. Probando task BACKEND:");
        testTask("http://" + backendIP + ":8080/task", "5,7,11");
    }
    
    private static void testServer(String url) throws IOException, InterruptedException {
        System.out.println("Conectando a: " + url);
        
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "GCP Client Test")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status: " + response.statusCode());
            System.out.println("Respuesta: " + response.body());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    
    private static void testTask(String url, String data) throws IOException, InterruptedException {
        System.out.println("Enviando POST a: " + url + " con datos: " + data);
        
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .uri(URI.create(url))
                .setHeader("User-Agent", "GCP Client Test")
                .setHeader("Content-Type", "text/plain")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status: " + response.statusCode());
            System.out.println("Resultado: " + response.body());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}