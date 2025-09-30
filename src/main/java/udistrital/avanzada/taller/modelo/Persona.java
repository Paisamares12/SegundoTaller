/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo;

/**
 * Clase base que representa una persona dentro del juego. Contiene atributos
 * generales como el nombre.
 *
 * @author Paula Martínez
 * @version 1.0 30/09/2025
 */
public class Persona {
    
    //Atributos de la clase
    private String nombre;
    
    /**
     * Constructor de la clase Persona.
     * @param nombre 
     */
    public Persona(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Obtiene el nombre de la persona.
     * 
     * @return nombre de la persona
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Modifica el nombre de la persona.
     * 
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
