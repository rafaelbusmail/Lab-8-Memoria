package lab.pkg8.memoria;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

public class PanelArbol extends JPanel {

    private JTree arbol;
    private DefaultTreeModel modelo;
    private Consumer<String> onCarpetaSeleccionada;

    private final java.util.Deque<String> pilaAtras = new java.util.ArrayDeque<>();
    private final java.util.Deque<String> pilaAdelante = new java.util.ArrayDeque<>();
    private String rutaActual = null;

    public PanelArbol() {
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xBBB8AF)));
        setBackground(new Color(0xFAFAF8));

        JLabel lblHeader = new JLabel("  Carpetas");
        lblHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblHeader.setForeground(new Color(0x444444));
        lblHeader.setBackground(new Color(0xECEAE3));
        lblHeader.setOpaque(true);
        lblHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xBBB8AF)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        add(lblHeader, BorderLayout.NORTH);

        File raiz = new File(System.getProperty("user.home"));

        DefaultMutableTreeNode nodoRaiz = new DefaultMutableTreeNode(new NodoInfo(raiz));
        cargarHijos(nodoRaiz, raiz);

        modelo = new DefaultTreeModel(nodoRaiz);
        arbol = new JTree(modelo);
        arbol.setRootVisible(true);
        arbol.setShowsRootHandles(true);
        arbol.setFont(new Font("Tahoma", Font.PLAIN, 13));
        arbol.setBackground(new Color(0xFAFAF8));
        arbol.setRowHeight(26);   

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(
                    JTree tree, Object value, boolean sel, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
                setFont(tree.getFont());
                if (sel) {
                    setForeground(Color.WHITE);
                    setBackground(new Color(0x316AC5));
                } else {
                    setForeground(new Color(0x1A1A1A));
                    setBackground(new Color(0xFAFAF8));
                }
                return this;
            }
        };
        renderer.setBackgroundNonSelectionColor(new Color(0xFAFAF8));
        renderer.setBackgroundSelectionColor(new Color(0x316AC5));
        renderer.setBorderSelectionColor(new Color(0x316AC5));
        arbol.setCellRenderer(renderer);

        arbol.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent e) {
                DefaultMutableTreeNode nodo
                        = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                NodoInfo info = (NodoInfo) nodo.getUserObject();
                if (nodo.getChildCount() == 0
                        || (nodo.getChildCount() == 1
                        && nodo.getFirstChild().toString().equals("..."))) {
                    nodo.removeAllChildren();
                    cargarHijos(nodo, info.archivo);
                    modelo.reload(nodo);
                }
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent e) {
            }
        });

        arbol.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode nodo
                    = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
            if (nodo == null) {
                return;
            }
            NodoInfo info = (NodoInfo) nodo.getUserObject();
            if (info.archivo.isDirectory()) {
                String ruta = info.archivo.getAbsolutePath();
                if (rutaActual != null && !rutaActual.equals(ruta)) {
                    pilaAtras.push(rutaActual);
                    pilaAdelante.clear();
                }
                rutaActual = ruta;
                if (onCarpetaSeleccionada != null) {
                    onCarpetaSeleccionada.accept(ruta);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(arbol);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBackground(new Color(0xFAFAF8));
        add(scroll, BorderLayout.CENTER);
    }

    private void cargarHijos(DefaultMutableTreeNode nodo, File dir) {
        File[] hijos = dir.listFiles(f -> f.isDirectory() && !f.isHidden());
        if (hijos == null) {
            return;
        }
        java.util.Arrays.sort(hijos, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        for (File h : hijos) {
            DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(new NodoInfo(h));
            File[] subCarpetas = h.listFiles(File::isDirectory);
            if (subCarpetas != null && subCarpetas.length > 0) {
                hijo.add(new DefaultMutableTreeNode("..."));
            }
            nodo.add(hijo);
        }
    }

    public void navegarA(String ruta) {
        if (onCarpetaSeleccionada != null) {
            onCarpetaSeleccionada.accept(ruta);
        }
    }

    public String navegarAtras() {
        if (pilaAtras.isEmpty()) {
            return null;
        }
        pilaAdelante.push(rutaActual);
        rutaActual = pilaAtras.pop();
        return rutaActual;
    }

    public String navegarAdelante() {
        if (pilaAdelante.isEmpty()) {
            return null;
        }
        pilaAtras.push(rutaActual);
        rutaActual = pilaAdelante.pop();
        return rutaActual;
    }

    public void setOnCarpetaSeleccionada(Consumer<String> c) {
        onCarpetaSeleccionada = c;
    }

    public void refrescar() {
        TreePath path = arbol.getSelectionPath();
        if (path == null) {
            return;
        }
        DefaultMutableTreeNode nodo
                = (DefaultMutableTreeNode) path.getLastPathComponent();
        nodo.removeAllChildren();
        NodoInfo info = (NodoInfo) nodo.getUserObject();
        cargarHijos(nodo, info.archivo);
        modelo.reload(nodo);
        arbol.setSelectionPath(path);
    }

    private static class NodoInfo {

        final File archivo;

        NodoInfo(File f) {
            this.archivo = f;
        }

        @Override
        public String toString() {
            String n = archivo.getName();
            return n.isEmpty() ? archivo.getAbsolutePath() : n;
        }
    }
}
