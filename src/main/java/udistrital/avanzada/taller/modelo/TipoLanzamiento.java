/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package udistrital.avanzada.taller.modelo;

/**
 * Enumeración que representa los diferentes tipos de lanzamiento
 * en el juego de Argolla Llanera y sus respectivos puntajes.
 *
 * @author Juan Ariza
 * @version 5.0
 * 06/10/2025
 */
public enum TipoLanzamiento {
    MONONA("Moñona", 8),
    ENGARZADA("Engarzada", 5),
    HUECO("Hueco", 3),
    PALMO("Palmo", 2),
    TIMBRE("Timbre", 1),
    OTRO("Otro", 0);
    
    private final String nombre;
    private final int puntos;
    
    /**
     * Constructor del enum
     * @param nombre nombre descriptivo del lanzamiento
     * @param puntos puntos que otorga este tipo de lanzamiento
     */
    TipoLanzamiento(String nombre, int puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }
    
    /**
     * Obtiene el nombre del tipo de lanzamiento
     * @return nombre del lanzamiento
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Obtiene los puntos del tipo de lanzamiento
     * @return puntos del lanzamiento
     */
    public int getPuntos() {
        return puntos;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + puntos + " puntos)";
    }
}