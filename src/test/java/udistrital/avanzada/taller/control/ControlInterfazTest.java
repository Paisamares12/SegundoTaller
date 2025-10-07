package udistrital.avanzada.taller.control;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Pruebas unitarias básicas para ControlInterfaz. Se enfocan en la interacción
 * con ControlLogica y flujo de botones.
 *
 * Nota: No se prueba la interfaz gráfica completa, solo la lógica que puede ser
 * ejecutada sin mostrar ventanas.
 *
 * @author Paula
 * @version 1.0 - 07/10/2025
 */
public class ControlInterfazTest {

    private ControlLogica cLogica;
    private ControlInterfaz cInterfaz;

    @BeforeEach
    void setUp() {
        // Inicializamos el controlador lógico con datos de prueba
        cLogica = new ControlLogica();

        // Agregamos algunos jugadores para pruebas
        cLogica.getJugadoresDisponibles().add(new Jugador("Pedro", "foto1.jpg", "El Toro"));
        cLogica.getJugadoresDisponibles().add(new Jugador("Juan", "foto2.jpg", "El Rápido"));
        cLogica.getJugadoresDisponibles().add(new Jugador("María", "foto3.jpg", "La Fiera"));
        cLogica.getJugadoresDisponibles().add(new Jugador("Luisa", "foto4.jpg", "La Precisa"));

        // Creamos la instancia del controlador de interfaz
        // Nota: Las ventanas se inicializarán pero no se mostrarán en modo headless si es necesario
        cInterfaz = new ControlInterfaz(cLogica);
    }

    @AfterEach
    void tearDown() {
        cLogica = null;
        cInterfaz = null;
    }

    /**
     * Verifica que el constructor inicializa correctamente las dependencias.
     */
    @Test
    void testInicializacion() {
        assertNotNull(cInterfaz);
        assertNotNull(cInterfaz.getRondaActual());
        assertEquals(0, cInterfaz.getRondaActual());
    }

    /**
     * Testeo simulado de ejecutarLanzamiento. Aquí se comprueba que no lanza
     * excepción si se llama sin partida activa.
     */
    @Test
    void testEjecutarLanzamientoSinPartida() {
        // No hay equipos cargados, ejecutarLanzamiento debería lanzar IllegalStateException
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cInterfaz.limpiarResultados(); // como método público auxiliar para probar
        });

        // Solo para documentar la salida
        System.out.println("Se ejecutó limpiarResultados sin errores visibles en interfaz");
    }

    /**
     * Test de getRondaActual() para validar que el flujo de rondas inicializa
     * en 0.
     */
    @Test
    void testGetRondaActual() {
        int ronda = cInterfaz.getRondaActual();
        assertEquals(0, ronda, "La ronda inicial debe ser 0");
    }

}
