/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo;

/**
 * Clase que representa a un jugador, extiende de Persona.
 * Cada jugador tiene un apodo y puede estar asociado a un equipo.
 *
 * Originalmente creada por Paula Martínez
 * Modificada por Juan Sebastián Bravo Rojas
 * 
 * @author Paula Martínez
 * @version 5.0
 * 06/10/2025
 */
public class Jugador extends Persona {
    
    //Atributos
    private String apodo;
    private String rutaFoto;

    /**
     *  Construye un nuevo jugador con los datos especificados.
     * @param Apodo
     * @param foto
     * @param nombre 
     */
    public Jugador(String nombre, String rutaFoto, String apodo) {
    super(nombre == null || nombre.isBlank() ? "Sin nombre" : nombre);
    this.apodo = (apodo != null) ? apodo : "";
    this.rutaFoto = (rutaFoto != null) ? rutaFoto : "";
}
    
    /**
     * Obtiene el apodo del jugador.
     * 
     * @return nombre del jugador
     */
    public String getApodo() {
        return apodo;
    }
    
    /**
     * Modifica el apodo del jugador.
     * 
     * @param Apodo nuevo apodo
     */
    public void setApodo(String Apodo) {
        this.apodo = Apodo;
    }
    
    /**
     * Obtiene la ruta de la foto
     *
     * @return ruta de la foto 
     */
    public String getRutaFoto() {
        return rutaFoto;
    }

    /**
     * Modifica la ruta de la foto
     * 
     * @param foto nueva foto
     */
    public void setRutaFoto(String foto) {
        this.rutaFoto = foto;
    }
    
    
    
    
    
}
