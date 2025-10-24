/* Ejercicio 21
 * Escribe un programa que lea un arreglo bidimensional de 5x5 y muestre la suma del total del arreglo.
 */
import java.util.*;

public class E21 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] matriz = new int[5][5];
        int suma = 0;
        System.out.println("Ingresa la matriz 5x5:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print("A[" + i + "][" + j + "]: ");
                matriz[i][j] = scanner.nextInt();
                suma += matriz[i][j];
            }
        }
        
        System.out.println("Suma de la matriz: " + suma);
        scanner.close();
    }
}

