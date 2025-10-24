import java.util.*;
import java.util.concurrent.*;

class Main2Hilos2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        if (args.length < 3) {
            System.out.println("Uso: java Main2Hilos2 <n> <m> <numThreads>");
            System.out.println("n = CURPs por lista, m = numero de listas, numThreads = tamaño del pool");
            return;
        }

        int n = Integer.parseInt(args[0]); // CURPs por ArrayList
        int m = Integer.parseInt(args[1]); // número de ArrayLists
        int numThreads = Integer.parseInt(args[2]); // tamaño del pool de threads

        // Crear ArrayList de objetos ArrayList<String>
        ArrayList<ArrayList<String>> arregloListas = new ArrayList<>();

        System.out.println("Generando " + m + " listas con " + n + " CURPs cada una...");
        
        // Generar m ArrayLists con n CURPs aleatorias cada uno
        for (int i = 0; i < m; i++) {
            ArrayList<String> curps = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                curps.add(getCURP());
            }
            arregloListas.add(curps);
        }

        System.out.println("Iniciando ordenamiento con " + numThreads + " threads...");
        
        // Medir tiempo de inicio del ordenamiento
        long startTime = System.nanoTime();

        // Crear ThreadPool con el número especificado de hilos
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);

        // Enviar tareas de ordenamiento al ThreadPool
        ArrayList<Future<Void>> futures = new ArrayList<>();
        for (int i = 0; i < arregloListas.size(); i++) {
            final int index = i;
            Future<Void> future = pool.submit(() -> {
                // Usar el mismo método de ordenamiento de la clase 9 (SIN impresiones)
                ordenarSinImprimir(arregloListas.get(index));
                return null;
            });
            futures.add(future);
        }

        // Esperar que todas las tareas terminen
        for (Future<Void> future : futures) {
            future.get();
        }

        // Medir tiempo final
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000_000.0; // Convertir a segundos

        // Cerrar ThreadPool
        pool.shutdown();

        // Imprimir estadísticas (sin las listas para evitar saturar la salida)
        System.out.println("========== ESTADÍSTICAS ==========");
        System.out.printf("Tiempo de ordenamiento: %.3f segundos%n", duration);
        System.out.println("Threads utilizados: " + numThreads);
        System.out.println("Listas procesadas: " + m);
        System.out.println("CURPs por lista: " + n);
        System.out.printf("Total CURPs ordenadas: %d%n", (long)m * n);
        System.out.printf("CURPs/segundo: %.0f%n", ((long)m * n) / duration);
    }

    // Método de ordenamiento SIN impresiones (para mejor rendimiento)
    // Reutiliza el código de ordenamiento de la clase 9 con Iterator
    private static void ordenarSinImprimir(ArrayList<String> curps) {
        // Crear ArrayList temporal para el ordenamiento
        ArrayList<String> temporal = new ArrayList<>();
        
        // Usar el MISMO método de ordenamiento de la clase 9 (con Iterator)
        for (String curp : curps) {
            // Insertar en orden ascendente usando Iterator (código original de clase 9)
            int pos = 0;
            Iterator<String> it = temporal.iterator();
            while (it.hasNext()) {
                String actual = it.next();
                if (curp.substring(0, 4).compareTo(actual.substring(0, 4)) > 0) {
                    pos++;
                } else {
                    break;
                }
            }
            temporal.add(pos, curp);
        }
        // NO imprimimos para mejor rendimiento
    }

    private static String getCURP() {
        String Letra = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Numero = "0123456789";
        String Sexo = "HM";
        String Entidad[] = {"AS", "BC", "BS", "CC", "CS", "CH", "CL", "CM", "DF", "DG", "GT", "GR", "HG", "JC", "MC", "MN", "MS", "NT", "NL", "OC", "PL", "QT", "QR", "SP", "SL", "SR", "TC", "TL", "TS", "VZ", "YN", "ZS"};
        int indice;
        
        StringBuilder sb = new StringBuilder(18);
        
        for (int i = 1; i < 5; i++) {
            indice = (int) (Letra.length()* Math.random());
            sb.append(Letra.charAt(indice));        
        }
        
        for (int i = 5; i < 11; i++) {
            indice = (int) (Numero.length()* Math.random());
            sb.append(Numero.charAt(indice));        
        }
        indice = (int) (Sexo.length()* Math.random());
        sb.append(Sexo.charAt(indice));        
        
        sb.append(Entidad[(int)(Math.random()*32)]);
        for (int i = 14; i < 17; i++) {
            indice = (int) (Letra.length()* Math.random());
            sb.append(Letra.charAt(indice));        
        }
        for (int i = 17; i < 19; i++) {
            indice = (int) (Numero.length()* Math.random());
            sb.append(Numero.charAt(indice));        
        }
        
        return sb.toString();
    }           
}
