/*Ejercicio 12
 * Un número perfecto es un número natural que es igual a la suma de sus divisores propios, sin incluirse él mismo. Por ejemplo, el número 6 es perfecto porque sus divisores son 1, 2 y 3; y dado que 1+2+3 = 6 entonces el 6 es un número perfecto. Escriba un programa que indique los números perfectos existentes entre 1 y 10,000.
 */
public class E12{
    public static void main(String[] args) {
        System.out.println("Numeros perfectos entre 1 y 10000:");

        for (int numero = 1; numero <= 10000; numero++) {
            int sumaDivisores = 0;
            //suma de los divisores propios
            for (int i = 1; i <= numero / 2; i++) {
                if (numero % i == 0) {
                    sumaDivisores += i;
                }
            }
            if (sumaDivisores == numero) {
                System.out.println(numero);
            }
        }
    }
}
