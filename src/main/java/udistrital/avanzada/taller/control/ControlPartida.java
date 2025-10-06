/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

import java.util.List;
import java.util.Random;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.TipoLanzamiento;

/**
 * Gestiona la lógica de la partida, turnos y lanzamientos.
 * 
 * Originalmente creada por Juan Estevan Ariza
 * Modificada por Juan Sebastián Bravo Rojas
 * 
 * @author Juan Ariza
 * @version 5.0 06/10/2025
 */

//TODO: Crear el apartado de muerte subita e integrarlo
public class ControlPartida {
    private List<Equipo> equipos;
    private boolean partidaActiva;
    private int puntajeObjetivo;
    private int turnoActual;
    private int rondaActual;
    private int maximoRondas;
    private TipoLanzamiento ultimoLanzamiento;
    private final Random random;

    /**
     * Constructor base de ControlPartida.
     * El juego se juega a 21 puntos sin límite de turnos.
     * Se pueden jugar máximo 2 rondas (la segunda es revancha).
     */
    public ControlPartida() {
        this.partidaActiva = false;
        this.puntajeObjetivo = 21;
        this.turnoActual = 0;
        this.rondaActual = 1;
        this.maximoRondas = 2;
        this.random = new Random();
    }

    /**
     * Asigna los equipos que participarán en la partida.
     * 
     * @param equipos lista de equipos cargados
     */
    public void setEquipos(List<Equipo> equipos) {
        if (equipos == null || equipos.size() < 2) {
            throw new IllegalArgumentException("Debe haber al menos dos equipos para iniciar la partida.");
        }
        this.equipos = equipos;
        this.partidaActiva = true;
        this.turnoActual = 0;
        this.rondaActual = 1;
        reiniciarPuntajes();
    }

    /**
     * Simula el lanzamiento de argolla del equipo en turno.
     * 
     * @return descripción del resultado del lanzamiento
     * @throws IllegalStateException si no hay partida activa
     */
    
    //TODO: Revisar ordén para lanzamiento si hay más de dos equipos en el archivo de propiedades, y como seleccionar después más equipos
    public String lanzarArgolla() {
        if (!partidaActiva || equipos == null) {
            throw new IllegalStateException("No hay una partida activa.");
        }

        Equipo equipo = equipos.get(turnoActual);

        // Seleccionar aleatoriamente un tipo de lanzamiento
        TipoLanzamiento[] tipos = TipoLanzamiento.values();
        ultimoLanzamiento = tipos[random.nextInt(tipos.length)];
        
        int puntos = ultimoLanzamiento.getPuntos();
        equipo.sumarPuntos(puntos);

        String resultado = "╔═══════════════════════════════════════╗\n"
                + "  EQUIPO: " + equipo.getNombre() + "\n"
                + "  LANZAMIENTO: " + ultimoLanzamiento.getNombre() + "\n"
                + "  PUNTOS OBTENIDOS: " + puntos + "\n"
                + "  PUNTAJE TOTAL: " + equipo.getPuntaje() + " / " + puntajeObjetivo + "\n"
                + "╚═══════════════════════════════════════╝";

        // Verificar si ganó (llegó a 21 puntos)
        if (equipo.getPuntaje() >= puntajeObjetivo) {
            partidaActiva = false;
            resultado += "\n\n*** ¡" + equipo.getNombre() + " ha GANADO la ronda " + rondaActual + "! ***";
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
     * Reinicia completamente la partida para una nueva ronda.
     */
    public void reiniciar() {
        reiniciarPuntajes();
        this.partidaActiva = true;
        this.turnoActual = 0;
    }

    /**
     * Avanza a la siguiente ronda.
     * @return true si se pudo avanzar, false si se alcanzó el máximo
     */
    public boolean avanzarRonda() {
        if (rondaActual < maximoRondas) {
            rondaActual++;
            reiniciar();
            return true;
        }
        return false;
    }

    /**
     * Indica si la partida sigue activa.
     */
    public boolean isPartidaActiva() {
        return partidaActiva;
    }

    /**
     * Devuelve el equipo ganador de la ronda actual.
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
     */
    public void setPuntajeObjetivo(int nuevoObjetivo) {
        if (nuevoObjetivo <= 0) {
            throw new IllegalArgumentException("El puntaje objetivo debe ser positivo.");
        }
        this.puntajeObjetivo = nuevoObjetivo;
    }

    public int getPuntajeObjetivo() {
        return puntajeObjetivo;
    }

    /**
     * Devuelve el equipo que tiene el turno actual.
     */
    public Equipo getEquipoEnTurno() {
        if (equipos == null || equipos.isEmpty()) {
            return null;
        }
        return equipos.get(turnoActual);
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public int getMaximoRondas() {
        return maximoRondas;
    }

    public TipoLanzamiento getUltimoLanzamiento() {
        return ultimoLanzamiento;
    }

    /**
     * Verifica si se pueden jugar más rondas.
     */
    public boolean puedeJugarOtraRonda() {
        return rondaActual < maximoRondas;
    }
}
