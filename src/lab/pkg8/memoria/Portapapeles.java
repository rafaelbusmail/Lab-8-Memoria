/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg8.memoria;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author riche
 */
public class Portapapeles {
  private List<String> rutasCopiadas;
    private String carpetaOrigen;
    private boolean activo;

    public Portapapeles() {
        rutasCopiadas = new ArrayList<>();
        carpetaOrigen = null;
        activo = false;
    }

    public void copiar(List<String> rutas, String carpetaOrigen) {
        this.rutasCopiadas.clear();

        if (rutas != null) {
            this.rutasCopiadas.addAll(rutas);
        }

        this.carpetaOrigen = carpetaOrigen;
        this.activo = !this.rutasCopiadas.isEmpty();
    }

    public void copiarUna(String ruta, String carpetaOrigen) {
        this.rutasCopiadas.clear();

        if (ruta != null && !ruta.isBlank()) {
            this.rutasCopiadas.add(ruta);
            this.carpetaOrigen = carpetaOrigen;
            this.activo = true;
        } else {
            this.carpetaOrigen = null;
            this.activo = false;
        }
    }

    public List<String> getRutasCopiadas() {
        return new ArrayList<>(rutasCopiadas);
    }

    public String getCarpetaOrigen() {
        return carpetaOrigen;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean estaVacio() {
        return rutasCopiadas.isEmpty();
    }

    public void limpiar() {
        rutasCopiadas.clear();
        carpetaOrigen = null;
        activo = false;
    }
}
