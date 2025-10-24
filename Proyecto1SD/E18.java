/* Ejercicio 18
 * Leer un arreglo de 10 nombres de ciudades e indicar cuál es la que tiene el nombre más largo
 */

import java.util.*;
public class E18 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] ciudades = new String[10];

        System.out.println("Ingresa 10 nombres de ciudades:");

        for (int i = 0; i < 10; i++) {
            ciudades[i] = scanner.nextLine();
        }

        String ciudadsota = ciudades[0];

        for (int i = 1; i < ciudades.length; i++) {
            if (ciudades[i].length() > ciudadsota.length()) {
                ciudadsota = ciudades[i];
            }
        }

        System.out.println("La ciudad con el nombre mas largo es: " + ciudadsota);
        scanner.close();
    }
}
