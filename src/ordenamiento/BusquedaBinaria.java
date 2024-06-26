package ordenamiento;

import java.util.ArrayList;
import java.util.List;

public class BusquedaBinaria {

    public static Boolean busquedaBinaria(List<String> corpusOrdenado, String elemento) {
        int inicio = 0;
        int fin = corpusOrdenado.size() - 1;
    
        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            if (corpusOrdenado.get(medio).equals(elemento)) {
                return true;
            } else if (corpusOrdenado.get(medio).compareTo(elemento) < 0) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return false;
    }  
    
}
