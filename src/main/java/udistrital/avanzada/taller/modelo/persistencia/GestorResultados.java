/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.modelo.persistencia;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import udistrital.avanzada.taller.modelo.Equipo;
import udistrital.avanzada.taller.modelo.Jugador;

/**
 * Clase encargada de gestionar la persistencia de resultados
 * en un archivo de acceso aleatorio.
 * 
 * Arreglo para persistir correctamente los nombres de los jugadores
 * 
 * Estructura del registro:
 * - Clave (int): 4 bytes
 * - Nombre Equipo (String): 50 chars = 100 bytes
 * - Jugador 1 (String): 50 chars = 100 bytes
 * - Jugador 2 (String): 50 chars = 100 bytes
 * - Jugador 3 (String): 50 chars = 100 bytes
 * - Jugador 4 (String): 50 chars = 100 bytes
 * - Resultado (String): 30 chars = 60 bytes
 * - Puntaje Final (int): 4 bytes
 * - Ronda (int): 4 bytes
 * 
 * Total: 572 bytes por registro
 * 
 * @author Juan Ariza
 * @version 7.0
 * 06/10/2025
 */
public class GestorResultados {
    
    private static final String NOMBRE_ARCHIVO = "resultados.dat";
    private static final int TAM_REGISTRO = 572;
    private static final int LONGITUD_NOMBRE_EQUIPO = 50;
    private static final int LONGITUD_NOMBRE_JUGADOR = 50;
    private static final int LONGITUD_RESULTADO = 30;
    
    private File archivo;
    
    /**
     * Constructor que inicializa el archivo de resultados.
     */
    public GestorResultados() {
        this.archivo = new File(NOMBRE_ARCHIVO);
    }
    
    /**
     * Guarda el resultado de un equipo en el archivo.
     * 
     * @param equipo equipo a guardar
     * @param resultado resultado de la ronda (Ganador/Perdedor)
     * @param ronda número de ronda
     * @throws IOException si hay error al escribir
     */
    public void guardarResultado(Equipo equipo, String resultado, int ronda) throws IOException {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo");
        }
        
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            // Ir al final del archivo
            raf.seek(raf.length());
            
            // Calcular clave (número de registro)
            int clave = (int) (raf.length() / TAM_REGISTRO) + 1;
            
            // Escribir clave
            raf.writeInt(clave);
            
            // Escribir nombre del equipo
            escribirString(raf, equipo.getNombre(), LONGITUD_NOMBRE_EQUIPO);
            
            // Escribir jugadores (máximo 4)
            List<Jugador> jugadores = equipo.getJugadores();
            if (jugadores == null) {
                jugadores = new ArrayList<>();
            }
            
            for (int i = 0; i < 4; i++) {
                if (i < jugadores.size() && jugadores.get(i) != null) {
                    Jugador j = jugadores.get(i);
                    String nombreCompleto = (j.getNombre() != null ? j.getNombre() : "Sin nombre") + 
                                          " (" + (j.getApodo() != null ? j.getApodo() : "Sin apodo") + ")";
                    escribirString(raf, nombreCompleto, LONGITUD_NOMBRE_JUGADOR);
                } else {
                    escribirString(raf, "---", LONGITUD_NOMBRE_JUGADOR);
                }
            }
            
            // Escribir resultado
            escribirString(raf, resultado != null ? resultado : "DESCONOCIDO", LONGITUD_RESULTADO);
            
            // Escribir puntaje final
            raf.writeInt(equipo.getPuntaje());
            
