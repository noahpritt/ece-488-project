/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javafx.scene.image.Image;


/**
 *
 * @author Team Neurun
 */
public class ColorHistogram {
    
    
    public static int[] GenerateColorHistogram(BufferedImage input)
    {
        int[] allBrightnesses = new int[101];
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr   = input.getRGB(x, y); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                 //Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                float hue = hsb[0];
                float saturation = hsb[1];
                float brightness = hsb[2];
                
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
    
    public static double[] GenerateProbabilityHistogram(BufferedImage input)
    {
        int[] allBrightnesses = new int[101];
        double [] allBrightnessProbabilities = new double[101];
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr   = input.getRGB(x, y); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                int average = (red + green + blue)/3;
                float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                float brightness = hsb[2];
                float roundedBrightness_float = (float) (Math.round(brightness * 100d) / 100d);
                int roundedBrightness = (int) (roundedBrightness_float * 100);

                allBrightnesses[roundedBrightness] = allBrightnesses[roundedBrightness] + 1;
             }
        }
        double totalSum = 0;
        for (int i = 0; i < allBrightnesses.length; i++)
        {
            totalSum += allBrightnesses[i];
        }
        
        for (int i = 0; i < allBrightnesses.length; i++)
        {
            allBrightnessProbabilities[i] = (double)(allBrightnesses[i]) / totalSum;
        }
        
        return allBrightnessProbabilities;
    } 
    
    public static double[] GenerateCumulativeProbabilityHistogram(BufferedImage input)
    {
        double[] allBrightnesses = new double[101];
        double [] allBrightnessProbabilities = new double[101];
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr   = input.getRGB(x, y); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                int average = (red + green + blue)/3;                
                
                float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                 //Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                float hue = hsb[0];
                float saturation = hsb[1];
                float brightness = hsb[2];
                // Round the brightness to the nearest 0.01
                float roundedBrightness_float = (float) (Math.round(brightness * 100d) / 100d);
                int roundedBrightness = (int) (roundedBrightness_float * 100);

                allBrightnesses[roundedBrightness] = allBrightnesses[roundedBrightness] + 1;
             }
        }
        int totalSum = 0;
        for (int i = 0; i < allBrightnesses.length; i++)
        {
            totalSum += allBrightnesses[i];
        }
        double current_probability = 0;
        for (int i = 0; i < allBrightnesses.length; i++)
        {
            current_probability += allBrightnesses[i] / totalSum;
            allBrightnessProbabilities[i] = current_probability;
        }
        
        return allBrightnessProbabilities;
    }
    
    public static BufferedImage AdjustContrast(BufferedImage input, int[] matrix_extries)
    {
        BufferedImage updated_contrast_image = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr   = input.getRGB(x, y); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                int average = (red + green + blue)/3;
                
                float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                 //Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                float hue = hsb[0];
                float saturation = hsb[1];
                float brightness = hsb[2];
               // hsb[2] = matrix_extries[]
                //System.out.println("x: " + x + ", y: " + y + ", value: " + matrix_extries[average] + ", original: " + average);
              //  int rgb = matrix_extries[average] * 0x00010101;
             //   updated_contrast_image.setRGB(x, y, rgb);
                //System.out.println("final: " + updated_contrast_image.getRGB(x, y));

             }
        }
        return updated_contrast_image;
    }
    
    // Compare BufferedImages.
    // Used for debugging.
    public static void Compare(BufferedImage input, BufferedImage input2)
    {
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr1   = input.getRGB(x, y); 
                int  red1   = (clr1 & 0x00ff0000) >> 16;
                int  green1 = (clr1 & 0x0000ff00) >> 8;
                int  blue1  =  clr1 & 0x000000ff;
                int average1 = (red1 + green1 + blue1)/3;
                
                int  clr2   = input2.getRGB(x, y); 
                int  red2   = (clr2 & 0x00ff0000) >> 16;
                int  green2 = (clr2 & 0x0000ff00) >> 8;
                int  blue2  =  clr2 & 0x000000ff;
                int average2 = (red2 + green2 + blue2)/3;
                
                System.out.println("intensity 1: " + average1 + "; intensity 2: " + average2);
             }
        }
    }
    
    
    
}
