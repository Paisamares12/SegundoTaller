/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Paula Martínez
 * @version 4.0
 * 30/09/2025 
 * La clase JPanelImage.java ha sido creada con el fin de
 * poner imagenes en paneles
 * 
 * //Codigo sacado de https://www.youtube.com/watch?v=wdoiH4c44pA&t=2s
 */


public class JPanelImage extends JLabel {
    
    private int x,y;
    private final String path;
    
    public JPanelImage(JPanel panel, String path){
        this.path = path;
        this.x = panel.getWidth();
        this.y = panel.getHeight();
        this.setSize(x,y);
    }
    
    @Override
    public void paint(Graphics g){
        ImageIcon img = new ImageIcon(getClass().getResource(path));
        g.drawImage(img.getImage(),0,0, x, y, null);
    }
    
}
