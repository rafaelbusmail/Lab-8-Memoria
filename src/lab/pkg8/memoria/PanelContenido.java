package lab.pkg8.memoria;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class PanelContenido extends JPanel {

    private static final int COL_NOMBRE = 0;
    private static final int COL_FECHA = 1;
    private static final int COL_TIPO = 2;
    private static final int COL_TAMANO = 3;

    private static final String[] COLUMNAS = {
            "Nombre", "Fecha de modificacion", "Tipo", "Tamano"
    };

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private String carpetaActual;
    private GestorDeArchivos gestor;
    private Consumer<String> onNavegar;

    public PanelContenido() {
        this.gestor = new GestorDeArchivos();
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

            @Override
            public Class<?> getColumnClass(int c) {
                if (c == COL_TAMANO) {
                    return Long.class;
                }
                return String.class;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tabla.setRowHeight(24);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 12));

        TableColumnModel cm = tabla.getColumnModel();
        cm.getColumn(COL_NOMBRE).setPreferredWidth(300);
        cm.getColumn(COL_FECHA).setPreferredWidth(150);
        cm.getColumn(COL_TIPO).setPreferredWidth(100);
        cm.getColumn(COL_TAMANO).setPreferredWidth(100);

        DefaultTableCellRenderer renderTamano = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                if (v instanceof Long) {
                    v = formatearTamano((Long) v);
                }
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                l.setHorizontalAlignment(SwingConstants.RIGHT);
                return l;
            }
        };
        cm.getColumn(COL_TAMANO).setCellRenderer(renderTamano);

        tabla.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tabla.columnAtPoint(e.getPoint());
                if (col >= 0) {
                    ordenarPorColumna(col);
                }
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    File f = getArchivoSeleccionado();
                    if (f != null && f.isDirectory() && onNavegar != null) {
                        onNavegar.accept(f.getAbsolutePath());
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);
    }

    public int cargarCarpeta(String ruta) {
        this.carpetaActual = ruta;
        File dir = new File(ruta);
        File[] hijos = dir.listFiles(f -> !f.isHidden());

        modeloTabla.setRowCount(0);
        if (hijos == null) {
            return 0;
        }

        ListaEnlazadaArchivos lista = net.convertirALista(hijos);
        Ordenador.para(Criterio.NOMBRE).ordenar(lista, Criterio.NOMBRE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        NodoArchivo actual = lista.getCabeza();
        while (actual != null) {
            modeloTabla.addRow(new Object[] {
                    actual.getNombre(),
                    sdf.format(actual.getFechaModificacion()),
                    actual.getTipo(),
                    actual.getTamano()
            });
            actual = actual.getSiguiente();
        }
        return hijos.length;
    }

    private void ordenarPorColumna(int col) {
        if (carpetaActual == null) {
            return;
        }
        Criterio crit;
        switch (col) {
            case COL_NOMBRE:
                crit = Criterio.NOMBRE;
                break;
            case COL_FECHA:
                crit = Criterio.FECHA;
                break;
            case COL_TIPO:
                crit = Criterio.TIPO;
                break;
            case COL_TAMANO:
                crit = Criterio.TAMANO;
                break;
            default:
                return;
        }

        File[] hijos = new File(carpetaActual).listFiles(f -> !f.isHidden());
        if (hijos == null) {
            return;
        }

        ListaEnlazadaArchivos lista = net.convertirALista(hijos);
        Ordenador.para(crit).ordenar(lista, crit);

        modeloTabla.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        NodoArchivo actual = lista.getCabeza();
        while (actual != null) {
            modeloTabla.addRow(new Object[] {
                    actual.getNombre(),
                    sdf.format(actual.getFechaModificacion()),
                    actual.getTipo(),
                    actual.getTamano()
            });
            actual = actual.getSiguiente();
        }
    }

    public void organizar() {
        if (carpetaActual == null) {
            return;
        }
        OrganizadorDeArchivos org = new OrganizadorDeArchivos();
        ResultadoOperacion res = org.organizar(new File(carpetaActual));
        JOptionPane.showMessageDialog(this, res.getMensaje(), "Organizar",
                res.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        cargarCarpeta(carpetaActual);
    }

    public void nuevaCarpeta() {
        if (carpetaActual == null) {
            return;
        }
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la carpeta:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            ResultadoOperacion res = gestor.crearCarpeta(new File(carpetaActual), nombre);
            if (!res.isExito()) {
                JOptionPane.showMessageDialog(this, res.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            cargarCarpeta(carpetaActual);
        }
    }

    public void copiar() {
        File[] seleccion = getArchivosSeleccionados();
        if (seleccion.length > 0) {
            gestor.copiar(seleccion);
        }
    }

    public void pegar() {
        if (carpetaActual == null || !gestor.tienePortapapeles()) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea pegar " + gestor.cantidadEnPortapapeles() + " elemento(s)?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            gestor.pegar(new File(carpetaActual));
            cargarCarpeta(carpetaActual);
        }
    }

    public void renombrar() {
        File f = getArchivoSeleccionado();
        if (f == null) {
            return;
        }
        String nuevo = JOptionPane.showInputDialog(this, "Nuevo nombre:", f.getName());
        if (nuevo != null && !nuevo.trim().isEmpty() && !nuevo.equals(f.getName())) {
            ResultadoOperacion res = gestor.renombrar(f, nuevo);
            if (!res.isExito()) {
                JOptionPane.showMessageDialog(this, res.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            cargarCarpeta(carpetaActual);
        }
    }

    private String clasificarBreve(File f) {
        if (f.isDirectory()) {
            return "Carpeta de archivos";
        }
        String n = f.getName().toLowerCase();
        if (n.endsWith(".jpg") || n.endsWith(".png") || n.endsWith(".gif")) {
            return "Imagen";
        }
        if (n.endsWith(".pdf") || n.endsWith(".docx") || n.endsWith(".txt")) {
            return "Documento";
        }
        if (n.endsWith(".mp3") || n.endsWith(".wav") || n.endsWith(".flac")) {
            return "Musica";
        }
        if (n.endsWith(".mp4") || n.endsWith(".avi") || n.endsWith(".mkv")) {
            return "Video";
        }
        if (n.endsWith(".zip") || n.endsWith(".rar") || n.endsWith(".7z")) {
            return "Comprimido";
        }
        if (n.endsWith(".exe")) {
            return "Ejecutable";
        }
        int dot = n.lastIndexOf('.');
        return dot >= 0 ? n.substring(dot + 1).toUpperCase() : "Archivo";
    }

    private String formatearTamano(long bytes) {
        if (bytes <= 0) {
            return "";
        }
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        }
        if (bytes < 1024L * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        }
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    public File getArchivoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || carpetaActual == null) {
            return null;
        }
        String nombre = (String) modeloTabla.getValueAt(fila, COL_NOMBRE);
        return new File(carpetaActual, nombre);
    }

    public File[] getArchivosSeleccionados() {
        int[] filas = tabla.getSelectedRows();
        File[] selec = new File[filas.length];
        for (int i = 0; i < filas.length; i++) {
            String nombre = (String) modeloTabla.getValueAt(filas[i], COL_NOMBRE);
            selec[i] = new File(carpetaActual, nombre);
        }
        return selec;
    }

    public void setOnNavegar(Consumer<String> c) {
        this.onNavegar = c;
    }
}
