package lab.pkg8.memoria;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class GestorDeArchivos {

    private File[] portapapeles;

    public GestorDeArchivos() {
        this.portapapeles = new File[0];
    }

    public ResultadoOperacion crearCarpeta(File destino, String nombre) {
        if (destino == null || !destino.isDirectory()) {
            return new ResultadoOperacion(false,
                    "Seleccione una carpeta en el arbol primero.", 0, 0);
        }
        if (!UtilArchivos.nombreValido(nombre)) {
            return new ResultadoOperacion(false,
                    "Nombre invalido. Evite: \\ / : * ? \" < > |", 0, 0);
        }
        File nueva = new File(destino, nombre);
        if (nueva.exists()) {
            return new ResultadoOperacion(false,
                    "Ya existe una carpeta con ese nombre.", 0, 0);
        }
        if (nueva.mkdir()) {
            return new ResultadoOperacion(true, "Carpeta creada.", 1, 0);
        }
        return new ResultadoOperacion(false, "No se pudo crear la carpeta.", 0, 0);
    }

    public ResultadoOperacion copiar(File[] seleccion) {
        if (seleccion == null || seleccion.length == 0) {
            return new ResultadoOperacion(false, "No hay archivos seleccionados.", 0, 0);
        }
        this.portapapeles = seleccion;
        return new ResultadoOperacion(true,
                seleccion.length + " elemento(s) listos para pegar.", seleccion.length, 0);
    }

    public ResultadoOperacion pegar(File destino) {
        if (destino == null || !destino.isDirectory()) {
            return new ResultadoOperacion(false, "Destino invalido.", 0, 0);
        }
        if (portapapeles == null || portapapeles.length == 0) {
            return new ResultadoOperacion(false, "Portapapeles vacio.", 0, 0);
        }

        int exitos = 0;
        int fallos = 0;

        for (File f : portapapeles) {
            if (!f.exists()) {
                fallos++;
                continue;
            }
            if (f.isDirectory() && destino.getAbsolutePath().startsWith(f.getAbsolutePath())) {
                fallos++;
                continue;
            }
            File nuevoDestino = new File(destino, f.getName());
            if (nuevoDestino.exists()) {
                nuevoDestino = UtilArchivos.generarNombreUnico(destino, f.getName());
            }

            boolean ok;
            if (f.isDirectory()) {
                ok = copiarCarpetaRecursivo(f, nuevoDestino);
            } else {
                ok = copiarArchivo(f, nuevoDestino);
            }

            if (ok) {
                exitos++;
            } else {
                fallos++;
            }
        }

        String msg = "Pegado finalizado. Exitos: " + exitos + (fallos > 0 ? ", Fallos: " + fallos : "");
        return new ResultadoOperacion(exitos > 0, msg, exitos, fallos);
    }

    public ResultadoOperacion renombrar(File archivo, String nuevoNombre) {
        if (archivo == null || !archivo.exists()) {
            return new ResultadoOperacion(false, "Archivo no encontrado.", 0, 0);
        }
        if (!UtilArchivos.nombreValido(nuevoNombre)) {
            return new ResultadoOperacion(false, "Nombre invalido.", 0, 0);
        }
        File destino = new File(archivo.getParentFile(), nuevoNombre);
        if (destino.exists()) {
            return new ResultadoOperacion(false, "Ya existe un archivo con ese nombre.", 0, 0);
        }
        if (archivo.renameTo(destino)) {
            return new ResultadoOperacion(true, "Renombrado con exito.", 1, 0);
        }
        return new ResultadoOperacion(false, "No se pudo renombrar.", 0, 0);
    }

    private boolean copiarCarpetaRecursivo(File origen, File destino) {
        if (!destino.exists() && !destino.mkdirs()) {
            return false;
        }
        File[] contenido = origen.listFiles();
        if (contenido == null) {
            return true;
        }
        boolean exito = true;
        for (File hijo : contenido) {
            File nuevoDestino = new File(destino, hijo.getName());
            if (hijo.isDirectory()) {
                exito = exito && copiarCarpetaRecursivo(hijo, nuevoDestino);
            } else {
                exito = exito && copiarArchivo(hijo, nuevoDestino);
            }
        }
        return exito;
    }

    private boolean copiarArchivo(File origen, File destino) {
        try (FileInputStream fis = new FileInputStream(origen);
                FileOutputStream fos = new FileOutputStream(destino);
                FileChannel fcOrigen = fis.getChannel();
                FileChannel fcDestino = fos.getChannel()) {

            fcDestino.transferFrom(fcOrigen, 0, fcOrigen.size());
            return true;

        } catch (IOException e) {
            System.err.println("Error al copiar " + origen.getName() + ": " + e.getMessage());
            return false;
        }
    }

    public boolean tienePortapapeles() {
        return portapapeles != null && portapapeles.length > 0;
    }

    public int cantidadEnPortapapeles() {
        return portapapeles == null ? 0 : portapapeles.length;
    }
}
