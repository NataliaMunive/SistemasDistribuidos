/*Ejercicio 1.
Escribir un programa que reciba una cantidad en grados centígrados e indique a cuánto equivalen en grados Fahrenheit. 
*/
import java.util.*;

public class E1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Grados Celsius: ");
        double celsius = scanner.nextDouble();
        //operacion de c a f= (c * 9/5) + 32
        double fahrenheit = (celsius * 9/5) + 32;
        System.out.println("Grados Fahrenheit: " + fahrenheit);
        scanner.close();
    }   
}

