/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo;

/**
 * Clase que representa una partida de argolla. Contiene los equipos
 * participantes y el estado de la partida.
 *
 * @author Paula Martínez
 * @version 1.0 30/09/2025
 */
public class Partida {

    //Atributos
    private Equipo equipoA;
    private Equipo equipoB;
    private int puntajeA; //Puntaje equipo A
    private int puntajeB; //Puntaje equipo B
    private int ronda;

    /**
     * Constructor clase Partida
     * 
     * @param equipoA
     * @param equipoB
     * @param puntajeA
     * @param puntajeB
     * @param ronda 
     */
    public Partida(Equipo equipoA, Equipo equipoB, int puntajeA, int puntajeB, int ronda) {
        this.equipoA = equipoA;
        this.equipoB = equipoB;
        this.puntajeA = 0;
        this.puntajeB = 0;
        this.ronda = 1;
    }  

    /**
     * Obtiene el equipo A de la partida.
     *
     * @return Equipo A
     */
    public Equipo getEquipoA() {
        return equipoA;
    }

    /**
     * Obtiene el equipo B de la partida.
     *
     * @return Equipo B
     */
    public Equipo getEquipoB() {
        return equipoB;
    }

    /**
     * Obtiene el puntaje actual del equipo A.
     *
     * @return Puntaje del equipo A
     */
    public int getPuntajeA() {
        return puntajeA;
    }
    
    /**
     * Modifica el puntaje del equipo A
     * 
     * @param puntajeA
     */
    public void setPuntajeA(int puntajeA) {
        this.puntajeA = puntajeA;
    }

    /**
     * Obtiene el puntaje actual del equipo B.
     *
     * @return Puntaje del equipo B
     */
    public int getPuntajeB() {
        return puntajeB;
    }

    /**
     * Modifica el puntaje del equipo B
     * 
     * @param puntajeB
     */
    public void setPuntajeB(int puntajeB) {
        this.puntajeB = puntajeB;
    }

    /**
     * Obtiene el número de la ronda actual.
     *
     * @return Número de ronda
     */
    public int getRonda() {
        return ronda;
    }

    /**
     * Modifica la ronda de la partida
     * 
     * @param ronda 
     */
    public void setRonda(int ronda) {
        this.ronda = ronda;
    }

}
