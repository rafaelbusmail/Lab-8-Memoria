/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab.pkg8.memoria;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelContenido extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private String carpetaActual;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final String[] COLUMNAS = {
        "Nombre", "Fecha de modificacion", "Tipo", "Tamano"
    };

    public PanelContenido() {
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tabla.setRowHeight(20);
        tabla.setGridColor(new Color(0xD8D8D8));
        tabla.setSelectionBackground(new Color(0x316AC5));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setFillsViewportHeight(true);

        TableColumnModel cols = tabla.getColumnModel();
        cols.getColumn(0).setPreferredWidth(200);
        cols.getColumn(1).setPreferredWidth(140);
        cols.getColumn(2).setPreferredWidth(100);
        cols.getColumn(3).setPreferredWidth(80);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Tahoma", Font.PLAIN, 12));
        header.setBackground(new Color(0xECE9D8));
        header.setForeground(new Color(0x222222));
        header.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void cargarCarpeta(String ruta) {
        this.carpetaActual = ruta;
        modeloTabla.setRowCount(0);
        File dir = new File(ruta);
        File[] items = dir.listFiles();
        if (items == null) {
            return;
        }

        java.util.Arrays.sort(items, (a, b) -> {
            if (a.isDirectory() != b.isDirectory()) {
                return a.isDirectory() ? -1 : 1;
            }
            return a.getName().compareToIgnoreCase(b.getName());
        });

        for (File f : items) {
            if (f.isHidden()) {
                continue;
            }
            String nombre = f.getName();
            String fecha = SDF.format(new Date(f.lastModified()));
            String tipo = f.isDirectory() ? "Carpeta" : obtenerTipo(f);
            String tamano = f.isDirectory() ? "" : formatearTamano(f.length());
            modeloTabla.addRow(new Object[]{nombre, fecha, tipo, tamano});
        }
    }

    private String obtenerTipo(File f) {
        String nombre = f.getName().toLowerCase();
        if (nombre.endsWith(".jpg") || nombre.endsWith(".png")
                || nombre.endsWith(".gif")) {
            return "Imagen";
        }
        if (nombre.endsWith(".pdf")) {
            return "Documento PDF";
        }
        if (nombre.endsWith(".docx")) {
            return "Documento Word";
        }
        if (nombre.endsWith(".txt")) {
            return "Documento de texto";
        }
        if (nombre.endsWith(".mp3") || nombre.endsWith(".wav")) {
            return "Musica";
        }
        int dot = nombre.lastIndexOf('.');
        return dot >= 0 ? nombre.substring(dot + 1).toUpperCase() : "Archivo";
    }

    private String formatearTamano(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return (bytes / 1024) + " KB";
        }
        return (bytes / (1024 * 1024)) + " MB";
    }

    public File getArchivoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || carpetaActual == null) {
            return null;
        }
        String nombre = (String) modeloTabla.getValueAt(fila, 0);
        return new File(carpetaActual, nombre);
    }

    public String getCarpetaActual() {
        return carpetaActual;
    }

    public void organizar() {
        JOptionPane.showMessageDialog(this,
                "organizar(): conectar con OrganizadorArchivos",
                "Stub", JOptionPane.INFORMATION_MESSAGE);
    }

    public void nuevaCarpeta() {
        String nombre = JOptionPane.showInputDialog(this,
                "Nombre de la nueva carpeta:", "Nueva carpeta",
                JOptionPane.PLAIN_MESSAGE);
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }
        JOptionPane.showMessageDialog(this,
                "nuevaCarpeta(): conectar con GestorArchivos.crearCarpeta(\""
                + nombre + "\")", "Stub", JOptionPane.INFORMATION_MESSAGE);
    }

    public void copiar() {
        File sel = getArchivoSeleccionado();
        if (sel == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un archivo o carpeta primero.");
            return;
        }
        JOptionPane.showMessageDialog(this,
                "copiar(): conectar con Portapapeles.copiar(\""
                + sel.getName() + "\")", "Stub", JOptionPane.INFORMATION_MESSAGE);
    }

    public void pegar() {
        JOptionPane.showMessageDialog(this,
                "pegar(): conectar con GestorArchivos.pegar(carpetaActual)",
                "Stub", JOptionPane.INFORMATION_MESSAGE);
    }

    public void renombrar() {
        File sel = getArchivoSeleccionado();
        if (sel == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un archivo o carpeta primero.");
            return;
        }
        String nuevoNombre = JOptionPane.showInputDialog(this,
                "Nuevo nombre:", sel.getName());
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return;
        }
        JOptionPane.showMessageDialog(this,
                "renombrar(): conectar con GestorArchivos.renombrar(\""
                + sel.getName() + "\", \"" + nuevoNombre + "\")",
                "Stub", JOptionPane.INFORMATION_MESSAGE);
    }
}
