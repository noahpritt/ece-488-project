/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Team Neurun
 */
public class BilateralFilter {
    
    public static int[] ApplyFilter(BufferedImage input)
    {
        int[] allBrightnesses = new int[101];
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr   = input.getRGB(x, y); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                float hue = hsb[0];
                float saturation = hsb[1];
                float brightness = hsb[2];
                
                // Average the 
                
                
                
                // Round the brightness to the nearest 0.01
                float roundedBrightness_float = (float) (Math.round(brightness * 100d) / 100d);
                int roundedBrightness = (int) (roundedBrightness_float * 100);
                //System.out.println("brightness is " + hsb[2]);
                //System.out.println("brightness is " + roundedBrightness);
                allBrightnesses[roundedBrightness] = allBrightnesses[roundedBrightness] + 1;
                
             }
        }
        return allBrightnesses;
    
}
