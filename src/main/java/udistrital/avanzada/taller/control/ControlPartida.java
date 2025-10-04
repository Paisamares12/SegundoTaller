/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Creación de métodos para la creación de equipos y carga
 * 
 * @author Juan Ariza
 * @version 2.0 03/10/2025
 */
public class ControlPartida {
    private Map<String, List<Jugador>> equipos;
    private ControlJugadores controlJugadores;
    private static final int TAMANO_EQUIPO = 4;
    private static final String RUTA_PROPERTIES = "equipos.properties";
    
    /**
     * Constructor de la clase ControlPartida.
     * 
     * @param controlJugadores Referencia al control de jugadores
     */
    public ControlPartida(ControlJugadores controlJugadores) {
        this.equipos = new HashMap<>();
        this.controlJugadores = controlJugadores;
    }
    
    /**
     * Carga equipos desde un archivo properties utilizando los jugadores
     * ya cargados en ControlJugadores.
     * 
     * El archivo debe tener el formato:
     * equipoN.nombre=NombreEquipo
     * equipoN.jugadorM.apodo=Apodo (debe existir en ControlJugadores)
     * 
     * @param rutaArchivo Ruta del archivo properties
     * @return Número de equipos cargados exitosamente, -1 si hay error de lectura
     */
    public int cargarEquiposDesdeProperties(String rutaArchivo) {
        Properties props = new Properties();
        int equiposCargados = 0;
        
        try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
            props.load(fis);
            
            int numEquipo = 1;
            while (true) {
                String nombreEquipo = props.getProperty("equipo" + numEquipo + ".nombre");
                
                if (nombreEquipo == null) {
                    break;
                }
                
                List<Jugador> jugadoresEquipo = new ArrayList<>();
                int numJugador = 1;
                
                while (numJugador <= TAMANO_EQUIPO) {
                    String prefijo = "equipo" + numEquipo + ".jugador" + numJugador;
                    String apodo = props.getProperty(prefijo + ".apodo");
                    
                    if (apodo == null) {
                        break;
                    }
                    
                    Jugador jugador = buscarJugadorEnLista(apodo);
                    if (jugador != null) {
                        jugadoresEquipo.add(jugador);
                    }
                    
                    numJugador++;
                }
                
                if (!jugadoresEquipo.isEmpty()) {
                    try {
                        if (crearEquipo(nombreEquipo, jugadoresEquipo)) {
                            equiposCargados++;
                        }
                    } catch (IllegalArgumentException e) {
                    }
                }
                
                numEquipo++;
            }
            
        } catch (IOException e) {
            return -1; // Indica error de lectura del archivo
        }
        
