/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import data.DataAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Mateusz
 */
public class Zajecia2 {
    //Wyrównanie i rozciąganie: http://ics.p.lodz.pl/~adamwoj/WSFI/PO/2_wyklad_PO.pdf
    //Większość algorytmów: http://www.algorytm.org/przetwarzanie-obrazow/
    
    public int tabred[] = new int[256];
    public int tabgreen[] = new int[256];
    public int tabblue[] = new int[256];
    public int tabgrey[] = new int[256];
    public int[] lut = new int[256];
    public int[] lutr = new int[256];
    public int[] lutg = new int[256];
    public int[] lutb = new int[256];
    public Image obrazek;
    
    public void brightnessDialog(){
        obrazek=DataAccessor.imageView.getImage();
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Zmiana jasnosci obrazka");
        dialog.setHeaderText("Zmieniaj jasnosc (funkcja kwadratowa)");
        Button button1=new Button("Ciemniej");
        Button button2=new Button("Jasniej");
        GridPane grid = new GridPane();
        grid.add(button1,1,1);
        grid.add(button2,1,2);
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().setContent(grid);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                obrazek=DataAccessor.imageView.getImage();
                darkenUp(2);
            }
        });
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                obrazek=DataAccessor.imageView.getImage();
                brightenUp(2);
            }
        });
        dialog.show();
    }
    
    public void darkenUpDialog(){
        obrazek=DataAccessor.imageView.getImage();
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Zaciemnianie obrazka");
        dialog.setHeaderText("Podaj wartość współczynnika (funkcja kwadratowa)");
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
                darkenUp(Integer.parseInt(text1.getText()));
            }
            else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Błąd wartości");
                alert.setContentText("Wpisano niewłaściwe wartości");
                alert.showAndWait();
            }
        }
    }
    
    public void darkenUp(int wykladnik){
        lut = new int[256];
        int red = 0;
        int green = 0;
        int blue = 0;
        PixelReader pixelReader = obrazek.getPixelReader();
        for (double i = 0; i < 256; i++) {
            if ((int)(Math.pow(255+Math.E,i/255)-Math.E)>255){
                lut[(int)i]=255;
            }
            else if((int)(Math.pow(255+Math.E,i/255)-Math.E)<0){
                lut[(int)i]=0;
            }
            else {
                lut[(int)i]=(int)(Math.pow(255+Math.E,i/255)-Math.E);
            }
            /*
            if ((int)(255*Math.pow(i/255,Math.E))>255){
                lut[(int)i]=255;
            }
            else if ((int)(255*Math.pow(i/255,Math.E))<0){
                lut[(int)i]=0;
            }
            else{
                lut[(int)i]=(int)(255*Math.pow(i/255,Math.E));
            }
            */
            
        }
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelWriter pw = wimage.getPixelWriter();
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pixelReader.getArgb(i, j);
                int a = (argb >> 24) & 0xFF;
                red = (argb >> 16) & 0xFF;
                green = (argb >> 8) & 0xFF;
                blue = argb & 0xFF;
                argb = (a << 24) + ((int) lut[red] << 16) + ((int) lut[green] << 8) + (int) lut[blue];
                pw.setArgb(i, j, argb);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }
    
    public void brightenUpDialog(){
        obrazek=DataAccessor.imageView.getImage();
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Rozjaśnienie obrazka");
        dialog.setHeaderText("Podaj wartość współczynnika (funkcja logarytmiczna)");
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
                brightenUp(Integer.parseInt(text1.getText()));
            }
            else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Błąd wartości");
                alert.setContentText("Wpisano niewłaściwe wartości");
                alert.showAndWait();
            }
        }
    }
    
    public void brightenUp(int wspolczynnik){
        lut = new int[256];
        int red = 0;
        int green = 0;
        int blue = 0;
        PixelReader pixelReader = obrazek.getPixelReader();
        for (double i = 0; i < 256; i++) {
            if ((int)(255*Math.log(Math.E+i)/Math.log(Math.E+255))>255){
                lut[(int)i]=255;
            }
            else if ((int)(255*Math.log(Math.E+i)/Math.log(Math.E+255))<0){
                lut[(int)i]=0;
            }
            else{
                lut[(int)i]=(int)(255*Math.log(Math.E+i)/Math.log(Math.E+255));
            }
        }
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelWriter pw = wimage.getPixelWriter();
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pixelReader.getArgb(i, j);
                int a = (argb >> 24) & 0xFF;
                red = (argb >> 16) & 0xFF;
                green = (argb >> 8) & 0xFF;
                blue = argb & 0xFF;
                argb = (a << 24) + ((int) lut[red] << 16) + ((int) lut[green] << 8) + (int) lut[blue];
                pw.setArgb(i, j, argb);
            }
        }
        DataAccessor.imageView.setImage(wimage);
    }
    public void showHistogramDialog(){
        List<String> choices = new ArrayList<>();
        choices.add("Czerwony");
        choices.add("Zielony");
        choices.add("Niebieski");
        choices.add("Uśredniony");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Czerwony", choices);
        dialog.setTitle("Histogram");
        dialog.setHeaderText("Wybierz kolor histogramu");
        dialog.setContentText("Wybierz kolor histogramu:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            showHistogram2(result.get());
        }
    }
    
    public void showHistogram2(String color){
        obrazek=DataAccessor.imageView.getImage();
        Stage stage = new Stage();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> barChart = 
            new BarChart<>(xAxis,yAxis);
        barChart.setCategoryGap(0);
        barChart.setBarGap(0);
        xAxis.setLabel("Skala");
        XYChart.Series series1 = new XYChart.Series();
        switch(color){
            case "Czerwony":
                series1.setName("Czerwone");
                break;
            case "Niebieski":
                series1.setName("Niebieskie");
                break;
            case "Zielony":
                series1.setName("Zielone");
                break;
            case "Uśredniony":
                series1.setName("Uśrednione");
                break;
        }
        yAxis.setLabel("Ilość pikseli");
        series1.setName("Czerwone");
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Zielone");
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Niebieskie");
        XYChart.Series series4 = new XYChart.Series();
        series4.setName("Uśrednione");
        int r = 0;
        int g = 0;
        int b = 0;
        tabred = new int[256];
        tabgreen = new int[256];
        tabblue = new int[256];
        tabgrey = new int[256];
        PixelReader pixelReader = obrazek.getPixelReader();
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                Color kolo = pixelReader.getColor(i, j);
                int argb = pixelReader.getArgb(i, j);
                r = (argb >> 16) & 0xFF;
                g = (argb >> 8) & 0xFF;
                b = argb  & 0xFF;
                tabred[r]++;
                tabgreen[g]++;
                tabblue[b]++;
            }
        }
        for (int i=0; i<256; i++){
            switch(color){
            case "Czerwony":
                series1.getData().add(new XYChart.Data(""+i,tabred[i]));
                barChart.setStyle("CHART_COLOR_1: #FF0000;");
                break;
            case "Niebieski":
                series1.getData().add(new XYChart.Data(""+i,tabblue[i]));
                barChart.setStyle("CHART_COLOR_1: #0000FF;");
                break;
            case "Zielony":
                series1.getData().add(new XYChart.Data(""+i,tabgreen[i]));
                barChart.setStyle("CHART_COLOR_1: #00FF00;");
                break;
            case "Uśredniony":
                int szare=(tabred[i]+tabgreen[i]+tabblue[i])/3;
                series1.getData().add(new XYChart.Data(""+i,szare));
                barChart.setStyle("CHART_COLOR_1: #000000;");
                break;
            }
        }
        barChart.getData().addAll(series1);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(barChart);
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
        Scene scene = new Scene(root, 800, 450);
        stage.setScene(scene);
        stage.show();
    }
    
    public void stretchHistogramDialog(){
        obrazek=DataAccessor.imageView.getImage();
        Dialog<int[]> dialog = new Dialog<>();
        dialog.setTitle("Rozciągnięcie obrazka");
        dialog.setHeaderText("Podaj wartości odcinające");
        Label label1 = new Label("Wartość A piksela: ");
	Label label2 = new Label("Wartość B piksela: ");
        TextField text1 = new TextField();
	TextField text2 = new TextField();
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
            if (Integer.parseInt(text1.getText())>=0 && Integer.parseInt(text2.getText())>=0 && Integer.parseInt(text1.getText())<=255 && Integer.parseInt(text2.getText())<=255){
                int[] parameters= {Integer.parseInt(text1.getText()), Integer.parseInt(text2.getText())};
                if (parameters[0]>parameters[1]){
                    int c=parameters[0];
                    parameters[0]=parameters[1];
                    parameters[1]=c;
                }
                stretchHistogram(parameters);
            }
            else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Błąd wartości");
                alert.setContentText("Wpisano niewłaściwe wartości");
                alert.showAndWait();
            }
        }
    }
    public void stretchHistogram(int[] parameters){
        PixelReader pixelReader = obrazek.getPixelReader();
        int red = 0;
        int green = 0;
        int blue = 0;
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelWriter pw = wimage.getPixelWriter();
        for (int i = 0; i < 256; i++) {
            lut[i] = (int)(255 / (parameters[1] - parameters[0])) * (i - parameters[0]);
            if (lut[i] < 0) {
                lut[i] = 0;
            }
            if (lut[i] > 255) {
                lut[i] = 255;
            }
        }
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pixelReader.getArgb(i, j);
                int a = (argb >> 24) & 0xFF;
                red = (argb >> 16) & 0xFF;
                green = (argb >> 8) & 0xFF;
                blue = argb & 0xFF;
                if (red > 255) {
                    red = 255;
                }
                if (green > 255) {
                    green = 255;
                }
                if (blue > 255) {
                    blue = 255;
                }
                if (red < 0) {
                    red = 0;
                }
                if (green < 0) {
                    green = 0;
                }
                if (blue < 0) {
                    blue = 0;
                }
                argb = (a << 24) + ((int) lut[red] << 16) + ((int) lut[green] << 8) + (int) lut[blue];
                pw.setArgb(i, j, argb);

            }
        }
        DataAccessor.imageView.setImage((Image) wimage);
    }
    
    public void equalizeHistogram(){
        obrazek=DataAccessor.imageView.getImage();
        int red = 0;
        int green = 0;
        int blue = 0;
        PixelReader pixelReader = obrazek.getPixelReader();
        tabred = new int[256];
        tabgreen = new int[256];
        tabblue = new int[256];
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                Color color = pixelReader.getColor(i, j);
                red = (int) (color.getRed() * 255);
                green = (int) (color.getGreen() * 255);
                blue = (int) (color.getBlue() * 255);
                tabred[red]++;
                tabgreen[green]++;
                tabblue[blue]++;
            }
        }
        int rozmiar = (int) (obrazek.getWidth() * obrazek.getHeight());
        int[] hr = tabred;
        int[] hg = tabgreen;
        int[] hb = tabblue;
        for (int i = 1; i < 256; i++) {
            hr[i] = hr[i] + hr[i - 1];
            hg[i] = hg[i] + hg[i - 1];
            hb[i] = hb[i] + hb[i - 1];
        }
        lutr = new int[256];
        lutg = new int[256];
        lutb = new int[256];
        for (int i = 0; i < 256; i++) {
            lutr[i] = (int)(255 * hr[i] / rozmiar);
            lutg[i] = (int)(255 * hg[i] / rozmiar);
            lutb[i] = (int)(255 * hb[i] / rozmiar);
        }
        WritableImage wimage = new WritableImage((int) obrazek.getWidth(), (int) obrazek.getHeight());
        PixelWriter pw = wimage.getPixelWriter();
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                int argb = pixelReader.getArgb(i, j);
                int a = (argb >> 24) & 0xFF;
                red = (argb >> 16) & 0xFF;
                green = (argb >> 8) & 0xFF;
                blue = argb & 0xFF;
                argb = (a << 24) + ((int) lutr[red] << 16) + ((int) lutg[green] << 8) + (int) lutb[blue];
                pw.setArgb(i, j, argb);
            }
        }
        DataAccessor.imageView.setImage(wimage);
        
    }
    //funkcja nieużywana
    public void showHistogram(){
        obrazek=DataAccessor.imageView.getImage();
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        Label redget = new Label();
        Label greenget = new Label();
        Label blueget = new Label();
        Rectangle polekolor = new Rectangle();
        Label hisred = new Label("Histogram koloru czerwonego");
        Label hisgreen = new Label("Histogram koloru zielonego");
        Label hisblue = new Label("Histogram koloru niebieskiego");
        Label hisgray = new Label("Histogram wartości uśrednionych");
        stage.setScene(new Scene(grid));
        stage.setTitle("Histogram");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        int r = 0;
        int g = 0;
        int b = 0;
        tabred = new int[256];
        tabgreen = new int[256];
        tabblue = new int[256];
        tabgrey = new int[256];
        PixelReader pixelReader = obrazek.getPixelReader();
        Canvas red = new Canvas(512, 200);
        Canvas green = new Canvas(512, 200);
        Canvas blue = new Canvas(512, 200);
        Canvas gray = new Canvas(512, 200);
        GraphicsContext gr = red.getGraphicsContext2D();
        GraphicsContext gg = green.getGraphicsContext2D();
        GraphicsContext gb = blue.getGraphicsContext2D();
        GraphicsContext ggr = gray.getGraphicsContext2D();
        gr.setFill(Color.RED);
        gg.setFill(Color.GREEN);
        gb.setFill(Color.BLUE);
        for (int i = 0; i < obrazek.getWidth(); i++) {
            for (int j = 0; j < obrazek.getHeight(); j++) {
                Color kolo = pixelReader.getColor(i, j);
                int argb = pixelReader.getArgb(i, j);
                r = (argb >> 16) & 0xFF;
                g = (argb >> 8) & 0xFF;
                b = argb  & 0xFF;
                tabred[r]++;
                tabgreen[g]++;
                tabblue[b]++;
            }
        }
        for (int i = 0; i < 256; i++) {
            tabgrey[i] = (tabred[i] + tabgreen[i] + tabblue[i]) / 3;
        }
        int maxr = 0, maxg = 0, maxb = 0;
        for (int i = 1; i < 255; i++) {
            if (tabred[i] > maxr) {
                maxr = tabred[i];
            }
            if (tabgreen[i] > maxg) {
                maxg = tabgreen[i];
            }
            if (tabblue[i] > maxb) {
                maxb = tabblue[i];
            }
        }
        double min = Math.max(maxr, Math.max(maxg, maxb));
        for (int i = 1; i < 255; i++) {
            int h = 200 * tabred[i] / (int) maxr;
            gr.fillRect(i * 2, 200 - h, 2, h);
        }
        for (int i = 1; i < 255; i++) {
            int h = 200 * tabgreen[i] / (int) maxg;
            gg.fillRect(i * 2, 200 - h, 2, h);
        }
        for (int i = 1; i < 255; i++) {
            int h = 200 * tabblue[i] / (int) maxb;
            gb.fillRect(i * 2, 200 - h, 2, h);
        }
        for (int i = 1; i < 255; i++) {
            int h = 200 * tabgrey[i] / (int) min;
            ggr.fillRect(i * 2, 200 - h, 2, h);
        }
        grid.add(hisred, 0, 0);
        grid.add(hisgreen, 1, 0);
        grid.add(hisblue, 0, 2);
        grid.add(hisgray, 1, 2);
        grid.add(red, 0, 1);
        grid.add(green, 1, 1);
        grid.add(blue, 0, 3);
        grid.add(gray, 1, 3);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.showAndWait();
    }
}
