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
 * Ventana principal mejorada del juego Argolla Llanera. Muestra información
 * detallada de los lanzamientos por jugador.
 *
 * Actualizaciones clara del estado de la ronda, mano y partida.
 *
 * Creada originalmente por Juan Sebastian Bravo Rojas Modificada: Juan Ariza
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 7.0 06/10/2025
 */
public class VentanaPrincipal extends JFrame {

    private final ControlInterfaz control;
    private final List<Equipo> equipos;

    // Componentes visuales
    private JPanel panelPrincipal;
    private JTextArea areaResultados;
    private JLabel labelEquipoUno;
    private JLabel labelEquipoDos;
    private JLabel labelPuntajeUno;
    private JLabel labelPuntajeDos;
    private JLabel labelRonda;
    private JLabel labelTurnoActual;
    private JButton botonLanzarArgollaUno;
    private JButton botonLanzarArgollaDos;
    private JPanel panelEquipoUno;
    private JPanel panelEquipoDos;
    private JTextArea infoJugadoresUno;
    private JTextArea infoJugadoresDos;
    private JPanel panelFoto;

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
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
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
        setSize(1200, 800);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(139, 69, 19));
        panel.setPreferredSize(new Dimension(1200, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("ARGOLLA LLANERA");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);

        labelRonda = new JLabel("RONDA 1 de 2 - Juego a 21 puntos");
        labelRonda.setFont(new Font("Arial", Font.BOLD, 20));
        labelRonda.setForeground(Color.YELLOW);

        labelTurnoActual = new JLabel("Turno: " + (equipos.isEmpty() ? "" : equipos.get(0).getNombre()));
        labelTurnoActual.setFont(new Font("Arial", Font.BOLD, 18));
        labelTurnoActual.setForeground(Color.CYAN);

        panel.setLayout(new GridLayout(3, 1));
        panel.add(titulo);
        panel.add(labelRonda);
        panel.add(labelTurnoActual);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(new Color(245, 245, 220));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelEquipoUno = crearPanelEquipo(0, new Color(102, 205, 170));
        JPanel panelEquipoDos = crearPanelEquipo(1, new Color(255, 160, 122));

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
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        Equipo equipo = equipos.get(indice);

        JLabel labelNombre = new JLabel(equipo.getNombre());
        labelNombre.setFont(new Font("Arial Black", Font.BOLD, 26));
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelNombre.setForeground(new Color(139, 69, 19));

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

        JTextArea infoJugadores = new JTextArea();
        infoJugadores.setEditable(false);
        infoJugadores.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoJugadores.setBackground(new Color(255, 255, 240));
        infoJugadores.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════╗\n");
        sb.append("║       JUGADORES DEL EQUIPO      ║\n");
        sb.append("╠════════════════════════════════╣\n");
        List<Jugador> jugadores = equipo.getJugadores();
        for (int i = 0; i < Math.min(4, jugadores.size()); i++) {
            Jugador j = jugadores.get(i);
            sb.append(String.format("║ %d. %-26s ║\n", i + 1, j.getNombre()));
            sb.append(String.format("║    \"%s\"%s║\n",
                    j.getApodo(),
                    " ".repeat(Math.max(0, 26 - j.getApodo().length()))));
            if (i < 3) {
                sb.append("║                                 ║\n");
            }
        }
        sb.append("╚════════════════════════════════╝");
        infoJugadores.setText(sb.toString());

        JScrollPane scrollJugadores = new JScrollPane(infoJugadores);
        scrollJugadores.setPreferredSize(new Dimension(500, 180));
        scrollJugadores.setMaximumSize(new Dimension(500, 180));

        // Panel para la foto
        panelFoto = new JPanel();
        panelFoto.setPreferredSize(new Dimension(180, 180));
        panelFoto.setMaximumSize(new Dimension(180, 180));
        panelFoto.setBackground(new Color(255, 255, 240));
        panelFoto.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        panelFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFoto.setLayout(new BorderLayout());
        setFoto();

        if (indice == 0) {
            infoJugadoresUno = infoJugadores;
        } else {
            infoJugadoresDos = infoJugadores;
        }

        JButton botonLanzar = new JButton("LANZAR ARGOLLA");
        botonLanzar.setFont(new Font("Arial", Font.BOLD, 16));
        botonLanzar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonLanzar.setBackground(new Color(139, 69, 19));
        botonLanzar.setForeground(Color.WHITE);
        botonLanzar.setFocusPainted(false);
        botonLanzar.setMaximumSize(new Dimension(200, 45));

        if (indice == 0) {
            botonLanzarArgollaUno = botonLanzar;
        } else {
            botonLanzarArgollaDos = botonLanzar;
        }

        panel.add(labelNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(labelPuntaje);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(scrollJugadores);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(panelFoto);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(botonLanzar);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 220));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelTitulo = new JLabel("📋 HISTORIAL DE LANZAMIENTOS");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        labelTitulo.setForeground(new Color(139, 69, 19));

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaResultados.setBackground(new Color(255, 255, 240));
        areaResultados.setText("🎯 Esperando el primer lanzamiento...\n\n");

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setPreferredSize(new Dimension(1150, 180));

        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Le da una imagen al panelFoto
     *
     * @param rutaFoto
     */
    public void setFoto() {
        String rutaFoto = equipos.get(0).getJugadores().get(0).getRutaFoto();
        // Crear el panel con la imagen
        JPanelImage panelFotito = new JPanelImage(panelFoto, rutaFoto);
        panelFoto.add(panelFotito);
        panelFoto.revalidate();
        panelFoto.repaint();
    }

    /**
     * Actualiza el área de resultados con nueva información
     */
    public void actualizarResultado(String texto) {
        areaResultados.append(texto + "\n\n");
        areaResultados.setCaretPosition(areaResultados.getDocument().getLength());
    }

    /**
     * Actualiza los puntajes mostrados de ambos equipos
     */
    public void actualizarPuntajes() {
        if (equipos.size() >= 2) {
            labelPuntajeUno.setText("Puntaje: " + equipos.get(0).getPuntaje());
            labelPuntajeDos.setText("Puntaje: " + equipos.get(1).getPuntaje());
        }
    }

    /**
     * Actualiza la etiqueta de ronda actual
     */
    public void actualizarRonda() {
        labelRonda.setText("RONDA " + control.getRondaActual() + " de 2 - Juego a 21 puntos");
    }

    /**
     * Actualiza la etiqueta del turno actual
     */
    public void actualizarTurno(String nombreEquipo) {
        labelTurnoActual.setText("🎯 Turno: " + nombreEquipo);
    }

    /**
     * Limpia el área de resultados para una nueva ronda
     */
    public void limpiarResultados() {
        areaResultados.setText("🔄 Nueva ronda iniciada...\n\n"
                + "📌 Los puntajes se han reiniciado a 0\n"
                + "🎯 ¡Que comience la revancha!\n\n");
    }

    /**
     * Muestra un mensaje en un diálogo
     */
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

    public JPanel getPanelFoto() {
        return panelFoto;
    }
}
