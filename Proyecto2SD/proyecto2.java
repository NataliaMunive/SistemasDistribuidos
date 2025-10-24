/*  Proyecto 2
 * Munive hernandez Erika Natalia
 * 7CM4
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class proyecto2 extends JFrame {

    public proyecto2(int cantidadAsteroides) {
        setTitle("Proyecto 2 de Nat");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new LienzoJuego(cantidadAsteroides));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // main
    public static void main(String[] args) {
        final int n = (args.length > 0) ? convertirAEntero(args[0], 20) : 20;
        SwingUtilities.invokeLater(() -> new proyecto2(n));
    }

    // convertir cadena a entero con valor de 20
    private static int convertirAEntero(String texto, int predeterminado) {
        try {
            return Integer.parseInt(texto);
        } catch (Exception e) {
            return predeterminado;
        }
    }

    // lienzo del juego
    private static class LienzoJuego extends JPanel {
        private final Timer temporizador = new Timer(16, e -> actualizar()); // ~60 FPS

        // zonas de entrada y salida
        private static final int entradaX = 20;
        private static final int margenDerecho = 20;

        // posicion y movimiento de la nave
        private double x = entradaX; // donde esta la nave horizontalmente
        private double y;             // donde esta la nave verticalmente  
        private double velocidadX = 0; // que tan rapido va
        private double duracionFrame = 1.0 / 60.0; // cuanto dura cada frame
        private int salidaX = -1;     // donde tiene que llegar
        private boolean naveTerminada = false; // si ya termino el juego

        // radio de colision de la nave
        private static final int radioNave = 18; 

        // asteroides
        private final int cantidadAsteroides;
        private final ArrayList<AsteroidePoligonal> listaAsteroides = new ArrayList<>();
        private final Random aleatorio = new Random();

        // estados del juego
        private enum Estado { CORRIENDO, EXITO, DERROTA }
        private Estado estado = Estado.CORRIENDO;
        private double avanceFinal = 0; // guardar % al terminar

        // configuracion para la nave
        private static final double velocidadMaxima = 300.0;
        private static final double aceleracion = 25.0;

        // constructor para cantidad de asteroides
        LienzoJuego(int cantidadAsteroides) {
            this.cantidadAsteroides = Math.max(0, cantidadAsteroides);
            setBackground(Color.black);
            temporizador.start();
        }

        // hacer los asteroides aleatorios
        private void generarAsteroides(int ancho, int alto) {
            listaAsteroides.clear();
            int intentos = 0;

            // crear asteroides hasta tener los que necesitamos
            while (listaAsteroides.size() < cantidadAsteroides && intentos < 1000) {
                intentos++;
                int tamaño = 20 + aleatorio.nextInt(30);   // tamaño random
                int puntas = 6 + aleatorio.nextInt(8);     // entre 6 y 13 puntas

                // poner en posicion random
                double posX = tamaño + aleatorio.nextDouble() * (ancho - 2 * tamaño);
                double posY = tamaño + aleatorio.nextDouble() * (alto - 2 * tamaño);

                // no poner cerca de donde empieza la nave
                double distanciaDesdeEntrada = Math.sqrt((posX - entradaX) * (posX - entradaX) + 
                                                       (posY - alto/2) * (posY - alto/2));
                if (distanciaDesdeEntrada < 100) continue;

                // velocidades random
                double velX = (aleatorio.nextBoolean() ? 1 : -1) * (20 + aleatorio.nextDouble() * 80);
                double velY = (aleatorio.nextBoolean() ? 1 : -1) * (10 + aleatorio.nextDouble() * 60);
                double giro = (aleatorio.nextBoolean() ? 1 : -1) * aleatorio.nextDouble();

                // puntas de diferentes tamaños
                double[] tamañoPuntas = new double[puntas];
                for (int i = 0; i < puntas; i++) {
                    tamañoPuntas[i] = tamaño * (0.7 + aleatorio.nextDouble() * 0.6);
                }

                listaAsteroides.add(new AsteroidePoligonal(posX, posY, velX, velY, tamañoPuntas, giro));
            }
        }

        // aqui se actualiza todo el juego cada frame
        private void actualizar() {
            // configurar al inicio
            if (salidaX < 0) {
                int ancho = getWidth() > 0 ? getWidth() : 1280;
                int alto = getHeight() > 0 ? getHeight() : 720;
                salidaX = ancho - margenDerecho;
                y = alto / 2.0; // nave en el centro vertical
                generarAsteroides(ancho, alto);
            }

            if (estado == Estado.CORRIENDO) {
                
                // mover la nave con inteligencia basica
                if (!naveTerminada) {
                    // decidir si acelerar o frenar segun asteroides
                    double aceleracionActual = decidirMovimiento();
                    
                    // aplicar aceleracion (movimiento uniformemente acelerado)
                    velocidadX += aceleracionActual * duracionFrame;
                    if (velocidadX < 10) velocidadX = 10; // velocidad minima
                    if (velocidadX > velocidadMaxima) velocidadX = velocidadMaxima;
                    
                    // mover la nave
                    x += velocidadX * duracionFrame;

                    // ganaste si llegas al final
                    if (x >= salidaX) {
                        x = salidaX;
                        naveTerminada = true;
                        estado = Estado.EXITO;
                        avanceFinal = 1.0;
                    }
                }

                // mover todos los asteroides
                int ancho = getWidth();
                int alto = getHeight();
                for (AsteroidePoligonal asteroide : listaAsteroides) {
                    // mover posicion
                    asteroide.centroX += asteroide.velocidadX * duracionFrame;
                    asteroide.centroY += asteroide.velocidadY * duracionFrame;
                    asteroide.angulo += asteroide.rotacion * duracionFrame;

                    // si se sale de la pantalla, aparece del otro lado
                    if (asteroide.centroX < -50) asteroide.centroX = ancho + 50;
                    if (asteroide.centroX > ancho + 50) asteroide.centroX = -50;
                    if (asteroide.centroY < -50) asteroide.centroY = alto + 50;
                    if (asteroide.centroY > alto + 50) asteroide.centroY = -50;
                }

                // ver si chocaste
                if (colisionConAsteroide()) {
                    estado = Estado.DERROTA;
                    naveTerminada = true;
                    avanceFinal = calcularAvance();
                }
            }

            repaint();
        }

        // inteligencia basica de la nave: acelerar o frenar segun asteroides
        private double decidirMovimiento() {
            boolean hayPeligroCerca = hayAsteroidesPeligrosos();
            
            if (hayPeligroCerca) {
                // si hay asteroides cerca, frenar
                return -aceleracion * 1.5; // desacelerar mas fuerte
            } else {
                // si esta libre, acelerar normal
                return aceleracion;
            }
        }
        
        // revisar si hay asteroides peligrosos en el camino
        private boolean hayAsteroidesPeligrosos() {
            for (AsteroidePoligonal asteroide : listaAsteroides) {
                // calcular distancia horizontal y vertical
                double distanciaX = asteroide.centroX - x;
                double distanciaY = Math.abs(asteroide.centroY - y);
                
                // solo considerar asteroides que estan adelante y cerca verticalmente
                if (distanciaX > 0 && distanciaX < 150 && distanciaY < 100) {
                    return true;
                }
            }
            return false;
        }



        // detectar colision 
        private boolean colisionConAsteroide() {
            for (AsteroidePoligonal asteroide : listaAsteroides) {
                // calcular distancia entre centros
                double distanciaX = x - asteroide.centroX;
                double distanciaY = y - asteroide.centroY;
                double distancia = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);
                
                // colision si la distancia es menor que los radios sumados
                if (distancia < radioNave + asteroide.radioMax()) {
                    return true;
                }
            }
            return false;
        }

        // avance (0% a 100%)
        private double calcularAvance() {
            double distanciaTotal = salidaX - entradaX;
            double distanciaRecorrida = x - entradaX;
            double porcentaje = distanciaRecorrida / distanciaTotal;
            
            if (porcentaje < 0) porcentaje = 0;
            if (porcentaje > 1) porcentaje = 1;
            return porcentaje;
        }

        // aqui se dibuja todo en la pantalla
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // hacer que se vea mas suave y menos pixelado
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();

            // dibujar las areas de inicio y fin
            g2.setColor(new Color(255, 255, 255, 100));
            g2.drawRect(5, (int) y - 50, 30, 100);
            g2.drawRect(ancho - 35, (int) y - 50, 30, 100);

            // dibujar asteroides
            g2.setColor(Color.pink);
            g2.setStroke(new BasicStroke(2f));
            for (AsteroidePoligonal asteroide : listaAsteroides) {
                g2.drawPolygon(asteroide.crearPoligono());
            }

            // dibujar la nave
            Polygon nave = new Polygon();
            nave.addPoint((int) x + 20, (int) y);
            nave.addPoint((int) x - 15, (int) y - 10);
            nave.addPoint((int) x - 15, (int) y + 10);
            g2.setColor(new Color(255, 20, 147));
            g2.fillPolygon(nave);

            // mostrar informacion
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(1f));
            int porcentaje = (int) (calcularAvance() * 100);
            g2.drawString("Avance: " + porcentaje + "%", 20, 30);
            g2.drawString("Asteroides: " + cantidadAsteroides, 20, 50);
            g2.drawString("Velocidad: " + (int) velocidadX, 20, 70);

            // mostrar mensajes de fin de juego
            if (estado == Estado.DERROTA) {
                mostrarMensaje(g2, "¡CHOCASTE! :( - Llegaste al: " + (int) (avanceFinal * 100) + "%");
            } else if (estado == Estado.EXITO) {
                mostrarMensaje(g2, "¡GANASTE! :D - Completaste el 100%");
            }
        }

        // mostrar mensaje en el centro de la pantalla
        private void mostrarMensaje(Graphics2D g2, String mensaje) {
            FontMetrics fm = g2.getFontMetrics();
            int anchoVentana = getWidth();
            int altoVentana = getHeight();
            int anchoMensaje = fm.stringWidth(mensaje);
            int altoMensaje = fm.getAscent();
            
            // centrar el mensaje
            int x = (anchoVentana - anchoMensaje) / 2;
            int y = (altoVentana + altoMensaje) / 2;
            g2.drawString(mensaje, x, y);
        }

        // clase para los asteroides
        private static class AsteroidePoligonal {
            double centroX, centroY; // posicion del asteroide
            double velocidadX, velocidadY; // que tan rapido se mueve
            double angulo; // rotacion
            double rotacion; // velocidad de rotacion
            final double[] radios; // tamaños de cada punta

            // crear asteroide
            AsteroidePoligonal(double centroX, double centroY, double velocidadX, double velocidadY, double[] radios, double rotacion) { 
                this.centroX = centroX; 
                this.centroY = centroY; 
                this.velocidadX = velocidadX; 
                this.velocidadY = velocidadY; 
                this.radios = radios;
                this.rotacion = rotacion;
                this.angulo = 0.0; 
            }

            // cual es la punta mas grande del asteroide
            int radioMax() {
                int mayor = 0;
                for (double r : radios) {
                    if (r > mayor) mayor = (int) r;
                }
                return mayor;
            }

            // dibujar el asteroide como poligono
            Polygon crearPoligono() {
                int puntas = radios.length;
                int[] puntosX = new int[puntas];
                int[] puntosY = new int[puntas];
                
                for (int i = 0; i < puntas; i++) {
                    double anguloPunta = angulo + (i * 2 * Math.PI / puntas);
                    puntosX[i] = (int) (centroX + Math.cos(anguloPunta) * radios[i]);
                    puntosY[i] = (int) (centroY + Math.sin(anguloPunta) * radios[i]);
                }
                return new Polygon(puntosX, puntosY, puntas);
            }
        }
    }
}
