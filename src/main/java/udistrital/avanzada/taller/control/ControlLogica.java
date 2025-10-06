/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

import java.io.File;
import udistrital.avanzada.taller.modelo.Equipo;
import java.util.List;
import udistrital.avanzada.taller.modelo.persistencia.ControlPersistencia;

/**
 * Clase que maneja toda la lógica del aplicativo, conecta modelo con control.
 * 
 * Originalmente creada por Paula Martínez
 * Modificada por Juan Sebastián Bravo Rojas y Juan Ariza
 * 
 * 
 * @author Paula Martínez
 * @version 4.0 06/10/2025 
 */
public class ControlLogica {

    private ControlInterfaz cInterfaz;
    private ControlPartida cPartida;
    private ControlPersistencia cPersistencia;
    private List<Equipo> equipos;

    /**
     * Constructor clase ControlLogica.
     */
    public ControlLogica() {
        this.cInterfaz = new ControlInterfaz(this);
        this.cPersistencia = new ControlPersistencia();
        this.cPartida = new ControlPartida();
    }
    
    /**
     * Carga los equipos desde un archivo .properties
     * @param archivo archivo seleccionado por el usuario
     * @return lista de equipos cargados
     */
    public List<Equipo> cargarEquipos(File archivo) {
        this.equipos = cPersistencia.cargarEquiposDesdeArchivo(archivo);
        cPartida.setEquipos(equipos);
        return equipos;
    }
    
    /**
     * Devuelve el controlador de partida.
     */
    public ControlPartida getControlPartida() {
        return cPartida;
    }

    /**
     * Devuelve la lista de equipos cargados.
     */
    public List<Equipo> getEquipos() {
        return equipos;
    }

    /**
     * Reinicia la partida para una nueva ronda.
     */
    public void reiniciarPartida() {
        cPartida.reiniciar();
    }
    
    /**
     * Realiza un lanzamiento de argolla del equipo en turno.
     * 
     * @return mensaje descriptivo del resultado
     */
    public String lanzarArgolla() {
        return cPartida.lanzarArgolla();
    }
    
    /**
     * Retorna si la partida sigue activa.
     * 
     * @return true si la partida continúa
     */
    public boolean partidaActiva() {
        return cPartida.isPartidaActiva();
    }

    /**
     * Devuelve el equipo ganador, si ya hay uno.
     * 
     * @return equipo ganador o null
     */
    public Equipo getGanador() {
        return cPartida.getGanador();
    }

    /**
     * Devuelve el equipo que tiene el turno actual.
     * 
     * @return equipo en turno
     */
    public Equipo getEquipoEnTurno() {
        return cPartida.getEquipoEnTurno();
    }
    
    /**
     * Avanza a la siguiente ronda.
     * 
     * @return true si se pudo avanzar
     */
    public boolean avanzarRonda() {
        return cPartida.avanzarRonda();
    }
    
    /**
     * Obtiene el número de ronda actual.
     * 
     * @return número de ronda
     */
    public int getRondaActual() {
        return cPartida.getRondaActual();
    }
    
    /**
     * Verifica si se pueden jugar más rondas.
     * 
     * @return true si hay rondas disponibles
     */
    public boolean puedeJugarOtraRonda() {
        return cPartida.puedeJugarOtraRonda();
    }
}