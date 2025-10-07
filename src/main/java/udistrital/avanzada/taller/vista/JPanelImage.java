/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udistrital.avanzada.taller.vista;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Paula Martínez
 * @version 4.0
 * 30/09/2025 
 * La clase JPanelImage.java ha sido creada con el fin de
 * poner imagenes en paneles
 * 
 * //Codigo modificado desde https://www.youtube.com/watch?v=wdoiH4c44pA&t=2s
 */


public class JPanelImage extends JPanel {

    private final String path;

    public JPanelImage(JPanel panel, String path) {
        this.path = path;
        this.setSize(panel.getWidth(), panel.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}
