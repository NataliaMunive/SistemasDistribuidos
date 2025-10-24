/*Ejercicio 3.
 * Escribe un programa que calcule el radio de la circunferencia inscrita en un triángulo.
 */

import java.util.Scanner;

public class E3 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print ("Valor para a: ");
        int a = scanner.nextInt();
        System.out.print ("Valor para b: ");
        int b = scanner.nextInt();
        System.out.print ("Valor para c: ");
        int c = scanner.nextInt();
        //operacion1
        double semiPerimetroTriangulo = (a + b + c) / 2;
        //operacion2
        double heronAreaTriangulo = Math.sqrt(semiPerimetroTriangulo * (semiPerimetroTriangulo - a) * (semiPerimetroTriangulo - b) * (semiPerimetroTriangulo - c));
        //operacion3
        double radioCircunferenciaInscrita = (heronAreaTriangulo / semiPerimetroTriangulo);
        System.out.println("El radio de la circunferencia inscrita es: " + radioCircunferenciaInscrita);
        scanner.close();
    }

}
