package lab.pkg8.memoria;

public class MergeSort implements Ordenador {

    @Override
    public void ordenar(ListaEnlazadaArchivos lista, Criterio criterio) {
        if (lista == null || lista.estaVacia() || lista.tamano() == 1) {
            return;
        }
        if (criterio != Criterio.FECHA) {
            throw new IllegalArgumentException(
                    "MergeSort solo soporta el criterio FECHA. "
                    + "Usa BubbleSort para: NOMBRE, TIPO, TAMANO.");
        }
        NodoArchivo nuevaCabeza = mergeSort(lista.getCabeza());
        lista.setCabeza(nuevaCabeza);
    }

    private NodoArchivo mergeSort(NodoArchivo cabeza) {
        if (cabeza == null || cabeza.getSiguiente() == null) {
            return cabeza;
        }
        NodoArchivo medio = obtenerMedio(cabeza);
        NodoArchivo siguienteMitad = medio.getSiguiente();
        medio.setSiguiente(null);

        NodoArchivo izquierda = mergeSort(cabeza);
        NodoArchivo derecha = mergeSort(siguienteMitad);
        return mezclar(izquierda, derecha);
    }

    private NodoArchivo obtenerMedio(NodoArchivo cabeza) {
        if (cabeza == null) {
            return null;
        }
        NodoArchivo lento = cabeza;
        NodoArchivo rapido = cabeza.getSiguiente();
        while (rapido != null && rapido.getSiguiente() != null) {
            lento = lento.getSiguiente();
            rapido = rapido.getSiguiente().getSiguiente();
        }
        return lento;
    }

    private NodoArchivo mezclar(NodoArchivo a, NodoArchivo b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }

        NodoArchivo resultado;
        if (a.getFechaModificacion().compareTo(b.getFechaModificacion()) <= 0) {
            resultado = a;
            resultado.setSiguiente(mezclar(a.getSiguiente(), b));
        } else {
            resultado = b;
            resultado.setSiguiente(mezclar(a, b.getSiguiente()));
        }
        return resultado;
    }
}
