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
        //System.out.println("lets go. size is " + size);
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                int intensitySum = 0;
                int numNeighbors = 0;
                int x_new = x;
                int y_new = y;
                if ( x - size < 0)
                {
                    x_new = size;
                }
                if ( x + size > input.getWidth())
                {
                    x_new = input.getWidth() - size;
                }
                if ( y - size < 0)
                {
                    y_new = size;
                }
                if ( y + size > input.getHeight())
                {
                    y_new = input.getHeight() - size;
                }
                for(int i = x_new - size; i < x_new + size; i++)
                {
                    for(int j = y_new - size; j < y_new + size; j++)
                    {
                       //System.out.println("x, y is " + x + ", " + y + ". i, j is " + i + ", " + j);
                        int  clr   = input.getRGB(i, j); 
                        int  red   = (clr & 0x00ff0000) >> 16;
                        int  green = (clr & 0x0000ff00) >> 8;
                        int  blue  =  clr & 0x000000ff;
                        int average = (red + green + blue)/3;
                        intensitySum += average; 
                        numNeighbors ++;
                    }   
                }
                double intensitySumDouble = (double) intensitySum;
                double neighborAverage = intensitySumDouble / numNeighbors;
                int neighborAverageRounded = (int) Math.round(neighborAverage);
                //System.out.println("total sum is " + intensitySum + ". total num is " + numNeighbors + ". double result is " + neighborAverage + ", int result is " + neighborAverageRounded);

                Color average_color = new Color(neighborAverageRounded, neighborAverageRounded, neighborAverageRounded);
                input.setRGB(x, y, average_color.getRGB());
                }
            }
        return input;
    }
}
