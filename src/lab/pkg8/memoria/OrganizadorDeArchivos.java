package lab.pkg8.memoria;

import java.io.File;

public class OrganizadorDeArchivos {

    private static final String[] IMAGENES = {"jpg", "jpeg", "png", "gif"};
    private static final String[] DOCUMENTOS = {"pdf", "docx", "doc", "txt"};
    private static final String[] MUSICA = {"mp3", "wav"};

    public ResultadoOperacion organizar(File carpeta) {
        if (carpeta == null || !carpeta.isDirectory()) {
            return new ResultadoOperacion(false,
                    "Seleccione una carpeta valida.", 0, 0);
        }
        File[] contenido = carpeta.listFiles();
        if (contenido == null || contenido.length == 0) {
            return new ResultadoOperacion(false,
                    "La carpeta esta vacia, no hay archivos para organizar.", 0, 0);
        }

        int movidos = 0;
        int errores = 0;

        for (File archivo : contenido) {
            if (archivo.isDirectory()) {
                continue;
            }
            String nombreSubcarpeta = clasificar(archivo);
            if (nombreSubcarpeta == null) {
                continue;
            }
            File subcarpeta = new File(carpeta, nombreSubcarpeta);
            if (!subcarpeta.exists() && !subcarpeta.mkdir()) {
                errores++;
                continue;
            }
            File destino = new File(subcarpeta, archivo.getName());
            if (destino.exists()) {
                destino = UtilArchivos.generarNombreUnico(subcarpeta, archivo.getName());
            }
            if (archivo.renameTo(destino)) {
                movidos++;
            } else {
                errores++;
            }
        }

        String detalle = errores > 0 ? "Errores: " + errores : "Sin errores.";
        String mensaje = "Organizacion completada.\nArchivos movidos: " + movidos
                + "\n" + detalle;
        return new ResultadoOperacion(movidos > 0 || errores == 0, mensaje, movidos, errores);
    }

    private String clasificar(File archivo) {
        String ext = UtilArchivos.obtenerExtension(archivo);
        for (String e : IMAGENES) {
            if (e.equals(ext)) {
                return "Imagenes";
            }
        }
        for (String e : DOCUMENTOS) {
            if (e.equals(ext)) {
                return "Documentos";
            }
        }
        for (String e : MUSICA) {
            if (e.equals(ext)) {
                return "Musica";
            }
        }
        return null;
    }
}
