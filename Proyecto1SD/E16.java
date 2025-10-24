/*Ejercicio 16
 * El programa debe leer números mientras sean diferentes de 0. Posteriormente calcular el promedio de los números leídos.
 */

import java.util.*; 

public class E16 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num;
        int suma = 0;
        int contador = 0;

        System.out.println("Iingresa numeros (0 para promediar)");
        num = scanner.nextInt();
        while (num != 0) {
            suma += num;
            contador++;
            num = scanner.nextInt();
        }

        if (contador > 0) {
            double promedio = (double) suma / contador;
            System.out.printf("El promedio es: %.2f%n", promedio);
        } else {
            System.out.println("Numeros no validos");
        }

        scanner.close();
    }
}
