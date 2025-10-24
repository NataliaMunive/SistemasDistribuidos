package contrycatch;

public class Rectangulo {
    private Coordenada superiorIzq, inferiorDer;

    public Rectangulo(){
        superiorIzq = new Coordenada(0,0);
        inferiorDer = new Coordenada(0,0);
    }

    public Rectangulo(double xSupIzq, double ySupIzq, double xInfDer, double yInfDer){
        superiorIzq = new Coordenada(xSupIzq, ySupIzq);
        inferiorDer = new Coordenada(xInfDer, yInfDer);        
    }

    // Constructor con try-catch
    public Rectangulo(Coordenada supIzq, Coordenada infDer){
        try {
            // supIzq debe estar arriba a la izquierda
            if (supIzq.abcisa() >= infDer.abcisa() || supIzq.ordenada() <= infDer.ordenada()) {
                throw new Exception("Las coordenadas no forman un rectángulo válido.");
            }

            //se crean las coordenadas
            this.superiorIzq = new Coordenada(supIzq.abcisa(), supIzq.ordenada());
            this.inferiorDer = new Coordenada(infDer.abcisa(), infDer.ordenada());

        } catch (Exception e) {
            // Si hay error, mostramos mensaje y se crea un rectángulo vacío
            System.out.println("Error: " + e.getMessage());
            this.superiorIzq = new Coordenada(0,0);
            this.inferiorDer = new Coordenada(0,0);
        }
    }

    //Metodo getter de la coordenada superior izquierda
    public Coordenada superiorIzquierda( ) { return superiorIzq; }
    //Metodo getter de la coordenada inferior derecha
    public Coordenada inferiorDerecha( ) { return inferiorDer; }

    //Sobreescritura del método de la superclase objeto para imprimir con System.out.println( )
    @Override
    public String toString( ) {
        return "Esquina superior izquierda: " + superiorIzq + "\tEsquina inferior derecha:" + inferiorDer + "\n";
    }
}
