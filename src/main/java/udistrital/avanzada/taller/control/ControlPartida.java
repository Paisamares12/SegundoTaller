/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

import java.util.List;
import java.util.Random;
import udistrital.avanzada.taller.modelo.Equipo;

/**
 * Creación de métodos para la creación de equipos y carga
 * 
 * Originalmente creada por Juan Estevan Ariza
 * Modificada por Juan Sebastián Bravo Rojas
 * 
 * 
 * @author Juan Ariza
 * @version 2.0 03/10/2025
 */
public class ControlPartida {
    private List<Equipo> equipos;
    private boolean partidaActiva;
    private int puntajeObjetivo;
    private int turnoActual; // Índice del equipo que lanza
    private final Random random;

    /**
     * Constructor base de ControlPartida.
     * No recibe datos externos, solo inicializa valores de control.
     */
    public ControlPartida() {
        this.partidaActiva = false;
        this.puntajeObjetivo = 20; // Gana quien llegue a 20 puntos
        this.turnoActual = 0;
        this.random = new Random();
    }

    /**
     * Asigna los equipos que participarán en la partida.
     * 
     * @param equipos lista de equipos cargados desde ControlPersistencia
     */
    public void setEquipos(List<Equipo> equipos) {
        if (equipos == null || equipos.size() < 2) {
            throw new IllegalArgumentException("Debe haber al menos dos equipos para iniciar la partida.");
        }
        this.equipos = equipos;
        this.partidaActiva = true;
        this.turnoActual = 0;
        reiniciarPuntajes();
    }

    /**
     * Simula el lanzamiento de argolla del equipo en turno.
     * 
     * @return descripción del resultado del lanzamiento
     * @throws IllegalStateException si no hay partida activa
     */
    public String lanzarArgolla() {
        if (!partidaActiva || equipos == null) {
            throw new IllegalStateException("No hay una partida activa.");
        }

        Equipo equipo = equipos.get(turnoActual);

        // Genera puntos aleatorios entre 0 y 5
        int puntos = random.nextInt(6);
        equipo.sumarPuntos(puntos);

        String resultado = "El equipo " + equipo.getNombre()
                + " lanzó la argolla y obtuvo " + puntos + " puntos.\n"
                + "Puntaje total: " + equipo.getPuntaje();

        // Verificar si ganó
        if (equipo.getPuntaje() >= puntajeObjetivo) {
            partidaActiva = false;
            resultado += "\n¡" + equipo.getNombre() + " ha ganado la partida!";
        } else {
            pasarTurno();
        }

        return resultado;
    }

    /**
     * Cambia el turno al siguiente equipo.
     */
    private void pasarTurno() {
        if (equipos != null && !equipos.isEmpty()) {
            turnoActual = (turnoActual + 1) % equipos.size();
        }
    }

    /**
     * Reinicia los puntajes de todos los equipos.
     */
    public void reiniciarPuntajes() {
        if (equipos != null) {
            for (Equipo e : equipos) {
                e.reiniciarPuntaje();
            }
        }
    }
    /**
     * Reinicia completamente la partida:
     * - Restablece los puntajes.
     * - Activa nuevamente la partida.
     * - Resetea el turno al primer equipo.
     */
    public void reiniciar() {
        reiniciarPuntajes();
        this.partidaActiva = true;
        this.turnoActual = 0;
    }

    /**
     * Indica si la partida sigue activa.
     * 
     * @return true si no hay ganador aún
     */
    public boolean isPartidaActiva() {
        return partidaActiva;
    }

    /**
     * Devuelve el equipo ganador, si ya terminó la partida.
     * 
     * @return equipo ganador o null si aún no hay ganador
     */
    public Equipo getGanador() {
        if (equipos == null) return null;
        for (Equipo e : equipos) {
            if (e.getPuntaje() >= puntajeObjetivo) {
                return e;
            }
        }
        return null;
    }

    /**
     * Cambia el puntaje necesario para ganar.
     * 
     * @param nuevoObjetivo nuevo puntaje objetivo
     */
    public void setPuntajeObjetivo(int nuevoObjetivo) {
        if (nuevoObjetivo <= 0) {
            throw new IllegalArgumentException("El puntaje objetivo debe ser positivo.");
        }
        this.puntajeObjetivo = nuevoObjetivo;
    }

    /**
     * Retorna el puntaje objetivo actual.
     * 
     * @return puntaje objetivo
     */
    public int getPuntajeObjetivo() {
        return puntajeObjetivo;
    }

    /**
     * Devuelve el equipo que tiene el turno actual.
     * 
     * @return equipo en turno
     */
    public Equipo getEquipoEnTurno() {
        if (equipos == null || equipos.isEmpty()) {
            return null;
        }
        return equipos.get(turnoActual);
    }
}
