package udistrital.avanzada.taller.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Ventana modal para crear dos equipos a partir de jugadores disponibles.
 *
 * Interfaz:
 * - Lista izquierda: jugadores disponibles.
 * - Lista centro/derecha: jugadores seleccionados para Equipo 1 y Equipo 2.
 * - Botones para mover entre listas y quitar.
 * - Validación: exactamente 4 jugadores por equipo, sin duplicados.
 *
 * Uso:
 *  VentanaCrearEquipos dialog = new VentanaCrearEquipos(parentFrame, jugadoresDisponibles);
 *  dialog.setVisible(true);
 *  if (dialog.isEquiposCreados()) {
 *      List<Jugador> eq1 = dialog.getJugadoresEquipo1();
 *      List<Jugador> eq2 = dialog.getJugadoresEquipo2();
 *  } else if (dialog.isCancelado()) { ... }
 *
 * @author
 * @version 1.0
 */
public class VentanaCrearEquipos extends JDialog {

    private final DefaultListModel<String> modelDisponibles = new DefaultListModel<>();
    private final DefaultListModel<String> modelEquipo1 = new DefaultListModel<>();
    private final DefaultListModel<String> modelEquipo2 = new DefaultListModel<>();

    private final JList<String> listaDisponibles;
    private final JList<String> listaEquipo1;
    private final JList<String> listaEquipo2;

    private final JButton btnAddEq1 = new JButton("→ Equipo 1");
    private final JButton btnAddEq2 = new JButton("→ Equipo 2");
    private final JButton btnRemoveEq1 = new JButton("← Quitar");
    private final JButton btnRemoveEq2 = new JButton("← Quitar");

    private final JButton botonCrear = new JButton("Crear Equipos");
    private final JButton botonCancelar = new JButton("Cancelar");

    private final JTextField nombreEquipo1 = new JTextField("Equipo 1");
    private final JTextField nombreEquipo2 = new JTextField("Equipo 2");

    private final Map<String, Jugador> labelToJugador = new HashMap<>();

    private boolean cancelado = false;
    private boolean equiposCreados = false;

