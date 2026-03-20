package lab.pkg8.memoria;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;

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

    private ListaEnlazadaArchivos listaActual = new ListaEnlazadaArchivos();
    private Criterio criterioActual = Criterio.NOMBRE;
    private boolean ordenAscendente = true;

    private final GestorDeArchivos gestor = new GestorDeArchivos();
    private final OrganizadorDeArchivos organizador = new OrganizadorDeArchivos();

    private Consumer<String> onNavegar;

    private static final SimpleDateFormat SDF
            = new SimpleDateFormat("dd/MM/yyyy  HH:mm");

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

            @Override
            public Class<?> getColumnClass(int c) {
                return c == COL_TAMANO ? Long.class : String.class;
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
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabla.setBackground(Color.WHITE);

        configurarRenderers();
        configurarHeaders();
        configurarDobleClick();

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);
    }

    private void configurarRenderers() {
        TableColumnModel cols = tabla.getColumnModel();
        cols.getColumn(COL_NOMBRE).setPreferredWidth(260);
        cols.getColumn(COL_FECHA).setPreferredWidth(160);
        cols.getColumn(COL_TIPO).setPreferredWidth(130);
        cols.getColumn(COL_TAMANO).setPreferredWidth(90);

        DefaultTableCellRenderer cell = stripedRenderer(false);
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
                    String tipo = (String) modeloTabla.getValueAt(r, COL_TIPO);
                    setIcon("Carpeta".equals(tipo)
                            ? UIManager.getIcon("FileView.directoryIcon")
                            : UIManager.getIcon("FileView.fileIcon"));
                }
                return this;
            }
        };
        DefaultTableCellRenderer tamanoRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                Object display = (v instanceof Long) ? formatearTamano((Long) v) : v;
                super.getTableCellRendererComponent(t, display, sel, focus, r, c);
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 14));
                if (!sel) {
                    setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF7F5F2));
                }
                return this;
            }
        };

        cols.getColumn(COL_NOMBRE).setCellRenderer(nombreRenderer);
        cols.getColumn(COL_FECHA).setCellRenderer(cell);
        cols.getColumn(COL_TIPO).setCellRenderer(cell);
        cols.getColumn(COL_TAMANO).setCellRenderer(tamanoRenderer);
    }

    private void configurarHeaders() {
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 12));
        header.setBackground(new Color(0xE2DEDB));
        header.setForeground(new Color(0x222222));
        header.setPreferredSize(new Dimension(0, 30));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xBBB8AF)));

        TableColumnModel cols = tabla.getColumnModel();
        for (int i = 0; i < cols.getColumnCount(); i++) {
            final int col = i;
            cols.getColumn(i).setHeaderRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                    Criterio crit = colToCriterio(col);
                    String flecha = (criterioActual == crit)
                            ? (ordenAscendente ? " ▲" : " ▼") : "";
                    super.getTableCellRendererComponent(t, v + flecha, sel, focus, r, c);
                    setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xD0CCC8)),
                            BorderFactory.createEmptyBorder(0, 10, 0, 6)));
                    setBackground(new Color(0xE2DEDB));
                    setFont(new Font("Tahoma", Font.BOLD, 12));
                    if (col == COL_TAMANO) {
                        setHorizontalAlignment(SwingConstants.RIGHT);
                    }
                    return this;
                }
            });
        }

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = header.columnAtPoint(e.getPoint());
                if (col < 0) {
                    return;
                }
                Criterio crit = colToCriterio(col);
                if (criterioActual == crit) {
                    ordenAscendente = !ordenAscendente;
                } else {
                    criterioActual = crit;
                    ordenAscendente = true;
                }
                aplicarOrden();
            }
        });
    }

    private void configurarDobleClick() {
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    File sel = getArchivoSeleccionado();
                    if (sel != null && sel.isDirectory() && onNavegar != null) {
                        onNavegar.accept(sel.getAbsolutePath());
                    }
                }
            }
        });
    }

    public int cargarCarpeta(String ruta) {
        this.carpetaActual = ruta;
        listaActual.limpiar();
        modeloTabla.setRowCount(0);

        File dir = new File(ruta);
        File[] items = dir.listFiles();
        if (items == null) {
            return 0;
        }

<<<<<<< HEAD
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
=======
        listaActual = UtilArchivos.convertirALista(items);
        aplicarOrden();
        return items.length;
>>>>>>> fix-conflicto
    }

    private void aplicarOrden() {
        if (listaActual.estaVacia()) {
            return;
        }

        Ordenador.para(criterioActual).ordenar(listaActual, criterioActual);

        if (!ordenAscendente) {
            invertirLista();
        }

        poblarTabla();
        tabla.getTableHeader().repaint();
    }

    private void invertirLista() {
        NodoArchivo prev = null;
        NodoArchivo curr = listaActual.getCabeza();
        while (curr != null) {
            NodoArchivo next = curr.getSiguiente();
            curr.setSiguiente(prev);
            prev = curr;
            curr = next;
        }
        listaActual.setCabeza(prev);
    }

    private void poblarTabla() {
        modeloTabla.setRowCount(0);
<<<<<<< HEAD
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        NodoArchivo actual = lista.getCabeza();
        while (actual != null) {
            modeloTabla.addRow(new Object[] {
                    actual.getNombre(),
                    sdf.format(actual.getFechaModificacion()),
                    actual.getTipo(),
                    actual.getTamano()
=======
        NodoArchivo nodo = listaActual.getCabeza();
        while (nodo != null) {
            modeloTabla.addRow(new Object[]{
                nodo.getNombre(),
                SDF.format(nodo.getFechaModificacion()),
                nodo.getTipo(),
                nodo.getTamano()
>>>>>>> fix-conflicto
            });
            nodo = nodo.getSiguiente();
        }
    }

    public void organizar() {
        if (carpetaActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una carpeta en el arbol primero.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Organizar automaticamente el contenido de:\n" + carpetaActual
                + "\n\nLos archivos se moveran a subcarpetas /Imagenes, /Documentos y /Musica.",
                "Confirmar organizacion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        ResultadoOperacion res = organizador.organizar(new File(carpetaActual));
        cargarCarpeta(carpetaActual);
        JOptionPane.showMessageDialog(this, res.getMensaje(),
                res.isExito() ? "Organizacion completa" : "Sin cambios",
                res.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    public void nuevaCarpeta() {
        if (carpetaActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una carpeta en el arbol primero.");
            return;
        }
        String nombre = JOptionPane.showInputDialog(this,
                "Nombre de la nueva carpeta:", "Nueva carpeta", JOptionPane.PLAIN_MESSAGE);
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
        File[] seleccion = getArchivosSeleccionados();
        if (seleccion.length == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione uno o mas elementos para copiar.");
            return;
        }
        ResultadoOperacion res = gestor.copiar(seleccion);
        JOptionPane.showMessageDialog(this, res.getMensaje());
    }

    public File[] getArchivosSeleccionados() {
        int[] filas = tabla.getSelectedRows();
        File[] seleccion = new File[filas.length];
        for (int i = 0; i < filas.length; i++) {
            String nombre = (String) modeloTabla.getValueAt(filas[i], COL_NOMBRE);
            seleccion[i] = new File(carpetaActual, nombre);
        }
        return seleccion;
    }

    public void pegar() {
        if (carpetaActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una carpeta destino en el arbol primero.");
            return;
        }
        if (gestor.tienePortapapeles()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Pegar " + gestor.cantidadEnPortapapeles()
                    + " elemento(s) en:\n" + carpetaActual + "?",
                    "Confirmar pegado", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        ResultadoOperacion res = gestor.pegar(new File(carpetaActual));
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
            JOptionPane.showMessageDialog(this,
                    "Selecciona un archivo o carpeta primero.");
            return;
        }
        String nuevoNombre = JOptionPane.showInputDialog(this,
                "Nuevo nombre para \"" + sel.getName() + "\":", sel.getName());
        if (nuevoNombre == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Renombrar \"" + sel.getName() + "\" a \"" + nuevoNombre.trim() + "\"?",
                "Confirmar renombrado", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
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

    private Criterio colToCriterio(int col) {
        switch (col) {
            case COL_FECHA:
                return Criterio.FECHA;
            case COL_TIPO:
                return Criterio.TIPO;
            case COL_TAMANO:
                return Criterio.TAMANO;
            default:
                return Criterio.NOMBRE;
        }
    }

    private DefaultTableCellRenderer stripedRenderer(boolean rightAlign) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, focus, r, c);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 6));
                if (!sel) {
                    setBackground(r % 2 == 0 ? Color.WHITE : new Color(0xF7F5F2));
                }
                if (rightAlign) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                }
                return this;
            }
        };
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

    public String getCarpetaActual() {
        return carpetaActual;
    }

    public void setOnNavegar(Consumer<String> c) {
        this.onNavegar = c;
    }
}