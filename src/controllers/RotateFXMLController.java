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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mateusz
 */
public class RotateFXMLController implements Initializable {

    @FXML
    public ComboBox chosenPrimitive;
    @FXML
    public TextField xCoordText;
    @FXML
    public TextField yCoordText;
    @FXML
    public TextField radiusText;

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
    public void rotate(ActionEvent event) {
        double xPoint = Double.parseDouble(xCoordText.getText());
        double yPoint = Double.parseDouble(yCoordText.getText());
        double angle = Double.parseDouble(radiusText.getText());
        DataAccessor.setChangePoint(new Circle(xPoint, yPoint, 5, Color.GREEN));
        List<Circle> polygon = DataAccessor.getMyPolygons().get(chosenPrimitive.getSelectionModel().getSelectedIndex());
        Rotate rotate = new Rotate(angle, xPoint, yPoint);
        double[] points = new double[polygon.size() * 2];
        for (int i = 0; i < points.length; i += 2) {
            points[i] = polygon.get(i / 2).getCenterX();
            points[i + 1] = polygon.get(i / 2).getCenterY();
        }
        rotate.transform2DPoints(points, 0, points, 0, points.length / 2);
        List<Circle> newPolygon = new ArrayList<>();
        for (int i = 0; i < points.length; i += 2) {
            Circle circle = new Circle(points[i], points[i + 1], 3);
            newPolygon.add(circle);
        }
        DataAccessor.getMyPolygons().add(newPolygon);
        Stage stage = (Stage) xCoordText.getScene().getWindow();
        stage.close();

    }
}
