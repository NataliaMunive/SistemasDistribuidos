import java.io.*;
import java.util.*;

public class contadorCaracteres
{
    public static void main(String[] args) throws Exception
    {
        //Creating BufferedReader for Input
        BufferedReader bfri = new BufferedReader(new InputStreamReader(System.in));

        // Ruta por defecto
        String path = "/home/natalia/SD/clase11/El_viejo_y_el_mar.txt";

        // Creating an object of BufferedReader class
        BufferedReader bfro = new BufferedReader(new FileReader(path));

        // Mapa para contar caracteres
        HashMap<Character, Integer> contador = new HashMap<>();
        // Declaring a string variable
        String st; 
        while ((st = bfro.readLine()) != null) {
            // contar caracteres de cada linea
            for (char cacarcter : st.toCharArray()) {
                contador.put(cacarcter, contador.getOrDefault(cacarcter, 0) + 1); 
            }
        }
        bfro.close();

        System.out.println("Numero de caracteres distintos: " + contador.size());
        //System.out.println("Mapa de caracteres y ocurrencias: " + contador);

            // almacenar en arraylist
            ArrayList<HashMap.Entry<Character, Integer>> lista = new ArrayList<>(contador.entrySet());
            // Comparator para ordenar de mayor a menor
            Comparator<HashMap.Entry<Character, Integer>> comparador = new Comparator<HashMap.Entry<Character, Integer>>() {
                @Override
                public int compare(HashMap.Entry<Character, Integer> a, HashMap.Entry<Character, Integer> b) {
                    return b.getValue().compareTo(a.getValue());
                }
            };
            lista.sort(comparador);

            System.out.println("Caracteres ordenados:");
            for (HashMap.Entry<Character, Integer> entry : lista) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
    }
}