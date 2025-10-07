package udistrital.avanzada.taller.control;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Clase de pruebas unitarias para {@link ControlPartida}. Verifica la correcta
 * gestión de rondas, lanzamientos, reinicio y puntajes.
 *
 * <p>
 * Incluye todos los bloques del esquema JUnit: BeforeAll, AfterAll, BeforeEach,
 * AfterEach.</p>
 *
 * @author
 * @version 1.0 - 07/10/2025
 */
public class ControlPartidaTest {

    private static List<Jugador> jugadoresEquipo1;
    private static List<Jugador> jugadoresEquipo2;
    private static Equipo equipo1;
    private static Equipo equipo2;
    private ControlPartida control;

    @BeforeAll
    static void inicializarDatos() {
        System.out.println("=== INICIO DE LAS PRUEBAS DE CONTROL PARTIDA ===");

        jugadoresEquipo1 = new ArrayList<>();
        jugadoresEquipo2 = new ArrayList<>();

        // Crear 4 jugadores por equipo
        for (int i = 1; i <= 4; i++) {
            jugadoresEquipo1.add(new Jugador("JugadorA" + i, "fotoA" + i + ".jpg", "ApodoA" + i));
            jugadoresEquipo2.add(new Jugador("JugadorB" + i, "fotoB" + i + ".jpg", "ApodoB" + i));
        }

        equipo1 = new Equipo("Llaneros", new ArrayList<>(jugadoresEquipo1));
        equipo2 = new Equipo("Guaros", new ArrayList<>(jugadoresEquipo2));

    }

    @AfterAll
    static void finalizarPruebas() {
        System.out.println("=== FIN DE LAS PRUEBAS DE CONTROL PARTIDA ===");
    }

    @BeforeEach
    void setUp() {
        control = new ControlPartida();
        List<Equipo> equipos = new ArrayList<>();
        equipos.add(equipo1);
        equipos.add(equipo2);
        control.setEquipos(equipos);
    }

    @AfterEach
    void limpiarDatos() {
        control = null;
    }

    @Test
    void testInicializacionPartida() {
        assertTrue(control.isPartidaActiva(), "La partida debería estar activa tras setEquipos.");
        assertEquals(1, control.getRondaActual(), "La ronda inicial debe ser 1.");
        assertEquals(0, control.getEquipoEnTurno().getPuntaje(), "El puntaje inicial debe ser 0.");
    }

    @Test
    void testLanzarArgolla() {
        String resultado = control.lanzarArgolla();
        assertNotNull(resultado, "El resultado del lanzamiento no debe ser nulo.");
        assertTrue(resultado.contains("EQUIPO:"), "El texto debe contener la palabra EQUIPO.");
    }

    @Test
    void testAvanzarRonda() {
        int rondaAntes = control.getRondaActual();
        boolean pudoAvanzar = control.avanzarRonda();
        if (rondaAntes < control.getMaximoRondas()) {
            assertTrue(pudoAvanzar, "Debería avanzar si no se alcanzó el máximo.");
            assertEquals(rondaAntes + 1, control.getRondaActual(), "La ronda debería incrementarse.");
        } else {
            assertFalse(pudoAvanzar, "No debería avanzar si ya está en la última ronda.");
        }
    }

    @Test
    void testReiniciarPartida() {
        equipo1.sumarPuntos(10);
        equipo2.sumarPuntos(5);
        control.reiniciar();
        assertEquals(0, equipo1.getPuntaje(), "El puntaje del equipo 1 debe reiniciarse a 0.");
        assertEquals(0, equipo2.getPuntaje(), "El puntaje del equipo 2 debe reiniciarse a 0.");
        assertTrue(control.isPartidaActiva(), "La partida debe volver a estar activa.");
    }

    @Test
    void testGetGanador() {
        equipo1.sumarPuntos(10);
        equipo2.sumarPuntos(5);
        assertEquals(equipo1, control.getGanador(), "El equipo con más puntos debe ser el ganador.");
    }

    @Test
    void testSetPuntajeObjetivoValido() {
        control.setPuntajeObjetivo(15);
        assertEquals(15, control.getPuntajeObjetivo(), "El puntaje objetivo debe actualizarse correctamente.");
    }

    @Test
    void testSetPuntajeObjetivoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> control.setPuntajeObjetivo(0),
                "Debe lanzar excepción si el puntaje objetivo es menor o igual a 0.");
    }

    @Test
    void testPuedeJugarOtraRonda() {
        assertTrue(control.puedeJugarOtraRonda(), "Debe poder jugar otra ronda al inicio.");
        control.avanzarRonda();
        control.avanzarRonda(); // ahora ronda = maximoRondas
        assertFalse(control.puedeJugarOtraRonda(), "No debe poder jugar otra ronda al llegar al máximo.");
    }

    @Test
    void testLanzamientoCompletaMano() {
        // Hacer 4 lanzamientos (una mano completa)
        for (int i = 0; i < 4; i++) {
            control.lanzarArgolla();
        }
        // Si completó la mano, el turno debería cambiar al otro equipo
        assertEquals("Guaros", control.getEquipoEnTurno().getNombre(),
                "Después de 4 lanzamientos debe cambiar el turno al segundo equipo.");
    }
}
