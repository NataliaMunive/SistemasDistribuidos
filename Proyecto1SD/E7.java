/*Ejercicio 7
La Comisión Federal de Electricidad cobra el consumo de electricidad de acuerdo con un tabulador basado en los kilowatts consumidos en el periodo. La tabla es la siguiente:
Costo del kW para Hogares:
De 0 a 250 kW el costo por kW es de $0.65
De 251 a 500 kW el costo por kW es de $0.85
De 501 a 1200 kW el costo por kW es de $1.50
De 1201 a 2100 kW el costo por kW es de $2.50
De 2101 kW hacia arriba el costo por kW es de $3.00
Costo del kW para Negocios:
$5.00, el costo es fijo por kilowatt sin importar la cantidad consumida.
 */
import java.util.*;
public class E7 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el tipo de cliente (1 para Hogares, 2 para Negocios): ");
        int tipoCliente = scanner.nextInt();
        System.out.print("Ingrese el consumo en kW: ");
        int consumoKW = scanner.nextInt();
        double costoTotal = 0;

        switch (tipoCliente) {
        case 1: 
            if (consumoKW >= 0 && consumoKW <= 250) {
                costoTotal = consumoKW * 0.65;
            } else if (consumoKW <= 500) {
                costoTotal = (250 * 0.65) + (consumoKW - 250) * 0.85;
            } else if (consumoKW <= 1200) {
                costoTotal = (250 * 0.65) + (250 * 0.85) + (consumoKW - 500) * 1.50;
            } else if (consumoKW <= 2100) {
                costoTotal = (250 * 0.65) + (250 * 0.85) + (700 * 1.50)+ (consumoKW - 1200) * 2.50;
            } else { 
                costoTotal = (250 * 0.65) + (250 * 0.85) + (700 * 1.50) + (900 * 2.50) + (consumoKW - 2100) * 3.00;
            }
            break;

        case 2: 
            if (consumoKW >= 0) {
                costoTotal = consumoKW * 5.00;
            } else {
                System.out.println("Consumo no valido.");
                scanner.close();
                return;
            }
            break;

    default:
        System.out.println("Tipo de cliente no valido.");
        scanner.close();
        return;
    }
        System.out.printf("El costo total es: $%.2f%n", costoTotal);
        scanner.close();
    }
}