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
public class GrayscaleBilateralFilter {
    
    public static BufferedImage ApplyGrayscaleBilateralFilter(BufferedImage input, int size)
    {
        for (int y = size; y < input.getHeight() - size; y++) {
            for (int x = size; x < input.getWidth() - size; x++) {
                int intensitySum = 0;
                for(int i = x - size; i < x + size; i++)
                {
                    for(int j = y - size; j < y + size; i++)
                    {
                        int  clr   = input.getRGB(x, y); 
                        int  red   = (clr & 0x00ff0000) >> 16;
                        int  green = (clr & 0x0000ff00) >> 8;
                        int  blue  =  clr & 0x000000ff;
                        int average = (red + green + blue)/3;
                        intensitySum += average;   
                    }   
                }
                double numNeighbors = size * size;
                double intensitySumDouble = (double) intensitySum;
                double neighborAverage = intensitySumDouble / numNeighbors;
                int neighborAverageRounded = (int) Math.round(neighborAverage);
                Color average_color = new Color(neighborAverageRounded, neighborAverageRounded, neighborAverageRounded);
                input.setRGB(x, y, average_color.getRGB());
                }
            }
        return input;
    }
}
