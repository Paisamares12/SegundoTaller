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
 * @version 7.0
 * 06/10/2025
 */
public class Jugador extends Persona {
    
    private String apodo;
    private String rutaFoto;

    /**
     * Construye un nuevo jugador con los datos especificados.
     * 
     * @param nombre nombre real del jugador
     * @param rutaFoto ruta de la foto del jugador
     * @param apodo apodo del jugador
     */
    public Jugador(String nombre, String rutaFoto, String apodo) {
        super(nombre == null || nombre.isBlank() ? "Sin nombre" : nombre);
        this.apodo = (apodo != null && !apodo.isBlank()) ? apodo : "Sin apodo";
        this.rutaFoto = (rutaFoto != null && !rutaFoto.isBlank()) ? rutaFoto : "sin_foto.jpg";
    }
    
    /**
     * Obtiene el apodo del jugador.
     * 
     * @return apodo del jugador
     */
    public String getApodo() {
        return apodo;
    }
    
    /**
     * Modifica el apodo del jugador.
     * 
     * @param apodo nuevo apodo
     */
    public void setApodo(String apodo) {
        if (apodo != null && !apodo.isBlank()) {
            this.apodo = apodo;
        }
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
     * @param rutaFoto nueva ruta de foto
     */
    public void setRutaFoto(String rutaFoto) {
        if (rutaFoto != null && !rutaFoto.isBlank()) {
            this.rutaFoto = rutaFoto;
        }
    }
    
    @Override
    public String toString() {
        return getNombre() + " (" + apodo + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Jugador jugador = (Jugador) obj;
        return apodo.equals(jugador.apodo);
    }
    
    @Override
    public int hashCode() {
        return apodo.hashCode();
    }
}