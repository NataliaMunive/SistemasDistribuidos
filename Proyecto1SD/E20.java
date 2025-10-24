/* Ejercicio 20
 * Leer una serie de 10 números, moverlos una posición hacia adelante en el arreglo y mostrar el arreglo resultante.
 */
import java.util.*;

public class E20 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numeros = new int[10];

        System.out.println("Ingresa 10 numeros:");
        for (int i = 0; i < 10; i++) {
            numeros[i] = scanner.nextInt();
        }
        // Guardamos el ultimo 
        int ultimo = numeros[numeros.length - 1];
        // Mover derecha
        for (int i = numeros.length - 1; i > 0; i--) {
            numeros[i] = numeros[i - 1];
        }
        // ultimo al inicio
        numeros[0] = ultimo;

        System.out.println("El arreglo resultante es:");
        for (int i = 0; i < numeros.length; i++) {
            System.out.print(numeros[i] + " ");
        }
        scanner.close();
    }
}
