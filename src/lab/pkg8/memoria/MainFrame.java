package lab.pkg8.memoria;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private PanelArbol panelArbol;
    private PanelContenido panelContenido;
    private BarraHerramientas barra;
    private JLabel lblEstado;
    private JLabel lblConteo;

    public MainFrame() {
        super("JAVA CENTER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 640);
        setMinimumSize(new Dimension(800, 520));
        setLocationRelativeTo(null);
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(new Color(0x1E4D78));
        panelTitulo.setPreferredSize(new Dimension(0, 46));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

        JLabel lblTitulo = new JLabel("  JAVA CENTER");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Explorador de archivos  ");
        lblSubtitulo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSubtitulo.setForeground(new Color(0xBBCCDD));

        panelTitulo.add(lblTitulo, BorderLayout.WEST);
        panelTitulo.add(lblSubtitulo, BorderLayout.EAST);

        barra = new BarraHerramientas();

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(barra, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(240);
        split.setDividerSize(5);
        split.setBorder(null);
        split.setContinuousLayout(true);

        panelArbol = new PanelArbol();
        panelContenido = new PanelContenido();

        split.setLeftComponent(panelArbol);
        split.setRightComponent(panelContenido);
        add(split, BorderLayout.CENTER);

        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(new Color(0xD4D0C8));
        panelEstado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0x808080)),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)));
        panelEstado.setPreferredSize(new Dimension(0, 26));

        lblEstado = new JLabel("Listo");
        lblEstado.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(0x333333));

        lblConteo = new JLabel("Derechos Reservados UNITEC");
        lblConteo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblConteo.setForeground(new Color(0x444444));

        panelEstado.add(lblEstado, BorderLayout.WEST);
        panelEstado.add(lblConteo, BorderLayout.EAST);
        add(panelEstado, BorderLayout.SOUTH);

        panelArbol.setOnCarpetaSeleccionada(ruta -> {
            barra.setRutaActual(ruta);
            int n = panelContenido.cargarCarpeta(ruta);
            setEstado("Carpeta: " + ruta);
            lblConteo.setText(n + " elemento(s)   |   Derechos Reservados UNITEC");
            actualizarBotonesNavegacion();
        });

        panelContenido.setOnNavegar(ruta -> {
            barra.setRutaActual(ruta);
            int n = panelContenido.cargarCarpeta(ruta);
            setEstado("Carpeta: " + ruta);
            lblConteo.setText(n + " elemento(s)   |   Derechos Reservados UNITEC");
            panelArbol.navegarProgramatico(ruta);
            actualizarBotonesNavegacion();
        });

        barra.setOnOrganizar(() -> {
            panelContenido.organizar();
            panelArbol.refrescar();
        });
        barra.setOnNuevaCarpeta(() -> {
            panelContenido.nuevaCarpeta();
            panelArbol.refrescar();
        });
        barra.setOnCopiar(() -> panelContenido.copiar());
        barra.setOnPegar(() -> {
            panelContenido.pegar();
            panelArbol.refrescar();
        });
        barra.setOnRenombrar(() -> {
            panelContenido.renombrar();
            panelArbol.refrescar();
        });

        barra.setOnAtras(() -> {
            String ant = panelArbol.navegarAtras();
            if (ant != null) {
                barra.setRutaActual(ant);
                int n = panelContenido.cargarCarpeta(ant);
                setEstado("Carpeta: " + ant);
                lblConteo.setText(n + " elemento(s)   |   Derechos Reservados UNITEC");
            }
            actualizarBotonesNavegacion();
        });

        barra.setOnAdelante(() -> {
            String sig = panelArbol.navegarAdelante();
            if (sig != null) {
                barra.setRutaActual(sig);
                int n = panelContenido.cargarCarpeta(sig);
                setEstado("Carpeta: " + sig);
                lblConteo.setText(n + " elemento(s)   |   Derechos Reservados UNITEC");
            }
            actualizarBotonesNavegacion();
        });
    }

    private void actualizarBotonesNavegacion() {
        barra.setAtrasEnabled(panelArbol.puedeIrAtras());
        barra.setAdelanteEnabled(panelArbol.puedeIrAdelante());
    }

    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }
}
