/*Ejercicio 8 
Escribe un programa que pida tres números y que los muestre ordenados de mayor a menor.
Nota: Este ejercicio no requiere ciclos ni ordenamientos especiales, puede realizarse utilizando “IF”.
*/

import java.util.*;

public class E8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("NUmero 1: ");
        int n1 = scanner.nextInt();
        System.out.print("Numero 2: ");
        int n2 = scanner.nextInt();
        System.out.print("Numero 3: ");
        int n3 = scanner.nextInt();

        int mayor, medio, menor;

        if (n1 >= n2 && n1 >= n3) {
            mayor = n1;
            if (n2 >= n3) {
                medio = n2;
                menor = n3;
            } else {
                medio = n3;
                menor = n2;
            }
        } else if (n2 >= n1 && n2 >= n3) {
            mayor = n2;
            if (n1 >= n3) {
                medio = n1;
                menor = n3;
            } else {
                medio = n3;
                menor = n1;
            }
        } else {
            mayor = n3;
            if (n1 >= n2) {
                medio = n1;
                menor = n2;
            } else {
                medio = n2;
                menor = n1;
            }
        }

        System.out.println("Numeros ordenados de mayor a menor: " + mayor + ", " + medio + ", " + menor);
        scanner.close();
    }
}
