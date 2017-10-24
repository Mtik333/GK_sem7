/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import data.DataAccessor;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
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
public class Zajecia3 {
    public Image obrazek;
    
    public void naSzaro() {
        obrazek=DataAccessor.imageView.getImage();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelReader pr = obrazek.getPixelReader();
        PixelWriter pw = wimage.getPixelWriter();
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pr.getArgb(i, j);
                int a = (argb >> 24) & 0xFF;
                int red = (argb >> 16) & 0xFF;
                int green = (argb >> 8) & 0xFF;
                int blue = argb & 0xFF;
                int gray = (int) (2*red+5*green+1*blue)/8;
                //int gray = (red + green + blue) / 3;
                argb = (a << 24) + (gray << 16) + (gray << 8) + gray;
                pw.setArgb(i, j, argb);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }
    public void binaryzacjaManualDialog(){
        obrazek=DataAccessor.imageView.getImage();
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Binaryzacja manualna");
        if (false)
            dialog.setHeaderText("Podaj wartość progową. Obraz zostanie przekonwertowany do skali szarości");
        else dialog.setHeaderText("Podaj wartość progową");
        Label label1 = new Label("Wartość: ");
        TextField text1 = new TextField();
        GridPane grid = new GridPane();
        grid.add(label1, 1, 2);
        grid.add(text1, 2, 2);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        Optional<int[]> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (Integer.parseInt(text1.getText())>=0){
                binaryzacjaManual(Integer.parseInt(text1.getText()));
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Błąd wartości");
                alert.setContentText("Wpisano niewłaściwe wartości");
                alert.showAndWait();
            }
        }
    }
    public void binaryzacjaManual(int parametr){
        if (false){
            naSzaro();
        }
        obrazek=DataAccessor.imageView.getImage();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelReader pr = obrazek.getPixelReader();
        PixelWriter pw = wimage.getPixelWriter();
        int newPixel;
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pr.getArgb(i, j);
                int a = (argb >> 24) & 0xFF;
                int blue = argb & 0xFF;
                if (blue > parametr) {
                    newPixel = 255;
                } else {
                    newPixel = 0;
                }
                argb = (a << 24) + ((int) newPixel << 16) + ((int) newPixel << 8) + (int) newPixel;
                pw.setArgb(i, j, argb);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }
    public void niblackDialog(){
        obrazek=DataAccessor.imageView.getImage();
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Metoda Niblacka");
        if (false)
            dialog.setHeaderText("Podaj parametry. Obraz zostanie przekonwertowany do skali szarości");
        else dialog.setHeaderText("Podaj parametry");
        Label label1 = new Label("Rozmiar okna: ");
	Label label2 = new Label("Parametr progowania: ");
        TextField text1 = new TextField();
	TextField text2 = new TextField();
        text1.setText("0");
        text2.setText("0.0");
        GridPane grid = new GridPane();
        grid.add(label1, 1, 2);
        grid.add(text1, 2, 2);
        grid.add(label2, 1, 3);
        grid.add(text2, 2, 3);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        Optional<int[]> result = dialog.showAndWait();
        if (result.isPresent()) {
            int bok=Integer.parseInt(text1.getText());
            double wspolczynnik=Double.parseDouble(text2.getText());
            binaryzacjaNiblack(bok, wspolczynnik);
            /*
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Błąd wartości");
                alert.setContentText("Wpisano niewłaściwe wartości");
                alert.showAndWait();
            }
            */
        }
    }
    public void binaryzacjaNiblack(int bok, double wspolczynnik){
        if (false){
            naSzaro();
        }
        obrazek=DataAccessor.imageView.getImage();
        PixelReader pr = obrazek.getPixelReader();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelWriter pw = wimage.getPixelWriter();
        bok=bok/2;
        double prog;
        int width = (int) obrazek.getWidth();
        int height = (int) obrazek.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double suma = 0;
                int ile = 0;
                //petla do zliczenia sumy i ilosci pikseli w oknie
                for (int x = i - bok; x < i + bok; x++) {
                    if (x >= 0 && x < width) {
                        for (int y = j - bok; y <= j + bok; y++) {
                            if (y >= 0 && y < height) {
                                suma += pr.getArgb(x, y) & 0xFF;
                                ile++;
                            }
                        }
                    }
                }
                double srednia = suma / ile;
                double wariancja = 0;
                //petla do zliczania wariancji w oknie
                for (int x = i - bok; x < i + bok; x++) {
                    if (x >= 0 && x < width) {
                        for (int y = j - bok; y <= j + bok; y++) {
                            if (y >= 0 && y < height) {
                                wariancja += ((pr.getArgb(x, y) & 0xFF) - srednia) * ((pr.getArgb(x, y) & 0xFF) - srednia);
                            }
                        }
                    }
                }
                wariancja /= ile;
                wariancja = Math.sqrt(wariancja);
                prog = srednia + (wspolczynnik * wariancja);
                int argb = pr.getArgb(i, j);
                int alpha = (argb >> 24) & 0xFF;
                int color = argb & 0xFF;
                if (color >= prog) {
                    color = 255;
                }
                if (color < prog) {
                    color = 0;
                }
                argb = (alpha << 24) + (color << 16) + (color << 8) + color;
                pw.setArgb(i, j, argb);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }
    public void binaryzacjaOtsu(){
        if (false){
            naSzaro();
        }
        obrazek=DataAccessor.imageView.getImage();
        int hist[] = new int[256];
        int total = (int) obrazek.getWidth() * (int) obrazek.getHeight();
        PixelReader pr = obrazek.getPixelReader();
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pr.getArgb(i, j);
                int blue = argb & 0xFF;
                hist[blue]++;
            }
        }
        float sum = 0;
        for (int t = 0; t < 256; t++) {
            sum += t * hist[t]; //zliczanie sumy bez prawdopodobienstwa
        }
        float sumaTla = 0; //suma tla, zliczana krok po kroku
        int wagaTlo = 0;
        int wagaObiektu = 0;
        float wariancjaMax = 0;
        int progOptymalny = 0;
        for (int t = 0; t < 256; t++) {
            wagaTlo += hist[t];               // liczymy wage klasy tla
            if (wagaTlo == 0) {
                continue;
            }
            wagaObiektu = total - wagaTlo;      // liczymy wage klasy obiektu, odejmujac od ilosci pikseli tyle, ile nalezy do tla
            if (wagaObiektu == 0) {
                break;
            }
            sumaTla += (float) (t * hist[t]);
            float sredniaTlo = sumaTla / wagaTlo;            // srednia tla
            float sredniaObiekt = (sum - sumaTla) / wagaObiektu;    // srednia obiektu
            // liczenie wariancji miedzyklasowej
            float wariancjaTeraz = (float) wagaTlo * (float) wagaObiektu * (sredniaTlo - sredniaObiekt) * (sredniaTlo - sredniaObiekt);
            // czy obliczona wariancja jest wieksza od maksymalnej dotychczas
            if (wariancjaTeraz > wariancjaMax) {
                wariancjaMax = wariancjaTeraz;
                progOptymalny = t;
            }
        }
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelWriter pw = wimage.getPixelWriter();
        int newPixel;
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pr.getArgb(i, j);
                int a = (argb >> 24) & 0xFF;
                int color = argb & 0xFF;
                if (color > progOptymalny) {
                    newPixel = 255;
                } else {
                    newPixel = 0;
                }
                argb = (a << 24) + ((int) newPixel << 16) + ((int) newPixel << 8) + (int) newPixel;
                pw.setArgb(i, j, argb);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }
}
