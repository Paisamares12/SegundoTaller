package udistrital.avanzada.taller.control;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;
import udistrital.avanzada.taller.modelo.TipoLanzamiento;

/**
 * Gestiona la lógica de la partida, turnos, lanzamientos por manos y desempates.
 * Implementa el sistema de manos: cada equipo lanza 4 veces (uno por jugador)
 * antes de pasar el turno al otro equipo.
 * 
 * Reglas para ganar bien incorporadas y entrada a muerte súbita arreglada
 *
 * @author Juan Ariza
 * @version 7.0 
 * 06/10/2025
 */
public class ControlPartida {

    private List<Equipo> equipos;
    private boolean partidaActiva;
    private int puntajeObjetivo;
    private int equipoEnTurno;
    private int lanzamientoEnMano;
    private int rondaActual;
    private int maximoRondas;
    private final Random random;
    private boolean muerteSubitaActiva;
    private boolean esperandoFinDeMano;
    private List<String> lanzamientosActuales;
    private int enfrentamientoActual;
    private int[] puntajesMuerteSubita;

    public ControlPartida() {
        this.partidaActiva = false;
        this.puntajeObjetivo = 21;
        this.equipoEnTurno = 0;
        this.lanzamientoEnMano = 0;
        this.rondaActual = 1;
        this.maximoRondas = 2;
        this.random = new Random();
        this.muerteSubitaActiva = false;
        this.esperandoFinDeMano = false;
        this.lanzamientosActuales = new ArrayList<>();
        this.enfrentamientoActual = 0;
        this.puntajesMuerteSubita = new int[2];
    }

    public void setEquipos(List<Equipo> equipos) {
        if (equipos == null || equipos.size() < 2) {
            throw new IllegalArgumentException("Debe haber al menos dos equipos para iniciar la partida.");
        }
        
        for (Equipo eq : equipos) {
            if (eq.getJugadores() == null || eq.getJugadores().size() != 4) {
                throw new IllegalArgumentException("Cada equipo debe tener exactamente 4 jugadores.");
            }
        }
        
        this.equipos = new ArrayList<>(equipos);
        this.partidaActiva = true;
        this.equipoEnTurno = 0;
        this.lanzamientoEnMano = 0;
        this.rondaActual = 1;
        this.muerteSubitaActiva = false;
        this.esperandoFinDeMano = false;
        this.lanzamientosActuales.clear();
        reiniciarPuntajes();
    }

    public String lanzarArgolla() {
        if (!partidaActiva || equipos == null) {
            throw new IllegalStateException("No hay una partida activa.");
        }

        // Si estamos en muerte súbita, usar lógica especial
        if (muerteSubitaActiva) {
            return lanzarArgollaMuerteSubita();
        }

        Equipo equipo = equipos.get(equipoEnTurno);
        List<Jugador> jugadores = equipo.getJugadores();
        
        if (jugadores == null || jugadores.size() < 4) {
            throw new IllegalStateException("El equipo no tiene suficientes jugadores.");
        }
        
        Jugador jugadorActual = jugadores.get(lanzamientoEnMano);
        
        // Simular el lanzamiento
        TipoLanzamiento[] tipos = TipoLanzamiento.values();
        TipoLanzamiento tipoLanzamiento = tipos[random.nextInt(tipos.length)];
        int puntos = tipoLanzamiento.getPuntos();
        equipo.sumarPuntos(puntos);

        // Registrar el lanzamiento individual
        String lanzamientoInfo = String.format("  Jugador: %-25s | Lanzamiento: %-12s | Puntos: %d", 
            jugadorActual.getNombre() + " (" + jugadorActual.getApodo() + ")",
            tipoLanzamiento.getNombre(),
            puntos);
        lanzamientosActuales.add(lanzamientoInfo);

        // Avanzar al siguiente lanzamiento en la mano
        lanzamientoEnMano++;

        String resultado = "";
        
        // Si completamos una mano (4 lanzamientos)
        if (lanzamientoEnMano >= 4) {
            resultado = construirResultadoMano(equipo);
            lanzamientoEnMano = 0;
            lanzamientosActuales.clear();
            
            // CASO 1: Equipo 2 termina su mano después de que Equipo 1 llegó a 21
            if (equipoEnTurno == 1 && esperandoFinDeMano) {
                Equipo equipo1 = equipos.get(0);
                Equipo equipo2 = equipos.get(1);
                
                // Si Equipo 2 NO llegó a 21 → Equipo 1 gana
                if (equipo2.getPuntaje() < puntajeObjetivo) {
                    partidaActiva = false;
                    esperandoFinDeMano = false;
                    resultado += "\n\n*** ¡" + equipo1.getNombre() + " ha GANADO la ronda " + rondaActual + "! ***";
                    resultado += "\n(" + equipo1.getNombre() + ": " + equipo1.getPuntaje() + " pts vs " 
                               + equipo2.getNombre() + ": " + equipo2.getPuntaje() + " pts)";
                }
                // Si Equipo 2 SÍ llegó a 21+ → MUERTE SÚBITA
                else {
                    activarMuerteSubita();
                    resultado += "\n\n⚔⚔⚔ MUERTE SÚBITA ACTIVADA ⚔⚔⚔";
                    resultado += "\nAmbos equipos han alcanzado " + puntajeObjetivo + " puntos!";
                    resultado += "\n" + equipo1.getNombre() + ": " + equipo1.getPuntaje() + " pts";
                    resultado += "\n" + equipo2.getNombre() + ": " + equipo2.getPuntaje() + " pts";
                    resultado += "\n\n🎯 ENFRENTAMIENTOS 1 VS 1";
                    resultado += "\nCada jugador se enfrentará a su contraparte.";
                    resultado += "\nPresiona cualquier botón para iniciar el primer enfrentamiento.";
                }
            }
            // CASO 2: Equipo 1 llega a 21 → marcar espera
            else if (equipoEnTurno == 0 && equipo.getPuntaje() >= puntajeObjetivo) {
                esperandoFinDeMano = true;
                resultado += "\n\n⚠⚠⚠ " + equipo.getNombre() + " ha alcanzado " + puntajeObjetivo + " puntos! ⚠⚠⚠";
                resultado += "\nEsperando la mano del " + equipos.get(1).getNombre() + "...";
                cambiarTurno();
            }
            // CASO 3: Equipo 2 llega a 21 PRIMERO (sin que Equipo 1 haya llegado)
            else if (equipoEnTurno == 1 && equipo.getPuntaje() >= puntajeObjetivo && !esperandoFinDeMano) {
                partidaActiva = false;
                resultado += "\n\n*** ¡" + equipo.getNombre() + " ha GANADO la ronda " + rondaActual + "! ***";
                resultado += "\n(Llegó primero a " + puntajeObjetivo + " puntos con " + equipo.getPuntaje() + " pts)";
            }
            // CASO 4: Nadie ha llegado a 21 → cambiar turno normal
            else {
                cambiarTurno();
            }
        } else {
            // Aún faltan lanzamientos en esta mano
            resultado = construirResultadoParcial(equipo, jugadorActual, tipoLanzamiento, puntos);
        }

        return resultado;
    }
    
