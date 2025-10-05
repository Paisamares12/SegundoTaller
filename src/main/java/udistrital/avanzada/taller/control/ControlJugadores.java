/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
//Importar clases desde otros paquetes
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Creación de métodos para crear jugadores y cargarlos desde el properties
 *
 * @author Juan Ariza
 * @version 2.0 03/10/2025
 *
 * Creación métodos para pasar los jugadores y los equipos a la vista
 *
 * @author Paula Martínez
 * @version 3.0 05/10/2025
 */
public class ControlJugadores {

    private ArrayList<Jugador> jugadores;
    private static final String RUTA_PROPERTIES = "equipos.properties";
    private static final int TAMANO_EQUIPO = 4;

    /**
     * Constructor de la clase ControlJugadores. Inicializa la lista de
     * jugadores vacía.
     */
    public ControlJugadores() {
        this.jugadores = new ArrayList<>();
    }

    /**
     * Crea y registra un nuevo jugador en el sistema.
     *
     * @param nombre Nombre completo del jugador
     * @param apodo Apodo del jugador
     * @param foto Ruta de la foto del jugador
     * @return true si el jugador se creó correctamente, false en caso contrario
     * @throws IllegalArgumentException si algún parámetro es inválido
     */
    public boolean crearJugador(String nombre, String apodo, String foto) {
        if (!validarDatosJugador(nombre, apodo)) {
            throw new IllegalArgumentException("Datos del jugador inválidos");
        }

        if (existeApodo(apodo)) {
            throw new IllegalArgumentException("Ya existe un jugador con el apodo: " + apodo);
        }

        Jugador nuevoJugador = new Jugador(apodo, foto, nombre);
        return jugadores.add(nuevoJugador);
    }

    /**
     * Valida los datos básicos de un jugador.
     *
     * @param nombre Nombre del jugador
     * @param apodo Apodo del jugador
     * @return true si los datos son válidos, false en caso contrario
     */
    private boolean validarDatosJugador(String nombre, String apodo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        if (apodo == null || apodo.trim().isEmpty()) {
            return false;
        }

        if (nombre.trim().length() < 2 || apodo.trim().length() < 2) {
            return false;
        }

        if (nombre.length() > 25 || apodo.length() > 25) {
            return false;
        }

        return true;
    }

