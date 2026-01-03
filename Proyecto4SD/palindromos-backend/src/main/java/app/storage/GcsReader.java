/*  Proyecto 4
 * Munive hernandez Erika Natalia
 * 7CM4
 */

package app.storage;

import com.google.cloud.storage.*;
import java.nio.charset.StandardCharsets;

public class GcsReader {
  private static final Storage storage = StorageOptions.getDefaultInstance().getService();

  public static String readObject(String bucket, String object) {
    Blob blob = storage.get(bucket, object);
    if (blob == null) throw new RuntimeException("No existe: gs://" + bucket + "/" + object);
    return new String(blob.getContent(), StandardCharsets.UTF_8);
  }
}

/*  Proyecto 4
 * Munive hernandez Erika Natalia
 * 7CM4
 */