import java.util.*;

class Clase12pt1{
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]); // CURPs por ArrayList
        int m = Integer.parseInt(args[1]); // número de ArrayLists

        // arayList de objetos ArrayList<String>
        ArrayList<ArrayList<String>> arregloListas = new ArrayList<>();

        // m ArrayLists con n CURPs aleatorias cada uno
        for (int i = 0; i < m; i++) {
            ArrayList<String> curps = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                curps.add(getCURP());
            }
            arregloListas.add(curps);
        }

        System.out.println("--- CURPS ALEATORIAS---");
        for (int i = 0; i < arregloListas.size(); i++) {
            System.out.println("ArrayList " + (i + 1) + ":");
            for (String curp : arregloListas.get(i)) {
                System.out.println(curp);
            }
            System.out.println();
        }

        System.out.println("--- CURPS ORDENADAS---");
        for (int i = 0; i < arregloListas.size(); i++) {
            System.out.println("ArrayList " + (i + 1) + " ordenado:");
            ordenarEImprimir(arregloListas.get(i));
            System.out.println();
        }
    }

    // Recibe un ArrayList<String> y lo ordena en un ArrayList temporal usando Iterator
    private static void ordenarEImprimir(ArrayList<String> curps) {
        // Crear ArrayList temporal para el ordenamiento para que no se modifique el original
        ArrayList<String> temporal = new ArrayList<>();
        
        // Usar iterator de ordenamiento de la clase 9
        for (String curp : curps) {
            // insertar ya ordenado usando Iterator
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
        
        // Imprimir el ArrayList temporal ya ordenado
        for (String c : temporal) {
            System.out.println(c);
        }
    }

    private static String getCURP()
    {
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