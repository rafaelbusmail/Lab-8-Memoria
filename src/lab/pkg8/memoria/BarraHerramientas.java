package lab.pkg8.memoria;

import javax.swing.*;
import java.awt.*;

public class BarraHerramientas extends JPanel {

    private JButton btnAtras;
    private JButton btnAdelante;
    private JTextField txtRuta;
    private JButton btnOrganizar;
    private JButton btnNuevaCarpeta;
    private JButton btnCopiar;
    private JButton btnPegar;
    private JButton btnRenombrar;

    private Runnable onAtras;
    private Runnable onAdelante;
    private Runnable onOrganizar;
    private Runnable onNuevaCarpeta;
    private Runnable onCopiar;
    private Runnable onPegar;
    private Runnable onRenombrar;

    private static final Color BG = new Color(0xF0EDE6);
    private static final Color BORDER = new Color(0xACA899);
    private static final Color BTN_BG = new Color(0xE8E4DC);
    private static final Color BTN_FG = new Color(0x1A1A1A);
    private static final Color RUTA_BG = Color.WHITE;
    private static final Color RUTA_BDR = new Color(0x7F9DB9);

    public BarraHerramientas() {
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(6, 0));
        setBackground(BG);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        JPanel panelNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        panelNav.setOpaque(false);

        btnAtras = crearBotonNav("<html><b>&lt;</b></html>", "Atras");
        btnAdelante = crearBotonNav("<html><b>&gt;</b></html>", "Adelante");

        btnAtras.addActionListener(e -> {
            if (onAtras != null) {
                onAtras.run();
            }
        });
        btnAdelante.addActionListener(e -> {
            if (onAdelante != null) {
                onAdelante.run();
            }
        });

        panelNav.add(btnAtras);
        panelNav.add(btnAdelante);

        txtRuta = new JTextField();
        txtRuta.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtRuta.setEditable(false);
        txtRuta.setBackground(RUTA_BG);
        txtRuta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RUTA_BDR),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelAcciones.setOpaque(false);

        btnOrganizar = crearBotonAccion("Organizar", "Organizar archivos por tipo", 95);
        btnNuevaCarpeta = crearBotonAccion("Nueva carpeta", "Crear una nueva carpeta", 115);
        btnCopiar = crearBotonAccion("Copiar", "Copiar seleccion", 75);
        btnPegar = crearBotonAccion("Pegar", "Pegar elementos copiados", 70);
        btnRenombrar = crearBotonAccion("Renombrar", "Renombrar archivo o carpeta", 95);

        btnOrganizar.addActionListener(e -> {
            if (onOrganizar != null) {
                onOrganizar.run();
            }
        });
        btnNuevaCarpeta.addActionListener(e -> {
            if (onNuevaCarpeta != null) {
                onNuevaCarpeta.run();
            }
        });
        btnCopiar.addActionListener(e -> {
            if (onCopiar != null) {
                onCopiar.run();
            }
        });
        btnPegar.addActionListener(e -> {
            if (onPegar != null) {
                onPegar.run();
            }
        });
        btnRenombrar.addActionListener(e -> {
            if (onRenombrar != null) {
                onRenombrar.run();
            }
        });

        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 22));
        sep.setForeground(BORDER);

        panelAcciones.add(btnOrganizar);
        panelAcciones.add(sep);
        panelAcciones.add(btnNuevaCarpeta);
        panelAcciones.add(btnCopiar);
        panelAcciones.add(btnPegar);
        panelAcciones.add(btnRenombrar);

        add(panelNav, BorderLayout.WEST);
        add(txtRuta, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.EAST);
    }

    private JButton crearBotonNav(String html, String tooltip) {
        JButton b = new JButton(html);
        b.setFont(new Font("Tahoma", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(34, 28));
        b.setFocusPainted(false);
        b.setBackground(BTN_BG);
        b.setForeground(BTN_FG);
        b.setToolTipText(tooltip);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton crearBotonAccion(String texto, String tooltip, int ancho) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Tahoma", Font.PLAIN, 11));
        b.setPreferredSize(new Dimension(ancho, 28));
        b.setFocusPainted(false);
        b.setBackground(BTN_BG);
        b.setForeground(BTN_FG);
        b.setToolTipText(tooltip);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public void setRutaActual(String ruta) {
        txtRuta.setText(ruta);
    }

    public void setOnAtras(Runnable r) {
        onAtras = r;
    }

    public void setOnAdelante(Runnable r) {
        onAdelante = r;
    }

    public void setOnOrganizar(Runnable r) {
        onOrganizar = r;
    }

    public void setOnNuevaCarpeta(Runnable r) {
        onNuevaCarpeta = r;
    }

    public void setOnCopiar(Runnable r) {
        onCopiar = r;
    }

    public void setOnPegar(Runnable r) {
        onPegar = r;
    }

    public void setOnRenombrar(Runnable r) {
        onRenombrar = r;
    }
}