            // Escribir ronda
            raf.writeInt(ronda);
        }
    }
    
    /**
     * Lee todos los resultados almacenados en el archivo.
     * 
     * @return lista de strings con la información de cada registro
     * @throws IOException si hay error al leer
     */
    public List<String> leerTodosLosResultados() throws IOException {
        List<String> resultados = new ArrayList<>();
        
        if (!archivo.exists() || archivo.length() == 0) {
            return resultados;
        }
        
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long cantidadRegistros = raf.length() / TAM_REGISTRO;
            
            for (int i = 0; i < cantidadRegistros; i++) {
                raf.seek(i * TAM_REGISTRO);
                
                // Leer clave
                int clave = raf.readInt();
                
                // Leer nombre equipo
                String nombreEquipo = leerString(raf, LONGITUD_NOMBRE_EQUIPO);
                
                // Leer jugadores
                String jugador1 = leerString(raf, LONGITUD_NOMBRE_JUGADOR);
                String jugador2 = leerString(raf, LONGITUD_NOMBRE_JUGADOR);
                String jugador3 = leerString(raf, LONGITUD_NOMBRE_JUGADOR);
                String jugador4 = leerString(raf, LONGITUD_NOMBRE_JUGADOR);
                
                // Leer resultado
                String resultado = leerString(raf, LONGITUD_RESULTADO);
                
                // Leer puntaje
                int puntaje = raf.readInt();
                
                // Leer ronda
                int ronda = raf.readInt();
                
                // Construir string formateado
                StringBuilder sb = new StringBuilder();
                sb.append("╔═══════════════════════════════════════════════════════╗\n");
                sb.append("REGISTRO #").append(clave).append(" - RONDA ").append(ronda).append("\n");
                sb.append("╠═══════════════════════════════════════════════════════╣\n");
                sb.append("Equipo: ").append(nombreEquipo).append("\n");
                sb.append("Puntaje Final: ").append(puntaje).append(" puntos\n");
                sb.append("Resultado: ").append(resultado).append("\n");
                sb.append("---------------------------------------------------\n");
                sb.append("Jugadores:\n");
                sb.append("  1. ").append(jugador1).append("\n");
                sb.append("  2. ").append(jugador2).append("\n");
                sb.append("  3. ").append(jugador3).append("\n");
                sb.append("  4. ").append(jugador4).append("\n");
                sb.append("╚═══════════════════════════════════════════════════════╝\n");
                
                resultados.add(sb.toString());
            }
        }
        
        return resultados;
    }
    
    /**
     * Escribe un String de longitud fija en el archivo.
     * 
     * @param raf archivo de acceso aleatorio
     * @param texto texto a escribir
     * @param longitud longitud fija en caracteres
     * @throws IOException si hay error al escribir
     */
    private void escribirString(RandomAccessFile raf, String texto, int longitud) throws IOException {
        if (texto == null) {
            texto = "";
        }
        
        StringBuilder sb = new StringBuilder(texto);
        
        // Ajustar la longitud
        if (sb.length() > longitud) {
            sb.setLength(longitud);
        } else {
            while (sb.length() < longitud) {
                sb.append(' ');
            }
        }
        
        // Escribir cada carácter
        raf.writeChars(sb.toString());
    }
    
    /**
     * Lee un String de longitud fija del archivo.
     * 
     * @param raf archivo de acceso aleatorio
     * @param longitud longitud en caracteres a leer
     * @return String leído y sin espacios al final
     * @throws IOException si hay error al leer
     */
    private String leerString(RandomAccessFile raf, int longitud) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            sb.append(raf.readChar());
        }
        return sb.toString().trim();
    }
    
    /**
     * Verifica si existen resultados previos.
     * 
     * @return true si el archivo existe y tiene contenido
     */
    public boolean existenResultadosPrevios() {
        return archivo.exists() && archivo.length() > 0;
    }
    
    /**
     * Elimina el archivo de resultados (útil para empezar de cero).
     */
    public void limpiarResultados() {
        if (archivo.exists()) {
            archivo.delete();
        }
    }
    
    /**
     * Obtiene la cantidad de registros guardados.
     * 
     * @return cantidad de registros
     */
    public int getCantidadRegistros() {
        if (!archivo.exists()) {
            return 0;
        }
        return (int) (archivo.length() / TAM_REGISTRO);
    }
}