/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import data.DataAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Mateusz
 */
public class Zajecia4 {

    public Image obrazek;

    public void matrixDialog() {
        obrazek = DataAccessor.imageView.getImage();
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Macierz filtru");
        if (false) {
            dialog.setHeaderText("Podaj parametry. Obraz zostanie przekonwertowany do skali szarości");
        } else {
            dialog.setHeaderText("Podaj parametry");
        }
        dialog.setHeaderText("Podaj wartości do macierzy filtru");
        GridPane grid = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TextField text1 = new TextField();
                text1.setText("0");
                grid.add(text1, i, j);
            }
        }
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        Optional<int[]> result = dialog.showAndWait();
        if (result.isPresent()) {
            int i = 0;
            int myMatrixFilter[][] = new int[3][3];
            for (Node node : grid.getChildren()) {
                if (node instanceof TextField) {
                    myMatrixFilter[i % 3][i / 3] = Integer.parseInt(((TextField) node).getText());
                    i++;
                }
            }
            applyFilter(myMatrixFilter);
        }
    }

    public void applyFilter(int[][] matrix) {
        int sumMask = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                sumMask = sumMask + matrix[i][j];
            }
        }
        obrazek = DataAccessor.imageView.getImage();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelReader pr = obrazek.getPixelReader();
        PixelWriter pw = wimage.getPixelWriter();
        for (int i = 1; i < obrazek.getWidth() - 1; i++) {
            for (int j = 1; j < obrazek.getHeight() - 1; j++) {
                int newPixel = pr.getArgb(i, j);
                int rsume = 0, gsume = 0, bsume = 0;
                int a2 = (newPixel >> 24) & 0xFF;
                for (int x = i - 1; x <= i + 1; x++) {
                    for (int y = j - 1; y <= j + 1; y++) {
                        int argb = pr.getArgb(x, y);
                        int red = (argb >> 16) & 0xFF;
                        int green = (argb >> 8) & 0xFF;
                        int blue = argb & 0xFF;
                        rsume += matrix[x - i + 1][y - j + 1] * red;
                        gsume += matrix[x - i + 1][y - j + 1] * green;
                        bsume += matrix[x - i + 1][y - j + 1] * blue;
                    }
                }
                if (sumMask != 0) {
                    rsume /= sumMask;
                    gsume /= sumMask;
                    bsume /= sumMask;
                }
                rsume = Math.max(0, Math.min(255, rsume));
                gsume = Math.max(0, Math.min(255, gsume));
                bsume = Math.max(0, Math.min(255, bsume));
                newPixel = (a2 << 24) + ((int) rsume << 16) + ((int) gsume << 8) + (int) bsume;
                pw.setArgb(i, j, newPixel);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }

    public void kuwaharFilter() {
        obrazek = DataAccessor.imageView.getImage();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelReader pr = obrazek.getPixelReader();
        PixelWriter pw = wimage.getPixelWriter();
        for (int i = 2; i < obrazek.getWidth() - 2; i++) {
            for (int j = 2; j < obrazek.getHeight() - 2; j++) {
                List<KuwaharArea> areas = new ArrayList<>(Arrays.asList(new KuwaharArea(i - 1, j - 1), new KuwaharArea(i + 1, j - 1), new KuwaharArea(i - 1, j + 1), new KuwaharArea(i + 1, j + 1)));
                for (KuwaharArea area : areas) {
                    area.setAvgRed(getAvg(area.startX, area.startY, 16));
                    area.setAvgGreen(getAvg(area.startX, area.startY, 8));
                    area.setAvgBlue(getAvg(area.startX, area.startY, 0));
                    area.setVarRed(getVar(area.startX, area.startY, area.getAvgRed(), 16));
                    area.setVarGreen(getVar(area.startX, area.startY, area.getAvgGreen(), 8));
                    area.setVarBlue(getVar(area.startX, area.startY, area.getAvgBlue(), 0));
                }
                double minRed = 255.0, minGreen = 255.0, minBlue = 255.0;
                CustomComparator comparing = new CustomComparator("RED");
                Collections.sort(areas, comparing);
                minRed = areas.get(0).getAvgRed();
                comparing = new CustomComparator("GREEN");
                Collections.sort(areas, comparing);
                minGreen = areas.get(0).getAvgGreen();
                comparing = new CustomComparator("BLUE");
                Collections.sort(areas, comparing);
                minBlue = areas.get(0).getAvgBlue();
                int newPixel = pr.getArgb(i, j);
                int a2 = (newPixel >> 24) & 0xFF;
                newPixel = (a2 << 24) + ((int) minRed << 16) + ((int) minGreen << 8) + (int) minBlue;
                pw.setArgb(i, j, newPixel);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }

    public int getAvg(int x, int y, int color) {
        int avg = 0;
        PixelReader pr = obrazek.getPixelReader();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                int newPixel = pr.getArgb(i, j);
                int theColor = (newPixel >> color) & 0xFF;
                avg += theColor;
            }
        }
        return (avg / 9);
    }

    public double getVar(int x, int y, int bright, int color) {
        double var = 0;
        PixelReader pr = obrazek.getPixelReader();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                int newPixel = pr.getArgb(i, j);
                int theColor = (newPixel >> color) & 0xFF;
                var += (bright - theColor) * (bright - theColor);
            }
        }
        return (var / 9);
    }

    public void medianaDialog() {
        obrazek = DataAccessor.imageView.getImage();
        List<Integer> choices = new ArrayList<>();
        choices.add(3);
        choices.add(5);
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(3, choices);
        dialog.setTitle("Wybór rozmiaru maski");
        dialog.setHeaderText("Rozmiar maski");
        dialog.setContentText("Wybierz rozmiar maski:");
        Optional<Integer> result = dialog.showAndWait();
        if (result.isPresent()){
            medianaFilter(result.get());
        }
    }

    public void medianaFilter(int mask) {
        obrazek = DataAccessor.imageView.getImage();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelReader pr = obrazek.getPixelReader();
        PixelWriter pw = wimage.getPixelWriter();
        for (int i = mask / 2; i < obrazek.getWidth() - mask / 2; i++) {
            for (int j = mask / 2; j < obrazek.getHeight() - mask / 2; j++) {
                int newPixel = pr.getArgb(i, j);
                int midR=getMiddle(i,j,mask,16);
                int midG=getMiddle(i,j,mask,8);
                int midB=getMiddle(i,j,mask,0);
                int a2 = (newPixel >> 24) & 0xFF;
                newPixel = (a2 << 24) + ((int) midR << 16) + ((int) midG << 8) + (int) midB;
                pw.setArgb(i, j, newPixel);
            }
        }
        DataAccessor.imageView.setImage(wimage);
        
    }

    public int getMiddle(int x, int y, int mask, int color) {
        PixelReader pr = obrazek.getPixelReader();
        List<Integer> pixels = new ArrayList<>();
        for (int i = x - mask / 2; i <= x + mask / 2; i++) {
            for (int j = y - mask / 2; j <= y + mask / 2; j++) {
                int newPixel = pr.getArgb(i, j);
                int theColor = (newPixel >> color) & 0xFF;
                pixels.add(theColor);
            }
        }
        Collections.sort(pixels);
        Collections.reverse(pixels);
        return pixels.get(pixels.size()/2);
    }
}
