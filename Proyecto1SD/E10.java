/*Ejercicio 10
 * Escribe un programa que solicite al usuario un número entero y dé como resultado la suma de todos los números desde el 1 hasta dicho número.
 */

import java.util.*;

public class E10 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa un numero: ");
        int numero = scanner.nextInt();

        int suma = 0;
        for (int i = 1; i <= numero; i++) {
            suma += i;
        }

        System.out.println("La suma de los numeros desde 1 hasta " + numero + " es: " + suma);
        scanner.close();
    }
}
