/* Calcular cuánto dinero tendría en una cuenta de ahorro al final de 20 años si al inicio de cada año añado $10,000, el rendimiento anual es 5% y reinvierto las ganancias obtenidas cada año. */
public class E13 {
    public static void main(String[] args) {
        double dinero = 0;
        double rendimiento = 0.05;
        double aporteAnual = 10000;

        for (int año = 1; año <= 20; año++) {
            dinero += aporteAnual;
            dinero += dinero * rendimiento;
        }

        System.out.printf("Al final de 20 años, el dinero es: $%.2f%n", dinero);
    }
}
