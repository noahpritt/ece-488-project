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
public class GrayscaleHistogram {
    
    public static int[] GenerateHistogram(BufferedImage input)
    {
        int[] allIntensities = new int[256];
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr   = input.getRGB(x, y); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                int average = (red + green + blue)/3;
                allIntensities[average] =  allIntensities[average] + 1;
             }
        }
        return allIntensities;
    } 
    
    public static double[] GenerateProbabilityHistogram(BufferedImage input)
    {
        double[] allIntensities = new double[256];
        double [] allIntensityProbabilities = new double[256];
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr   = input.getRGB(x, y); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                int average = (red + green + blue)/3;
                allIntensities[average] =  allIntensities[average] + 1;
             }
        }
        int totalSum = 0;
        for (int i = 0; i < allIntensities.length; i++)
        {
            totalSum += allIntensities[i];
        }
        
        for (int i = 0; i < allIntensities.length; i++)
        {
            allIntensityProbabilities[i] = allIntensities[i] / totalSum;
        }
        
        return allIntensityProbabilities;
    } 
    
    public static double[] GenerateCumulativeProbabilityHistogram(BufferedImage input)
    {
        double[] allIntensities = new double[256];
        double [] allIntensityProbabilities = new double[256];
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int  clr   = input.getRGB(x, y); 
                int  red   = (clr & 0x00ff0000) >> 16;
                int  green = (clr & 0x0000ff00) >> 8;
                int  blue  =  clr & 0x000000ff;
                int average = (red + green + blue)/3;
                allIntensities[average] =  allIntensities[average] + 1;
             }
        }
        int totalSum = 0;
        for (int i = 0; i < allIntensities.length; i++)
        {
            totalSum += allIntensities[i];
        }
        double current_probability = 0;
        for (int i = 0; i < allIntensities.length; i++)
        {
            current_probability += allIntensities[i] / totalSum;
            allIntensityProbabilities[i] = current_probability;
        }
        
        return allIntensityProbabilities;
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
                //System.out.println("x: " + x + ", y: " + y + ", value: " + matrix_extries[average] + ", original: " + average);
                int rgb = matrix_extries[average] * 0x00010101;
                updated_contrast_image.setRGB(x, y, rgb);
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
