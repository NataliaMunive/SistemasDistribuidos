import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ejemplo {
    public static void main(String[] args) {
        // Creamos un ThreadPool con 2 empleados (hilos)
        ExecutorService empleados = Executors.newFixedThreadPool(2);

        // Simulamos 4 pedidos
        for (int i = 1; i <= 4; i++) {
            int pedidoId = i;
            Runnable tarea = () -> {
                System.out.println("Empleado " + Thread.currentThread().getName() + " está preparando el pedido #" + pedidoId);
                try {
                    Thread.sleep(2000); // Simula que tarda 2 segundos en prepararlo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Pedido #" + pedidoId + " entregado por " + Thread.currentThread().getName());
            };

            empleados.execute(tarea); // Asignamos el pedido al pool
        }

        empleados.shutdown(); // Cerramos el pool después de enviar todos los pedidos
    }
}
