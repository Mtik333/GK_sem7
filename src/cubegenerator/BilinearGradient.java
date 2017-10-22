/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubegenerator;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author Mateusz
 */
public class BilinearGradient {

    private final Color COLOR_UP_LEFT; // upper left corner
    private final Color COLOR_UP_RIGHT; // upper right corner
    private final Color COLOR_DN_LEFT; // lower left corner
    private final Color COLOR_DN_RIGHT; // lower right corner

    public BilinearGradient(Color c00, Color c10, Color c01, Color c11) {
        COLOR_UP_LEFT = c00;
        COLOR_UP_RIGHT = c10;
        COLOR_DN_LEFT = c01;
        COLOR_DN_RIGHT = c11;
    }

    private Color fromColorToColor(Color c1, Color c2, double frac) {
        double red = c1.getRed() + (c2.getRed() - c1.getRed()) * frac;
        double green = c1.getGreen() + (c2.getGreen() - c1.getGreen()) * frac;
        double blue = c1.getBlue() + (c2.getBlue() - c1.getBlue()) * frac;
        double alpha = c1.getOpacity() + (c2.getOpacity() - c1.getOpacity()) * frac;
        red = red < 0 ? 0 : (red > 1 ? 1 : red);
        green = green < 0 ? 0 : (green > 1 ? 1 : green);
        blue = blue < 0 ? 0 : (blue > 1 ? 1 : blue);
        alpha = alpha < 0 ? 0 : (alpha > 1 ? 1 : alpha);
        return Color.color(red, green, blue, alpha);
    }

    private Color bilinearGradientColors(Color c00, Color c10, Color c01,
            Color c11, double fracX, double fracY) {
        Color interpolatedColX1 = fromColorToColor(c00, c10, fracX);
        Color interpolatedColX2 = fromColorToColor(c01, c11, fracX);
        return fromColorToColor(interpolatedColX1, interpolatedColX2, fracY);
    }

    public Image getImage(double w, double h) {
        int width = (int) w <= 0 ? 100 : (int) w;
        int height = (int) h <= 0 ? 100 : (int) h;
        double fractionX = 0;
        double fractionY = 0;
        WritableImage raster = new WritableImage(width, height);
        PixelWriter pixelWriter = raster.getPixelWriter();
        double fractionStepX = 1.0 / (w - 1);
        double fractionStepY = 1.0 / (h - 1);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixelWriter.setColor(x, y,
                        bilinearGradientColors(COLOR_UP_LEFT, COLOR_UP_RIGHT,
                                COLOR_DN_LEFT, COLOR_DN_RIGHT,
                                fractionX, fractionY));
                fractionX += fractionStepX;
                fractionX = fractionX > 1 ? 1 : fractionX;
            }
            fractionY += fractionStepY;
            fractionY = fractionY > 1 ? 1 : fractionY;
            fractionX = 0;
        }
        return raster;
    }

}
