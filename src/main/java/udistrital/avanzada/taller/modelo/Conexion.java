/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Clase que conecta la persistencia del archivo con el resto del aplicativo
 * Se modifica para que cumpla con el diseño MVC eliminando todo System.out...
 *
 * @author Paula Martínez
 * @version 7.0 
 * 06/10/2025
 */
public class Conexion {

    private File fl;
    RandomAccessFile archivo;

    private long tamreg;
    private long canreg;

    /**
     * Constructor de la clase conexion
     *
     * @param fl archivo a gestionar
     */
    public Conexion(File fl) {
        this.tamreg = 304;
        this.canreg = 0;
        /**
         * Recibe un archivo desde el JFileChooser en la ventana
         * VentanaPrincipal y lo pasa a un Random Access File con modo rw lo que
         * significa que está abierto para escribir y para leer.
         */
        try {
            this.fl = fl;
            archivo = new RandomAccessFile(fl, "rw");
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException("No se pudo abrir el archivo: " + fnfe.getMessage());
        }
    }

    /**
     * Escribe un nuevo registro en el archivo con clave incremental y campos de
     * longitud fija.
     *
     * @param clave Número de identificación del registro
     * @param nombreEquipo Nombre del equipo (longitud fija de 25 caracteres)
     * @param jugador1 Nombre del primer jugador (longitud fija de 25 caracteres)
     * @param jugador2 Nombre del segundo jugador (longitud fija de 25 caracteres)
     * @param jugador3 Nombre del tercer jugador (longitud fija de 25 caracteres)
     * @param jugador4 Nombre del cuarto jugador (longitud fija de 25 caracteres)
     * @param resultado Resultado asociado al equipo (longitud fija de 25 caracteres)
     */
    public void escribir(int clave, String nombreEquipo, String jugador1,
            String jugador2, String jugador3, String jugador4,
            String resultado) {
        try {
            // Asegurarse de que la escritura se haga al final del archivo
            if (archivo.length() != 0) {
                archivo.seek(archivo.length());
            }

            // Incrementa la clave para que sea única
            clave++;

            // Escribe primero la clave como entero (4 bytes)
            archivo.writeInt(clave);

            // Escribe cada campo como cadena de longitud fija de 25 caracteres
            archivo.writeChars(fijarLongitud(nombreEquipo, 25));
            archivo.writeChars(fijarLongitud(jugador1, 25));
            archivo.writeChars(fijarLongitud(jugador2, 25));
            archivo.writeChars(fijarLongitud(jugador3, 25));
            archivo.writeChars(fijarLongitud(jugador4, 25));
            archivo.writeChars(fijarLongitud(resultado, 25));

        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException("Archivo no encontrado: " + fnfe.getMessage());
        } catch (IOException ioe) {
            throw new RuntimeException("Error de escritura: " + ioe.getMessage());
        }
    }

    /**
     * Lee todos los registros almacenados en el archivo y los devuelve en una
     * lista de cadenas.
     *
     * @return ArrayList con todos los registros
     * @throws IOException si ocurre un error durante la lectura
     */
    public ArrayList<String> leerTodo() {
        ArrayList<String> registros = new ArrayList<>();

        try {
            // Posicionarse al inicio del archivo
            archivo.seek(0);
            // Calcular la cantidad de registros en el archivo
            canreg = archivo.length() / tamreg;
            
            // Recorrer todos los registros
            for (int r = 0; r < canreg; r++) {
                // Leer la clave de 4 bytes
                int clave = archivo.readInt();
                
                // Leer nombre del equipo
                String nombreEquipo = "";
                for (int i = 0; i < 25; i++) {
                    nombreEquipo += archivo.readChar();
                }
                
                // Leer Jugadores
                String jugador1 = "";
                for (int i = 0; i < 25; i++) {
                    jugador1 += archivo.readChar();
                }
                
                String jugador2 = "";
                for (int i = 0; i < 25; i++) {
                    jugador2 += archivo.readChar();
                }
                
                String jugador3 = "";
                for (int i = 0; i < 25; i++) {
                    jugador3 += archivo.readChar();
                }
                
                String jugador4 = "";
                for (int i = 0; i < 25; i++) {
                    jugador4 += archivo.readChar();
                }
                
                // Leer Resultado
                String resultado = "";
                for (int i = 0; i < 25; i++) {
                    resultado += archivo.readChar();
                }

                // Construir el String del registro
                String registro = "Registro No: " + clave
                        + " Equipo: " + nombreEquipo.trim()
                        + " Jug1: " + jugador1.trim()
                        + " Jug2: " + jugador2.trim()
                        + " Jug3: " + jugador3.trim()
                        + " Jug4: " + jugador4.trim()
                        + " Resultado: " + resultado.trim();

                registros.add(registro);
            }
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException("Archivo no encontrado: " + fnfe.getMessage());
        } catch (IOException ioe) {
            throw new RuntimeException("Error de lectura: " + ioe.getMessage());
        }

        return registros;
    }

    /**
     * Ajusta un String a una longitud fija.
     * @param s la cadena a ajustar
     * @param len longitud deseada
     * @return cadena ajustada
     */
    private String fijarLongitud(String s, int len) {
        if (s == null) s = "";
        
        // Si la cadena supera la longitud deseada, la recortamos
        if (s.length() > len) {
            return s.substring(0, len);
        }

        // Agregar espacios hasta alcanzar la longitud
        while (s.length() < len) {
            s += " ";
        }

        return s;
    }
    
    /**
     * Cierra el archivo
     */
    public void cerrar() {
        try {
            if (archivo != null) {
                archivo.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cerrar el archivo: " + e.getMessage());
        }
    }
}
