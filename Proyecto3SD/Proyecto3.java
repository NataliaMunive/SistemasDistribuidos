/*  Proyecto 3
 * Munive hernandez Erika Natalia
 * 7CM4
*/

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ThreadLocalRandom;

public class Proyecto3 {

    // partidos
    private static final String[] PARTIDOS = {"PAN","PRI","PRD","EVA01","PT","ATWTMVTVFTV","MORENA"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: Debe proporcionar el numero de votos por segundo");         
            return;
        }
        
        int votosPorSegundo;
        try { 
            votosPorSegundo = Integer.parseInt(args[0]); 
        } catch (Exception e) {
            System.out.println("Error: El numero de votos debe ser un entero valido");
            return;
        }

        System.out.println("generando " + votosPorSegundo + " votos por segundo en VOTOS.dat (Ctrl+C para salir)...");
        Path file = Paths.get("VOTOS.dat");

        try (BufferedWriter bw = Files.newBufferedWriter(
                file,
                java.nio.charset.StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)
        ) {
            while (true) {
                for (int i = 0; i < votosPorSegundo; i++) {
                    String curp = generarCurp(1);
                    String partido = PARTIDOS[ThreadLocalRandom.current().nextInt(PARTIDOS.length)];
                    bw.write(curp + "|" + partido);
                    bw.newLine();
                }
                bw.flush();
                Thread.sleep(1000); // espera 1 segundo
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ejecuta: java MainCURPS N y toma la ultima linea que parezca curp
    private static String generarCurp(int n) {
        try {
            Process p = new ProcessBuilder("java", "MainCURPS", String.valueOf(n))
                    .redirectErrorStream(true)
                    .start();

            String regex = "^[A-Z]{4}\\d{6}[HM][A-Z]{2}[A-Z]{3}\\d{2}$";
            String line;
            String ultima = null;

            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.matches(regex)) {
                        ultima = line;
                    }
                }
            }
            p.waitFor();
            return (ultima != null) ? ultima : "XXXX000000HDFXXX00";
        } catch (Exception e) {
            e.printStackTrace();
            return "XXXX000000HDFXXX00";
        }
    }
}

/*  Proyecto 3
 * Munive hernandez Erika Natalia
 * 7CM4
 */
