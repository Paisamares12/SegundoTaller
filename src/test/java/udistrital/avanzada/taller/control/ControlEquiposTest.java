package udistrital.avanzada.taller.control;

//Importar Librerias
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
//Importar clases del paquete modelo
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Clase de pruebas unitarias para la clase ControlEquipos.
 *
 * Cada test valida el comportamiento de los métodos de ControlEquipos en
 * relación con la gestión de jugadores disponibles y equipos. Se documenta la
 * relación con las clases Jugador y Equipo.
 *
 * @author Paula Martínez
 * @version 7.0 - 07/10/2025
 */
public class ControlEquiposTest {

    // Objeto de control que será probado
    private ControlEquipos control;
    // Lista de jugadores para pruebas
    private List<Jugador> jugadores;

    /**
     * Se ejecuta antes de cada prueba. Inicializa ControlEquipos y la lista de
     * jugadores disponibles. Relacionado con
     * ControlEquipos.setJugadoresDisponibles()
     */
    @BeforeEach
    public void setUp() {
        control = new ControlEquipos();
        jugadores = new ArrayList<>();

        // Crear jugadores de prueba con nombre, ruta de foto y apodo
        jugadores.add(new Jugador("Pedro", "foto1.jpg", "El Toro"));
        jugadores.add(new Jugador("Juan", "foto2.jpg", "El Rápido"));
        jugadores.add(new Jugador("María", "foto3.jpg", "La Fiera"));
        jugadores.add(new Jugador("Luisa", "foto4.jpg", "La Precisa"));
        jugadores.add(new Jugador("Camilo", "foto5.jpg", "El Profe"));

        // Establecer los jugadores como disponibles en el objeto de control
        control.setJugadoresDisponibles(jugadores);
    }

    /**
     * Se ejecuta una sola vez antes de toda la suite. Relacionado con la
     * inicialización general de pruebas.
     */
    @BeforeAll
    static void iniciarSuite() {
        System.out.println("=== INICIO DE LAS PRUEBAS DE CONTROLEQUIPOS ===");
    }

    /**
     * Se ejecuta una sola vez al finalizar toda la suite. Relacionado con la
     * liberación de recursos al final de las pruebas.
     */
    @AfterAll
    static void finalizarSuite() {
        System.err.println("=== FIN DE LAS PRUEBAS DE CONTROLEQUIPOS ===");
    }

    /**
     * Se ejecuta después de cada prueba. Limpia las referencias para evitar
     * efectos colaterales entre pruebas.
     */
    @AfterEach
    void limpiarDatos() {
        control = null;
        jugadores = null;
    }

    /**
     * Test de setEquipos(). Verifica que se pueda establecer una lista de
     * equipos en ControlEquipos. Aunque se pase null, no debería lanzar
     * excepción. Relacionado con la lista interna de ControlEquipos.equipos.
     */
    @Test
    public void testSetEquipos() {
        System.out.println("setEquipos");
        List<Equipo> equipos = null;
        ControlEquipos instance = new ControlEquipos();
        instance.setEquipos(equipos);
    }

    /**
     * Test de setJugadoresDisponibles(). Verifica que se pueda establecer una
     * lista de jugadores disponibles. Relacionado con
     * ControlEquipos.jugadoresDisponibles.
     */
    @Test
    public void testSetJugadoresDisponibles() {
        System.out.println("setJugadoresDisponibles");
        List<Jugador> jugadores = null;
        ControlEquipos instance = new ControlEquipos();
        instance.setJugadoresDisponibles(jugadores);
    }

    /**
     * Test de buscarJugadorDisponible(). Busca un jugador en la lista de
     * disponibles por su apodo. Relacionado con
     * ControlEquipos.buscarJugadorDisponible() y Jugador.getApodo().
     */
    @Test
    void testBuscarJugadorDisponible() {
        // Buscar un jugador por apodo
        Jugador buscado = control.buscarJugadorDisponible("La Fiera");

        // Verificar que se encontró
        assertNotNull(buscado, "El jugador no fue encontrado");
        assertEquals("La Fiera", buscado.getApodo());
    }

    /**
     * Test de crearEquipo() con datos válidos. Verifica que
     * ControlEquipos.crearEquipo(): - Cree un equipo con el nombre y jugadores
     * indicados - Remueva los jugadores de la lista de disponibles Relacionado
     * con Equipo (nombre y lista de jugadores) y Jugador (disponibilidad)
     */
    @Test
    public void testCrearEquipoValido() {
        List<Jugador> seleccion = jugadores.subList(0, 4);
        Equipo equipo = control.crearEquipo("Llaneros", seleccion);

        assertEquals("Llaneros", equipo.getNombre());
        assertEquals(4, equipo.getJugadores().size());
        assertFalse(control.getJugadoresDisponibles().containsAll(seleccion),
                "Los jugadores del equipo no deben seguir disponibles");
    }

    /**
     * Test de crearEquipo() con nombre vacío. Se espera
     * IllegalArgumentException. Relacionado con la validación de nombre en
     * ControlEquipos.crearEquipo().
     */
    @Test
    public void testCrearEquipoSinNombre() {
        List<Jugador> seleccion = jugadores.subList(0, 4);
        assertThrows(IllegalArgumentException.class, () -> {
            control.crearEquipo("", seleccion);
        });
    }

    /**
     * Test de crearEquipo() con lista de jugadores inválida (vacía). Se espera
     * IllegalArgumentException. Relacionado con la validación de tamaño de
     * jugadores en ControlEquipos.crearEquipo().
     */
    @Test
    public void testCrearEquipoConJugadoresInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> {
            control.crearEquipo("Guaros", new ArrayList<>());
        });
    }

    /**
     * Test de haySuficientesJugadores(). Verifica que retorne true si hay al
     * menos 4 jugadores disponibles, y false si la lista está vacía.
     * Relacionado con ControlEquipos.haySuficientesJugadores() y lista de
     * Jugadores.
     */
    @Test
    public void testHaySuficientesJugadores() {
        assertTrue(control.haySuficientesJugadores());
        control.setJugadoresDisponibles(new ArrayList<>()); // vaciar
        assertFalse(control.haySuficientesJugadores());
    }

    /**
     * Test de deshacerEquipo(). Verifica que los jugadores de un equipo
     * regresen a la lista de disponibles y que el equipo se elimine de
     * ControlEquipos.equipos. Relacionado con ControlEquipos.deshacerEquipo(),
     * Equipo.getJugadores() y Jugador.
     */
    @Test
    public void testDeshacerEquipo() {
        List<Jugador> seleccion = jugadores.subList(0, 4);
        Equipo equipo = control.crearEquipo("Llaneros", seleccion);

        int antes = control.getJugadoresDisponibles().size();
        control.deshacerEquipo(equipo);

        assertTrue(control.getJugadoresDisponibles().containsAll(seleccion));
        assertEquals(antes + 4, control.getJugadoresDisponibles().size());
    }

    /**
     * Test de limpiarEquipos(). Verifica que todos los equipos sean eliminados
     * y los jugadores regresen a la lista de disponibles. Relacionado con
     * ControlEquipos.limpiarEquipos(), Equipo y Jugador.
     */
    @Test
    public void testLimpiarEquipos() {
        List<Jugador> seleccion = jugadores.subList(0, 4);
        control.crearEquipo("Llaneros", seleccion);
        control.limpiarEquipos();

        assertTrue(control.getEquipos().isEmpty());
        assertEquals(5, control.getJugadoresDisponibles().size());
    }

}
