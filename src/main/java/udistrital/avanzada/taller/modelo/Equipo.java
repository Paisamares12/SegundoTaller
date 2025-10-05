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
 * @author Paula Martínez
 * @version 1.0
 * 30/09/2025
 */
public class Equipo {
    
    //Atributos
    private String nombre;
    private ArrayList<Jugador> jugadores;
    
    /**
     * Constructor para la clase equipo
     * 
     * @param nombre
     * @param jugadores 
     */
    public Equipo(String nombre) {
        this.nombre = nombre;
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
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
}
