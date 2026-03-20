/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg8.memoria;

/**
 *
 * @author riche
 */
public class ListaEnlazadaArchivos {
    private NodoArchivo cabeza;
    private int tamano;
    
    public ListaEnlazadaArchivos(){
        this.cabeza= null;
        this.tamano=0;
    }
    public void agregar(NodoArchivo nuevo) {
    if (nuevo == null) {
        return;
    }
    NodoArchivo copia = new NodoArchivo(nuevo);
    if (cabeza == null) {
        cabeza = copia;
    } else {
        NodoArchivo aux = cabeza;
        while (aux.getSiguiente() != null) {
            aux = aux.getSiguiente();
        }
        aux.setSiguiente(copia);
    }
    tamano++;
}
    public boolean eliminar(String ruta) {
        if (cabeza == null || ruta == null) {
            return false;
        }

        if (cabeza.getRuta().equals(ruta)) {
            cabeza = cabeza.getSiguiente();
            tamano--;
            return true;
        }
         NodoArchivo anterior = cabeza;
        NodoArchivo actual = cabeza.getSiguiente();

        while (actual != null) {
            if (actual.getRuta().equals(ruta)) {
                anterior.setSiguiente(actual.getSiguiente());
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }

        return false;
    }
    public NodoArchivo obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            return null;
        }

        NodoArchivo aux = cabeza;
        int contador = 0;

        while (aux != null) {
            if (contador == indice) {
                return aux;
            }
            aux = aux.getSiguiente();
            contador++;
        }

        return null;
    }
     public NodoArchivo buscarPorRuta(String ruta) {
        NodoArchivo aux = cabeza;

        while (aux != null) {
            if (aux.getRuta().equals(ruta)) {
                return aux;
            }
            aux = aux.getSiguiente();
        }

        return null;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamano() {
        return tamano;
    }

    public NodoArchivo getCabeza() {
        return cabeza;
    }

    public void setCabeza(NodoArchivo cabeza) {
        this.cabeza = cabeza;
        recalcularTamano();
    }

    public NodoArchivo[] toArray() {
        NodoArchivo[] arreglo = new NodoArchivo[tamano];
        NodoArchivo aux = cabeza;
        int i = 0;

        while (aux != null) {
            arreglo[i] = aux;
            aux = aux.getSiguiente();
            i++;
        }

        return arreglo;
    }

    public void limpiar() {
        cabeza = null;
        tamano = 0;
    }

    public void recalcularTamano() {
        int contador = 0;
        NodoArchivo aux = cabeza;

        while (aux != null) {
            contador++;
            aux = aux.getSiguiente();
        }

        tamano = contador;
    }

    
}
