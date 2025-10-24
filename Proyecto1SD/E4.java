/*Ejercicio 4
Una universidad comenzará a asignar a sus estudiantes a diferentes dormitorios según su sexo y edad. Escribe un programa que solicite el sexo (H/M) y edad e indique de acuerdo con la siguiente tabla en qué edificio deben ser asignados. Validar que el sexo y edad sean valores correctos.
Hombre, 18 años = Edificio A
Mujer, 18 años = Edificio B
Hombre, 19 a 22 años = Edificio C
Mujer, 19 a 22 años = Edificio D
Hombre, Mayor de 22 años = Edificio E1
Mujer, Mayor de 22 años = Edificio E2
 */

import java.util.*;
public class E4 {
    public static void main(String[] args) {
        char sexo;
        int edad;
        String edificio = "";

        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print("Ingresa el sexo (H/M): ");
            sexo = scanner.next().charAt(0);
        } while (sexo != 'H' && sexo != 'M');

        do {
            System.out.print("Ingresa la edad: ");
            edad = scanner.nextInt();
        } while (edad < 0);

        if (sexo == 'H') {
            if (edad == 18) {
                edificio = "Edificio A";
            } else if (edad < 18) {
                System.out.println("Edad no valida, debe ser mayor o igual a 18.");
                edificio = "Ninguno";
            } else if (edad >= 19 && edad <= 22) {
                edificio = "Edificio C";
            } else {
                edificio = "Edificio E1";
            }
        } else {
            if (edad == 18) {
                edificio = "Edificio B";
            } else if (edad < 18) {
                System.out.println("Edad no valida, debe ser mayor o igual a 18.");
                edificio = "Ninguno";
            } else if (edad >= 19 && edad <= 22) {
                edificio = "Edificio D";
            } else {
                edificio = "Edificio E2";
            } 
        }

        System.out.println("El estudiante debe ser asignado a " + edificio);
        scanner.close();
    }
}
