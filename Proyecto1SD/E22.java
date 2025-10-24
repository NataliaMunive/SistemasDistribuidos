/* Ejercicio 22
 * Escribe un programa que solicite al usuario los tamaños de las dos matrices a multiplicar y luego solicite los valores, realice la multiplicación y muestre el resultado.
 */

import java.util.*;

public class E22 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Numero de filas de la matriz A: ");
        int filasA = scanner.nextInt();
        System.out.print("Numero de columnas de la matriz A: ");
        int columnasA = scanner.nextInt();
        System.out.print("Numero de filas de la matriz B: ");
        int filasB = scanner.nextInt();
        System.out.print("Numero de columnas de la matriz B: ");
        int columnasB = scanner.nextInt();

        if (columnasA != filasB) {
            System.out.println("Las columnas de A deben ser iguales a las filas de B.");
            scanner.close();
            return;
        }
        int[][] A = new int[filasA][columnasA];
        int[][] B = new int[filasB][columnasB];
        int[][] resultado = new int[filasA][columnasB];

        System.out.println("Elementos de la matriz A:");
        for (int i = 0; i < filasA; i++) {
            for (int j = 0; j < columnasA; j++) {
                System.out.print("A[" + i + "][" + j + "]: ");
                A[i][j] = scanner.nextInt();
            }
        }
        
        System.out.println("Elementos de la matriz B:");
        for (int i = 0; i < filasB; i++) {
            for (int j = 0; j < columnasB; j++) {
                System.out.print("B[" + i + "][" + j + "]: ");
                B[i][j] = scanner.nextInt();
            }
        }
        
        for (int i = 0; i < filasA; i++) {
            for (int j = 0; j < columnasB; j++) {
                int suma = 0;
                for (int k = 0; k < columnasA; k++) {
                    suma += A[i][k] * B[k][j];
                }
                resultado[i][j] = suma;
            }
        }

        System.out.println("Resultado de la multiplicación A x  B:");
        for (int i = 0; i < filasA; i++) {
            for (int j = 0; j < columnasB; j++) {
                System.out.print(resultado[i][j] + "\t");
            }
            System.out.println();
        }

        scanner.close();
    }
}
