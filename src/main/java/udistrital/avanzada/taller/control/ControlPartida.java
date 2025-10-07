package udistrital.avanzada.taller.control;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.TipoLanzamiento;

/**
 * Gestiona la lógica de la partida, turnos, lanzamientos y desempates.
 *
 * Originalmente creada por Juan Estevan Ariza
 * Modificada por Juan Sebastián Bravo Rojas
 * Completada según observaciones del Taller 2
 *
 * @author Juan Ariza
 * @version 6.0 06/10/2025
 */
public class ControlPartida {

    private List<Equipo> equipos;
    private boolean partidaActiva;
    private int puntajeObjetivo;
    private int turnoActual;
    private int rondaActual;
    private int maximoRondas;
    private TipoLanzamiento ultimoLanzamiento;
    private final Random random;
    private boolean muerteSubitaActiva;

    public ControlPartida() {
        this.partidaActiva = false;
        this.puntajeObjetivo = 21;
        this.turnoActual = 0;
        this.rondaActual = 1;
        this.maximoRondas = 2;
        this.random = new Random();
        this.muerteSubitaActiva = false;
    }

    /**
     * Asigna los equipos que participarán en la partida.
     */
    public void setEquipos(List<Equipo> equipos) {
        if (equipos == null || equipos.size() < 2) {
            throw new IllegalArgumentException("Debe haber al menos dos equipos para iniciar la partida.");
        }
        this.equipos = new ArrayList<>(equipos);
        this.partidaActiva = true;
        this.turnoActual = 0;
        this.rondaActual = 1;
        this.muerteSubitaActiva = false;
        reiniciarPuntajes();
    }

    /**
     * Simula el lanzamiento de argolla del equipo en turno.
     * Si hay más de dos equipos, se sigue un orden circular.
     */
    public String lanzarArgolla() {
        if (!partidaActiva || equipos == null) {
            throw new IllegalStateException("No hay una partida activa.");
        }

        Equipo equipo = equipos.get(turnoActual);
        TipoLanzamiento[] tipos = TipoLanzamiento.values();
        ultimoLanzamiento = tipos[random.nextInt(tipos.length)];
        int puntos = ultimoLanzamiento.getPuntos();
        equipo.sumarPuntos(puntos);

        String resultado = "╔═══════════════════════════════════════╗\n"
                + "  EQUIPO: " + equipo.getNombre() + "\n"
                + "  LANZAMIENTO: " + ultimoLanzamiento.getNombre() + "\n"
                + "  PUNTOS OBTENIDOS: " + puntos + "\n"
                + "  PUNTAJE TOTAL: " + equipo.getPuntaje() + " / " + puntajeObjetivo + "\n"
                + "╚═══════════════════════════════════════╝";

        // Si un equipo alcanza el puntaje objetivo
        if (equipo.getPuntaje() >= puntajeObjetivo) {
            partidaActiva = false;
            resultado += "\n\n*** ¡" + equipo.getNombre() + " ha GANADO la ronda " + rondaActual + "! ***";

            // Si hay empate al final de la última ronda, activar muerte súbita
            if (rondaActual == maximoRondas && hayEmpate()) {
                activarMuerteSubita();
                resultado += "\n\n⚔ Muerte súbita activada entre equipos empatados ⚔";
            }
        } else {
            pasarTurno();
        }

        return resultado;
    }

    /**
     * Cambia el turno al siguiente equipo.
     */
    private void pasarTurno() {
        if (equipos != null && !equipos.isEmpty()) {
            turnoActual = (turnoActual + 1) % equipos.size();
        }
    }

    /**
     * Reinicia los puntajes de todos los equipos.
     */
    public void reiniciarPuntajes() {
        if (equipos != null) {
            for (Equipo e : equipos) {
                e.reiniciarPuntaje();
            }
        }
    }

    /**
     * Reinicia completamente la partida para una nueva ronda.
     */
    public void reiniciar() {
        reiniciarPuntajes();
        this.partidaActiva = true;
        this.turnoActual = 0;
        this.muerteSubitaActiva = false;
    }

    /**
     * Avanza a la siguiente ronda, si no hay empate.
     */
    public boolean avanzarRonda() {
        if (rondaActual < maximoRondas) {
            rondaActual++;
            reiniciar();
            return true;
        }
        return false;
    }

    /**
     * Determina si hay empate al final de una ronda.
     */
    private boolean hayEmpate() {
        if (equipos == null || equipos.isEmpty()) return false;

        int maxPuntaje = equipos.stream()
                .mapToInt(Equipo::getPuntaje)
                .max()
                .orElse(0);

        long empatados = equipos.stream()
                .filter(e -> e.getPuntaje() == maxPuntaje)
                .count();

        return empatados > 1;
    }

    /**
     * Activa el modo de muerte súbita entre equipos empatados.
     */
    private void activarMuerteSubita() {
        this.muerteSubitaActiva = true;
        this.partidaActiva = true;

        int maxPuntaje = equipos.stream()
                .mapToInt(Equipo::getPuntaje)
                .max()
                .orElse(0);

        // Filtrar solo equipos empatados
        equipos = equipos.stream()
                .filter(e -> e.getPuntaje() == maxPuntaje)
                .toList();

        reiniciarPuntajes();
        turnoActual = 0;
    }

    /**
     * Indica si hay una muerte súbita en curso.
     */
    public boolean isMuerteSubitaActiva() {
        return muerteSubitaActiva;
    }

    /**
     * Indica si la partida sigue activa.
     */
    public boolean isPartidaActiva() {
        return partidaActiva;
    }

    /**
     * Devuelve el equipo ganador.
     */
    public Equipo getGanador() {
        if (equipos == null) return null;

        return equipos.stream()
                .max(Comparator.comparingInt(Equipo::getPuntaje))
                .orElse(null);
    }

    public void setPuntajeObjetivo(int nuevoObjetivo) {
        if (nuevoObjetivo <= 0) {
            throw new IllegalArgumentException("El puntaje objetivo debe ser positivo.");
        }
        this.puntajeObjetivo = nuevoObjetivo;
    }

    public int getPuntajeObjetivo() {
        return puntajeObjetivo;
    }

    public Equipo getEquipoEnTurno() {
        if (equipos == null || equipos.isEmpty()) {
            return null;
        }
        return equipos.get(turnoActual);
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public int getMaximoRondas() {
        return maximoRondas;
    }

    public TipoLanzamiento getUltimoLanzamiento() {
        return ultimoLanzamiento;
    }

    public boolean puedeJugarOtraRonda() {
        return rondaActual < maximoRondas;
    }
}
