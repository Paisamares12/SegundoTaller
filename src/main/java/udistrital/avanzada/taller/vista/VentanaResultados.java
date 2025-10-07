/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Ventana emergente que muestra los resultados de una ronda.
 * 
 * @author Juan Ariza
 * @version 7.0
 * 06/10/2025
 */
public class VentanaResultados extends JDialog {
    
    private JTextArea areaResultados;
    
    /**
     * Constructor de la ventana de resultados.
     * 
     * @param parent ventana padre
     * @param modal si es modal o no
     * @param ganador equipo ganador de la ronda
     * @param equipos todos los equipos participantes
     * @param ronda número de ronda
     */
    public VentanaResultados(JFrame parent, boolean modal, Equipo ganador, 
                            List<Equipo> equipos, int ronda) {
        super(parent, modal);
        initComponents(ganador, equipos, ronda);
    }
    
    /**
     * Inicializa los componentes de la ventana.
     */
    private void initComponents(Equipo ganador, List<Equipo> equipos, int ronda) {
        setTitle("Resultados - Ronda " + ronda);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaResultados.setBackground(new Color(245, 245, 220));
        
        JScrollPane scroll = new JScrollPane(areaResultados);
        add(scroll);
        
        // Construir el texto de resultados
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════╗\n");
        sb.append("║                   RESULTADOS - RONDA ").append(ronda).append("                   ║\n");
        sb.append("╚═══════════════════════════════════════════════════════════╝\n\n");
        
        sb.append("★★★ EQUIPO GANADOR: ").append(ganador.getNombre()).append(" ★★★\n");
        sb.append("    Puntaje Final: ").append(ganador.getPuntaje()).append(" puntos\n\n");
        
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                    DETALLE DE EQUIPOS\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");
        
        for (Equipo equipo : equipos) {
            sb.append("┌─────────────────────────────────────────────────────────┐\n");
            sb.append("│ EQUIPO: ").append(String.format("%-46s", equipo.getNombre())).append("│\n");
            sb.append("│ PUNTAJE: ").append(String.format("%-45d", equipo.getPuntaje())).append("│\n");
            sb.append("│ RESULTADO: ").append(String.format("%-43s", 
                equipo.equals(ganador) ? "GANADOR ★" : "PERDEDOR")).append("│\n");
            sb.append("├─────────────────────────────────────────────────────────┤\n");
            sb.append("│ JUGADORES:                                              │\n");
            
            List<Jugador> jugadores = equipo.getJugadores();
            for (int i = 0; i < Math.min(4, jugadores.size()); i++) {
                Jugador j = jugadores.get(i);
                String info = String.format("  %d. %-25s [%s]", 
                    i+1, j.getNombre(), j.getApodo());
                sb.append("│ ").append(String.format("%-56s", info)).append("│\n");
            }
            
            sb.append("└─────────────────────────────────────────────────────────┘\n\n");
        }
        
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("           Estos resultados han sido guardados\n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        
        areaResultados.setText(sb.toString());
        areaResultados.setCaretPosition(0);
    }
}