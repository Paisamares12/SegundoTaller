package udistrital.avanzada.taller.control;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Pruebas unitarias para la clase ControlLogica.
 * Se enfoca en la creación de equipos, gestión de jugadores y delegación a ControlEquipos y ControlPartida.
 * 
 * Nota: No se prueba la interfaz gráfica ni JOptionPane, solo la lógica de negocio.
 * 
 * @author Paula
 * @version 1.0 - 07/10/2025
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControlLogicaTest {

    private ControlLogica cLogica;

    private List<Jugador> jugadoresTest;

    @BeforeAll
    void iniciarSuite() {
        System.out.println("=== INICIO DE PRUEBAS DE CONTROLLOGICA ===");
    }

    @AfterAll
    void finalizarSuite() {
        System.out.println("=== FIN DE PRUEBAS DE CONTROLLOGICA ===");
    }

    @BeforeEach
    void setUp() {
        cLogica = new ControlLogica();

        // Crear jugadores de prueba
        jugadoresTest = new ArrayList<>();
        jugadoresTest.add(new Jugador("Pedro", "foto1.jpg", "El Toro"));
        jugadoresTest.add(new Jugador("Juan", "foto2.jpg", "El Rápido"));
        jugadoresTest.add(new Jugador("María", "foto3.jpg", "La Fiera"));
        jugadoresTest.add(new Jugador("Luisa", "foto4.jpg", "La Precisa"));
        jugadoresTest.add(new Jugador("Camilo", "foto5.jpg", "El Profe"));
    }

    @AfterEach
    void limpiarDatos() {
        cLogica = null;
        jugadoresTest = null;
    }

    /**
     * Test de creación de un nuevo equipo directamente desde ControlLogica
     */
    @Test
    void testCrearNuevoEquipoValido() {
        List<Jugador> seleccion = jugadoresTest.subList(0, 4);
        Equipo equipo = cLogica.crearNuevoEquipo("Llaneros", seleccion);

        assertNotNull(equipo, "El equipo no debe ser null");
        assertEquals("Llaneros", equipo.getNombre(), "El nombre del equipo debe coincidir");
        assertEquals(4, equipo.getJugadores().size(), "El equipo debe tener 4 jugadores");
    }

    /**
     * Test de creación de equipo con nombre vacío
     */
    @Test
    void testCrearNuevoEquipoNombreVacio() {
        List<Jugador> seleccion = jugadoresTest.subList(0, 4);
        assertThrows(IllegalArgumentException.class, () -> {
            cLogica.crearNuevoEquipo("", seleccion);
        });
    }

    /**
     * Test de creación de equipo con cantidad de jugadores inválida
     */
    @Test
    void testCrearNuevoEquipoJugadoresInvalidos() {
        List<Jugador> seleccion = new ArrayList<>(); // vacía
        assertThrows(IllegalArgumentException.class, () -> {
            cLogica.crearNuevoEquipo("Guaros", seleccion);
        });
    }

    /**
     * Test de obtención de lista de jugadores disponibles
     */
    @Test
    void testGetJugadoresDisponibles() {
        // Inicialmente la lista de ControlLogica está vacía
        assertNotNull(cLogica.getJugadoresDisponibles(), "Lista de jugadores no debe ser null");
        assertEquals(0, cLogica.getJugadoresDisponibles().size(), "Inicialmente debe estar vacía");
    }

    /**
     * Test de avance de ronda usando ControlPartida
     */
    @Test
    void testAvanzarRonda() {
        boolean resultado = cLogica.avanzarRonda();
        // Depende de ControlPartida, normalmente la primera ronda puede iniciarse
        assertFalse(resultado, "Al iniciar no debería permitir avanzar si no hay partida activa");
    }

    /**
     * Test de obtener ronda actual
     */
    @Test
    void testGetRondaActual() {
        int ronda = cLogica.getRondaActual();
        assertEquals(0, ronda, "La ronda inicial debe ser 0");
    }

    /**
     * Test de lanzar argolla sin partida activa
     */
    @Test
    void testLanzarArgollaSinPartida() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cLogica.lanzarArgolla();
        });
        assertNotNull(exception.getMessage(), "Debe lanzar excepción indicando que no hay partida activa");
    }

    /**
     * Test de reiniciar partida
     */
    @Test
    void testReiniciarPartida() {
        assertDoesNotThrow(() -> cLogica.reiniciarPartida(), "Reiniciar partida no debe lanzar excepción");
    }
}
