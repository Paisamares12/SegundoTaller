/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;
//Importar las clases necesarias traidas desde otros paquetes

/**
 *
 * @author Paula Martínez
 * @version 1.0
 * 30/09/2025 
 * La clase ControlLogica.java ha sido creada con el fin de manejar toda la lógica
 * del aplicativo
 */
public class ControlLogica {

    //Creamos como atributos privados las clases necesarias
    private ControlInterfaz cInterfaz;
        
    //Creamos el constructor el cual se usa apenas se lanza el launcher
    public ControlLogica() {
        
        //Inyectamos ControlLogica (mandamos su copia)
        cInterfaz = new ControlInterfaz(this);
        
    }  
    
}
