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
public class OrganizadorDeArchivos {
    private static final String[] imagenes = {"jpg", "png", "gif"};
       private static final String[] documentos = {"pdf", "docx", "txt"};
          private static final String[] musica = {"mp3", "wav"};
          
          public ResultadoOperacion organizar(File carpeta){
              if(carpeta == null || !carpeta.isDirectory()){
                  return new ResultadoOperacion(false, "Seleccione una carpeta valida.",0,0);
              }
              File [] contenido =  carpeta.listFiles();
              if(contenido == null || contenido.length == 0){
                    return new ResultadoOperacion(false, "La carpeta esta vacia, no hay archivo para organizar.",0,0);
              }
              int movidos = 0;
              int errores = 0;
              for (int i = 0; i < contenido.length; i++) {
                  File archivo = contenido[i];
                  if(archivo.isDirectory()){
                      continue;
                  }
                  String nameSubcarpeta = clasificar(archivo);
                  if(nameSubcarpeta == null){
                      continue;
                  }
                  File Subcarpeta = new File(carpeta,nameSubcarpeta);
                  if(!Subcarpeta.exists()){
                      if(!Subcarpeta.mkdir()){
                          errores++;
                          continue;
                      }
                  }
                  File destino = new File(Subcarpeta, archivo.getName());
                  if(destino.exists()){
                      destino = generarNombreUnico(Subcarpeta, archivo.getName());
                      
                  }
                  if(archivo.renameTo(destino)){
                      movidos++;
                  }else{
                      errores++;
                  }
              }
              String hayerroes = "";
              if(errores > 0){
                  hayerroes = "Errores" + errores;
              }else{
                  hayerroes = "Sin errores";
              }
              String mensaje = "Organizacion completada.\n"
                      + "Archivos movidos: "+ movidos + "\n"
                      + hayerroes;
              return new ResultadoOperacion(true,mensaje,movidos,errores);
          }
          private String clasificar(File archivo){
              String ext = obtenerExtension(archivo);
              for (int i = 0; i < imagenes.length; i++) {
                  if(imagenes[i].equals(ext)){
                      return "Imagenes";
                  }
              }
              for (int i = 0; i < documentos.length; i++) {
                  if(documentos[i].equals(ext)){
                      return "Documentos";
                  }
              }
              for (int i = 0; i < musica.length; i++) {
                  if(musica[i].equals(ext)){
                      return "Musica";
                  }
              }
              return null;
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
           private String obtenerExtension(File archivo){
               String nombre = archivo.getName();
               int punto = nombre.lastIndexOf('.');
               if(punto == -1 || punto == nombre.length() - 1){
                   return "";
               }
               return nombre.substring(punto + 1).toLowerCase();
           }
}
