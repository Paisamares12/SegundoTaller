/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.control;
//Importar las clases necesarias traidas desde otros paquetes
import udistrital.avanzada.taller.modelo.Conexion;

/**
 *
 * @author Paula Martínez
 * @version 1.0
 * 30/09/2025 
 * La clase ControlLogica.java ha sido creada con el fin de manejar toda la lógica
 * del aplicativo, conectar modelo con control
 */
public class ControlLogica {

    //Creamos como atributos privados las clases necesarias
    private ControlInterfaz cInterfaz;
    private Conexion cnx;
     /**
     * Constructor clase ControlLogica el cual se usa apenas se lanza el
     * launcher
     * 
     */
    public ControlLogica() {
       //Inyectamos ControlLogica (mandamos su copia) a ControlInterfaz
       cInterfaz = new ControlInterfaz(this);
       //Se pasa el archivo obtenido desde la ventana a la conexion
       cnx = new Conexion(cInterfaz.darArchivo());
    }  
    
}
