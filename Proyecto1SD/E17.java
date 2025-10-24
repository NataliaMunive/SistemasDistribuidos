/*Ejercicio 17
 * Leer 10 números enteros, guardarlos en orden inverso al que fueron introducidos y mostrarlos en pantalla.
 */
import java.util.*;
public class E17 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numeros = new int[10];

        System.out.println("ingresa 10 numeros enteros:");

        for (int i = 0; i < 10; i++) {
            numeros[i] = scanner.nextInt();
        }

        System.out.println("Los numeros en orden inverso son:");
        for (int i = 9; i >= 0; i--) {
            System.out.println(numeros[i]);
        }

        scanner.close();
    }
}
