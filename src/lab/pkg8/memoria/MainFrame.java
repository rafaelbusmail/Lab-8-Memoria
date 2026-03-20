/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab.pkg8.memoria;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private PanelArbol panelArbol;
    private PanelContenido panelContenido;
    private BarraHerramientas barra;
    private JLabel lblEstado;

    public MainFrame() {
        super("JAVA CENTER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 560);
        setMinimumSize(new Dimension(700, 450));
        setLocationRelativeTo(null);
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());

        // encabezado azul institucional
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(new Color(0x2C5F8A));
        panelTitulo.setPreferredSize(new Dimension(0, 36));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        JLabel lblTitulo = new JLabel("JAVA CENTER");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo, BorderLayout.WEST);

        barra = new BarraHerramientas();
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(barra, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(220);
        split.setDividerSize(4);
        split.setBorder(null);

        panelArbol    = new PanelArbol();
        panelContenido = new PanelContenido();

        split.setLeftComponent(panelArbol);
        split.setRightComponent(panelContenido);
        add(split, BorderLayout.CENTER);

        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(new Color(0xD4D0C8));
        panelEstado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0x808080)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        panelEstado.setPreferredSize(new Dimension(0, 22));

        lblEstado = new JLabel("Listo");
        lblEstado.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(0x333333));

        JLabel lblCreditos = new JLabel("Derechos Reservados UNITEC");
        lblCreditos.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblCreditos.setForeground(new Color(0x444444));

        panelEstado.add(lblEstado,   BorderLayout.WEST);
        panelEstado.add(lblCreditos, BorderLayout.EAST);
        add(panelEstado, BorderLayout.SOUTH);

        panelArbol.setOnCarpetaSeleccionada(ruta -> {
            barra.setRutaActual(ruta);
            panelContenido.cargarCarpeta(ruta);
            setEstado("Carpeta: " + ruta);
        });

        barra.setOnOrganizar(  () -> panelContenido.organizar());
        barra.setOnNuevaCarpeta(() -> panelContenido.nuevaCarpeta());
        barra.setOnCopiar(     () -> panelContenido.copiar());
        barra.setOnPegar(      () -> panelContenido.pegar());
        barra.setOnRenombrar(  () -> panelContenido.renombrar());

        barra.setOnAtras(() -> {
            String ant = panelArbol.navegarAtras();
            if (ant != null) {
                barra.setRutaActual(ant);
                panelContenido.cargarCarpeta(ant);
            }
        });
        barra.setOnAdelante(() -> {
            String sig = panelArbol.navegarAdelante();
            if (sig != null) {
                barra.setRutaActual(sig);
                panelContenido.cargarCarpeta(sig);
            }
        });
    }

    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }
}