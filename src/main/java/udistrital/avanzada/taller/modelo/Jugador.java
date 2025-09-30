/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo;

/**
 * Clase que representa a un jugador, extiende de Persona.
 * Cada jugador tiene un apodo y puede estar asociado a un equipo.
 *
 * @author Paula Martínez
 * @version 1.0
 * 30/09/2025
 */
public class Jugador extends Persona {
    
    //Atributos
    private String Apodo;
    private String foto; //Todavia no estoy segura de como implementar esto jeje

    /**
     *  Construye un nuevo jugador con los datos especificados.
     * @param Apodo
     * @param foto
     * @param nombre 
     */
    public Jugador(String Apodo, String foto, String nombre) {
        super(nombre);
        this.Apodo = Apodo;
        this.foto = foto;
    }
    
    /**
     * Obtiene el apodo del jugador.
     * 
     * @return nombre del jugador
     */
    public String getApodo() {
        return Apodo;
    }
    
    /**
     * Modifica el apodo del jugador.
     * 
     * @param Apodo nuevo apodo
     */
    public void setApodo(String Apodo) {
        this.Apodo = Apodo;
    }
    
    /**
     * Obtiene la ruta de la foto
     *
     * @return ruta de la foto 
     */
    public String getFoto() {
        return foto;
    }

    /**
     * Modifica la ruta de la foto
     * 
     * @param foto nueva foto
     */
    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    
    
    
    
}
