package udistrital.avanzada.taller.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.vista.Inicio;
import udistrital.avanzada.taller.vista.VentanaPrincipal;

/**
 * Clase encargada de administrar la interfaz.
 * Cumple el principio de responsabilidad única (SRP):
 * solo gestiona la lógica de la vista.
 *
 * Originalmente creada por Paula Martínez
 * Modificada por Juan Sebastián Bravo Rojas
 * 
 * 
 * @author Paula Martinez
 * @version 4.0
 * 06/10/2025
 */
public class ControlInterfaz implements ActionListener {

    private final ControlLogica cLogica;
    private final Inicio inicio;
    private VentanaPrincipal vPrincipal; // se crea después de cargar equipos

    public ControlInterfaz(ControlLogica cLogica) {
        this.cLogica = cLogica;
        this.inicio = new Inicio();

        // Mostrar la ventana de inicio
        this.inicio.setVisible(true);

        // Listeners
        this.inicio.getBotonSalir().addActionListener(this);
        this.inicio.getBotonJugar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Botón Salir
        if (e.getSource() == inicio.getBotonSalir()) {
            inicio.dispose();
            System.exit(0);
        }

        // Botón Jugar: cargar equipos y abrir ventana principal
        if (e.getSource() == inicio.getBotonJugar()) {
            File archivo = inicio.obtenerArchivoEquipos(); // o donde sea que obtengas el archivo
            try {
                cLogica.cargarEquipos(archivo);
                List<Equipo> equipos = cLogica.getEquipos();

                // ahora sí puedes crear la ventana principal
                vPrincipal = new VentanaPrincipal(this, equipos);
                inicio.dispose();
                vPrincipal.setVisible(true);

                // listeners de la ventana principal
                vPrincipal.getBotonLanzarArgollaUno().addActionListener(this);
                vPrincipal.getBotonLanzarArgollaDos().addActionListener(this);
            } catch (IllegalArgumentException ex) {
                inicio.mostrarMensaje(ex.getMessage());
            }
        }

        // Lanzar argolla (desde ventana principal)
        if (vPrincipal != null && 
           (e.getSource() == vPrincipal.getBotonLanzarArgollaUno() || 
            e.getSource() == vPrincipal.getBotonLanzarArgollaDos())) {
            ejecutarLanzamiento();
        }
    }

    private void ejecutarLanzamiento() {
        try {
            String resultado = cLogica.lanzarArgolla();
            vPrincipal.actualizarResultado(resultado);

            if (!cLogica.partidaActiva()) {
                String ganador = cLogica.getGanador();
                vPrincipal.mostrarMensaje("¡Partida terminada! Ganador: " + ganador);
            }
        } catch (IllegalStateException ex) {
            vPrincipal.mostrarMensaje(ex.getMessage());
        }
    }
}

