package udistrital.avanzada.taller.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.persistencia.GestorResultados;
import udistrital.avanzada.taller.vista.Inicio;
import udistrital.avanzada.taller.vista.VentanaPrincipal;
import udistrital.avanzada.taller.vista.VentanaResultados;

/**
 * Clase encargada de administrar la interfaz y coordinar las vistas.
 * Cumple el principio de responsabilidad única (SRP).
 * 
 * Creada por: Paula Martinez
 * Modificada por: Sebastián Bravo y Juan Ariza
 *
 * @author Paula Martinez   
 * @version 5.0
 * 06/10/2025
 */
public class ControlInterfaz implements ActionListener {

    private final ControlLogica cLogica;
    private final Inicio inicio;
    private VentanaPrincipal vPrincipal;
    private GestorResultados gestorResultados;

    public ControlInterfaz(ControlLogica cLogica) {
        this.cLogica = cLogica;
        this.inicio = new Inicio();
        this.gestorResultados = new GestorResultados();

        // Mostrar la ventana de inicio
        this.inicio.setVisible(true);

        // Listeners
        this.inicio.getBotonSalir().addActionListener(this);
        this.inicio.getBotonJugar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Botón Salir
        if (e.getSource() == inicio.getBotonSalir()) {
            inicio.dispose();
            System.exit(0);
        }

        // Botón Jugar: cargar equipos y abrir ventana principal
        if (e.getSource() == inicio.getBotonJugar()) {
            File archivo = inicio.obtenerArchivoEquipos();
            if (archivo == null) {
                inicio.mostrarMensaje("Debe seleccionar un archivo de equipos.");
                return;
            }
            
            try {
                cLogica.cargarEquipos(archivo);
                List<Equipo> equipos = cLogica.getEquipos();

                // Verificar si hay resultados previos
                if (gestorResultados.existenResultadosPrevios()) {
                    int opcion = JOptionPane.showConfirmDialog(
                        inicio,
                        "Se encontraron resultados de partidas anteriores.\n¿Desea verlos antes de comenzar?",
                        "Resultados Previos",
                        JOptionPane.YES_NO_OPTION
                    );
                    
                    if (opcion == JOptionPane.YES_OPTION) {
                        mostrarResultadosPrevios();
                    }
                }

                // Crear ventana principal
                vPrincipal = new VentanaPrincipal(this, equipos);
                inicio.dispose();
                vPrincipal.setVisible(true);

                // Listeners de la ventana principal
                vPrincipal.getBotonLanzarArgollaUno().addActionListener(this);
                vPrincipal.getBotonLanzarArgollaDos().addActionListener(this);
                
            } catch (IllegalArgumentException ex) {
                inicio.mostrarMensaje(ex.getMessage());
            }
        }

        // Lanzar argolla (desde ventana principal)
        if (vPrincipal != null && 
           (e.getSource() == vPrincipal.getBotonLanzarArgollaUno() || 
            e.getSource() == vPrincipal.getBotonLanzarArgollaDos())) {
            ejecutarLanzamiento();
        }
    }

    /**
     * Ejecuta un lanzamiento de argolla y maneja el resultado.
     */
    private void ejecutarLanzamiento() {
        try {
            String resultado = cLogica.lanzarArgolla();
            vPrincipal.actualizarResultado(resultado);
            vPrincipal.actualizarPuntajes();

            // Verificar si terminó la ronda
            if (!cLogica.partidaActiva()) {
                finalizarRonda();
            }
        } catch (IllegalStateException ex) {
            vPrincipal.mostrarMensaje(ex.getMessage());
        }
    }

    /**
     * Maneja la finalización de una ronda.
     */
    private void finalizarRonda() {
        Equipo ganador = cLogica.getGanador();
        if (ganador == null) return;
        
        int rondaActual = cLogica.getRondaActual();
        
        // Guardar resultados de ambos equipos
        try {
            List<Equipo> equipos = cLogica.getEquipos();
            for (Equipo equipo : equipos) {
                String resultado = equipo.equals(ganador) ? "GANADOR" : "PERDEDOR";
                gestorResultados.guardarResultado(equipo, resultado, rondaActual);
            }
        } catch (IOException ex) {
            System.err.println("Error al guardar resultados: " + ex.getMessage());
        }
        
        // Mostrar ventana de resultados
        VentanaResultados ventanaRes = new VentanaResultados(
            vPrincipal, 
            true, 
            ganador, 
            cLogica.getEquipos(),
            rondaActual
        );
        ventanaRes.setVisible(true);
        
        // Preguntar si desea jugar la revancha (segunda ronda)
        if (cLogica.puedeJugarOtraRonda()) {
            int opcion = JOptionPane.showConfirmDialog(
                vPrincipal,
                "¿Desean jugar la REVANCHA (Ronda 2)?",
                "Nueva Ronda",
                JOptionPane.YES_NO_OPTION
            );
            
            if (opcion == JOptionPane.YES_OPTION) {
                iniciarNuevaRonda();
            } else {
                mostrarResultadosFinales();
                System.exit(0);
            }
        } else {
            vPrincipal.mostrarMensaje("Se han completado las 2 rondas. ¡Partida finalizada!");
            mostrarResultadosFinales();
            System.exit(0);
        }
    }

    /**
     * Inicia una nueva ronda del juego.
     */
    private void iniciarNuevaRonda() {
        if (cLogica.avanzarRonda()) {
            vPrincipal.limpiarResultados();
            vPrincipal.actualizarPuntajes();
            vPrincipal.actualizarRonda();
            vPrincipal.mostrarMensaje("¡Nueva ronda iniciada!");
        }
    }

    /**
     * Muestra todos los resultados guardados.
     */
    private void mostrarResultadosPrevios() {
        try {
            List<String> resultados = gestorResultados.leerTodosLosResultados();
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(
                    inicio,
                    "No hay resultados previos registrados.",
                    "Resultados",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("HISTORIAL DE PARTIDAS\n\n");
            for (String res : resultados) {
                sb.append(res).append("\n");
            }
            
            JOptionPane.showMessageDialog(
                inicio,
                sb.toString(),
                "Resultados Previos",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                inicio,
                "Error al leer los resultados: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Muestra el resumen final de todas las rondas.
     */
    private void mostrarResultadosFinales() {
        try {
            List<String> resultados = gestorResultados.leerTodosLosResultados();
            if (resultados.isEmpty()) {
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════\n");
            sb.append("     RESULTADOS FINALES DE TODAS LAS RONDAS\n");
            sb.append("═══════════════════════════════════════════\n\n");
            
            for (String res : resultados) {
                sb.append(res).append("\n");
            }
            
            JOptionPane.showMessageDialog(
                vPrincipal,
                sb.toString(),
                "Resultados Finales",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException ex) {
            System.err.println("Error al mostrar resultados finales: " + ex.getMessage());
        }
    }
    
    /**
     * Obtiene el número de ronda actual.
     * 
     * @return número de ronda
     */
    public int getRondaActual() {
        return cLogica.getRondaActual();
    }
}