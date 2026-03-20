/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab.pkg8.memoria;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
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

    public BarraHerramientas() {
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(4, 0));
        setBackground(new Color(0xECE9D8));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xACA899)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));

        JPanel panelNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        panelNav.setOpaque(false);

        btnAtras = crearBotonNav("\u2190"); // flecha izquierda
        btnAdelante = crearBotonNav("\u2192"); // flecha derecha
        btnAtras.setToolTipText("Atrás");
        btnAdelante.setToolTipText("Adelante");

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
        txtRuta.setBackground(Color.WHITE);
        txtRuta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x7F9DB9)),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
        panelAcciones.setOpaque(false);

        btnOrganizar = crearBotonAccion("Organizar");
        btnNuevaCarpeta = crearBotonAccion("Nueva carpeta");
        btnCopiar = crearBotonAccion("Copiar");
        btnPegar = crearBotonAccion("Pegar");
        btnRenombrar = crearBotonAccion("Renombrar");

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

        panelAcciones.add(btnOrganizar);
        panelAcciones.add(btnNuevaCarpeta);
        panelAcciones.add(btnCopiar);
        panelAcciones.add(btnPegar);
        panelAcciones.add(btnRenombrar);

        add(panelNav, BorderLayout.WEST);
        add(txtRuta, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.EAST);
    }

    private JButton crearBotonNav(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Tahoma", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(28, 24));
        b.setFocusPainted(false);
        b.setBackground(new Color(0xECE9D8));
        return b;
    }

    private JButton crearBotonAccion(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Tahoma", Font.PLAIN, 11));
        b.setPreferredSize(new Dimension(texto.length() * 7 + 16, 24));
        b.setFocusPainted(false);
        b.setBackground(new Color(0xECE9D8));
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


