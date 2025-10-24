/* Ejercicio 2.
La multiplicación de fracciones se define como:
   (a/b) * (c/d) = (a*c) / (b*d)
Escriba un programa que solicite los valores de a, b, c y d (como números enteros) y calcule el valor de la multiplicación y lo muestre en pantalla en forma de número con decimales y en forma de fracción (ejemplo 17/33).
*/

import java.util.*;

public class E2 {
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Valor para a: ");
        int a = scanner.nextInt();
        System.out.print("Valor para b: ");
        int b = scanner.nextInt();        
        System.out.print("Valor para c: ");
        int c = scanner.nextInt();
        System.out.print("Valor para d: ");
        int d = scanner.nextInt();
        //operacion1
        int numerador = a * c;
        int denominador = b * d;
        //operacion2
        double resultadoDecimal = (double) numerador / denominador;
        System.out.println("Resultado en fraccion: " + numerador + "/" + denominador);
        System.out.println("Resultado en decimal: " + resultadoDecimal);
        scanner.close();
    }
}
