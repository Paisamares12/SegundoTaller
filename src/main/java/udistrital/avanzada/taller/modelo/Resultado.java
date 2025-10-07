/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo;

/**
 * Clase que representa el resultado final de una partida. Se utiliza para
 * persistir la información de equipos y jugadores en un archivo de acceso
 * aleatorio.
 *
 * @author Paula Martínez
 * @version 7.0 30/09/2025
 */
public class Resultado {

    //Atributos a persistir 
    private int clave;
    private String nombreEquipo;
    private String jugador1;
    private String jugador2;
    private String jugador3;
    private String jugador4;
    private String resultado; //Gano o perdio

    /**
     * Constructor de la clase Resultado
     *
     * @param clave
     * @param nombreEquipo
     * @param jugador1
     * @param jugador2
     * @param jugador3
     * @param jugador4
     * @param resultado
     */
    public Resultado(int clave, String nombreEquipo, String jugador1, String jugador2, String jugador3, String jugador4, String resultado) {
        this.clave = clave;
        this.nombreEquipo = nombreEquipo;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.jugador3 = jugador3;
        this.jugador4 = jugador4;
        this.resultado = resultado;
    }

    /**
     * Obtiene la clave
     *
     * @return Clave
     */
    public int getClave() {
        return clave;
    }

    /**
     * Obtiene el nombre del equipo asociado al resultado
     *
     * @return Nombre del equipo
     */
    public String getNombreEquipo() {
        return nombreEquipo;
    }

    /**
     * Obtiene el nombre del primer Jugador
     *
     * @return Nombre Primer Jugador
     */
    public String getJugador1() {
        return jugador1;
    }

    /**
     * Obtiene el nombre del segundo Jugador
     *
     * @return Nombre segundo Jugador
     */
    public String getJugador2() {
        return jugador2;
    }

    /**
     * Obtiene el nombre del tercer Jugador
     *
     * @return Nombre tercer Jugador
     */
    public String getJugador3() {
        return jugador3;
    }

    /**
     * Obtiene el nombre del cuarto Jugador
     *
     * @return Nombre Cuarto Jugador
     */
    public String getJugador4() {
        return jugador4;
    }

    /**
     * Obtiene si gano o perdio
     *
     * @return Resultado equipo
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * Devuelve una representación en forma de cadena (String) del objeto
     * {@code Registro}.
     *
     * <p>
     * Este método sobrescribe el método {@link Object#toString()} y construye
     * un texto legible que contiene todos los campos principales de un
     * registro: la clave, el nombre del equipo, los nombres de los jugadores y
     * el resultado.</p>
     *
     * <p>
     * La salida se genera en una sola línea concatenando los valores de los
     * atributos con etiquetas descriptivas.</p>
     *
     * @return una cadena con el siguiente formato:      <pre>
 *         Registro No: [clave]
     *         Equipo: [nombreEquipo]
     *         Jug1: [jugador1]
     *         Jug2: [jugador2]
     *         Jug3: [jugador3]
     *         Jug4: [jugador4]
     *         Resultado: [resultado]
     * </pre>
     */
    @Override
    public String toString() {
        return "Registro No: " + clave
                + " Equipo: " + nombreEquipo
                + " Jug1: " + jugador1
                + " Jug2: " + jugador2
                + " Jug3: " + jugador3
                + " Jug4: " + jugador4
                + " Resultado: " + resultado;
    }

}
