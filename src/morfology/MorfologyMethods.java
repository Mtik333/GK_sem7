/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morfology;

import data.DataAccessor;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 *
 * @author Mateusz
 */
public class MorfologyMethods {

    public Image obrazek;
    public int width;
    public int height;
    public int countTrue = 0;
    public boolean isTheEnd = false;

    public void dilation() {
        obrazek = DataAccessor.imageView.getImage();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelReader pr = obrazek.getPixelReader();
        PixelWriter pw = wimage.getPixelWriter();
        width = (int) obrazek.getWidth();
        height = (int) obrazek.getHeight();
        int newPixel;
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pr.getArgb(i, j);
                int a = (argb >> 24) & 0xFF;
                int blue = argb & 0xFF;
                if (blue == 0) {
                    if (!neighBoursAreAllZeros(i, j, pr)) {
                        newPixel = 255;
                    } else {
                        newPixel = 0;
                    }
                } else {
                    newPixel = blue;
                }
                argb = (a << 24) + ((int) newPixel << 16) + ((int) newPixel << 8) + (int) newPixel;
                pw.setArgb(i, j, argb);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }

    private boolean neighBoursAreAllZeros(int i, int j, PixelReader pr) {
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (k > -1 && l > -1 && k < width && l < height && k != i && l != j) {
                    int argb = pr.getArgb(k, l);
                    int blue = argb & 0xFF;
                    if (blue == 255) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void erosion() {
        obrazek = DataAccessor.imageView.getImage();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelReader pr = obrazek.getPixelReader();
        PixelWriter pw = wimage.getPixelWriter();
        width = (int) obrazek.getWidth();
        height = (int) obrazek.getHeight();
        int newPixel;
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pr.getArgb(i, j);
                int a = (argb >> 24) & 0xFF;
                int blue = argb & 0xFF;
                if (blue == 255) {
                    if (!noZeroValuedNeighbours(i, j, pr)) {
                        newPixel = 0;
                    } else {
                        newPixel = 255;
                    }
                } else {
                    newPixel = blue;
                }
                argb = (a << 24) + ((int) newPixel << 16) + ((int) newPixel << 8) + (int) newPixel;
                pw.setArgb(i, j, argb);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }

    private boolean noZeroValuedNeighbours(int i, int j, PixelReader pr) {
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (k > -1 && l > -1 && k < width && l < height && k != i && l != j) {
                    int argb = pr.getArgb(k, l);
                    int blue = argb & 0xFF;
                    if (blue == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void opening() {
        erosion();
        dilation();
    }

    public void closing() {
        dilation();
        erosion();
    }

    public void thinning() {
        List<int[][]> matrixes = new ArrayList<>();
        countTrue = 0;
        matrixes.add(thin0DegOne);
        matrixes.add(thin0DegTwo);
        matrixes.add(thin90DegOne);
        matrixes.add(thin90DegTwo);
        matrixes.add(thin180DegOne);
        matrixes.add(thin180DegTwo);
        matrixes.add(thin270DegOne);
        matrixes.add(thin270DegTwo);
        for (int[][] matrix : matrixes) {
            obrazek = DataAccessor.imageView.getImage();
            WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
            PixelReader pr = obrazek.getPixelReader();
            PixelWriter pw = wimage.getPixelWriter();
            width = (int) obrazek.getWidth();
            height = (int) obrazek.getHeight();
            int newPixel = 0;
            for (int i = 0; i < obrazek.getWidth(); i++) {
                for (int j = 0; j < obrazek.getHeight(); j++) {
                    int argb = pr.getArgb(i, j);
                    int a = (argb >> 24) & 0xFF;
                    int blue = argb & 0xFF;
                    if (i == 0 || j == 0 || i == obrazek.getHeight() - 1 || j == obrazek.getWidth() - 1) {
                        newPixel = blue;
                        argb = (a << 24) + ((int) newPixel << 16) + ((int) newPixel << 8) + (int) newPixel;
                        pw.setArgb(i, j, argb);
                    } else {
                        if (hitOrMissFits(i, j, pr, matrix)) {
                            newPixel = 255;
                        } else {
                            newPixel = 0;
                        }
                        newPixel = substract(blue, newPixel);
                        argb = (a << 24) + ((int) newPixel << 16) + ((int) newPixel << 8) + (int) newPixel;
                        pw.setArgb(i, j, argb);
                    }

                }
            }
            DataAccessor.imageView.setImage(wimage);
        }
    }

    public void thickening() {
        List<int[][]> matrixes = new ArrayList<>();
        countTrue = 0;
        matrixes.add(thick0DegOne);
        matrixes.add(thick0DegTwo);
        matrixes.add(thick90DegOne);
        matrixes.add(thick90DegTwo);
        matrixes.add(thick180DegOne);
        matrixes.add(thick180DegTwo);
        matrixes.add(thick270DegOne);
        matrixes.add(thick270DegTwo);
        for (int[][] matrix : matrixes) {
            obrazek = DataAccessor.imageView.getImage();
            WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
            PixelReader pr = obrazek.getPixelReader();
            PixelWriter pw = wimage.getPixelWriter();
            width = (int) obrazek.getWidth();
            height = (int) obrazek.getHeight();
            int newPixel = 0;
            for (int i = 0; i < obrazek.getWidth(); i++) {
                for (int j = 0; j < obrazek.getHeight(); j++) {
                    int argb = pr.getArgb(i, j);
                    int a = (argb >> 24) & 0xFF;
                    int blue = argb & 0xFF;
                    if (i == 0 || j == 0 || i == obrazek.getHeight() - 1 || j == obrazek.getWidth() - 1) {
                        newPixel = blue;
                        argb = (a << 24) + ((int) newPixel << 16) + ((int) newPixel << 8) + (int) newPixel;
                        pw.setArgb(i, j, argb);
                    } else {
                        if (hitOrMissFits(i, j, pr, matrix)) {
                            newPixel = 255;
                        } else {
                            newPixel = 0;
                        }
                        newPixel = sum(blue, newPixel);
                        argb = (a << 24) + ((int) newPixel << 16) + ((int) newPixel << 8) + (int) newPixel;
                        pw.setArgb(i, j, argb);
                    }
                }
            }
            DataAccessor.imageView.setImage(wimage);
        }
    }

    private boolean hitOrMissFits(int i, int j, PixelReader pr, int[][] matrix) {
        int g = 0, h = 0;
        for (int k = i - 1; k <= i + 1; k++) {
            h = 0;
            for (int l = j - 1; l <= j + 1; l++) {
                if (k > -1 && l > -1 && k < width && l < height) {
                    int argb = pr.getArgb(k, l);
                    int blue = argb & 0xFF;
                    if (matrix[g][h] != 0) {
                        if ((matrix[g][h] == 1 && blue != 255) || (matrix[g][h] == -1 && blue != 0)) {
                            return false;
                        }
                    }
                }
                h++;
            }
            g++;
        }
        countTrue++;
        return true;
    }

    private int substract(int valueOrig, int valueThin) {
        if (valueOrig == 255 && valueThin == 0) {
            return 255;
        } else {
            return 0;
        }
    }

    private int sum(int valueOrig, int valueThin) {
        if (valueOrig == 0 && valueThin == 0) {
            return 0;
        } else {
            return 255;
        }
    }

    public void testThinning() {
        do {
            thinning();
        } while (countTrue != 0);
    }

    public void testThickening() {
        do {
            thickening();
        } while (countTrue != 0);
    }

    public int[][] thin0DegOne = new int[][]{
        {-1, -1, -1},
        {0, 1, 0},
        {1, 1, 1}
    };
    public int[][] thin0DegTwo = new int[][]{
        {0, -1, -1},
        {1, 1, -1},
        {0, 1, 0}
    };
    public int[][] thin90DegOne = new int[][]{
        {1, 0, -1},
        {1, 1, -1},
        {1, 0, -1}
    };
    public int[][] thin90DegTwo = new int[][]{
        {0, 1, 0},
        {1, 1, -1},
        {0, -1, -1}
    };
    public int[][] thin180DegOne = new int[][]{
        {1, 1, 1},
        {0, 1, 0},
        {-1, -1, -1}
    };
    public int[][] thin180DegTwo = new int[][]{
        {0, 1, 0},
        {-1, 1, 1},
        {-1, -1, 0}
    };
    public int[][] thin270DegOne = new int[][]{
        {-1, 0, 1},
        {-1, 1, 1},
        {-1, 0, 1}
    };
    public int[][] thin270DegTwo = new int[][]{
        {-1, -1, 0},
        {-1, 1, 1},
        {0, 1, 0}
    };

    public int[][] thick0DegOne = new int[][]{
        {1, 1, 0},
        {1, -1, 0},
        {1, 0, -1}
    };
    public int[][] thick0DegTwo = new int[][]{
        {0, 1, 1},
        {0, -1, 1},
        {-1, 0, 1}
    };
    public int[][] thick90DegOne = new int[][]{
        {1, 1, 1},
        {0, -1, 1},
        {-1, 0, 0}
    };
    public int[][] thick90DegTwo = new int[][]{
        {-1, 0, 0},
        {0, -1, 1},
        {1, 1, 1}
    };
    public int[][] thick180DegOne = new int[][]{
        {-1, 0, 1},
        {0, -1, 1},
        {0, 1, 1}
    };
    public int[][] thick180DegTwo = new int[][]{
        {1, 0, -1},
        {1, -1, 0},
        {1, 1, 0}
    };
    public int[][] thick270DegOne = new int[][]{
        {0, 0, -1},
        {1, -1, 0},
        {1, 1, 1}
    };
    public int[][] thick270DegTwo = new int[][]{
        {1, 1, 1},
        {1, -1, 0},
        {0, 0, -1}
    };
}
