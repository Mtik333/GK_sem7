/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binarization;

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
public class BinariizationMethods {
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
        DataAccessor.setIsGray(true);
    }
    
    public void binaryzacjaManualDialog(){
        obrazek=DataAccessor.imageView.getImage();
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Binaryzacja manualna");
        if (!DataAccessor.isIsGray())
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
            int percent = Integer.parseInt(text1.getText());
            if (percent>=0 && percent <=100){
                percentBlackSelection(percent);
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
    
    public void percentBlackSelection(int percent){
        if (!DataAccessor.isIsGray()){
            naSzaro();
        }
        obrazek=DataAccessor.imageView.getImage();
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelReader pr = obrazek.getPixelReader();
        PixelWriter pw = wimage.getPixelWriter();
        
        DataAccessor.imageView.setImage(wimage);
    }
}
