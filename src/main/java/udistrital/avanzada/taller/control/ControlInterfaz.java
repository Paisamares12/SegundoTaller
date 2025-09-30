/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;

//Importamos clases necesarias desde otros paquetes
import udistrital.avanzada.taller.vista.Inicio;

/**
 *
 * @author Paula Martínez
 * @version 1.0 
 * 30/09/2025 
 * La clase ControlInterfaz.java ha sido creada con el fin de
 * manejar toda la interfaz del programa
 */
public class ControlInterfaz {
    
    //Creamos como atributos privados las clases inyectadas desde ControlLogica
    private ControlLogica cLogica;
    private Inicio inicio;

    public ControlInterfaz(ControlLogica cLogica, Inicio inicio) {
        //Inyección de todas las clases mandadas desde ControlLogica
        this.cLogica = cLogica;
        this.inicio = inicio;
        
        //Iniciamos la ventana principal 
        inicio.setVisible(true);
    }
    
    
    
}
