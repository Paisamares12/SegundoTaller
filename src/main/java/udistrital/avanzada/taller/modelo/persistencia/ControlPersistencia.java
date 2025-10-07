package udistrital.avanzada.taller.modelo.persistencia;

import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

import java.io.*;
import java.util.*;

/**
 * Maneja la carga y guardado de datos desde un archivo .properties.
 * Lee equipos, jugadores disponibles con sus atributos (nombre, apodo, foto).
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 7.0 
 * 06/10/2025
 */
public class ControlPersistencia {

    private List<Jugador> jugadoresDisponibles;
    
    /**
     * Constructor
     */
    public ControlPersistencia() {
        this.jugadoresDisponibles = new ArrayList<>();
    }

    /**
     * Carga los equipos y jugadores desde un archivo .properties.
     * Cada equipo debe tener al menos un jugador válido.
     *
     * @param archivo archivo .properties seleccionado por el usuario
     * @return lista de equipos cargados
     */
    public List<Equipo> cargarEquiposDesdeArchivo(File archivo) {
        List<Equipo> equipos = new ArrayList<>();
        jugadoresDisponibles.clear();

        if (archivo == null || !archivo.exists()) {
            throw new IllegalArgumentException("El archivo no existe o es nulo.");
        }

        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(archivo)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + e.getMessage());
        }

        Set<String> keys = props.stringPropertyNames();
        
        // Cargar equipos predefinidos
        Set<Integer> indicesEquipos = new TreeSet<>();
        for (String key : keys) {
            if (key.startsWith("equipo") && !key.contains("disponible")) {
                try {
                    String numero = key.substring(6, key.indexOf('.', 6));
                    indicesEquipos.add(Integer.parseInt(numero));
                } catch (Exception ignored) {}
            }
        }

        if (indicesEquipos.isEmpty()) {
            throw new IllegalStateException("No se encontraron equipos válidos en el archivo.");
        }

        // Cargar cada equipo
        for (Integer indice : indicesEquipos) {
            String prefix = "equipo" + indice;
            String nombreEquipo = props.getProperty(prefix + ".nombre");

            if (nombreEquipo == null || nombreEquipo.isBlank()) {
                continue;
            }

            ArrayList<Jugador> jugadores = new ArrayList<>();

            // Buscar jugadores asociados a ese equipo
            for (int j = 1; j <= 4; j++) {
                String base = prefix + ".jugador" + j;
                String nombreJugador = props.getProperty(base + ".nombre");
                String apodo = props.getProperty(base + ".apodo");
                String foto = props.getProperty(base + ".foto");

                if (nombreJugador != null && apodo != null && foto != null) {
                    jugadores.add(new Jugador(nombreJugador.trim(), foto.trim(), apodo.trim()));
                }
            }

            if (!jugadores.isEmpty()) {
                // Crear equipo con lista vacía
                Equipo equipo = new Equipo(nombreEquipo.trim(), null);
                // Ahora sí agregar los jugadores uno por uno
                for (Jugador j : jugadores) {
                    equipo.agregarJugador(j);
                }
                equipos.add(equipo);
            }
        }

        // Cargar jugadores disponibles (no asignados a equipos)
        cargarJugadoresDisponibles(props);

        if (equipos.isEmpty()) {
            throw new IllegalStateException("No se encontraron equipos con jugadores válidos.");
        }

        return equipos;
    }
    
    /**
     * Carga los jugadores disponibles desde el archivo properties
     * @param props propiedades cargadas
     */
    private void cargarJugadoresDisponibles(Properties props) {
        Set<String> keys = props.stringPropertyNames();
        Set<Integer> indicesDisponibles = new TreeSet<>();
        
        // Identificar jugadores disponibles
        for (String key : keys) {
            if (key.startsWith("jugador.disponible")) {
                try {
                    String numero = key.substring(18, key.indexOf('.', 18));
                    indicesDisponibles.add(Integer.parseInt(numero));
                } catch (Exception ignored) {}
            }
        }
        
        // Cargar cada jugador disponible
        for (Integer indice : indicesDisponibles) {
            String base = "jugador.disponible" + indice;
            String nombre = props.getProperty(base + ".nombre");
            String apodo = props.getProperty(base + ".apodo");
            String foto = props.getProperty(base + ".foto");
            
            if (nombre != null && apodo != null && foto != null) {
                jugadoresDisponibles.add(new Jugador(nombre.trim(), foto.trim(), apodo.trim()));
            }
        }
    }
    
    /**
     * Obtiene la lista de jugadores disponibles cargados del archivo
     * @return lista de jugadores disponibles
     */
    public List<Jugador> getJugadoresDisponibles() {
        return new ArrayList<>(jugadoresDisponibles);
    }

    /**
     * Guarda los resultados actuales de los equipos (puntajes y jugadores)
     * en un archivo .properties.
     *
     * @param archivo destino del guardado
     * @param equipos lista de equipos con sus puntajes
     */
    public void guardarResultados(File archivo, List<Equipo> equipos) {
        if (archivo == null) {
            throw new IllegalArgumentException("El archivo destino no puede ser nulo.");
        }

        Properties props = new Properties();

        for (int i = 0; i < equipos.size(); i++) {
            Equipo eq = equipos.get(i);
            String prefix = "equipo" + (i + 1);
            props.setProperty(prefix + ".nombre", eq.getNombre());
            props.setProperty(prefix + ".puntaje", String.valueOf(eq.getPuntaje()));

            List<Jugador> jugadores = eq.getJugadores();
            for (int j = 0; j < jugadores.size(); j++) {
                Jugador jugador = jugadores.get(j);
                String base = prefix + ".jugador" + (j + 1);
                props.setProperty(base + ".nombre", jugador.getNombre() != null ? jugador.getNombre() : "Sin nombre");
                props.setProperty(base + ".apodo", jugador.getApodo() != null ? jugador.getApodo() : "Sin apodo");
                props.setProperty(base + ".foto", jugador.getRutaFoto() != null ? jugador.getRutaFoto() : "sin_foto.jpg");
            }
        }

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            props.store(fos, "Resultados del juego - Argolla Llanera");
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }
}