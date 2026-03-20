/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg8.memoria;

/**
 *
 * @author riche
 */
public class BubbleSort implements Ordenador {
    @Override
    public void ordenar(ListaEnlazadaArchivos lista, Criterio criterio) {
        if (lista == null || lista.estaVacia() || lista.tamano() == 1) {
            return;
        }
        boolean huboCambio;
        do {
            huboCambio = false;
            NodoArchivo actual = lista.getCabeza();
            while (actual != null && actual.getSiguiente() != null) {
                if (comparar(actual, actual.getSiguiente(), criterio) > 0) {
                    intercambiarDatos(actual, actual.getSiguiente());
                    huboCambio = true;
                }
                actual = actual.getSiguiente();
            }
        } while (huboCambio);
    }
    private int comparar(NodoArchivo a, NodoArchivo b, Criterio criterio) {
        switch (criterio) {
            case NOMBRE:
                return a.getNombre().compareToIgnoreCase(b.getNombre());
            case TIPO:
                return a.getTipo().compareToIgnoreCase(b.getTipo());
            case TAMANO:
                return Long.compare(a.getTamano(), b.getTamano());
            default:
                return 0;
        }
    }
    private void intercambiarDatos(NodoArchivo a, NodoArchivo b) {
        String nombre = a.getNombre();
        String ruta = a.getRuta();
        String tipo = a.getTipo();
        java.util.Date fecha = a.getFechaModificacion();
        long tamano = a.getTamano();
        boolean carpeta = a.isCarpeta();
        a.setNombre(b.getNombre());
        a.setRuta(b.getRuta());
        a.setTipo(b.getTipo());
        a.setFechaModificacion(b.getFechaModificacion());
        a.setTamano(b.getTamano());
        a.setCarpeta(b.isCarpeta());
        b.setNombre(nombre);
        b.setRuta(ruta);
        b.setTipo(tipo);
        b.setFechaModificacion(fecha);
        b.setTamano(tamano);
        b.setCarpeta(carpeta);
    }
}