    /**
     * Constructor.
     *
     * @param parent ventana padre
     * @param disponibles lista de objetos Jugador leída del properties
     */
    public VentanaCrearEquipos(JFrame parent, List<Jugador> disponibles) {
        super(parent, "Crear Nuevos Equipos", true);

        // Mapear etiquetas y llenar modelo de disponibles
        for (Jugador j : Objects.requireNonNull(disponibles)) {
            String etiqueta = formatoLabel(j);
            labelToJugador.put(etiqueta, j);
            modelDisponibles.addElement(etiqueta);
        }

        // Crear JLists
        listaDisponibles = new JList<>(modelDisponibles);
        listaEquipo1 = new JList<>(modelEquipo1);
        listaEquipo2 = new JList<>(modelEquipo2);

        // Configuración de selección
        listaDisponibles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaEquipo1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaEquipo2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Layout
        setSize(760, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(8, 8));

        // Nombres
        JPanel pnlNombres = new JPanel(new GridLayout(1, 4, 6, 6));
        pnlNombres.add(new JLabel("Nombre del equipo 1:"));
        pnlNombres.add(nombreEquipo1);
        pnlNombres.add(new JLabel("Nombre del equipo 2:"));
        pnlNombres.add(nombreEquipo2);
        add(pnlNombres, BorderLayout.NORTH);

        // Panel central con listas y botones
        JPanel centro = new JPanel(new GridLayout(1, 3, 8, 8));

        // Panel disponibles
        JPanel pnlDisp = new JPanel(new BorderLayout());
        pnlDisp.setBorder(BorderFactory.createTitledBorder("Disponibles"));
        pnlDisp.add(new JScrollPane(listaDisponibles), BorderLayout.CENTER);

        // Panel de control central (botones)
        JPanel pnlControl = new JPanel(new GridLayout(4, 1, 6, 6));
        pnlControl.add(btnAddEq1);
        pnlControl.add(btnRemoveEq1);
        pnlControl.add(btnAddEq2);
        pnlControl.add(btnRemoveEq2);

        // Panel equipo1 y equipo2
        JPanel pnlEq1 = new JPanel(new BorderLayout());
        pnlEq1.setBorder(BorderFactory.createTitledBorder("Equipo 1 (max 4)"));
        pnlEq1.add(new JScrollPane(listaEquipo1), BorderLayout.CENTER);

        JPanel pnlEq2 = new JPanel(new BorderLayout());
        pnlEq2.setBorder(BorderFactory.createTitledBorder("Equipo 2 (max 4)"));
        pnlEq2.add(new JScrollPane(listaEquipo2), BorderLayout.CENTER);

        centro.add(pnlDisp);
        centro.add(pnlControl);
        centro.add(new JPanel(new GridLayout(2,1,6,6)) {{
            add(pnlEq1);
            add(pnlEq2);
        }});

        add(centro, BorderLayout.CENTER);

        // Botones inferiores
        JPanel pnlBotones = new JPanel();
        pnlBotones.add(botonCrear);
        pnlBotones.add(botonCancelar);
        add(pnlBotones, BorderLayout.SOUTH);

        // Acciones de botones
        btnAddEq1.addActionListener((ActionEvent e) -> moverAEquipo(modelDisponibles, modelEquipo1, modelEquipo2, listaDisponibles.getSelectedValuesList(), 1));
        btnAddEq2.addActionListener((ActionEvent e) -> moverAEquipo(modelDisponibles, modelEquipo2, modelEquipo1, listaDisponibles.getSelectedValuesList(), 2));

        btnRemoveEq1.addActionListener((ActionEvent e) -> devolverADisponibles(modelEquipo1, modelDisponibles, listaEquipo1.getSelectedValuesList()));
        btnRemoveEq2.addActionListener((ActionEvent e) -> devolverADisponibles(modelEquipo2, modelDisponibles, listaEquipo2.getSelectedValuesList()));

        botonCrear.addActionListener((ActionEvent e) -> accionCrear());
        botonCancelar.addActionListener((ActionEvent e) -> {
            cancelado = true;
            equiposCreados = false;
            dispose();
        });

        // Soporte doble clic: mover seleccionado directamente (opcional)
        listaDisponibles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    // doble clic -> priorizar equipo 1 si tiene menos de 4, si no equipo2
                    if (modelEquipo1.size() < 4) moverAEquipo(modelDisponibles, modelEquipo1, modelEquipo2, listaDisponibles.getSelectedValuesList(), 1);
                    else moverAEquipo(modelDisponibles, modelEquipo2, modelEquipo1, listaDisponibles.getSelectedValuesList(), 2);
                }
            }
        });
    }

    // ---- helpers ----

    private static String formatoLabel(Jugador j) {
        return j.getApodo() + " (" + j.getNombre() + ")";
    }

    /**
     * Mueve los elementos seleccionados desde el modelo source al target.
     * targetOpp es el otro equipo: se evita duplicados en ambos equipos.
     *
     * @param source modelo de donde salen (Disponibles)
     * @param target modelo destino (Equipo 1 o 2)
     * @param targetOpp modelo del equipo opuesto (para evitar duplicados)
     * @param selected lista de etiquetas seleccionadas
     * @param equipoNumero número (solo para mensajes)
     */
    private void moverAEquipo(DefaultListModel<String> source,
                             DefaultListModel<String> target,
                             DefaultListModel<String> targetOpp,
                             List<String> selected,
                             int equipoNumero) {
        if (selected == null || selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un jugador en Disponibles.", "Atención", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (String etiqueta : new ArrayList<>(selected)) {
            // no permitir si ya está en el otro equipo
            if (targetOpp.contains(etiqueta)) {
                JOptionPane.showMessageDialog(this, "El jugador " + etiqueta + " ya está en el otro equipo.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (target.contains(etiqueta)) continue; // ya está en este equipo

            if (target.size() >= 4) {
                JOptionPane.showMessageDialog(this, "El Equipo " + equipoNumero + " ya tiene 4 jugadores.", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
                break;
            }

            // mover: quitar de disponibles y añadir al equipo
            if (source.contains(etiqueta)) source.removeElement(etiqueta);
            target.addElement(etiqueta);
        }
    }

    private void devolverADisponibles(DefaultListModel<String> from, DefaultListModel<String> dispo, List<String> selected) {
        if (selected == null || selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un jugador del equipo para quitar.", "Atención", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        for (String etiqueta : new ArrayList<>(selected)) {
            if (from.contains(etiqueta)) from.removeElement(etiqueta);
            if (!dispo.contains(etiqueta)) dispo.addElement(etiqueta);
        }
    }

    private void accionCrear() {
        if (modelEquipo1.size() != 4 || modelEquipo2.size() != 4) {
            JOptionPane.showMessageDialog(this, "Cada equipo debe tener exactamente 4 jugadores.", "Selección inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        equiposCreados = true;
        cancelado = false;
        dispose();
    }

    // ---- getters que usa ControlLogica ----

    public boolean isCancelado() {
        return cancelado;
    }

    public boolean isEquiposCreados() {
        return equiposCreados;
    }

    public String getNombreEquipo1() {
        return nombreEquipo1.getText().trim();
    }

    public String getNombreEquipo2() {
        return nombreEquipo2.getText().trim();
    }

    /**
     * Devuelve objetos Jugador seleccionados para equipo 1, en el mismo orden que aparecen.
     */
    public List<Jugador> getJugadoresEquipo1() {
        List<Jugador> out = new ArrayList<>();
        for (int i = 0; i < modelEquipo1.size(); i++) {
            String etiqueta = modelEquipo1.getElementAt(i);
            Jugador j = labelToJugador.get(etiqueta);
            if (j != null) out.add(j);
        }
        return out;
    }

    /**
     * Devuelve objetos Jugador seleccionados para equipo 2, en el mismo orden que aparecen.
     */
    public List<Jugador> getJugadoresEquipo2() {
        List<Jugador> out = new ArrayList<>();
        for (int i = 0; i < modelEquipo2.size(); i++) {
            String etiqueta = modelEquipo2.getElementAt(i);
            Jugador j = labelToJugador.get(etiqueta);
            if (j != null) out.add(j);
        }
        return out;
    }
}