        return equiposCargados;
    }
    
    /**
     * Carga equipos usando la ruta por defecto.
     * 
     * @return Número de equipos cargados exitosamente, -1 si hay error
     */
    public int cargarEquiposDesdeProperties() {
        return cargarEquiposDesdeProperties(RUTA_PROPERTIES);
    }
    
    /**
     * Busca un jugador en la lista de jugadores del ControlJugadores por su apodo.
     * 
     * @param apodo Apodo del jugador a buscar
     * @return Jugador encontrado o null si no existe
     */
    private Jugador buscarJugadorEnLista(String apodo) {
        List<Jugador> todosLosJugadores = controlJugadores.obtenerJugadores();
        for (Jugador jugador : todosLosJugadores) {
            if (jugador.getApodo().equalsIgnoreCase(apodo)) {
                return jugador;
            }
        }
        return null;
    }
    
    /**
     * Crea un nuevo equipo con jugadores nuevos que se crean en el momento.
     * 
     * @param nombreEquipo Nombre del equipo
     * @param datosJugadores Lista de arreglos con datos de jugadores [nombre, apodo, foto]
     * @return true si el equipo se creó correctamente
     * @throws IllegalArgumentException si el equipo ya existe o datos inválidos
     */
    public boolean crearEquipoConJugadoresNuevos(String nombreEquipo, List<String[]> datosJugadores) {
        if (nombreEquipo == null || nombreEquipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío");
        }
        
        if (equipos.containsKey(nombreEquipo)) {
            throw new IllegalArgumentException("Ya existe un equipo con el nombre: " + nombreEquipo);
        }
        
        if (datosJugadores == null || datosJugadores.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar datos de al menos un jugador");
        }
        
        List<Jugador> jugadoresEquipo = new ArrayList<>();
        
        for (String[] datos : datosJugadores) {
            if (datos.length < 2) {
                throw new IllegalArgumentException("Datos incompletos para un jugador");
            }
            
            String nombre = datos[0];
            String apodo = datos[1];
            String foto = datos.length > 2 ? datos[2] : "default.jpg";
            
            try {
                if (controlJugadores.crearJugador(nombre, apodo, foto)) {
                    Jugador jugador = buscarJugadorEnLista(apodo);
                    if (jugador != null) {
                        jugadoresEquipo.add(jugador);
                    }
                }
            } catch (IllegalArgumentException e) {
                Jugador jugadorExistente = buscarJugadorEnLista(apodo);
                if (jugadorExistente != null) {
                    jugadoresEquipo.add(jugadorExistente);
                } else {
                    throw new IllegalArgumentException("Error al crear/agregar jugador '" + apodo + "': " + e.getMessage());
                }
            }
        }
        
        if (jugadoresEquipo.isEmpty()) {
            throw new IllegalArgumentException("No se pudo crear ningún jugador para el equipo");
        }
        
        equipos.put(nombreEquipo, jugadoresEquipo);
        return true;
    }
    
    /**
     * Crea un nuevo equipo con jugadores ya existentes en el sistema.
     * 
     * @param nombreEquipo Nombre del equipo
     * @param jugadoresEquipo Lista de jugadores del equipo
     * @return true si el equipo se creó correctamente
     * @throws IllegalArgumentException si el equipo ya existe o datos inválidos
     */
    public boolean crearEquipo(String nombreEquipo, List<Jugador> jugadoresEquipo) {
        if (nombreEquipo == null || nombreEquipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío");
        }
        
        if (equipos.containsKey(nombreEquipo)) {
            throw new IllegalArgumentException("Ya existe un equipo con el nombre: " + nombreEquipo);
        }
        
        if (jugadoresEquipo == null || jugadoresEquipo.isEmpty()) {
            throw new IllegalArgumentException("El equipo debe tener al menos cuatro jugadores");
        }
        
        equipos.put(nombreEquipo, new ArrayList<>(jugadoresEquipo));
        return true;
    }
    
    /**
     * Obtiene los jugadores de un equipo específico.
     * 
     * @param nombreEquipo Nombre del equipo
     * @return Lista de jugadores del equipo o null si no existe
     */
    public List<Jugador> obtenerJugadoresEquipo(String nombreEquipo) {
        return equipos.get(nombreEquipo);
    }
    
    /**
     * Obtiene todos los nombres de equipos registrados.
     * 
     * @return Lista con los nombres de los equipos
     */
    public List<String> obtenerNombresEquipos() {
        return new ArrayList<>(equipos.keySet());
    }
    
    /**
     * Obtiene la cantidad de equipos registrados.
     * 
     * @return Número de equipos
     */
    public int obtenerCantidadEquipos() {
        return equipos.size();
    }
    
    /**
     * Verifica si un equipo está completo (tiene 4 jugadores).
     * 
     * @param nombreEquipo Nombre del equipo
     * @return true si el equipo tiene 4 jugadores, false en caso contrario
     */
    public boolean equipoCompleto(String nombreEquipo) {
        List<Jugador> jugadoresEquipo = equipos.get(nombreEquipo);
        return jugadoresEquipo != null && jugadoresEquipo.size() == TAMANO_EQUIPO;
    }
    
    /**
     * Verifica si existe un equipo con el nombre dado.
     * 
     * @param nombreEquipo Nombre del equipo
     * @return true si el equipo existe, false en caso contrario
     */
    public boolean existeEquipo(String nombreEquipo) {
        return equipos.containsKey(nombreEquipo);
    }
    
    /**
     * Obtiene el mapa completo de equipos.
     * 
     * @return Mapa de equipos (nombre -> lista de jugadores)
     */
    public Map<String, List<Jugador>> obtenerEquipos() {
        return new HashMap<>(equipos);
    }
    
    /**
     * Limpia todos los equipos del sistema.
     */
    public void limpiarEquipos() {
        equipos.clear();
    }
}