    private String lanzarArgollaMuerteSubita() {
        Equipo equipo1 = equipos.get(0);
        Equipo equipo2 = equipos.get(1);
        
        Jugador jugador1 = equipo1.getJugadores().get(enfrentamientoActual);
        Jugador jugador2 = equipo2.getJugadores().get(enfrentamientoActual);
        
        // Lanzamiento del jugador 1
        TipoLanzamiento[] tipos = TipoLanzamiento.values();
        TipoLanzamiento tipo1 = tipos[random.nextInt(tipos.length)];
        int puntos1 = tipo1.getPuntos();
        
        // Lanzamiento del jugador 2
        TipoLanzamiento tipo2 = tipos[random.nextInt(tipos.length)];
        int puntos2 = tipo2.getPuntos();
        
        // Determinar ganador del enfrentamiento
        String ganadorEnfrentamiento;
        if (puntos1 > puntos2) {
            puntajesMuerteSubita[0]++;
            ganadorEnfrentamiento = "⭐ " + equipo1.getNombre();
        } else if (puntos2 > puntos1) {
            puntajesMuerteSubita[1]++;
            ganadorEnfrentamiento = "⭐ " + equipo2.getNombre();
        } else {
            ganadorEnfrentamiento = "🤝 EMPATE - No suma";
        }
        
        // Construir resultado
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════════╗\n");
        sb.append("  ⚔ MUERTE SÚBITA - ENFRENTAMIENTO ").append(enfrentamientoActual + 1).append(" de 4 ⚔\n");
        sb.append("╠═══════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("  %s: %s\n", equipo1.getNombre(), jugador1.getNombre()));
        sb.append(String.format("    → %s = %d puntos\n", tipo1.getNombre(), puntos1));
        sb.append("  -VS-\n");
        sb.append(String.format("  %s: %s\n", equipo2.getNombre(), jugador2.getNombre()));
        sb.append(String.format("    → %s = %d puntos\n", tipo2.getNombre(), puntos2));
        sb.append("╠═══════════════════════════════════════════════════════════════╣\n");
        sb.append("  Ganador del enfrentamiento: ").append(ganadorEnfrentamiento).append("\n");
        sb.append("╠═══════════════════════════════════════════════════════════════╣\n");
        sb.append("  MARCADOR MUERTE SÚBITA:\n");
        sb.append(String.format("    %s: %d   |   %s: %d\n", 
            equipo1.getNombre(), puntajesMuerteSubita[0],
            equipo2.getNombre(), puntajesMuerteSubita[1]));
        sb.append("╚═══════════════════════════════════════════════════════════════╝");
        
        // Avanzar al siguiente enfrentamiento
        enfrentamientoActual++;
        
        // Si completamos los 4 enfrentamientos, determinar ganador
        if (enfrentamientoActual >= 4) {
            sb.append("\n\n");
            sb.append("═══════════════════════════════════════════════════════════════\n");
            sb.append("           FIN DE LA MUERTE SÚBITA\n");
            sb.append("═══════════════════════════════════════════════════════════════\n");
            
            if (puntajesMuerteSubita[0] > puntajesMuerteSubita[1]) {
                sb.append("\n*** ¡").append(equipo1.getNombre()).append(" GANA LA MUERTE SÚBITA! ***\n");
                sb.append("Enfrentamientos ganados: ").append(puntajesMuerteSubita[0]).append(" vs ").append(puntajesMuerteSubita[1]);
                equipo1.setPuntaje(puntajeObjetivo + 1);
            } else if (puntajesMuerteSubita[1] > puntajesMuerteSubita[0]) {
                sb.append("\n*** ¡").append(equipo2.getNombre()).append(" GANA LA MUERTE SÚBITA! ***\n");
                sb.append("Enfrentamientos ganados: ").append(puntajesMuerteSubita[1]).append(" vs ").append(puntajesMuerteSubita[0]);
                equipo2.setPuntaje(puntajeObjetivo + 1);
            } else {
                sb.append("\n⚠ EMPATE PERFECTO (").append(puntajesMuerteSubita[0]).append("-").append(puntajesMuerteSubita[0]).append(") ⚠\n");
                sb.append("Se repetirá la muerte súbita...");
                enfrentamientoActual = 0;
                puntajesMuerteSubita[0] = 0;
                puntajesMuerteSubita[1] = 0;
                return sb.toString();
            }
            
            partidaActiva = false;
            muerteSubitaActiva = false;
        }
        
        return sb.toString();
    }
    
    private String construirResultadoMano(Equipo equipo) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════════╗\n");
        sb.append("  MANO COMPLETA - EQUIPO: ").append(equipo.getNombre()).append("\n");
        sb.append("╠═══════════════════════════════════════════════════════════════╣\n");
        
        for (String lanzamiento : lanzamientosActuales) {
            sb.append(lanzamiento).append("\n");
        }
        
        sb.append("╠═══════════════════════════════════════════════════════════════╣\n");
        sb.append("  PUNTAJE TOTAL DEL EQUIPO: ").append(equipo.getPuntaje()).append(" / ").append(puntajeObjetivo).append("\n");
        sb.append("╚═══════════════════════════════════════════════════════════════╝");
        
        return sb.toString();
    }
    
