/*  Proyecto 4
 * Munive hernandez Erika Natalia
 * 7CM4
 */

package app.text;

import java.text.Normalizer;
import java.util.*;

public class PalindromeFinder {
  private static String[] tokenize(String text) {
    String norm = Normalizer.normalize(text, Normalizer.Form.NFD)
      .replaceAll("\\p{M}", "")                 // quita acentos
      .replaceAll("[^A-Za-z0-9\\s]", " ")      // quita puntuación
      .toLowerCase(Locale.ROOT);
    return norm.trim().split("\\s+");
  }

  public static List<String> findKWordPalindromes(String text, int k) {
    String[] w = tokenize(text);
    List<String> out = new ArrayList<>();
    for (int i=0; i + k <= w.length; i++) {
      boolean ok = true;
      for (int a=0, b=k-1; a<b; a++, b--) {
        if (!w[i+a].equals(w[i+b])) { ok = false; break; }
      }
      if (ok) {
        StringBuilder sb = new StringBuilder();
        for (int j=0;j<k;j++) { if (j>0) sb.append(' '); sb.append(w[i+j]); }
        out.add(sb.toString());
      }
    }
    return out;
  }
}

/*  Proyecto 4
 * Munive hernandez Erika Natalia
 * 7CM4
 */