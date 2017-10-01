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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import shapes.*;

/**
 * FXML Controller class
 *
 * @author Mateusz
 */
public class ChangePrimitiveFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private ShapeObj shape;
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
        // TODO
        shape = DataAccessor.getAnalyzedPrimitive();
        setInterfaceLook();
    }    
    
    private void setInterfaceLook(){
        circleBox.setDisable(true);
        widthBox.setDisable(true);
        xCoordText.setText(String.valueOf(shape.getxCoord()));
        yCoordText.setText(String.valueOf(shape.getyCoord()));
        switch(DataAccessor.getPrimitiveType()){
            case "Line":
                lengthText.setText(String.valueOf(((LineObj)shape).getLength()));
                break;
            case "Rectangle":
                lengthText.setText(String.valueOf(((RectangleObj)shape).getHeight()));
                widthText.setText(String.valueOf(((RectangleObj)shape).getWidth()));
                lengthBox.setDisable(false);
                widthBox.setDisable(false);
                break;
            case "Circle":
                radiusText.setText(String.valueOf(((CircleObj)shape).getRadius()));
                lengthBox.setDisable(true);
                circleBox.setDisable(false);
                break;
        }
    }
    
    @FXML
    public void drawOnCanva(ActionEvent event){
        shape.setxCoord(Double.valueOf(xCoordText.getText()));
        shape.setyCoord(Double.valueOf(yCoordText.getText()));
        switch(DataAccessor.getPrimitiveType()){
            case "Line":
                ((LineObj)shape).setLength(Double.valueOf(lengthText.getText()));
                Line line = (Line)DataAccessor.getMapping().get(shape);
                line.setStartX(shape.getxCoord());
                line.setEndX(shape.getxCoord()+((LineObj)shape).getLength());
                line.setStartY(shape.getyCoord());
                line.setEndY(shape.getyCoord()+((LineObj)shape).getLength());
                break;
            case "Rectangle":
                ((RectangleObj)shape).setHeight(Double.valueOf(lengthText.getText()));
                ((RectangleObj)shape).setWidth(Double.valueOf(widthText.getText()));
                Rectangle rectangle = (Rectangle)DataAccessor.getMapping().get(shape);
                rectangle.setX(shape.getxCoord());
                rectangle.setY(shape.getyCoord());
                rectangle.setWidth(((RectangleObj)shape).getWidth());
                rectangle.setHeight(((RectangleObj)shape).getHeight());
                break;
            case "Circle":
                ((CircleObj)shape).setRadius(Double.valueOf(radiusText.getText()));
                Circle circle = (Circle)DataAccessor.getMapping().get(shape);
                circle.setCenterX(shape.getxCoord());
                circle.setCenterY(shape.getyCoord());
                circle.setRadius(((CircleObj)shape).getRadius());
                break;
        }
        DataAccessor.setAnalyzedPrimitive(shape);
        Stage stage = (Stage) xCoordText.getScene().getWindow();
        stage.close();
    }
}
