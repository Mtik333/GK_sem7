/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mw_gk_projekt;

import data.DataAccessor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Mateusz
 */
public class MW_GK_projekt extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxmls/FXMLDocument.fxml"));

        Scene scene = new Scene(root);
        DataAccessor.setPrimaryStage(stage);
        stage.setScene(scene);
        stage.setTitle("Grafika komputerowa");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
