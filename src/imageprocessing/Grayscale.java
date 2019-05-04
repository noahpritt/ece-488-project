/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;


/**
 *
 * @author Team Neurun
 */
public class Grayscale {
    
    public static BufferedImage MakeImageGrayscale(BufferedImage input)
    {
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr   = input.getRGB(x, y); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                int average = (red + green + blue)/3;
                Color average_color = new Color(average, average, average);
                input.setRGB(x, y, average_color.getRGB());
             }
        }
        return input;
    } 

}
