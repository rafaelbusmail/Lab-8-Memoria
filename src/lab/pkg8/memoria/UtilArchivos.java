package lab.pkg8.memoria;

import java.io.File;

public class UtilArchivos {

    private UtilArchivos() {
    }

    public static File generarNombreUnico(File carpeta, String nombreOriginal) {
        File candidato = new File(carpeta, "copia_" + nombreOriginal);
        int contador = 2;
        while (candidato.exists()) {
            candidato = new File(carpeta, "copia(" + contador + ")_" + nombreOriginal);
            contador++;
        }
        return candidato;
    }

    public static String obtenerExtension(File archivo) {
        String nombre = archivo.getName();
        int punto = nombre.lastIndexOf('.');
        if (punto == -1 || punto == nombre.length() - 1) {
            return "";
        }
        return nombre.substring(punto + 1).toLowerCase();
    }

    public static boolean nombreValido(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        String[] prohibidos = {"\\", "/", ":", "*", "?", "\"", "<", ">", "|"};
        for (String p : prohibidos) {
            if (nombre.contains(p)) {
                return false;
            }
        }
        return true;
    }
}
