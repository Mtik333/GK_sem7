/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataAccessor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import shapes.CircleObj;
import shapes.LineObj;
import shapes.RectangleObj;
import shapes.ShapeObj;

/**
 * FXML Controller class
 *
 * @author Mateusz
 */
public class PrimitiveSelectFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    public ComboBox chosenPrimitive;
    @FXML
    public TextField xCoordText;
    @FXML
    public TextField yCoordText;
    @FXML
    public TextField radiusText;
    @FXML
    public TextField lengthText;
    @FXML
    public TextField widthText;
    @FXML
    public HBox circleBox;
    @FXML
    public HBox lengthBox;
    @FXML
    public HBox widthBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chosenPrimitive.getItems().addAll("Line", "Rectangle", "Circle");
        chosenPrimitive.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                setInterfaceLook();
            }
        });
        chosenPrimitive.getSelectionModel().select(DataAccessor.getPrimitiveType());

    }

    private void setInterfaceLook() {
        circleBox.setDisable(true);
        widthBox.setDisable(true);
        switch (chosenPrimitive.getSelectionModel().getSelectedItem().toString()) {
            case "Line":
                break;
            case "Rectangle":
                lengthBox.setDisable(false);
                widthBox.setDisable(false);
                break;
            case "Circle":
                lengthBox.setDisable(true);
                circleBox.setDisable(false);
                break;
        }
    }

    @FXML
    public void dismiss(ActionEvent event) {
        Stage stage = (Stage) xCoordText.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void drawOnCanva(ActionEvent event) {
        DataAccessor.setPrimitiveType(chosenPrimitive.getSelectionModel().getSelectedItem().toString());
        double x = Double.valueOf(xCoordText.getText());
        double y = Double.valueOf(yCoordText.getText());
        double length = 0;
        double radius = 0;
        double width = 0;
        if (!circleBox.isDisabled()) {
            radius = Double.valueOf(radiusText.getText());
        }
        if (!lengthBox.isDisabled()) {
            length = Double.valueOf(lengthText.getText());
        }
        if (!widthBox.isDisabled()) {
            width = Double.valueOf(widthText.getText());
        }
        ShapeObj shape = null;
        switch (DataAccessor.getPrimitiveType()) {
            case "Line":
                shape = new LineObj(x, y, length);
                break;
            case "Rectangle":
                shape = new RectangleObj(x, y, length, width);
                break;
            case "Circle":
                shape = new CircleObj(x, y, radius);
                break;
        }
        DataAccessor.getShapes().add(shape);
        Stage stage = (Stage) xCoordText.getScene().getWindow();
        stage.close();
        DataAccessor.setDraw(true);
    }
}
