/*  Proyecto 3
 * Munive hernandez Erika Natalia
 * 7CM4
 */

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Proyecto3Interfaz {

    // Lista de partidos politicos 
    private static final String[] PARTIDOS = {
        "PAN","PRI","PRD","EVA01","PT","ATWTMVTVFTV","MORENA"
    };

    // Contadores thread-safe para estadisticas de votacion
    private static final Map<String, AtomicLong> votosPartido = new HashMap<>();
    private static final Map<String, AtomicLong> votosSexo = new HashMap<>();
    private static final Map<String, AtomicLong> votosEstado = new HashMap<>();
    private static final AtomicLong totalVotos = new AtomicLong(0);

    // Variables para consulta especifica de edad
    private static volatile int edadConsulta = 0;
    private static final Map<Integer, AtomicLong> votosPorEdadHombre = new HashMap<>();
    private static final Map<Integer, AtomicLong> votosPorEdadMujer = new HashMap<>();

    // Variables para mostrar respuestas
    private static boolean mostrarRespuesta1 = false;
    private static boolean mostrarRespuesta2 = false;
    private static boolean mostrarRespuesta3 = false;

    // Buffer para capturar entrada numerica del usuario
    private static final StringBuilder inputBuf = new StringBuilder();

    public static void main(String[] args) throws Exception {
        // Inicializar contadores de partidos y sexos
        for (String p : PARTIDOS) {
            votosPartido.put(p, new AtomicLong(0));
        }
        votosSexo.put("H", new AtomicLong(0));
        votosSexo.put("M", new AtomicLong(0));

        // Iniciar el hilo que lee continuamente el archivo de votos
        Path path = Paths.get("VOTOS.dat");
        startReader(path);

        // Crear e iniciar la interfaz grafica con Lanterna
        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        try { 
            interfaz(screen); 
        }
        finally { 
            screen.stopScreen(); 
        }
    }

    private static void startReader(Path path) {
        Thread t = new Thread(() -> {
            // Crear el archivo si no existe
            try { 
                if (!Files.exists(path)) Files.createFile(path); 
            } catch (IOException e) {}
            
            try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
                long pos = 0;
                
                while (true) {
                    long len = raf.length();
                    
                    if (len < pos) pos = len;
                    
                    if (len > pos) {
                        raf.seek(pos);
                        String line;
                        
                        while ((line = raf.readLine()) != null) {
                            line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                            leerLinea(line.trim());
                        }
                        pos = raf.getFilePointer();
                    }
                    
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private static void leerLinea(String line) {
        String[] p = line.split("\\|");
        if (p.length < 2) return;
        
        String curp = p[0];
        String partido = p[1];

        // Validar que el partido sea valido
        boolean partidoValido = false;
        for (String partidoVal : PARTIDOS) {
            if (partidoVal.equals(partido)) {
                partidoValido = true;
                break;
            }
        }
        if (!partidoValido) return;

        // Actualizar contadores principales
        totalVotos.incrementAndGet();
        votosPartido.get(partido).incrementAndGet();

        // Extraer y contar por sexo
        String sexo = sexoCurp(curp);
        if (sexo != null) {
            votosSexo.get(sexo).incrementAndGet();
        }

        // Extraer y contar por estado
        String edo = estadoCurp(curp);
        if (edo != null) {
            votosEstado.computeIfAbsent(edo, k -> new AtomicLong(0)).incrementAndGet();
        }

        // Conteo por edad y sexo (siempre se hace para todas las edades)
        Integer edad = edadCurp(curp);
        if (edad != null && sexo != null) {
            if ("H".equals(sexo)) {
                votosPorEdadHombre.computeIfAbsent(edad, k -> new AtomicLong(0)).incrementAndGet();
            } else if ("M".equals(sexo)) {
                votosPorEdadMujer.computeIfAbsent(edad, k -> new AtomicLong(0)).incrementAndGet();
            }
        }
    }

    // Metodo debug para ver que esta pasando con las edades
    private static void leerLineaDebug(String line) {
        String[] p = line.split("\\|");
        if (p.length < 2) return;
        
        String curp = p[0];
        String partido = p[1];

        // Validar que el partido sea valido
        boolean partidoValido = false;
        for (String partidoVal : PARTIDOS) {
            if (partidoVal.equals(partido)) {
                partidoValido = true;
                break;
            }
        }
        if (!partidoValido) return;

        // Extraer datos
        String sexo = sexoCurp(curp);
        Integer edad = edadCurp(curp);
        
        // Debug especifico para la edad consultada
        if (edad != null && edad == edadConsulta) {
            System.out.println("DEBUG ENCONTRADO: CURP=" + curp + " Edad=" + edad + " Sexo=" + sexo);
        }

        // Conteo por edad y sexo (siempre se hace para todas las edades)
        if (edad != null && sexo != null) {
            if ("H".equals(sexo)) {
                votosPorEdadHombre.computeIfAbsent(edad, k -> new AtomicLong(0)).incrementAndGet();
            } else if ("M".equals(sexo)) {
                votosPorEdadMujer.computeIfAbsent(edad, k -> new AtomicLong(0)).incrementAndGet();
            }
        }
    }

    // Extrae el sexo de la CURP (posicion 11)
    private static String sexoCurp(String curp) {
        if (curp.length() >= 11) {
            char sexoChar = curp.charAt(10);
            return (sexoChar == 'H' || sexoChar == 'M') ? String.valueOf(sexoChar) : null;
        }
        return null;
    }
    
    // Extrae el estado de la CURP (posiciones 12 y 13) 
    private static String estadoCurp(String curp) {
        return (curp.length() >= 13) ? curp.substring(11, 13) : null;
    }

    // Extrae la edad de la CURP y calcula la edad actual
    private static Integer edadCurp(String curp) {
        try {
            if (curp.length() < 10) return null;
            
            String yymmdd = curp.substring(4, 10);
            int yy = Integer.parseInt(yymmdd.substring(0, 2));
            int mm = Integer.parseInt(yymmdd.substring(2, 4));
            int dd = Integer.parseInt(yymmdd.substring(4, 6));
            
            if (mm < 1 || mm > 12 || dd < 1 || dd > 31) return null;
            
            // Determinar el siglo basado en el rango valido de nacimiento (1925-2007)
            int year;
            if (yy >= 25) {
                // 25-99 → 1925-1999
                year = 1900 + yy;
            } else {
                // 00-07 → 2000-2007
                year = 2000 + yy;
            }

            // Validar que el año este en el rango permitido
            if (year < 1925 || year > 2007) {
                return null;
            }
            
            LocalDate dob = LocalDate.of(year, mm, dd);
            if (dob.isAfter(LocalDate.now())) {
                return null;
            }
            
            int edad = java.time.Period.between(dob, LocalDate.now()).getYears();
            
            // Debug para algunas CURPs
            if (curp.startsWith("FSPN") || curp.startsWith("AKUW")) {
                System.out.println("DEBUG EDAD: CURP=" + curp + " yymmdd=" + yymmdd + 
                    " año=" + year + " edad=" + edad);
            }
            
            return edad;
        } catch (Exception e) { 
            return null;
        }
    }

    private static void interfaz(Screen screen) throws Exception {
        TextGraphics g = screen.newTextGraphics();
        screen.setCursorPosition(null);
        long last = 0;

        while (true) {
            // Procesar entrada del usuario 
            var key = screen.pollInput();
            if (key != null) {
                switch (key.getKeyType()) {
                    case Character -> {
                        char ch = key.getCharacter();
                        if (ch == 'q' || ch == 'Q') return;
                        if (Character.isDigit(ch) || ch == ' ' || ch == ',' || ch == '.') {
                            inputBuf.append(ch);
                        }
                    }
                    case Enter -> {
                        String s = inputBuf.toString().trim();
                        inputBuf.setLength(0);
                        
                        if (s.equals("1")) {
                            // Opcion 1: Votos por sexo
                            mostrarRespuesta1 = true;
                            mostrarRespuesta2 = false;
                            mostrarRespuesta3 = false;
                        } else if (s.equals("2")) {
                            // Opcion 2: Votos por estado
                            mostrarRespuesta1 = false;
                            mostrarRespuesta2 = true;
                            mostrarRespuesta3 = false;
                        } else if (s.startsWith("3")) {
                            // Opcion 3: Votos por edad y sexo
                            procesarOpcion3(s);
                        } else if (s.startsWith("edad=") && s.length() > 5) {
                            // Formato alternativo: edad=XX
                            try {
                                int nuevaEdad = Integer.parseInt(s.substring(5));
                                if (nuevaEdad >= 18 && nuevaEdad <= 100) {
                                    establecerEdadConsulta(nuevaEdad);
                                }
                            } catch (Exception e) {}
                        }
                    }
                    case Backspace -> {
                        if (inputBuf.length() > 0) {
                            inputBuf.setLength(inputBuf.length()-1);
                        }
                    }
                    default -> {}
                }
            }

            // Actualizar pantalla periodicamente
            long now = System.currentTimeMillis();
            if (now - last >= 120) {
                render(screen, g);
                last = now;
            }
            Thread.sleep(30);
        }
    }

    private static void procesarOpcion3(String input) {
        mostrarRespuesta1 = false;
        mostrarRespuesta2 = false;
        mostrarRespuesta3 = true;
        
        // Extraer la edad del input
        String[] partes = input.split("[ ,.]+");
        if (partes.length >= 2) {
            try {
                int nuevaEdad = Integer.parseInt(partes[1]);
                if (nuevaEdad >= 18 && nuevaEdad <= 100) {
                    establecerEdadConsulta(nuevaEdad);
                }
            } catch (NumberFormatException e) {
                // Si no se puede parsear, mantener la edad actual
            }
        }
        // Si solo se ingresa "3", mantener la edad actual (si existe)
    }

    private static void establecerEdadConsulta(int nuevaEdad) {
        if (edadConsulta != nuevaEdad) {
            edadConsulta = nuevaEdad;
            // Siempre reprocesar cuando cambias la edad
            new Thread(() -> {
                try {
                    Thread.sleep(100); // Pequena pausa para evitar conflictos
                    reprocesarArchivo();
                } catch (InterruptedException e) {}
            }).start();
        }
    }

    // Metodo para reprocesar todo el archivo cuando se cambia la edad de consulta
    private static void reprocesarArchivo() {
        Path path = Paths.get("VOTOS.dat");
        try {
            if (!Files.exists(path)) return;
            
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    leerLinea(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void render(Screen screen, TextGraphics g) throws IOException {
        var size = screen.getTerminalSize();
        int width = size.getColumns();
        int height = size.getRows();

        screen.clear();
        g.setForegroundColor(TextColor.ANSI.WHITE);

        // Mostrar numero total de votos
        g.putString(1, 1, "Numero total de votos = " + totalVotos.get());
        g.putString(1, 2, "");

        // Mostrar votos por partido con barras
        int y = 3;
        long total = Math.max(1, totalVotos.get());
        
        for (String p : PARTIDOS) {
            long c = votosPartido.getOrDefault(p, new AtomicLong(0)).get();
            int pct = (int)Math.round((c * 100.0) / total);

            g.setForegroundColor(colorPartido(p));
            
            g.putString(1, y, p);
            g.putString(15, y, String.format("%4d", c));
            
            int longitudBarra = Math.max(0, Math.min(20, pct * 20 / 100));
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < longitudBarra; i++) {
                bar.append("|");
            }
            g.putString(25, y, bar.toString());
            g.putString(50, y, String.format("[%2d%%]", pct));
            
            y++;
        }
        
        g.setForegroundColor(TextColor.ANSI.WHITE);
        y += 2;
        
        // Mostrar respuestas segun la opcion seleccionada
        if (mostrarRespuesta1) {
            g.putString(1, y++, "Respuesta 1 - Votos por sexo:");
            g.putString(1, y++, "Hombres (H): " + votosSexo.getOrDefault("H", new AtomicLong(0)).get());
            g.putString(1, y++, "Mujeres (M): " + votosSexo.getOrDefault("M", new AtomicLong(0)).get());
            y++;
        }
        
        if (mostrarRespuesta2) {
            g.putString(1, y++, "Respuesta 2 - Todas las entidades:");
            List<Map.Entry<String, AtomicLong>> estados = new ArrayList<>(votosEstado.entrySet());
            estados.sort((a, b) -> Long.compare(b.getValue().get(), a.getValue().get()));
            
            // Mostrar entidades en formato tabular (4 columnas por linea)
            int columnasPerLine = 4;
            int columnaWidth = 18; // Ancho de cada columna
            
            for (int i = 0; i < estados.size(); i++) {
                var e = estados.get(i);
                String entradaTexto = String.format("%-2s: %4d", e.getKey(), e.getValue().get());
                
                int columna = i % columnasPerLine;
                int x = 1 + (columna * columnaWidth);
                
                // Si es la primera columna de una nueva linea, incrementar y
                if (columna == 0 && i > 0) {
                    y++;
                }
                
                g.putString(x, y, entradaTexto);
            }
            
            // Asegurar que y se incremente despues de la ultima linea
            if (estados.size() > 0) {
                y++;
            }
            y++;
        }
        
        if (mostrarRespuesta3) {
            if (edadConsulta == 0) {
                g.putString(1, y++, "Respuesta 3 - Especifique una edad (18-100): ");
            } else {
                g.putString(1, y++, "Respuesta 3 - Votos para edad de " + edadConsulta + " años: ");
                long hombres = votosPorEdadHombre.getOrDefault(edadConsulta, new AtomicLong(0)).get();
                long mujeres = votosPorEdadMujer.getOrDefault(edadConsulta, new AtomicLong(0)).get();
                g.putString(1, y++, "Hombres (H): " + hombres);
                g.putString(1, y++, "Mujeres (M): " + mujeres);
            }
            y++;
        }
        
        // Preguntas al final
        int preguntasY = Math.max(y, height - 10);
        if (y > height - 10) {
            preguntasY = y;
        }
        
        g.putString(1, preguntasY++, "1.- ¿Cuantos votos totales se han realizado por sexo?");
        g.putString(1, preguntasY++, "2.- ¿Cuantos votos totales se han realizado por ciudadanos de cada uno de los estados de la republica?");
        g.putString(1, preguntasY++, "3.- ¿Cuantos votos se han realizado por ciudadanos de x años de edad, separados por sexo?");
        preguntasY++;
        g.putString(1, preguntasY++, "Edad actual para pregunta 3 (18-100): " + (edadConsulta == 0 ? "NO ESPECIFICADA" : edadConsulta + " años"));
        preguntasY++;
        g.putString(1, preguntasY, "Ingresa una opcion: " + inputBuf.toString());

        screen.refresh();
    }

    private static TextColor colorPartido(String p) {
        return switch (p) {
            case "PAN"           -> TextColor.ANSI.BLUE;
            case "PRI"           -> TextColor.ANSI.MAGENTA;
            case "PRD"           -> TextColor.ANSI.CYAN;
            case "EVA01"         -> TextColor.ANSI.MAGENTA_BRIGHT;
            case "PT"            -> TextColor.ANSI.BLUE_BRIGHT;
            case "ATWTMVTVFTV"   -> TextColor.ANSI.MAGENTA;
            case "MORENA"        -> TextColor.ANSI.CYAN;
            default              -> TextColor.ANSI.WHITE;
        };
    }
}

/*  Proyecto 3
 * Munive hernandez Erika Natalia
 * 7CM4
 */
