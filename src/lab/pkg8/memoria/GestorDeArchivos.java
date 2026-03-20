/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg8.memoria;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *
 * @author diego
 */
public class GestorDeArchivos {
    private File[] portapapeles;

    public GestorDeArchivos() {
        this.portapapeles = new File[0];
    }
    

    
    public ResultadoOperacion crearCarpeta(File destino, String nombre){
        if(destino == null || !destino.isDirectory()){
            return new ResultadoOperacion(false, "Seleccione una carpeta en el arbol primero",0,0);
        }
         if(nombre == null || nombre.trim().isEmpty()){
            return new ResultadoOperacion(false, "El nombre no puede estar vacio",0,0);
        }
          if(!nombreValido(nombre)){
            return new ResultadoOperacion(false, "Nombre invalido, Evidte: \\ / : * ? \" < > | ",0,0);
        }
          File nueva = new File(destino, nombre.trim());
          if(nueva.exists()){
              return new ResultadoOperacion(false, "Ya existe una carpeta llamada \""+nombre.trim()+"\".",0,0);
          }
          if(nueva.mkdir()){
              return new ResultadoOperacion(true, "Carpeta \""+ nombre.trim()+"\" creada exitosamente,",1,0);
          }else{
             return new ResultadoOperacion(false, "No se pudo crear la carpeta.",0,1); 
          }
    }
    
    public ResultadoOperacion renombrar(File archivo, String nuevoNombre){
        if(archivo == null || !archivo.exists()){
            return new ResultadoOperacion(false, "El elemento seleccionado no existe.",0,0);
        }
         if(nuevoNombre == null || nuevoNombre.trim().isEmpty()){
            return new ResultadoOperacion(false, "El nuevo nombre no puede estar vacio",0,0);
        }
          if(!nombreValido(nuevoNombre)){
            return new ResultadoOperacion(false, "Nombre invalido, Evidte: \\ / : * ? \" < > | ",0,0);
        }
          File destino = new File(archivo.getParent(), nuevoNombre.trim());
          if(destino.exists()){
              return new ResultadoOperacion(false, "Ya existe un elemento llamado \""+nuevoNombre.trim()+"\".",0,0);
          }
          if(archivo.renameTo(destino)){
              return new ResultadoOperacion(true, "\""+ archivo.getName()+"\" renombrado a \""+nuevoNombre.trim()+"\" exitosamente",1,0);
          }else{
             return new ResultadoOperacion(false, "No se pudo renombrar.",0,1); 
          }
    }
    
    public ResultadoOperacion copiar(File[] archivos){
        if(archivos == null || archivos.length == 0){
            return new ResultadoOperacion(false, "Seleccione al menos un elemento para copiar.",0,0); 
        }
        int validos = 0;
        for (int i = 0; i < archivos.length; i++) {
            if(archivos[i] != null && archivos[i].exists()){
                validos++;
            }
        }
        if(validos == 0){
             return new ResultadoOperacion(false, "Ninguno de los elementos seleccionados existe.",0,0); 
       
        }
        this.portapapeles = archivos;
        String mensaje;
        if(validos == 1){
            mensaje = "\"" + archivos[0].getName() + "\" copiaod al portapapeles";
        }else{
            mensaje = validos + "Elementos copiados al portapapeles";
        }
        return new ResultadoOperacion(true,mensaje,validos,0);
    }
    
    public ResultadoOperacion copiar(File destino){
        if(portapapeles == null || portapapeles.length == 0){
             return new ResultadoOperacion(false, "El portapapeles esta vacio. Copie algo primero.",0,0); 
        }
        if(destino == null || !destino.isDirectory()){
             return new ResultadoOperacion(false, "Sellecione una carpeta destino en el arbol",0,0); 
        }
        int pegados = 0;
        int errores = 0;
        for (int i = 0; i < portapapeles.length; i++) {
            File item =  portapapeles[i];
            if(item == null || !item.exists()){
                errores++;
                continue;
            }
            File archivoDestino = new File(destino, item.getName());
            if(archivoDestino.exists()){
                archivoDestino = generarNombreUnico(destino, item.getName());
            }
            boolean exito;
            if(item.isDirectory()){
                exito = copiarCarpetaRecursivo(item, archivoDestino);
            }else{
                exito = copiarArchivo(item, archivoDestino);
            }
            if(exito){
                pegados++;
            }else{
                errores ++;
            }
        }
        String hayerrores = "";
        if(errores > 0){
            hayerrores = "Erroes" + errores;
        }else{
            hayerrores = "Sin errores";
        }
    
        portapapeles = new File[0];
        String mensaje = "Pegado completo.\n"
                + "Elementos pegados: "+ pegados + "\n"
                + hayerrores;
        return new ResultadoOperacion(pegados > 0, mensaje, pegados, errores);
    }
    
    
    private boolean nombreValido(String nombre){
        if(nombre == null || nombre.trim().isEmpty()){
            return false;
        }
        String[] prohibidos = {"\\","/",":","*","?","\"","<",">","|"};
        for(int i = 0; i < prohibidos.length;i++){
            if(nombre.contains(prohibidos[i])){
                return false;
            }
        }
        return true;
    }
    private File generarNombreUnico(File carpeta, String nombreOriginal){
        File name = new File(carpeta, "copia_" + nombreOriginal);
        int contador = 2;
        while(name.exists()){
            name = new File(carpeta, "copia("+contador+")_" + nombreOriginal);
            contador++;
        }
        return name;
    }
    private boolean copiarCarpetaRecursivo(File origen, File destino){
        if(!destino.exists()){
            destino.mkdirs();
        }
        File [] contenido = origen.listFiles();
        if(contenido == null){
            return true;
        }
        boolean exito = true;
        for (int i = 0; i < contenido.length; i++) {
            File nuevoDestino = new File(destino, contenido[i].getName());
            if(contenido[i].isDirectory()){
                exito = exito && copiarCarpetaRecursivo(contenido[i],nuevoDestino);
            }else{
                exito = exito && copiarArchivo(contenido[i],nuevoDestino);
            }
            
        }
        return exito;
    }
    private boolean copiarArchivo(File origen, File destino){
        try(FileInputStream fis =  new FileInputStream(origen);
            FileOutputStream fos = new  FileOutputStream(destino);
            FileChannel fcOrigen = fis.getChannel();
            FileChannel fcDestino = fos.getChannel()){
            
            fcDestino.transferFrom(fcOrigen, 0, fcOrigen.size());
            return true;
            
        }catch(IOException e){
            System.out.println("Error al copiar: " + e.getMessage());
            return false;
        }
    }
    
    public boolean tienePortapapeles(){
        return portapapeles != null && portapapeles.length > 0;
    }
    public int cantidadEnPortapapeles(){
        if(portapapeles == null){
            return 0;
        }
        return portapapeles.length;
    }
}
