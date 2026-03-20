/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg8.memoria;

import java.io.File;

/**
 *
 * @author diego
 */
public class GestorDeArchivos {
    private File[] portapapeles;

    public GestorDeArchivos() {
        this.portapapeles = new File[0];
    }
    
    //crear carpeta
    
    public ResultadoOperacion crearCarpeta(File destino, String nombre){
        if(destino == null || !destino.isDirectory()){
            return new ResultadoOperacion(false, "Seleccione una carpeta en el arbol primero",0,0);
        }
         if(nombre == null || !nombre.trim().isEmpty()){
            return new ResultadoOperacion(false, "El nombre no puede estar vacio",0,0);
        }
          if(){
            return new ResultadoOperacion(false, "Seleccione una carpeta en el arbol primero",0,0);
        }
    }
    private boolean nombreValido(String nombre){
        
    }
}
