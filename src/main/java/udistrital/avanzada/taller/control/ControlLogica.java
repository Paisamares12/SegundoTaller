/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;
//Importar las clases necesarias traidas desde otros paquetes
import java.io.File;
import udistrital.avanzada.taller.modelo.Conexion;
import udistrital.avanzada.taller.modelo.Equipo;
//Importar Librerias importantes
import java.util.List;
import java.io.IOException;
import udistrital.avanzada.taller.modelo.persistencia.ControlPersistencia;

/**
 *
 * Originalmente creada por Paula Martínez
 * Modificada por Juan Sebastián Bravo Rojas
 * 
 * 
 * @author Paula Martínez
 * @version 4.0 06/10/2025 
 * La clase ControlLogica.java ha sido creada con el fin
 * de manejar toda la lógica del aplicativo, conectar modelo con control
 */
public class ControlLogica {

    //Creamos como atributos privados las clases necesarias
    private ControlInterfaz cInterfaz;
    private ControlPartida cPartida;
    private ControlPersistencia cPersistencia;
    //Listas
    private List<Equipo> equipos;

    /**
     * Constructor clase ControlLogica el cual se usa apenas se lanza el
     * launcher
     *
     */
    public ControlLogica() {
        //Inyectamos ControlLogica (mandamos su copia) a ControlInterfaz
        this.cInterfaz= new ControlInterfaz(this);
        this.cPersistencia = new ControlPersistencia();
        this.cPartida = new ControlPartida();
    }
    
    /**
     * Carga los equipos desde un archivo .properties
     * @param archivo archivo seleccionado por el usuario
     * @return lista de equipos cargados
     */
    public List<Equipo> cargarEquipos(File archivo) {
        this.equipos = cPersistencia.cargarEquiposDesdeArchivo(archivo);
        cPartida.setEquipos(equipos);
        return equipos;
    }
    
    /**
     * Devuelve el controlador de partida (usado por ControlInterfaz)
     */
    public ControlPartida getControlPartida() {
        return cPartida;
    }

    /**
     * Devuelve la lista de equipos cargados.
     */
    public List<Equipo> getEquipos() {
        return equipos;
    }

    /**
     * Reinicia la partida para una nueva ronda.
     */
    public void reiniciarPartida() {
        cPartida.reiniciar();
    }
    
    /**
     * Realiza un lanzamiento de argolla del equipo en turno.
     * 
     * @return mensaje descriptivo del resultado
     */
    public String lanzarArgolla() {
        return cPartida.lanzarArgolla();
    }
    
    /**
     * Retorna si la partida sigue activa.
     * 
     * @return true si la partida continúa
     */
    public boolean partidaActiva() {
        return cPartida.isPartidaActiva();
    }

    /**
     * Devuelve el nombre del equipo ganador, si ya hay uno.
     * 
     * @return nombre del equipo ganador o null
     */
    public String getGanador() {
        var ganador = cPartida.getGanador();
        return (ganador != null) ? ganador.getNombre() : null;
    }

    /**
     * Devuelve el equipo que tiene el turno actual.
     * 
     * @return equipo en turno
     */
    public Equipo getEquipoEnTurno() {
        return cPartida.getEquipoEnTurno();
    }
    

}
