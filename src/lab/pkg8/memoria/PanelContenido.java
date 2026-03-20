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

    private final GestorDeArchivos gestor = new GestorDeArchivos();

    private static final SimpleDateFormat SDF
            = new SimpleDateFormat("dd/MM/yyyy  HH:mm");

    private static final String[] COLUMNAS = {
        "Nombre", "Fecha de modificacion", "Tipo", "Tamano"
    };

    public PanelContenido() {
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel lblHeader = new JLabel("  Contenido");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblHeader.setForeground(new Color(0x444444));
        lblHeader.setBackground(new Color(0xECEAE3));
        lblHeader.setOpaque(true);
        lblHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xBBB8AF)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        add(lblHeader, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Tahoma", Font.PLAIN, 13));
        tabla.setRowHeight(28);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setGridColor(new Color(0xE8E5E0));
        tabla.setSelectionBackground(new Color(0x316AC5));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setFillsViewportHeight(true);
        tabla.setBackground(Color.WHITE);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 6));
                if (!sel) {
                    setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF7F5F2));
                }
                return this;
            }
        };


        DefaultTableCellRenderer nombreRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 6));
                if (!sel) {
                    setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF7F5F2));
                }
                if (r < modeloTabla.getRowCount()) {
                    String tipo = (String) modeloTabla.getValueAt(r, 2);
                    setIcon("Carpeta".equals(tipo)
                            ? UIManager.getIcon("FileView.directoryIcon")
                            : UIManager.getIcon("FileView.fileIcon"));
                }
                return this;
            }
        };

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 14));
                if (!sel) {
                    setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF7F5F2));
                }
                return this;
            }
        };

        TableColumnModel cols = tabla.getColumnModel();
        cols.getColumn(0).setPreferredWidth(260);
        cols.getColumn(1).setPreferredWidth(160);
        cols.getColumn(2).setPreferredWidth(130);
        cols.getColumn(3).setPreferredWidth(90);
        cols.getColumn(0).setCellRenderer(nombreRenderer);
        cols.getColumn(1).setCellRenderer(cellRenderer);
        cols.getColumn(2).setCellRenderer(cellRenderer);
        cols.getColumn(3).setCellRenderer(rightRenderer);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 12));
        header.setBackground(new Color(0xE2DEDB));
        header.setForeground(new Color(0x222222));
        header.setPreferredSize(new Dimension(0, 30));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xBBB8AF)));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xD0CCC8)),
                        BorderFactory.createEmptyBorder(0, 10, 0, 6)));
                setBackground(new Color(0xE2DEDB));
                setFont(new Font("Tahoma", Font.BOLD, 12));
                return this;
            }
        };
        for (int i = 0; i < cols.getColumnCount(); i++) {
            cols.getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer headerRight = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 14));
                setBackground(new Color(0xE2DEDB));
                setFont(new Font("Tahoma", Font.BOLD, 12));
                return this;
            }
        };
        cols.getColumn(3).setHeaderRenderer(headerRight);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);
    }

    public int cargarCarpeta(String ruta) {
        this.carpetaActual = ruta;
        modeloTabla.setRowCount(0);
        File dir = new File(ruta);
        File[] items = dir.listFiles();
        if (items == null) {
            return 0;
        }

        java.util.Arrays.sort(items, (a, b) -> {
            if (a.isDirectory() != b.isDirectory()) {
                return a.isDirectory() ? -1 : 1;
            }
            return a.getName().compareToIgnoreCase(b.getName());
        });

        int count = 0;
        for (File f : items) {
            if (f.isHidden()) {
                continue;
            }
            modeloTabla.addRow(new Object[]{
                f.getName(),
                SDF.format(new Date(f.lastModified())),
                f.isDirectory() ? "Carpeta" : obtenerTipo(f),
                f.isDirectory() ? "" : formatearTamano(f.length())
            });
            count++;
        }
        return count;
    }

    public void organizar() {
        JOptionPane.showMessageDialog(this,
                "Funcion Organizar pendiente de implementacion por Alumno 2.",
                "Pendiente", JOptionPane.INFORMATION_MESSAGE);
    }

    public void nuevaCarpeta() {
        if (carpetaActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una carpeta en el arbol primero.");
            return;
        }
        String nombre = JOptionPane.showInputDialog(this,
                "Nombre de la nueva carpeta:", "Nueva carpeta",
                JOptionPane.PLAIN_MESSAGE);
        if (nombre == null) {
            return;
        }

        ResultadoOperacion res = gestor.crearCarpeta(new File(carpetaActual), nombre);
        if (res.isExito()) {
            cargarCarpeta(carpetaActual);  
        }
        JOptionPane.showMessageDialog(this, res.getMensaje(),
                res.isExito() ? "Exito" : "Error",
                res.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    public void copiar() {
        File sel = getArchivoSeleccionado();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo o carpeta primero.");
            return;
        }
        ResultadoOperacion res = gestor.copiar(new File[]{sel});
        JOptionPane.showMessageDialog(this, res.getMensaje(),
                res.isExito() ? "Copiado" : "Error",
                res.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    public void pegar() {
        if (carpetaActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una carpeta destino en el arbol primero.");
            return;
        }
        ResultadoOperacion res = gestor.copiar(new File(carpetaActual));
        if (res.isExito()) {
            cargarCarpeta(carpetaActual);
        }
        JOptionPane.showMessageDialog(this, res.getMensaje(),
                res.isExito() ? "Pegado" : "Error",
                res.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    public void renombrar() {
        File sel = getArchivoSeleccionado();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo o carpeta primero.");
            return;
        }
        String nuevoNombre = JOptionPane.showInputDialog(this,
                "Nuevo nombre:", sel.getName());
        if (nuevoNombre == null) {
            return;
        }

        ResultadoOperacion res = gestor.renombrar(sel, nuevoNombre);
        if (res.isExito()) {
            cargarCarpeta(carpetaActual);
        }
        JOptionPane.showMessageDialog(this, res.getMensaje(),
                res.isExito() ? "Renombrado" : "Error",
                res.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    private String obtenerTipo(File f) {
        String n = f.getName().toLowerCase();
        if (n.endsWith(".jpg") || n.endsWith(".jpeg")
                || n.endsWith(".png") || n.endsWith(".gif")) {
            return "Imagen";
        }
        if (n.endsWith(".pdf")) {
            return "Documento PDF";
        }
        if (n.endsWith(".docx") || n.endsWith(".doc")) {
            return "Documento Word";
        }
        if (n.endsWith(".xlsx") || n.endsWith(".xls")) {
            return "Hoja de calculo";
        }
        if (n.endsWith(".txt")) {
            return "Texto plano";
        }
        if (n.endsWith(".mp3") || n.endsWith(".wav")
                || n.endsWith(".flac")) {
            return "Musica";
        }
        if (n.endsWith(".mp4") || n.endsWith(".avi")
                || n.endsWith(".mkv")) {
            return "Video";
        }
        if (n.endsWith(".zip") || n.endsWith(".rar")
                || n.endsWith(".7z")) {
            return "Comprimido";
        }
        if (n.endsWith(".exe")) {
            return "Ejecutable";
        }
        int dot = n.lastIndexOf('.');
        return dot >= 0 ? n.substring(dot + 1).toUpperCase() : "Archivo";
    }

    private String formatearTamano(long bytes) {
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
        String nombre = (String) modeloTabla.getValueAt(fila, 0);
        return new File(carpetaActual, nombre);
    }

    public String getCarpetaActual() {
        return carpetaActual;
    }
}
