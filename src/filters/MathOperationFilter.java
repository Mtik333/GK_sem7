/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import data.DataAccessor;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 *
 * @author Mateusz
 */
public class MathOperationFilter {
    
    public Image obrazek;
    
    public void mathOperation(){
        obrazek = DataAccessor.imageView.getImage();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelReader pr = obrazek.getPixelReader();
        PixelWriter pw = wimage.getPixelWriter();
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int newPixel = pr.getArgb(i, j);
                int a2 = (newPixel >> 24) & 0xFF;
                int red = (newPixel >> 16) & 0xFF;
                int green = (newPixel >> 8) & 0xFF;
                int blue = newPixel & 0xFF;
                switch(DataAccessor.getMathOperation()){
                    case "Sum":
                        red = red+DataAccessor.rgbValues.get("r");
                        green = green+DataAccessor.rgbValues.get("g");
                        blue = blue+DataAccessor.rgbValues.get("b");
                        break;
                    case "Subtract":
                        red = red-DataAccessor.rgbValues.get("r");
                        green = green-DataAccessor.rgbValues.get("g");
                        blue = blue-DataAccessor.rgbValues.get("b");
                        break;
                    case "Multiply":
                        red = red*DataAccessor.rgbValues.get("r");
                        green = green*DataAccessor.rgbValues.get("g");
                        blue = blue*DataAccessor.rgbValues.get("b");
                        break;
                    case "Divide":
                        red = red/DataAccessor.rgbValues.get("r");
                        green = green/DataAccessor.rgbValues.get("g");
                        blue = blue/DataAccessor.rgbValues.get("b");
                        break;
                }
                red = red < 0 ? 0 : red;
                green = green < 0 ? 0 : green;
                blue = blue < 0 ? 0 : blue;
                red = red > 255 ? 255 : red;
                green = green > 255 ? 255 : green;
                blue = blue > 255 ? 255 : blue;
                newPixel = (a2 << 24) + ((int) red << 16) + ((int) green << 8) + (int) blue;
                pw.setArgb(i, j, newPixel);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }
}