    /**
     * Verifica si ya existe un jugador con el apodo dado.
     *
     * @param apodo Apodo a verificar
     * @return true si el apodo ya existe, false en caso contrario
     */
    private boolean existeApodo(String apodo) {
        for (Jugador jugador : jugadores) {
            if (jugador.getApodo().equalsIgnoreCase(apodo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene la cantidad de jugadores registrados.
     *
     * @return Número de jugadores
     */
    public int obtenerCantidadJugadores() {
        return jugadores.size();
    }

    /**
     * Obtiene la lista de todos los jugadores registrados.
     *
     * @return Lista de jugadores
     */
    public List<Jugador> obtenerJugadores() {
        return new ArrayList<>(jugadores);
    }

    /**
     * Carga jugadores desde un archivo properties. El archivo debe tener el
     * formato: equipoN.jugadorM.nombre=NombreCompleto
     * equipoN.jugadorM.apodo=Apodo equipoN.jugadorM.foto=ruta/foto.jpg
     * (opcional)
     *
     * @param rutaArchivo Ruta del archivo properties
     * @return Número de jugadores cargados exitosamente
     */
    public int cargarJugadoresDesdeProperties(String rutaArchivo) {
        Properties props = new Properties();
        int jugadoresCargados = 0;

        try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
            props.load(fis);

            int numEquipo = 1;
            while (true) {
                String nombreEquipo = props.getProperty("equipo" + numEquipo + ".nombre");

                if (nombreEquipo == null) {
                    break;
                }

                int numJugador = 1;

                while (numJugador <= TAMANO_EQUIPO) {
                    String prefijo = "equipo" + numEquipo + ".jugador" + numJugador;
                    String nombre = props.getProperty(prefijo + ".nombre");
                    String apodo = props.getProperty(prefijo + ".apodo");
                    String foto = props.getProperty(prefijo + ".foto", "default.jpg");

                    if (nombre == null || apodo == null) {
                        break;
                    }

                    try {
                        if (!existeApodo(apodo)) {
                            if (crearJugador(nombre, apodo, foto)) {
                                jugadoresCargados++;
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        // Error silencioso - la vista manejará el error
                    }

                    numJugador++;
                }

                numEquipo++;
            }

        } catch (IOException e) {
            return -1; // Indica error de lectura del archivo
        }

        return jugadoresCargados;
    }

    /**
     * Carga jugadores usando la ruta por defecto.
     *
     * @return Número de jugadores cargados exitosamente, -1 si hay error
     */
    public int cargarJugadoresDesdeProperties() {
        return cargarJugadoresDesdeProperties(RUTA_PROPERTIES);
    }

    /**
     * Limpia todos los jugadores del sistema.
     */
    public void limpiarJugadores() {
        jugadores.clear();
    }

    /**
     * Carga un solo equipo a partir de las propiedades usando el prefijo dado
     * (ejemplo: "equipo1").
     *
     * @param props Objeto Properties ya cargado.
     * @param prefix Prefijo del equipo ("equipo1", "equipo2", ...).
     * @return Equipo construido (puede tener 0 o más jugadores).
     */
    public static Equipo cargarEquipo(Properties props, String prefix) {
        String nombreEquipo = props.getProperty(prefix + ".nombre");
        Equipo equipo = new Equipo(nombreEquipo != null ? nombreEquipo : "SinNombre");

        // Detecta dinámicamente jugadores: jugador1, jugador2, ... hasta que faltan
        int i = 1;
        while (true) {
            String nombre = props.getProperty(prefix + ".jugador" + i + ".nombre");
            if (nombre == null) {
                break; // ya no hay más jugadores numerados
            }
            String apodo = props.getProperty(prefix + ".jugador" + i + ".apodo");
            String foto = props.getProperty(prefix + ".jugador" + i + ".foto");
            Jugador j = new Jugador(nombre, apodo, foto);
            equipo.agregarJugador(j);
            i++;
        }
        return equipo;
    }

    /**
     * Dado un Properties, detecta todos los prefijos de equipos (ej. equipo1,
     * equipo2) y devuelve la lista de equipos construidos.
     *
     * @param props Properties ya cargado con todas las claves.
     * @return Lista de equipos (ordenada por prefijo: equipo1, equipo2, ...).
     */
    public static List<Equipo> cargarEquipos(Properties props) {
        Set<String> prefixes = new HashSet<>();
        for (String key : props.stringPropertyNames()) {
            if (!key.startsWith("equipo")) {
                continue;
            }
            String prefix = key.split("\\.")[0]; // "equipo1" de "equipo1.jugador1.nombre"
            prefixes.add(prefix);
        }

        List<String> orden = new ArrayList<>(prefixes);
        Collections.sort(orden); // opcional: para mantener orden numérico

        List<Equipo> equipos = new ArrayList<>();
        for (String p : orden) {
            equipos.add(cargarEquipo(props, p));
        }
        return equipos;
    }

    /**
     * Conveniencia: carga el archivo .properties desde la ruta y devuelve la
     * lista de equipos.
     *
     * @param rutaProperties Ruta al archivo .properties (ej:
     * "Specs/data/equipos.properties")
     * @return Lista de equipos cargados
     * @throws IOException Si no existe el archivo o hay error de lectura
     */
    public static List<Equipo> cargarEquiposDesdeArchivo(String rutaProperties) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(rutaProperties)) {
            props.load(fis);
        }
        return cargarEquipos(props);
    }
}
