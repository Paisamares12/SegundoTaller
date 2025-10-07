package udistrital.avanzada.taller.modelo.persistencia;

import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

import java.io.*;
import java.util.*;

/**
 * Maneja la carga y guardado de datos desde un archivo .properties.
 * Lee equipos y jugadores con sus atributos (nombre, apodo, foto).
 *
 * @author Juan Sebastián Bravo Rojas
 * @version 6.0 - 06/10/2025
 */
public class ControlPersistencia {

    /**
     * Carga los equipos y jugadores desde un archivo .properties.
     * Cada equipo debe tener al menos un jugador válido.
     *
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

        // Revisión más flexible: detecta automáticamente cuántos equipos hay
        Set<String> keys = props.stringPropertyNames();
        Set<Integer> indicesEquipos = new TreeSet<>();

        for (String key : keys) {
            if (key.startsWith("equipo")) {
                try {
                    String numero = key.substring(6, key.indexOf('.', 6));
                    indicesEquipos.add(Integer.parseInt(numero));
                } catch (Exception ignored) {}
            }
        }

        if (indicesEquipos.isEmpty()) {
            throw new IllegalStateException("No se encontraron equipos válidos en el archivo.");
        }

        // Iterar sobre cada índice encontrado
        for (Integer indice : indicesEquipos) {
            String prefix = "equipo" + indice;
            String nombreEquipo = props.getProperty(prefix + ".nombre");

            if (nombreEquipo == null || nombreEquipo.isBlank()) {
                continue; // Si el equipo no tiene nombre, se salta
            }

            ArrayList<Jugador> jugadores = new ArrayList<>();

            // Buscar jugadores asociados a ese equipo sin asumir límite fijo
            for (int j = 1; ; j++) {
                String base = prefix + ".jugador" + j;
                String nombreJugador = props.getProperty(base + ".nombre");
                String apodo = props.getProperty(base + ".apodo");
                String foto = props.getProperty(base + ".foto");

                // Si ya no hay jugador j, se rompe el ciclo
                if (nombreJugador == null && apodo == null && foto == null) {
                    break;
                }

                if (nombreJugador != null && apodo != null && foto != null) {
                    jugadores.add(new Jugador(nombreJugador.trim(), apodo.trim(), foto.trim()));
                }
            }

            if (!jugadores.isEmpty()) {
                equipos.add(new Equipo(nombreEquipo.trim(), jugadores));
            }
        }

        if (equipos.isEmpty()) {
            throw new IllegalStateException("No se encontraron equipos con jugadores válidos.");
        }

        return equipos;
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

