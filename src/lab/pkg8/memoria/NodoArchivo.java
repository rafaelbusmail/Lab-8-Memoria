/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg8.memoria;

import java.util.Date;

/**
 *
 * @author riche
 */
public class NodoArchivo {
    private String nombre;
    private String ruta;
    private String tipo;
    private Date fechaModificacion;
    private long tamano;
    private boolean carpeta;
    private NodoArchivo siguiente;
    
    public NodoArchivo(String nombre, String ruta, String tipo, Date fechaModificacion,long tamano,boolean carpeta){
        this.nombre = nombre;
        this.ruta=ruta;
        this.tipo=tipo;
        this.fechaModificacion=fechaModificacion;
        this.tamano = tamano;
        this.carpeta = carpeta;
        this.siguiente = null;
    }
     public NodoArchivo(NodoArchivo otro) {
        this.nombre = otro.nombre;
        this.ruta = otro.ruta;
        this.tipo = otro.tipo;
        this.fechaModificacion = otro.fechaModificacion;
        this.tamano = otro.tamano;
        this.carpeta = otro.carpeta;
        this.siguiente = null;
    }   
    
     public String getNombre(){
         return nombre;
     }
     
     public void setNombre(String nombre){
         this.nombre=nombre;
     }
      public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }
    
    public void setFechaModificacion(Date fechaModificacion){
        this.fechaModificacion=fechaModificacion;
    }
    public long getTamano() {
        return tamano;
    }

    public void setTamano(long tamano) {
        this.tamano = tamano;
    }

    public boolean isCarpeta() {
        return carpeta;
    }

    public void setCarpeta(boolean carpeta) {
        this.carpeta = carpeta;
    }

    public NodoArchivo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoArchivo siguiente) {
        this.siguiente = siguiente;
    }
        public Object[] toRow() {
        return new Object[] { nombre, fechaModificacion, tipo, tamano };
    }

    @Override
    public String toString() {
        return nombre + " - " + tipo;
    }
    
}
