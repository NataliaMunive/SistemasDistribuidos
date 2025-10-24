/*Ejercicio 15
 * En este programa la computadora debe elegir un número al azar entre 1 y 100 y solicitará al usuario que “adivine” el número. A cada intento del usuario la computadora debe indicar si el número que el usuario introdujo es mayor o menor que el número prefijado. El programa debe terminar cuando el usuario “adivine” el número o introduzca el número “0” para salir.
 */
import java.util.*;

public class E15 {
    public static void main(String[] args) {
        Random random = new Random();
        int numeroSecreto = random.nextInt(100) + 1;
        Scanner scanner = new Scanner(System.in);
        int intento;

        System.out.println("Adivina un numero entre 1 y 100 (0 para salir):");

        do {
            intento = scanner.nextInt();

            if (intento == 0) {
                System.out.println("Salier");
                break;
            }

            if (intento == numeroSecreto) {
                System.out.println("Encontraste el numero");
            } else if (intento > numeroSecreto) {
                System.out.println("es menor");
            } else if (intento < numeroSecreto) {
                System.out.println("es mayor");
            }
        } while (intento != numeroSecreto);

        scanner.close();
    }
}
