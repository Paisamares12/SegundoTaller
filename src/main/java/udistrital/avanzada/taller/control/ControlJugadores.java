/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

import java.util.ArrayList;
import java.util.List;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Clase encargada de administrar los jugadores del sistema.
 * Cumple el principio de responsabilidad única (SRP):
 * solo gestiona la lógica de los jugadores, no persistencia ni GUI.
 *
 * Originalmente creada por Paula Martínez
 * Modificada por Juan Sebastián Bravo Rojas 
 * 
 * 
 * @author Paula Martinez
 * @version 5.0
 * 06/10/2025
 */
public class ControlJugadores {

    private List<Jugador> jugadores;

    /**
     * Constructor: inicializa la lista de jugadores.
     */
    public ControlJugadores() {
        this.jugadores = new ArrayList<>();
    }

    /**
     * Crea un nuevo jugador si no existe otro con el mismo apodo.
     *
     * @param nombre nombre real del jugador
     * @param apodo apodo del jugador (único)
     * @param foto ruta o nombre del archivo de la foto
     * @return true si el jugador se creó correctamente
     * @throws IllegalArgumentException si los datos son inválidos o el apodo ya existe
     */
    public boolean crearJugador(String nombre, String apodo, String foto) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío.");
        }
        if (apodo == null || apodo.isBlank()) {
            throw new IllegalArgumentException("El apodo del jugador no puede estar vacío.");
        }
        if (existeApodo(apodo)) {
            throw new IllegalArgumentException("Ya existe un jugador con el apodo '" + apodo + "'.");
        }

        jugadores.add(new Jugador(nombre, apodo, foto));
        return true;
    }

    /**
     * Busca un jugador por su apodo.
     *
     * @param apodo apodo del jugador
     * @return el jugador encontrado o null si no existe
     */
    public Jugador buscarPorApodo(String apodo) {
        if (apodo == null) return null;
        for (Jugador j : jugadores) {
            if (j.getApodo().equalsIgnoreCase(apodo)) {
                return j;
            }
        }
        return null;
    }

    /**
     * Verifica si un jugador con el apodo dado ya existe.
     *
     * @param apodo apodo a verificar
     * @return true si ya existe, false si no
     */
    public boolean existeApodo(String apodo) {
        return buscarPorApodo(apodo) != null;
    }

    /**
     * Devuelve la lista completa de jugadores registrados.
     *
     * @return lista de jugadores
     */
    public List<Jugador> obtenerJugadores() {
        return new ArrayList<>(jugadores);
    }

    /**
     * Limpia todos los jugadores registrados.
     */
    public void limpiarJugadores() {
        jugadores.clear();
    }

    /**
     * Elimina un jugador por su apodo.
     *
     * @param apodo apodo del jugador a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminarJugador(String apodo) {
        Jugador jugador = buscarPorApodo(apodo);
        if (jugador != null) {
            jugadores.remove(jugador);
            return true;
        }
        return false;
    }
}
