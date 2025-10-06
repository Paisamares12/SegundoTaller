package udistrital.avanzada.taller.modelo.persistencia;

import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

import java.io.*;
import java.util.*;

/**
 * Maneja la carga de datos desde un archivo .properties.
 * Lee equipos y jugadores con sus atributos (nombre, apodo, foto).
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 4.0 - 06/10/2025
 */
public class ControlPersistencia {

    /**
     * Carga los equipos y jugadores desde un archivo .properties.
     * @param archivo archivo .properties seleccionado por el usuario
     * @return lista de equipos cargados
     */
    public List<Equipo> cargarEquiposDesdeArchivo(File archivo) {
        List<Equipo> equipos = new ArrayList<>();

        if (archivo == null || !archivo.exists()) {
            throw new IllegalArgumentException("El archivo no existe o es nulo.");
        }

        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(archivo)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + e.getMessage());
        }

        // Iterar sobre los equipos: equipo1, equipo2, etc.
        int contadorEquipo = 1;
        while (props.containsKey("equipo" + contadorEquipo + ".nombre")) {
            String nombreEquipo = props.getProperty("equipo" + contadorEquipo + ".nombre").trim();

            // Crear lista de jugadores para este equipo
            ArrayList<Jugador> jugadores = new ArrayList<>();

            // Leer hasta 4 jugadores (por formato del archivo)
            for (int i = 1; i <= 4; i++) {
                String base = "equipo" + contadorEquipo + ".jugador" + i;
                String nombreJugador = props.getProperty(base + ".nombre");
                String apodo = props.getProperty(base + ".apodo");
                String foto = props.getProperty(base + ".foto");

                if (nombreJugador != null && apodo != null && foto != null) {
                    jugadores.add(new Jugador(nombreJugador.trim(), apodo.trim(), foto.trim()));
                }
            }

            // Crear el equipo con su lista de jugadores
            Equipo equipo = new Equipo(nombreEquipo, jugadores);
            equipos.add(equipo);
            contadorEquipo++;
        }

        if (equipos.isEmpty()) {
            throw new IllegalStateException("No se encontraron equipos válidos en el archivo.");
        }

        return equipos;
    }

    /**
     * Guarda los resultados actuales de los equipos (opcional).
     * @param archivo destino del guardado
     * @param equipos lista de equipos con sus puntajes
     */
    public void guardarResultados(File archivo, List<Equipo> equipos) {
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
                props.setProperty(base + ".nombre", jugador.getNombre());
                props.setProperty(base + ".apodo", jugador.getApodo());
                props.setProperty(base + ".foto", jugador.getRutaFoto());
            }
        }

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            props.store(fos, "Resultados del juego - Argolla Llanera");
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }
}

