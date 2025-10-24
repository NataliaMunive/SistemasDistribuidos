/*Ejercio 23
 * Escribe un programa que lea un archivo de texto y que escriba en otro archivo solo las líneas impares del archivo original.
 */

import java.io.*;
import java.util.*;

public class E23 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("archivo de entrada: ");
        String archivoEntrada = scanner.nextLine();
        String archivoSalida = "e23res.txt";

        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivoEntrada));
            PrintWriter escritor = new PrintWriter(new FileWriter(archivoSalida));

            String linea;
            int numeroLinea = 1;

            while ((linea = lector.readLine()) != null) {
                if (numeroLinea % 2 != 0) { // impar
                    escritor.println(linea);
                }
                numeroLinea++;
            }

            lector.close();
            escritor.close();

            System.out.println("Las lineas impares se han guardado en '" + archivoSalida + "' correctamente");
        } catch (FileNotFoundException e) {
            System.out.println("El archivo" + archivoEntrada + "no fue encontrado");
        } catch (IOException e) {
            System.out.println("Error al leer o escribir archivos:");
            e.printStackTrace();
        }

        scanner.close();
    }
}
