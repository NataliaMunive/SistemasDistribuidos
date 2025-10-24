/*Ejercicio 14
Escribe un programa que lea un número X y un número Y. Mostrar los números de Y en Y, comenzando en X hasta llegar a 200. * 
 */

import java.util.*;

public class E14 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("X: ");
        int x = scanner.nextInt();

        System.out.print("Y: ");
        int y = scanner.nextInt();

        if (y <= 0) {
            System.out.println("numero negativo o cero");
        } else {
            int i = x;

            while (i <= 200) {
                System.out.println(i);
                i += y;
            }
        }

        scanner.close();
    }
}

