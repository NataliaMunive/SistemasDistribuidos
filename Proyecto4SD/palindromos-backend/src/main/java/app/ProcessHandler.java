/*  Proyecto 4
 * Munive hernandez Erika Natalia
 * 7CM4
 */

package app;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import app.storage.GcsReader;
import app.text.PalindromeFinder;

public class ProcessHandler implements HttpHandler {
  @Override public void handle(HttpExchange ex) throws IOException {
    Map<String,String> q = parse(ex.getRequestURI().getQuery());
    String bucket = q.getOrDefault("bucket", "");
    String object = q.getOrDefault("object", "");
    int k = Integer.parseInt(q.getOrDefault("k", "1"));

    if (bucket.isEmpty() || object.isEmpty() || k < 1 || k > 5) {
      send(ex, 400, "Uso: /process?bucket=...&object=...&k=1..5");
      return;
    }

    try {
      String content = GcsReader.readObject(bucket, object);
      List<String> pals = PalindromeFinder.findKWordPalindromes(content, k);
      StringBuilder sb = new StringBuilder();
      for (String p : pals) sb.append(p).append("\t").append(object).append("\n");
      System.out.printf("Trabajé: objeto=%s  k=%d  pals=%d%n", object, k, pals.size());
      send(ex, 200, sb.toString());
    } catch (Exception e) {
      send(ex, 500, "Error: " + e.getMessage());
    }
  }

  private static void send(HttpExchange ex, int code, String body) throws IOException {
    byte[] b = body.getBytes(StandardCharsets.UTF_8);
    ex.sendResponseHeaders(code, b.length);
    try (OutputStream os = ex.getResponseBody()) { os.write(b); }
  }

  private static Map<String,String> parse(String q) {
    Map<String,String> m = new HashMap<>();
    if (q == null) return m;
    for (String part : q.split("&")) {
      String[] kv = part.split("=",2);
      if (kv.length==2) m.put(urlDecode(kv[0]), urlDecode(kv[1]));
    }
    return m;
  }
  private static String urlDecode(String s){ try { return URLDecoder.decode(s, "UTF-8"); } catch(Exception e){ return s; } }
}

/*  Proyecto 4
 * Munive hernandez Erika Natalia
 * 7CM4
 */