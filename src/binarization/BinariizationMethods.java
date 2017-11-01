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
    
    public void percentBlackDialog(){
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
        int threshold=0;
        int realPercent = (int)(total*((double)percent/100));
        for (int i=0; i<256; i++){
            if (realPercent<=0){
                threshold=i;
                break;
            }
            else{
                realPercent=realPercent-hist[i];
            }
        }
        System.out.println(threshold);
        binaryzacjaManual(threshold);
    }
    
    public void binaryzacjaManual(int parametr){
        if (!DataAccessor.isIsGray()){
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
    
    public void meanIterativeSelection(){
        if (!DataAccessor.isIsGray()){
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
        boolean endOfIter=false;
        do{
            endOfIter = calculatingIterations(DataAccessor.getThresholdForIterations(), hist);
            System.out.println(DataAccessor.getThresholdForIterations());
        }while (!endOfIter);
        binaryzacjaManual(DataAccessor.getThresholdForIterations());
        
    }
    
    private boolean calculatingIterations(int threshold, int[] hist){
        double meanBackground=0;
        double meanObject=0;
        int totalBackground=0;
        int totalObject=0;
        for (int i=0; i<=threshold; i++){
            meanBackground=meanBackground+hist[i]*i;
            totalBackground+=hist[i];
        }
        meanBackground=meanBackground/totalBackground;
        for (int i=1+threshold; i<256; i++){
            meanObject=meanObject+hist[i]*i;
            totalObject+=hist[i];
        }
        meanObject=meanObject/totalObject;
        int newThreshold = (int)Math.abs(meanBackground+meanObject)/2;
        if (Math.abs(newThreshold-threshold)<=3){
            return true;
        }
        else {
            DataAccessor.setThresholdForIterations(newThreshold);
            return false;
        }
    }
    
    public void entropySelection(){
        if (!DataAccessor.isIsGray()){
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
        boolean endOfEntropy=false;
        do{
            endOfEntropy = calculateEntropy(DataAccessor.getThresholdForIterations(), total, hist);
        }while (!endOfEntropy);
        System.out.println(DataAccessor.getThresholdForIterations());
        binaryzacjaManual(DataAccessor.getThresholdForIterations());
//        
//        int totalBackground=0;
//        int threshold=128;
//        for (int i=0; i<=threshold; i++){
//            totalBackground+=hist[i];
//        }
//        double probBackground=(double)totalBackground/(double)total;
//        double probObject=1-probBackground;
//        double logarithmBackground = (Math.log(probBackground) / Math.log(2));
//        double logarithmObject = (Math.log(probObject) / Math.log(2));
//        double entropy=(-1)*probObject*logarithmObject+(-1)*probBackground*logarithmBackground;
//        System.out.println(entropy);
        
    }
    
    private boolean calculateEntropy(int threshold, int total, int[] hist){
        int totalBackground=0;
        for (int i=0; i<=threshold; i++){
            totalBackground+=hist[i];
        }
        double probBackground=(double)totalBackground/(double)total;
        if (Math.abs(probBackground-0.5)<0.025){
            return true;
        }
        else {
            if (probBackground<0.5)
                DataAccessor.setThresholdForIterations(threshold+1);
            else DataAccessor.setThresholdForIterations(threshold-1);
            return false;
        }
    }
}
