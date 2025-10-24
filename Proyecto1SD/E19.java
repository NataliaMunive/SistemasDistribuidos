/* Ejercicio 19
Leer en un arreglo una serie de 10 números e indicar si todos los elementos están ordenados de forma descendente, es decir, si cumplen la regla de que cada elemento del arreglo es menor o igual que el elemento anterior.
 */
import java.util.*;
public class E19 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numeros = new int[10];
        boolean ordenado = true;

        System.out.println("Ingresa 10 numeros enteros:");

        for (int i = 0; i < 10; i++) {
            numeros[i] = scanner.nextInt();
        }

        for (int i = 1; i < numeros.length; i++) {
            if (numeros[i] > numeros[i - 1]) {
                ordenado = false;
                break;
            }
        }

        if (ordenado) {
            System.out.println("Los numeros estan ordenados de forma descendente.");
        } else {
            System.out.println("Los numeros no estan ordenados de forma descendente.");
        }

        scanner.close();
    }
}
