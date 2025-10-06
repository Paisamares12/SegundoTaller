/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo;

//Importar librerias
import java.util.ArrayList;

/**
 * Clase que representa un equipo en el juego de la argolla.
 * Contiene el nombre del equipo y la lista de jugadores que lo conforman.
 *
 * Originalmente creada por Paula Martínez
 * Modificada por Juan Sebastián Bravo Rojas
 * 
 * @author Paula Martínez
 * @version 5.0
 * 06/10/2025
 */
public class Equipo {
    
    //Atributos
    private String nombre;
    private int puntaje;
    private ArrayList<Jugador> jugadores;
    
    /**
     * Constructor para la clase equipo
     * 
     * @param nombre
     * @param jugadores 
     */
    public Equipo(String nombre, ArrayList<Jugador> jugadores) {
        this.nombre = nombre;
        this.puntaje=0;
        if(jugadores!=null)
        this.jugadores = new ArrayList<>();
    }
    
    /**
     * Obtiene el nombre del equipo.
     * 
     * @return nombre del equipo
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Modifica el nombre del equipo.
     * 
     * @param nombre nuevo nombre
     */
//    public void setNombre(String nombre) {
//        if (nombre == null || nombre.isBlank()) {
//            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
//        }
//        this.nombre = nombre;
//    }

    public int getPuntaje() {
        return puntaje;
    }
    
    public void setPuntaje(int puntaje) {
        if (puntaje < 0) {
            throw new IllegalArgumentException("El puntaje no puede ser negativo.");
        }
        this.puntaje = puntaje;
    }
    
    
    /**
     * Aumenta el puntaje del equipo según los puntos obtenidos.
     * @param puntos cantidad de puntos ganados
     */
    public void sumarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("No se pueden sumar puntos negativos.");
        }
        this.puntaje += puntos;
    }
    
    /**
     * Reinicia el puntaje a cero (para una nueva ronda).
     */
    public void reiniciarPuntaje() {
        this.puntaje = 0;
    }

    
    /**
     * Obtiene la lista de jugadores del equipo.
     * 
     * @return lista de jugadores
     */
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    
    //Métodos aparte
    /**
     * Agrega jugadores a la lista de jugadroes del equipo
     * 
     * @param jugador 
     */
    public void agregarJugador(Jugador jugador){
        if (jugadores.size() >= 4) {
            throw new IllegalStateException("Un equipo no puede tener más de 4 jugadores.");
        }
        this.jugadores.add(jugador);
    }
    
    /**
     * Para validar que la cantidad de jugadores siempre sea 4
     * 
     * @return 
     */
    public int getCantidadJugadores(){
        return jugadores.size();
    }

    @Override
    public String toString() {
        return this.nombre+" - Puntos: "+this.puntaje;
    }
}
