/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import udistrital.avanzada.taller.control.ControlInterfaz;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Ventana principal mejorada del juego Argolla Llanera.
 * 
 * Creada originalmente por Juan Sebastian Bravo Rojas
 * Modificada: Juan Ariza
 * 
 * @author Juan Sebastián Bravo Rojas
 * @version 5.0 - 06/10/2025
 */
public class VentanaPrincipal extends JFrame {

    private final ControlInterfaz control;
    private final List<Equipo> equipos;

    // Componentes visuales
    private JTextArea areaResultados;
    private JLabel labelEquipoUno;
    private JLabel labelEquipoDos;
    private JLabel labelPuntajeUno;
    private JLabel labelPuntajeDos;
    private JLabel labelRonda;
    private JButton botonLanzarArgollaUno;
    private JButton botonLanzarArgollaDos;
    private JPanel panelEquipoUno;
    private JPanel panelEquipoDos;
    private JTextArea infoJugadoresUno;
    private JTextArea infoJugadoresDos;

    public VentanaPrincipal(ControlInterfaz control, List<Equipo> equipos) {
        this.control = control;
        this.equipos = equipos;
        inicializarComponentes();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Argolla Llanera - ¡Que gane el mejor equipo!");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(false);
    }

    private void inicializarComponentes() {
        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(245, 245, 220));

        // Panel superior - Información de ronda
        JPanel panelSuperior = crearPanelSuperior();
        
        // Panel central - Equipos y controles
        JPanel panelCentral = crearPanelCentral();
        
        // Panel inferior - Área de resultados
        JPanel panelInferior = crearPanelInferior();

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        getContentPane().add(panelPrincipal);
        setSize(1000, 750);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(139, 69, 19));
        panel.setPreferredSize(new Dimension(1000, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("ARGOLLA LLANERA");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        
        labelRonda = new JLabel("RONDA 1 de 2 - Juego a 21 puntos");
        labelRonda.setFont(new Font("Arial", Font.BOLD, 18));
        labelRonda.setForeground(Color.YELLOW);
        
        panel.setLayout(new GridLayout(2, 1));
        panel.add(titulo);
        panel.add(labelRonda);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(new Color(245, 245, 220));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Equipo 1
        panelEquipoUno = crearPanelEquipo(0, new Color(102, 205, 170));
        
        // Panel Equipo 2
        panelEquipoDos = crearPanelEquipo(1, new Color(255, 160, 122));

        panel.add(panelEquipoUno);
        panel.add(panelEquipoDos);

        return panel;
    }

    private JPanel crearPanelEquipo(int indice, Color colorFondo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(colorFondo);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 3),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        Equipo equipo = equipos.get(indice);

        // Nombre del equipo
        JLabel labelNombre = new JLabel(equipo.getNombre());
        labelNombre.setFont(new Font("Arial Black", Font.BOLD, 24));
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelNombre.setForeground(new Color(139, 69, 19));

        // Puntaje
        JLabel labelPuntaje;
        if (indice == 0) {
            labelPuntajeUno = new JLabel("Puntaje: 0");
            labelPuntaje = labelPuntajeUno;
        } else {
            labelPuntajeDos = new JLabel("Puntaje: 0");
            labelPuntaje = labelPuntajeDos;
        }
        labelPuntaje.setFont(new Font("Arial", Font.BOLD, 20));
        labelPuntaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPuntaje.setForeground(Color.BLACK);

        // Información de jugadores
        JTextArea infoJugadores = new JTextArea();
        infoJugadores.setEditable(false);
        infoJugadores.setFont(new Font("Monospaced", Font.PLAIN, 13));
        infoJugadores.setBackground(new Color(255, 255, 240));
        infoJugadores.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        StringBuilder sb = new StringBuilder();
        sb.append("JUGADORES:\n\n");
        List<Jugador> jugadores = equipo.getJugadores();
        for (int i = 0; i < Math.min(4, jugadores.size()); i++) {
            Jugador j = jugadores.get(i);
            sb.append(String.format("  %d. %s\n", i+1, j.getNombre()));
            sb.append(String.format("     \"%s\"\n\n", j.getApodo()));
        }
        infoJugadores.setText(sb.toString());
        
        if (indice == 0) {
            infoJugadoresUno = infoJugadores;
        } else {
            infoJugadoresDos = infoJugadores;
        }

        JScrollPane scrollJugadores = new JScrollPane(infoJugadores);
        scrollJugadores.setPreferredSize(new Dimension(400, 200));
        scrollJugadores.setMaximumSize(new Dimension(400, 200));

        // Botón de lanzar
        JButton botonLanzar = new JButton("LANZAR ARGOLLA");
        botonLanzar.setFont(new Font("Arial", Font.BOLD, 18));
        botonLanzar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonLanzar.setBackground(new Color(139, 69, 19));
        botonLanzar.setForeground(Color.WHITE);
        botonLanzar.setFocusPainted(false);
        botonLanzar.setBorder(BorderFactory.createRaisedBevelBorder());
        botonLanzar.setMaximumSize(new Dimension(250, 50));
        botonLanzar.setPreferredSize(new Dimension(250, 50));

        if (indice == 0) {
            botonLanzarArgollaUno = botonLanzar;
        } else {
            botonLanzarArgollaDos = botonLanzar;
        }

        // Agregar componentes al panel
        panel.add(labelNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(labelPuntaje);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(scrollJugadores);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(botonLanzar);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 220));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelTitulo = new JLabel("HISTORIAL DE LANZAMIENTOS");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        labelTitulo.setForeground(new Color(139, 69, 19));

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultados.setBackground(new Color(255, 255, 240));
        areaResultados.setText("Esperando el primer lanzamiento...\n");

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setPreferredSize(new Dimension(960, 200));

        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    public void actualizarResultado(String texto) {
        areaResultados.append(texto + "\n\n");
        areaResultados.setCaretPosition(areaResultados.getDocument().getLength());
    }

    public void actualizarPuntajes() {
        if (equipos.size() >= 2) {
            labelPuntajeUno.setText("Puntaje: " + equipos.get(0).getPuntaje());
            labelPuntajeDos.setText("Puntaje: " + equipos.get(1).getPuntaje());
        }
    }

    public void actualizarRonda() {
        // Obtener la ronda actual desde el control
        labelRonda.setText("RONDA " + control.getRondaActual() + " de 2 - Juego a 21 puntos");
    }

    public void limpiarResultados() {
        areaResultados.setText("Nueva ronda iniciada...\n\n");
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // Getters
    public JButton getBotonLanzarArgollaUno() {
        return botonLanzarArgollaUno;
    }

    public JButton getBotonLanzarArgollaDos() {
        return botonLanzarArgollaDos;
    }

    public JTextArea getAreaResultados() {
        return areaResultados;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }
}
