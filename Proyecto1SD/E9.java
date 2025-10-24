/*
scribe un programa que reciba un número entre 0 y 9999 e indique si es un número capicúa. Nota, no es necesario utilizar ciclos, es posible realizarlo utilizando divisiones enteras.
 */

import java.util.*;

public class E9 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("numero entre 0 y 9999: ");
        int numero = scanner.nextInt();
        
        if (numero >= 0 && numero <= 9999) {
            boolean esCapicua = false;

            if (numero < 10) {
                esCapicua = true;
            } else if (numero < 100) { 
                int a = numero / 10;
                int b = numero % 10;
                esCapicua = (a == b);
            } else if (numero < 1000) { 
                int a = numero / 100;
                int b = (numero / 10) % 10;
                int c = numero % 10;
                esCapicua = (a == c);
            } else { 
                int a = numero / 1000;
                int b = (numero / 100) % 10;
                int c = (numero / 10) % 10;
                int d = numero % 10;
                esCapicua = (a == d && b == c);
            }

            if (esCapicua) {
                System.out.println(numero + " es capicua.");
            } else {
                System.out.println(numero + " no capicua.");
            }
        } else {
            System.out.println("El numero esta fuera del rango.");
        }

        scanner.close();
    }
}
