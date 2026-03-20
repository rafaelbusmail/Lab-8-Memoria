package lab.pkg8.memoria;

public interface Ordenador {

    void ordenar(ListaEnlazadaArchivos lista, Criterio criterio);

    static Ordenador para(Criterio criterio) {
        if (criterio == Criterio.FECHA) {
            return new MergeSort();
        }
        return new BubbleSort();
    }
}
