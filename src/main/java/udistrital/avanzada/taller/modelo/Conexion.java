/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo;
//Importar las librerias

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Clase que conecta la persistencia del archivo con el resto del aplicativo
 *
 * @author Paula Martínez
 * @version 5.0 06/10/2025
 */
public class Conexion {

    //Archivos
    private File fl;
    RandomAccessFile archivo;

    //Atributos basicos de la clase
    private long tamreg;
    private long canreg;

    /**
     * Constructor de la clase conexion
     *
     * @param fl
     */
    public Conexion(File fl) {
        //Asigna las variables tamreg y canreg
        this.tamreg = 304;
        this.canreg = 0;
        /**
         * Recibe un archivo desde el JFileChooser en la ventana
         * VentanaPrincipal y lo pasa a un Random Access File con modo rw lo que
         * significa que está abierto para escribir y para leer. Si no se halla
         * el archivo lanza una excepcion del tipo FileNotFound
         */
        try {
            this.fl = fl;
            archivo = new RandomAccessFile(fl, "rw");
        } catch (FileNotFoundException fnfe) {

        }
    }

    /**
     * Escribe un nuevo registro en el archivo con clave incremental y campos de
     * longitud fija.
     *
     * @param clave Número de identificación del registro (se puede calcular
     * automáticamente).
     * @param nombreEquipo Nombre del equipo (se almacena con longitud fija de
     * 25 caracteres).
     * @param jugador1 Nombre del primer jugador (longitud fija de 25
     * caracteres).
     * @param jugador2 Nombre del segundo jugador (longitud fija de 25
     * caracteres).
     * @param jugador3 Nombre del tercer jugador (longitud fija de 25
     * caracteres).
     * @param jugador4 Nombre del cuarto jugador (longitud fija de 25
     * caracteres).
     * @param resultado Resultado asociado al equipo (longitud fija de 25
     * caracteres).
     */
    public void escribir(int clave, String nombreEquipo, String jugador1,
            String jugador2, String jugador3, String jugador4,
            String resultado) {
        try {
            //QUITARLO CUANDO SE VEA QUE FUNCIONE BIEN ESTA VAINA
            System.out.println("Escribiendo Registro...");

            // Asegurarse de que la escritura se haga al final del archivo
            if (archivo.length() != 0) {
                archivo.seek(archivo.length());
            }

            // Incrementa la clave para que sea única
            clave++;

            // Escribe primero la clave como entero (4 bytes)
            archivo.writeInt(clave);

            // Escribe cada campo como cadena de longitud fija de 25 caracteres.
            // Si el campo es más corto, se rellena con espacios.
            // Si es más largo, se recorta a 25 caracteres.
            archivo.writeChars(fijarLongitud(nombreEquipo, 25));
            archivo.writeChars(fijarLongitud(jugador1, 25));
            archivo.writeChars(fijarLongitud(jugador2, 25));
            archivo.writeChars(fijarLongitud(jugador3, 25));
            archivo.writeChars(fijarLongitud(jugador4, 25));
            archivo.writeChars(fijarLongitud(resultado, 25));

        } catch (FileNotFoundException fnfe) {
            // Si el archivo no existe o no se puede abrir
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            // Si ocurre un error durante la escritura
            ioe.printStackTrace();
        }
    }

    /**
     * Lee todos los registros almacenados en el archivo y los devuelve en una
     * lista de cadenas.
     *
     * <p>
     * Cada registro tiene un tamaño fijo definido por {@code tamreg} e incluye:
     * <ul>
     * <li>Una clave entera (4 bytes).</li>
     * <li>El nombre del equipo (25 caracteres, 50 bytes).</li>
     * <li>Cuatro nombres de jugadores (cada uno de 25 caracteres, 50
     * bytes).</li>
     * <li>El resultado (25 caracteres, 50 bytes).</li>
     * </ul>
     * </p>
     *
     * <p>
     * El método posiciona el puntero al inicio del archivo, recorre todos los
     * registros existentes y construye una representación en texto de cada uno.
     * Para eliminar los espacios de relleno, se utiliza {@code trim()} en cada
     * campo de texto.</p>
     *
     * @return una {@code ArrayList<String>} donde cada elemento corresponde a
     * un registro, representado como una cadena con el formato:      <pre>
 *         Registro No: [clave]
     *         Equipo: [nombreEquipo]
     *         Jug1: [jugador1]
     *         Jug2: [jugador2]
     *         Jug3: [jugador3]
     *         Jug4: [jugador4]
     *         Resultado: [resultado]
     * </pre>
     *
     * @throws IOException si ocurre un error durante la lectura del archivo.
     */
    public ArrayList<String> leerTodo() {

        ArrayList<String> registros = new ArrayList<>();

        try {
            //Posicionarse al inicio del archivo
            archivo.seek(0);
            //Calcular la cantidad de registros en el archivo
            canreg = archivo.length() / tamreg;
            //Recorrer todos los registros
            for (int r = 0; r < canreg; r++) {
                //Leer la clave de 4 bytes
                int clave = archivo.readInt();
                //Leer los campos de 25 caracteres que contienen el resto de datos
                String nombreEquipo = "";
                for (int i = 0; i < 25; i++) {
                    nombreEquipo += archivo.readChar();
                }
                //Jugador 1
                String jugador1 = "";
                for (int i = 0; i < 25; i++) {
                    jugador1 += archivo.readChar();
                }
                //Jugador 2
                String jugador2 = "";
                for (int i = 0; i < 25; i++) {
                    jugador2 += archivo.readChar();
                }
                //Jugador 3
                String jugador3 = "";
                for (int i = 0; i < 25; i++) {
                    jugador3 += archivo.readChar();
                }
                //Jugador 4
                String jugador4 = "";
                for (int i = 0; i < 25; i++) {
                    jugador4 += archivo.readChar();
                }
                //Resultado
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

                // Agregar a la lista
                registros.add(registro);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return registros; //Retornar la lista
    }

    /**
     * Ajusta un String a una longitud fija. - Si la cadena es más larga que
     * "len", la recorta. - Si es más corta, le agrega espacios en blanco hasta
     * llegar a "len".
     * @return s la cadena deseada
     */
    private String fijarLongitud(String s, int len) {
        // Si la cadena supera la longitud deseada, la recortamos
        if (s.length() > len) {
            return s.substring(0, len);
        }

        // Mientras la cadena sea más corta que la longitud deseada,
        // agregamos un espacio en blanco al final
        while (s.length() < len) {
            s += " ";
        }

        // Devolvemos la cadena ajustada exactamente al tamaño "len"
        return s;
    }

}
