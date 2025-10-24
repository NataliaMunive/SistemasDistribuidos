/*  Proyecto 3
 * Munive hernandez Erika Natalia
 * 7CM4
 */
    
    import java.util.*;

    class MainCURPS
    {
        private static ArrayList<String> CURPs = new ArrayList<>();  
        public static void main(String[] args)
        {
            int num = Integer.parseInt(args[0]);

            for (int i = 0; i < num; i++) {
                String curp = getCURP();
                
                // Insertar en orden ascendente
                int pos = 0;
                Iterator<String> it = CURPs.iterator();
                while (it.hasNext()) {
                    String actual = it.next();
                    if (curp.substring(0, 4).compareTo(actual.substring(0, 4)) > 0) {
                        pos++;
                    } else {
                        break;
                    }
                }

                CURPs.add(pos, curp);
                // Imprimir lista por cada curp
                System.out.println("Lista tras insertar CURP " + (i+1) + ":");
                for (String c : CURPs) {
                    System.out.println(c);
                }
                System.out.println();
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
            
            // Generar primeras 4 letras (apellidos y nombre)
            for (int i = 1; i < 5; i++) {
                indice = (int) (Letra.length()* Math.random());
                sb.append(Letra.charAt(indice));        
            }
            
            // Generar fecha válida (YYMMDD) - rango 1925-2007
            String fechaValida = generarFechaValida();
            sb.append(fechaValida);
            
            // Generar sexo (H o M)
            indice = (int) (Sexo.length()* Math.random());
            sb.append(Sexo.charAt(indice));        
            
            // Generar entidad federativa
            sb.append(Entidad[(int)(Math.random()*32)]);
            
            // Generar consonantes internas (3 letras)
            for (int i = 14; i < 17; i++) {
                indice = (int) (Letra.length()* Math.random());
                sb.append(Letra.charAt(indice));        
            }
            
            // Generar dígito verificador (2 números)
            for (int i = 17; i < 19; i++) {
                indice = (int) (Numero.length()* Math.random());
                sb.append(Numero.charAt(indice));        
            }
            
            return sb.toString();
        }
        
        private static String generarFechaValida() {
            // Generar año entre 1925 y 2007
            int year = 1925 + (int)(Math.random() * (2007 - 1925 + 1));
            
            // Convertir a formato YY
            String yy = String.format("%02d", year % 100);
            
            // Generar mes valido (01-12)
            int mes = 1 + (int)(Math.random() * 12);
            String mm = String.format("%02d", mes);
            
            // Generar dia valido
            int maxDias;
            if (mes == 2) {
                // Febrero - considerar años bisiestos
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    maxDias = 29; // Año bisiesto
                } else {
                    maxDias = 28; // Año normal
                }
            } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
                maxDias = 30; // Abril, Junio, Septiembre, Noviembre
            } else {
                maxDias = 31; // Enero, Marzo, Mayo, Julio, Agosto, Octubre, Diciembre
            }
            
            int dia = 1 + (int)(Math.random() * maxDias);
            String dd = String.format("%02d", dia);
            
            return yy + mm + dd;
        }           
    }

/*  Proyecto 3
 * Munive hernandez Erika Natalia
 * 7CM4
 */
