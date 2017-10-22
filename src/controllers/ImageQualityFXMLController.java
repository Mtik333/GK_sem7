/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataAccessor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mateusz
 */
public class ImageQualityFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    public ComboBox qualityChoose;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        qualityChoose.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        qualityChoose.getSelectionModel().select(0);
    }

    @FXML
    public void setQuality(ActionEvent event) {
        int value = Integer.parseInt((String) qualityChoose.getSelectionModel().getSelectedItem());
        float myValue = ((float) value / 10);
        DataAccessor.setJpegQuality(myValue);
        Stage stage = (Stage) qualityChoose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void dismiss(ActionEvent event) {
        Stage stage = (Stage) qualityChoose.getScene().getWindow();
        stage.close();
    }
}
