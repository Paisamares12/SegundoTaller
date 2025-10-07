/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

import java.util.ArrayList;
import java.util.List;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Clase creada para la creación y ajuste de equipos
 * Creación de métodos para crear equipos con los jugadores disponibles en el archivo de propiedades
 * 
 * Originalmente creada por Juan Ariza
 * Modificada por Juan Sebastián Bravo Rojas
 * 
 * @author Juan Ariza
 * @version 7.0
 * 06/10/2025
 */
public class ControlEquipos {
    
    private List<Equipo> equipos;
    private List<Jugador> jugadoresDisponibles;
    
    /**
     * Constructor que inicializa las listas
     */
    public ControlEquipos() {
        this.equipos = new ArrayList<>();
        this.jugadoresDisponibles = new ArrayList<>();
    }
    
    /**
     * Establece la lista de equipos cargados
     * @param equipos lista de equipos
     */
    public void setEquipos(List<Equipo> equipos) {
        if (equipos != null) {
            this.equipos = new ArrayList<>(equipos);
        }
    }
    
    /**
     * Establece la lista de jugadores disponibles para crear equipos
     * @param jugadores lista de jugadores disponibles
     */
    public void setJugadoresDisponibles(List<Jugador> jugadores) {
        if (jugadores != null) {
            this.jugadoresDisponibles = new ArrayList<>(jugadores);
        }
    }
    
    /**
     * Crea un nuevo equipo con jugadores seleccionados
     * @param nombreEquipo nombre del equipo
     * @param jugadores lista de jugadores (debe ser exactamente 4)
     * @return el equipo creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    public Equipo crearEquipo(String nombreEquipo, List<Jugador> jugadores) {
        if (nombreEquipo == null || nombreEquipo.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
        }
        
        if (jugadores == null || jugadores.size() != 4) {
            throw new IllegalArgumentException("Un equipo debe tener exactamente 4 jugadores.");
        }
        
        // Verificar que todos los jugadores estén disponibles
        for (Jugador j : jugadores) {
            if (!jugadoresDisponibles.contains(j)) {
                throw new IllegalArgumentException("El jugador " + j.getNombre() + " no está disponible.");
            }
        }
        
        // Crear el equipo (sin duplicar los jugadores)
        Equipo nuevoEquipo = new Equipo(nombreEquipo, new ArrayList<>(jugadores));

        // Remover los jugadores de la lista de disponibles
        jugadoresDisponibles.removeAll(jugadores);

        equipos.add(nuevoEquipo);
        return nuevoEquipo;
    }
    
    /**
     * Obtiene la lista de equipos
     * @return lista de equipos
     */
    public List<Equipo> getEquipos() {
        return new ArrayList<>(equipos);
    }
    
    /**
     * Obtiene la lista de jugadores disponibles
     * @return lista de jugadores disponibles
     */
    public List<Jugador> getJugadoresDisponibles() {
        return new ArrayList<>(jugadoresDisponibles);
    }
    
    /**
     * Verifica si hay suficientes jugadores disponibles para crear un equipo
     * @return true si hay al menos 4 jugadores disponibles
     */
    public boolean haySuficientesJugadores() {
        return jugadoresDisponibles.size() >= 4;
    }
    
    /**
     * Libera los jugadores de un equipo, haciéndolos disponibles nuevamente
     * @param equipo equipo a deshacer
     */
    public void deshacerEquipo(Equipo equipo) {
        if (equipo != null && equipos.contains(equipo)) {
            jugadoresDisponibles.addAll(equipo.getJugadores());
            equipos.remove(equipo);
        }
    }
    
    /**
     * Obtiene un jugador disponible por su apodo
     * @param apodo apodo del jugador
     * @return el jugador si existe, null si no
     */
    public Jugador buscarJugadorDisponible(String apodo) {
        if (apodo == null) return null;
        for (Jugador j : jugadoresDisponibles) {
            if (j.getApodo().equalsIgnoreCase(apodo)) {
                return j;
            }
        }
        return null;
    }
    
    /**
     * Limpia todos los equipos y devuelve los jugadores a disponibles
     */
    public void limpiarEquipos() {
        for (Equipo equipo : equipos) {
            jugadoresDisponibles.addAll(equipo.getJugadores());
        }
        equipos.clear();
    }
}