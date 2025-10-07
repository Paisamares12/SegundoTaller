package udistrital.avanzada.taller.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;
import udistrital.avanzada.taller.modelo.persistencia.GestorResultados;
import udistrital.avanzada.taller.vista.Inicio;
import udistrital.avanzada.taller.vista.VentanaPrincipal;
import udistrital.avanzada.taller.vista.VentanaResultados;
import udistrital.avanzada.taller.vista.VentanaCrearEquipos;

/**
 * ControlInterfaz coordina el flujo principal entre la lógica del programa y las ventanas gráficas.
 * 
 * <p>Se encarga de manejar los eventos de la ventana de inicio, gestionar la creación
 * de nuevos equipos, cargar resultados previos y controlar el flujo de las partidas.</p>
 *
 * <p>Aplica el principio de responsabilidad única (SRP) separando las tareas de interfaz
 * de la lógica de negocio y persistencia.</p>
 * 
 * @author Paula
 * @version 9.0 - 07/10/2025
 */
public class ControlInterfaz implements ActionListener {

    private final ControlLogica cLogica;
    private final Inicio inicio;
    private VentanaPrincipal vPrincipal;
    private final GestorResultados gestorResultados;

    /**
     * Constructor: inicializa el controlador principal y muestra la ventana de inicio.
     * 
     * @param cLogica instancia del controlador lógico principal
     */
    public ControlInterfaz(ControlLogica cLogica) {
        this.cLogica = cLogica;
        this.inicio = new Inicio();
        this.gestorResultados = new GestorResultados();

        // Mostrar ventana de inicio
        this.inicio.setVisible(true);

        // Listeners
        this.inicio.getBotonSalir().addActionListener(this);
        this.inicio.getBotonJugar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // ---- BOTÓN SALIR ----
        if (e.getSource() == inicio.getBotonSalir()) {
            inicio.dispose();
            System.exit(0);
        }

        // ---- BOTÓN JUGAR ----
        if (e.getSource() == inicio.getBotonJugar()) {
            File archivo = inicio.obtenerArchivoEquipos();
            if (archivo == null) {
                inicio.mostrarMensaje("Debe seleccionar un archivo de equipos.");
                return;
            }

            try {
                // Cargar equipos y jugadores
                cLogica.cargarEquipos(archivo);
                List<Equipo> equipos = cLogica.getEquipos();

                // Si existen resultados previos, ofrecer opciones
                if (gestorResultados.existenResultadosPrevios()) {
                    Object[] opciones = {
                        "Ver resultados",
                        "Crear nuevos equipos",
                        "Continuar sin cambios"
                    };

                    int eleccion = JOptionPane.showOptionDialog(
                        inicio,
                        "Se encontraron resultados de partidas anteriores.\n¿Qué deseas hacer?",
                        "Resultados Previos",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                    );

                    // Opción 1: Ver resultados previos
                    if (eleccion == 0) {
                        mostrarResultadosPrevios();
                        return;
                    }

                    // Opción 2: Crear nuevos equipos
                    else if (eleccion == 1) {
                        List<Jugador> disponibles = cLogica.getJugadoresDisponibles();
                        VentanaCrearEquipos dialog = new VentanaCrearEquipos(inicio, disponibles);
                        dialog.setVisible(true);

                        // Si se cancela, detener flujo
                        if (dialog.isCancelado()) {
                            JOptionPane.showMessageDialog(
                                inicio,
                                "Operación cancelada. Regresando al inicio.",
                                "Cancelado",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            return;
                        }

                        try {
                            // Crear equipos desde el diálogo
                            List<Equipo> nuevos = cLogica.crearEquiposDesdeDialogo(dialog);
                            JOptionPane.showMessageDialog(
                                inicio,
                                "Se han creado dos nuevos equipos:\n" +
                                nuevos.get(0).getNombre() + " y " + nuevos.get(1).getNombre(),
                                "Equipos creados",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            equipos = nuevos; // Actualiza los equipos activos
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(
                                inicio,
                                "Error al crear equipos: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                    }
                }

                // ---- Iniciar ventana principal ----
                vPrincipal = new VentanaPrincipal(this, equipos);
                inicio.dispose();
                vPrincipal.setVisible(true);

                // Listeners de botones
                vPrincipal.getBotonLanzarArgollaUno().addActionListener(this);
                vPrincipal.getBotonLanzarArgollaDos().addActionListener(this);

            } catch (IllegalArgumentException ex) {
                inicio.mostrarMensaje(ex.getMessage());
            }
        }

        // ---- LANZAR ARGOLLA ----
        if (vPrincipal != null
                && (e.getSource() == vPrincipal.getBotonLanzarArgollaUno()
                || e.getSource() == vPrincipal.getBotonLanzarArgollaDos())) {
            ejecutarLanzamiento();
        }
    }

    // ================================================================
    // Métodos auxiliares del flujo de partida
    // ================================================================

    /** Ejecuta un lanzamiento de argolla y actualiza la interfaz. */
    private void ejecutarLanzamiento() {
        try {
            String resultado = cLogica.lanzarArgolla();
            vPrincipal.actualizarResultado(resultado);
            vPrincipal.actualizarPuntajes();

            if (!cLogica.partidaActiva()) {
                finalizarRonda();
            }
        } catch (IllegalStateException ex) {
            vPrincipal.mostrarMensaje(ex.getMessage());
        }
    }

    /** Finaliza la ronda actual, guarda los resultados y ofrece revancha. */
    private void finalizarRonda() {
        Equipo ganador = cLogica.getGanador();
        if (ganador == null) return;

        int rondaActual = cLogica.getRondaActual();

        try {
            for (Equipo equipo : cLogica.getEquipos()) {
                String resultado = equipo.equals(ganador) ? "GANADOR" : "PERDEDOR";
                gestorResultados.guardarResultado(equipo, resultado, rondaActual);
            }
        } catch (IOException ex) {
            vPrincipal.mostrarMensaje("Error al guardar resultados: " + ex.getMessage());
        }

        // Mostrar resultados de la ronda
        VentanaResultados ventanaRes = new VentanaResultados(
            vPrincipal,
            true,
            ganador,
            cLogica.getEquipos(),
            rondaActual
        );
        ventanaRes.setVisible(true);

        // Preguntar revancha
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

    /** Inicia una nueva ronda y actualiza la interfaz. */
    private void iniciarNuevaRonda() {
        if (cLogica.avanzarRonda()) {
            limpiarResultados();
            vPrincipal.actualizarPuntajes();
            vPrincipal.actualizarRonda();
            vPrincipal.mostrarMensaje("¡Nueva ronda iniciada!");
        }
    }

    /** Muestra los resultados previos almacenados. */
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

            StringBuilder sb = new StringBuilder("HISTORIAL DE PARTIDAS\n\n");
            for (String res : resultados) sb.append(res).append("\n");

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

    /** Muestra todos los resultados finales. */
    private void mostrarResultadosFinales() {
        try {
            List<String> resultados = gestorResultados.leerTodosLosResultados();
            if (resultados.isEmpty()) return;

            StringBuilder sb = new StringBuilder();
            sb.append("╔═══════════════════════════════════════════════════════╗\n");
            sb.append("     RESULTADOS FINALES DE TODAS LAS RONDAS\n");
            sb.append("╚═══════════════════════════════════════════════════════╝\n\n");

            for (String res : resultados) sb.append(res).append("\n");

            JOptionPane.showMessageDialog(
                vPrincipal,
                sb.toString(),
                "Resultados Finales",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException ex) {
            vPrincipal.mostrarMensaje("Error al mostrar resultados finales: " + ex.getMessage());
        }
    }

    /** Devuelve la ronda actual. */
    public int getRondaActual() {
        return cLogica.getRondaActual();
    }

    /** Limpia el área de resultados para iniciar una nueva ronda. */
    public void limpiarResultados() {
        this.vPrincipal.getAreaResultados().setText(
            "🔄 Nueva ronda iniciada...\n\n" +
            "📌 Los puntajes se han reiniciado a 0\n" +
            "🎯 ¡Que comience la revancha!\n\n"
        );
    }
}

