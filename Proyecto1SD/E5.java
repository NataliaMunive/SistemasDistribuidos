/*Ejercicio 5
 * Escribir un programa que indique cuántos días de vacaciones le corresponde a un empleado considerando los años que ha trabajado en la empresa. Entre 1 y 5 años corresponden 5 días de vacaciones, entre 6 y 10 años deben ser 10 días de vacaciones, de allí en adelante, es un día de vacaciones extra por cada año de trabajo (a partir de 10), a partir de 20 años de trabajo se añaden 2 días de vacaciones por cada año hasta un máximo de 45 días.
 */

import java.util.*;
public class E5 {
    public static void main(String[] args) {
        int añosTrabajados = 0;
        int diasVacaciones = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese los años trabajados: ");
        añosTrabajados = scanner.nextInt();

        if (añosTrabajados >= 1 && añosTrabajados <= 5) {
            diasVacaciones = 5;
        } else if (añosTrabajados >= 6 && añosTrabajados <= 10) {
            diasVacaciones = 10;
        } else if (añosTrabajados > 10 && añosTrabajados < 20) {
            diasVacaciones = 10 + (añosTrabajados - 10);
        } else if (añosTrabajados >= 20) {
            diasVacaciones = 30 + (añosTrabajados - 20) * 2;
        } else if (añosTrabajados < 1) {
            diasVacaciones = 0;
        }

        if (diasVacaciones > 45) {
            diasVacaciones = 45;
        }

        System.out.println("Días de vacaciones: " + diasVacaciones);
        scanner.close();
    }
}
