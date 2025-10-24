import java.io.*;
import java.util.*;

public class E24 {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int[] letras = new int[26];  // 0 = a, 1 = b, ..., 25 = z
        System.out.print("nombre del archivo: ");
        String nombreArchivo = teclado.nextLine();

        try {
            File archivo = new File(nombreArchivo);
            Scanner lector = new Scanner(archivo);

            // Leer
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().toLowerCase(); // minuscula
                for (int i = 0; i < linea.length(); i++) {
                    char letra = linea.charAt(i);
                    //letra entre a y z
                    if (letra >= 'a' && letra <= 'z') {
                        int posicion = letra - 'a'; // a = 0, b = 1, ..., z = 25
                        letras[posicion]++;
                    }
                }
            }
            lector.close();
            System.out.println("\nCantidad de veces que aparece cada letra:");
            for (int i = 0; i < 26; i++) {
                char letra = (char) ('a' + i);
                System.out.println(letra + ": " + letras[i]);
            }

        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el archivo");
        }

        teclado.close();
    }
}
