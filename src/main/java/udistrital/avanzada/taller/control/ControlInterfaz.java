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
import udistrital.avanzada.taller.vista.VentanaPrincipal;

/**
 *
 * @author Paula Martínez
 * @version 1.0 30/09/2025 La clase ControlInterfaz.java ha sido creada con el
 * fin de manejar toda la interfaz del programa
 */
public class ControlInterfaz implements ActionListener {

    //Inyectamos ControlLogica
    private ControlLogica cLogica;
    //Creamos Atributos de las clases
    private Inicio inicio;
    private VentanaPrincipal vPrincipal;

    public ControlInterfaz(ControlLogica cLogica) {
        //Inyección
        this.cLogica = cLogica;
        
        //Instanciacion
        this.inicio = inicio;
        this.vPrincipal = vPrincipal;
        
        //Iniciamos la ventana principal 
        inicio.setVisible(true);

        //Añadir el ActionListener a todos los botones usados
        this.inicio.getBotonSalir().addActionListener(this);
        this.inicio.getBotonJugar().addActionListener(this);
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
            this.vPrincipal.setVisible(true);
            this.inicio.setVisible(false);
        }

    }

}
