/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;
import udistrital.avanzada.taller.modelo.persistencia.ControlPersistencia;
import udistrital.avanzada.taller.vista.VentanaCrearEquipos;

/**
 * Clase principal de la capa de control que gestiona la lógica central del programa.
 * Se encarga de coordinar la carga de datos, creación de equipos y comunicación entre
 * los distintos controladores (interfaz, partida, persistencia, etc).
 *
 * <p>Esta clase cumple con el principio de responsabilidad única (SRP) y
 * sirve de enlace entre la interfaz gráfica y las operaciones internas del sistema.</p>
 *
 * @author Juan Sebastián Bravo
 * @version 8.0 - 07/10/2025
 */
public class ControlLogica {

    // Controladores auxiliares
    private final ControlInterfaz cInterfaz;
    private final ControlPartida cPartida;
    private final ControlPersistencia cPersistencia;
    private final ControlEquipos cEquipos;

    // Datos del modelo
    private List<Equipo> equipos;
    private List<Jugador> jugadoresDisponibles; // 🔹 ahora sí se inicializa correctamente


    /**
     * Constructor de la clase ControlLogica.
     * Inicializa todos los controladores secundarios.
     */
    public ControlLogica() {
        this.cPersistencia = new ControlPersistencia();
        this.cPartida = new ControlPartida();
        this.cEquipos = new ControlEquipos();
        this.cInterfaz = new ControlInterfaz(this);
        this.jugadoresDisponibles = new ArrayList<>();
        this.equipos = new ArrayList<>();
    }

    /**
     * Carga los equipos y jugadores desde un archivo .properties.
     * También configura el controlador de equipos y de partida.
     *
     * @param archivo archivo de configuración (.properties)
     * @return lista de equipos cargados
     */
    public List<Equipo> cargarEquipos(File archivo) {
        this.equipos = cPersistencia.cargarEquiposDesdeArchivo(archivo);

        // 🔹 Guardar también los jugadores disponibles globalmente
        this.jugadoresDisponibles = cPersistencia.getJugadoresDisponibles();

        // Configurar controladores dependientes
        cEquipos.setEquipos(equipos);
        cEquipos.setJugadoresDisponibles(jugadoresDisponibles);
        cPartida.setEquipos(equipos);

        return equipos;
    }

    /**
     * Crea nuevos equipos seleccionando jugadores desde los disponibles.
     * Llama a la ventana gráfica VentanaCrearEquipos y valida la selección.
     *
     * @return lista con los dos nuevos equipos creados
     */
    public List<Equipo> crearEquiposDesdeJugadoresDisponibles() {
        // 🔹 Asegurarse de tener jugadores cargados
        if (jugadoresDisponibles == null || jugadoresDisponibles.isEmpty()) {
            jugadoresDisponibles = cPersistencia.getJugadoresDisponibles();
        }

        // Abrir la ventana para que el usuario seleccione
        VentanaCrearEquipos dialog = new VentanaCrearEquipos(null, jugadoresDisponibles);
        dialog.setVisible(true);

        // Si el usuario canceló
        if (dialog.isCancelado()) {
            JOptionPane.showMessageDialog(
                null,
                "Operación cancelada. No se crearon equipos.",
                "Cancelado",
                JOptionPane.INFORMATION_MESSAGE
            );
            return new ArrayList<>();
        }

        // 🔹 Si el usuario completó la creación correctamente
        try {
            return crearEquiposDesdeDialogo(dialog);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                null,
                ex.getMessage(),
                "Error en la creación",
                JOptionPane.ERROR_MESSAGE
            );
            return new ArrayList<>();
        }
    }

    /**
     * Crea dos nuevos equipos a partir de los datos seleccionados
     * por el usuario en la ventana de creación de equipos.
     *
     * Este método respeta el patrón MVC, procesando la lógica del modelo
     * a partir de la información de la vista (VentanaCrearEquipos).
     *
     * @param dialog Ventana de creación de equipos con los datos seleccionados
     * @return Lista con los dos equipos creados
     * @throws IllegalArgumentException si hay errores en la selección
     */
    /**
 * Crea dos equipos a partir de la ventana con la selección del usuario.
 */
public List<Equipo> crearEquiposDesdeDialogo(VentanaCrearEquipos dialog) {

    // obtener listas de Jugador desde la vista (ya mapeadas)
    List<Jugador> jugadoresEquipo1 = dialog.getJugadoresEquipo1();
    List<Jugador> jugadoresEquipo2 = dialog.getJugadoresEquipo2();

    if (jugadoresEquipo1.size() != 4 || jugadoresEquipo2.size() != 4) {
        throw new IllegalArgumentException("Cada equipo debe tener exactamente 4 jugadores.");
    }

    // crear equipos (usar ArrayList explícito si el constructor lo exige)
    Equipo equipo1 = new Equipo(dialog.getNombreEquipo1(), new ArrayList<>(jugadoresEquipo1));
    Equipo equipo2 = new Equipo(dialog.getNombreEquipo2(), new ArrayList<>(jugadoresEquipo2));

    // reemplazar equipos en la lógica y en controles dependientes
    this.equipos = new ArrayList<>();
    this.equipos.add(equipo1);
    this.equipos.add(equipo2);

    cEquipos.setEquipos(this.equipos);
    cPartida.setEquipos(this.equipos);

    return java.util.Arrays.asList(equipo1, equipo2);
}


    // ============================
    // Métodos auxiliares
    // ============================

    /** @return Controlador de partida */
    public ControlPartida getControlPartida() {
        return cPartida;
    }

    /** @return Controlador de equipos */
    public ControlEquipos getControlEquipos() {
        return cEquipos;
    }

    /** @return Lista actual de equipos */
    public List<Equipo> getEquipos() {
        return equipos;
    }

    /** Reinicia la partida */
    public void reiniciarPartida() {
        cPartida.reiniciar();
    }

    /** Realiza un lanzamiento de argolla */
    public String lanzarArgolla() {
        return cPartida.lanzarArgolla();
    }

    /** @return true si la partida sigue activa */
    public boolean partidaActiva() {
        return cPartida.isPartidaActiva();
    }

    /** @return equipo ganador actual */
    public Equipo getGanador() {
        return cPartida.getGanador();
    }

    /** @return equipo que tiene el turno actual */
    public Equipo getEquipoEnTurno() {
        return cPartida.getEquipoEnTurno();
    }

    /** Avanza a la siguiente ronda */
    public boolean avanzarRonda() {
        return cPartida.avanzarRonda();
    }

    /** @return número de ronda actual */
    public int getRondaActual() {
        return cPartida.getRondaActual();
    }

    /** @return true si se pueden jugar más rondas */
    public boolean puedeJugarOtraRonda() {
        return cPartida.puedeJugarOtraRonda();
    }

    /** Crea un nuevo equipo desde código (sin ventana) */
    public Equipo crearNuevoEquipo(String nombreEquipo, List<Jugador> jugadores) {
        return cEquipos.crearEquipo(nombreEquipo, jugadores);
    }

    /** @return lista de jugadores disponibles para nuevos equipos */
    public List<Jugador> getJugadoresDisponibles() {
        return jugadoresDisponibles;
    }
}
