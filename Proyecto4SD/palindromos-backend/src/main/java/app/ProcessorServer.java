/*  Proyecto 4
 * Munive hernandez Erika Natalia
 * 7CM4
 */

package app;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.Locale;

public class ProcessorServer{
  private static String INSTANCE_NAME = System.getenv().getOrDefault("INSTANCE_NAME", "processor-unknown");

  public static void main(String[] args) throws Exception {
    int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/health", exchange -> {
      String resp = "OK:" + INSTANCE_NAME;
      byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, bytes.length);
      try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    });

    server.createContext("/metrics", exchange -> {
      OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
      double cpu = osBean.getCpuLoad(); // 0..1
      String resp = String.format(Locale.US, "instance=%s cpu=%.2f", INSTANCE_NAME, cpu);
      byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, bytes.length);
      try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    });

    server.createContext("/process", new ProcessHandler());

    server.start();
    System.out.println("ProcessorServer " + INSTANCE_NAME + " listening on " + port);
  }
}

/*  Proyecto 4
 * Munive hernandez Erika Natalia
 * 7CM4
 */