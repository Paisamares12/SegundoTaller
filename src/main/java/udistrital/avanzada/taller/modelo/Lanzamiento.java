/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo;

/**
 * Clase que representa un lanzamiento realizado por un jugador.
 * Almacena el resultado del lanzamiento y los puntos obtenidos.
 *
 * @author Paula Martínez
 * @version 4.0
 * 30/09/2025
 */
public class Lanzamiento {
    
    //Atributos
    private Jugador jugador; //Para saber que jugador realizo el lanzamiento
    private String resultado; //Moñona, engarzada, hueco, palmo, timbre, otro
    private int puntos; //Se calculan los puntos respecto al resultado

    /**
     * Constructor clase Lanzamiento
     * 
     * @param jugador
     * @param resultado
     * @param puntos 
     */
    public Lanzamiento(Jugador jugador, String resultado, int puntos) {
        this.jugador = jugador;
        this.resultado = resultado;
        this.puntos = puntos;
    }
    
    /**
     * Obtiene el jugador que esta realizando el lanzamiento
     * 
     * @return Jugador actual
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Tipo de resultado moñona,engarzada, hueco, palmo, timbre, otro
     * 
     * @return resultado del lanzamiento 
     */
    public String getResultado() {
        return resultado;
    }
    
    /**
     * Obtiene la cantidad de puntos relacionados al tipo del resultado
     * 
     * @return puntos del lanzamiento
     */
    public int getPuntos() {
        return puntos;
    } 
}