    private String construirResultadoParcial(Equipo equipo, Jugador jugador, TipoLanzamiento tipo, int puntos) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════════╗\n");
        sb.append("  EQUIPO: ").append(equipo.getNombre()).append("\n");
        sb.append("  JUGADOR: ").append(jugador.getNombre()).append(" (").append(jugador.getApodo()).append(")\n");
        sb.append("╠═══════════════════════════════════════════════════════════════╣\n");
        sb.append("  LANZAMIENTO: ").append(tipo.getNombre()).append("\n");
        sb.append("  PUNTOS OBTENIDOS: ").append(puntos).append("\n");
        sb.append("  PUNTAJE ACTUAL: ").append(equipo.getPuntaje()).append(" / ").append(puntajeObjetivo).append("\n");
        sb.append("╠═══════════════════════════════════════════════════════════════╣\n");
        sb.append("  Lanzamiento ").append(lanzamientoEnMano).append(" de 4 en esta mano\n");
        sb.append("╚═══════════════════════════════════════════════════════════════╝");
        
        return sb.toString();
    }

    private void cambiarTurno() {
        if (equipos != null && !equipos.isEmpty()) {
            equipoEnTurno = (equipoEnTurno + 1) % equipos.size();
        }
    }
    
    private void activarMuerteSubita() {
        this.muerteSubitaActiva = true;
        this.partidaActiva = true;
        this.esperandoFinDeMano = false;
        this.enfrentamientoActual = 0;
        this.puntajesMuerteSubita[0] = 0;
        this.puntajesMuerteSubita[1] = 0;
        this.lanzamientosActuales.clear();
    }

    public void reiniciarPuntajes() {
        if (equipos != null) {
            for (Equipo e : equipos) {
                e.reiniciarPuntaje();
            }
        }
    }

    public void reiniciar() {
        reiniciarPuntajes();
        this.partidaActiva = true;
        this.equipoEnTurno = 0;
        this.lanzamientoEnMano = 0;
        this.muerteSubitaActiva = false;
        this.esperandoFinDeMano = false;
        this.lanzamientosActuales.clear();
        this.enfrentamientoActual = 0;
        this.puntajesMuerteSubita[0] = 0;
        this.puntajesMuerteSubita[1] = 0;
    }

    public boolean avanzarRonda() {
        if (rondaActual < maximoRondas) {
            rondaActual++;
            reiniciar();
            return true;
        }
        return false;
    }

    public boolean isMuerteSubitaActiva() {
        return muerteSubitaActiva;
    }

    public boolean isPartidaActiva() {
        return partidaActiva;
    }

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
        return equipos.get(equipoEnTurno);
    }
    
    public int getLanzamientoEnMano() {
        return lanzamientoEnMano;
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public int getMaximoRondas() {
        return maximoRondas;
    }

    public boolean puedeJugarOtraRonda() {
        return rondaActual < maximoRondas;
    }
}
