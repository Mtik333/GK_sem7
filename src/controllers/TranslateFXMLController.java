/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataAccessor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mateusz
 */
public class TranslateFXMLController implements Initializable {

    @FXML
    public ComboBox chosenPrimitive;
    @FXML
    public TextField xCoordText;
    @FXML
    public TextField yCoordText;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        DataAccessor.getMyPolygons().forEach((polygon) -> {
            chosenPrimitive.getItems().add(polygon.toString());
        });
    }    
    
    @FXML
    public void dismiss(ActionEvent event) {
        Stage stage = (Stage) xCoordText.getScene().getWindow();
        stage.close();
    }   
    
    @FXML
    public void rotate(ActionEvent event){
        double xPoint = Double.parseDouble(xCoordText.getText());
        double yPoint = Double.parseDouble(yCoordText.getText());
        List<Circle> polygon = DataAccessor.getMyPolygons().get(chosenPrimitive.getSelectionModel().getSelectedIndex());
        List<Circle> newPolygon = new ArrayList<>();
        polygon.forEach((circle) -> {
            Double circlexPoint=circle.getCenterX();
            Double circleyPoint=circle.getCenterY();
            circlexPoint=circlexPoint+xPoint;
            circleyPoint=circleyPoint+yPoint;
            Circle ncircle = new Circle(circlexPoint, circleyPoint, 5);
            newPolygon.add(ncircle);
//            circle.setCenterX(circlexPoint);
//            circle.setCenterY(circleyPoint);
        });
        DataAccessor.getMyPolygons().add(newPolygon);
        Stage stage = (Stage) xCoordText.getScene().getWindow();
        stage.close();
    }
    
}
