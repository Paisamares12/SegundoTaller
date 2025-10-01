/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

//Importamos librerias
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
//Importamos clases necesarias desde otros paquetes
import udistrital.avanzada.taller.vista.Inicio;
import udistrital.avanzada.taller.vista.CrearEquipos;
import udistrital.avanzada.taller.vista.VentanaPrincipal;

/**
 *
 * @author Paula Martínez
 * @version 1.0 30/09/2025 La clase ControlInterfaz.java ha sido creada con el
 * fin de manejar toda la interfaz del programa
 */
public class ControlInterfaz implements ActionListener {

    //Creamos como atributos privados las clases inyectadas desde ControlLogica
    private ControlLogica cLogica;
    private Inicio inicio;
    private CrearEquipos crearEquipos;
    private VentanaPrincipal vPrincipal;

    public ControlInterfaz(ControlLogica cLogica, Inicio inicio, CrearEquipos crearEquipos, VentanaPrincipal vPrincipal) {
        //Inyección de todas las clases mandadas desde ControlLogica
        this.cLogica = cLogica;
        this.inicio = inicio;
        this.crearEquipos = crearEquipos;
        this.vPrincipal = vPrincipal;
        
        //Iniciamos la ventana principal 
        inicio.setVisible(true);

        //Añadir el ActionListener a todos los botones usados
        this.inicio.getBotonSalir().addActionListener(this);
        this.inicio.getBotonJugar().addActionListener(this);
        this.crearEquipos.getBotonCrearJugador().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Al tocar el boton salir se sale del programa jaja
        if (e.getSource() == this.inicio.getBotonSalir()) {
            this.inicio.setVisible(false);
            System.exit(0);
        }
        //Al tocar el boton jugar se pasa a la ventana CrearEquipos
        if (e.getSource() == this.inicio.getBotonJugar()) {
            this.crearEquipos.setVisible(true);
            this.inicio.setVisible(false);
        }
        /*
        Al tocar el boton de crear jugador se crea un nuevo jugador cambiando asi
        el label de numero de jugador y cuando se llega a 4 se cambia el equipo
        cuando ya se terminan ambos equipos se muestra un mensaje diciendo que 
        se han creado exitosamente y se pasa a la ventana principal del juego
         */
        if (e.getSource() == this.crearEquipos.getBotonCrearJugador()) {
            int i = Integer.parseInt(this.crearEquipos.getlNumeroJugador().getText());
            if (i < 4) {
                i++;
                this.crearEquipos.getlNumeroJugador().setText(Integer.toString(i));
            } else if (!this.crearEquipos.getlTipoEquipo().getText().equals("B")) {
                this.crearEquipos.getlTipoEquipo().setText("B");
                i = 1;
                this.crearEquipos.getlNumeroJugador().setText(Integer.toString(i));
            } else {
                JOptionPane.showMessageDialog(null,
                        "¡Se han creado todos los equipos con éxito!",
                        "Equipos listos",
                        JOptionPane.INFORMATION_MESSAGE);
                this.vPrincipal.setVisible(true);
                this.crearEquipos.setVisible(false);
            }

        }

    }

}
