public class PruebaRectangulo {
    public static void main (String[] args) {
        
        // PRIMER RECTANGULO: usando el constructor con 4 valores
        Rectangulo rect1 = new Rectangulo(2,3,5,1);
        double ancho, alto;

        System.out.println("Calculando el area de un rectangulo dadas sus coordenadas en un plano cartesiano:");
        System.out.println(rect1);
        alto = rect1.superiorIzquierda().ordenada() - rect1.inferiorDerecha().ordenada();
        ancho = rect1.inferiorDerecha().abcisa() - rect1.superiorIzquierda().abcisa();
        double area1 = ancho * alto;
        System.out.println("El area del rectangulo (rect1) es = " + area1);

        // SEGUNDO RECTANGULO: usando el constructor con dos Coordenadas
        Coordenada supIzq = new Coordenada(2, 3);
        Coordenada infDer = new Coordenada(5, 1);
        Rectangulo rect2 = new Rectangulo(supIzq, infDer);  // usa nuevo constructor

        double alto2 = rect2.superiorIzquierda().ordenada() - rect2.inferiorDerecha().ordenada();
        double ancho2 = rect2.inferiorDerecha().abcisa() - rect2.superiorIzquierda().abcisa();
        double area2 = ancho2 * alto2;

        System.out.println("Rectangulo creado con el nuevo constructor:");
        System.out.println(rect2);
        System.out.println("El area del rectangulo (rect2) es = " + area2);

        System.out.println("\nProbando rectangulo con coordenadas invalidas:");

        Coordenada c1 = new Coordenada(5, 1); // Esta debería ser la inferior derecha
        Coordenada c2 = new Coordenada(2, 3); // Esta debería ser la superior izquierda, pero las pusimos al revés

        Rectangulo rectInvalido = new Rectangulo(c1, c2);  // Se activará el try-catch
        System.out.println("Rectangulo con coordenadas invalidas:");
        System.out.println(rectInvalido);

    }
}
