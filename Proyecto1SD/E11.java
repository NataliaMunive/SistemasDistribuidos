/* Ejercicio 11
 * Encontrar los números entre el 1 y el 5000 que cumplen la regla de que la suma del cubo de sus dígitos es igual al número mismo.
 */
public class E11 {
    public static void main(String[] args) {
        System.out.println("Numeros entre 1 y 5000 que cumplen la condicion:");

        for (int numero = 1; numero <= 5000; numero++) {
            int sumaCubos = 0;
            int temp = numero;

            for (; temp > 0; temp /= 10) {
                int digito = temp % 10;
                sumaCubos += digito * digito * digito;
            }

            if (sumaCubos == numero) {
                System.out.println(numero);
            }
        }
    }
}
