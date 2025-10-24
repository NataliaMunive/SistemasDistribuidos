/*Ejercicio 6
Calcular el cobro que una caseta de cuota debe realizar a un vehículo de acuerdo con las siguientes reglas: Motocicleta = $20, 2 ejes(automóviles) = $40, 3 ejes (camionetas) = $60, 4, 5 y 6 ejes (camiones de carga) = $250, eje adicional $50. Por ejemplo, si llega un trailer de 8 ejes se deben cobrar $350.
 */

import java.util.*;

public class E6 {

    public static void main(String[] args) {
        int numeroEjes;
        int cobro = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.print("NUmero de ejes del vehiculo; ");
        numeroEjes = scanner.nextInt();

        if (numeroEjes == 1) {
            cobro = 20;
        } else if (numeroEjes == 2) {
            cobro = 40; 
        } else if (numeroEjes == 3) {
            cobro = 60;
        } else if (numeroEjes >= 4 && numeroEjes <= 6) {
            cobro = 250; 
        } else if (numeroEjes > 6) {
            cobro = 250 + (numeroEjes - 6) * 50; 
        } else {
            System.out.println("Numero de ejes no valido.");
            scanner.close();
            return;
        }

        System.out.println("El cobro para el vehiculo es: $" + cobro);
        
    }
}