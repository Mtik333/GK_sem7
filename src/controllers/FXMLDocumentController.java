/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataAccessor;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shapes.*;

/**
 *
 * @author Mateusz
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ToggleButton lineButton;
    @FXML
    private ToggleButton circleButton;
    @FXML
    private ToggleButton rectangleButton;
    private ToggleGroup toggleGroup;
    @FXML
    private Pane solver;
    double orgSceneX, orgSceneY; //do przenoszenia wierzcholkow/krawedzi
    double orgTranslateX, orgTranslateY; //do przenoszenia wierzcholkow/krawedzi

    @FXML
    public void exitApp(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        DataAccessor.setShapes(new ArrayList<>());
        DataAccessor.setMapping(new HashMap<>());
        toggleGroup = new ToggleGroup();
        lineButton.setToggleGroup(toggleGroup);
        circleButton.setToggleGroup(toggleGroup);
        rectangleButton.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue==null){
                    DataAccessor.setDrawnShape(null);
                }
                else if (newValue.equals(lineButton)) {
                    DataAccessor.setDrawnShape(new Line(0, 0, 0, 0));
                    DataAccessor.setPrimitiveType("Line");
                }
                else if (newValue.equals(circleButton)) {
                    DataAccessor.setDrawnShape(new Circle(0, 0, 0));
                    DataAccessor.setPrimitiveType("Circle");
                }
                else if (newValue.equals(rectangleButton)) {
                    DataAccessor.setDrawnShape(new Rectangle(0, 0, 0, 0));
                    DataAccessor.setPrimitiveType("Rectangle");
                }
            }

        });

        solver.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Shape shape = DataAccessor.getDrawnShape();
                if (shape != null) {
                    if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                        shape.setVisible(true);
                        shape.setTranslateX(mouseEvent.getX());
                        shape.setTranslateY(mouseEvent.getY());
                    }
                    if (mouseEvent.getEventType() == MouseEvent.MOUSE_MOVED && shape.isVisible()) {
                        if (shape instanceof Line) {
                            ((Line) shape).setEndX(mouseEvent.getX() - shape.getTranslateX());
                            ((Line) shape).setEndY(mouseEvent.getY() - shape.getTranslateY());
                        }
                        if (shape instanceof Rectangle) {
                            ((Rectangle) shape).setWidth(mouseEvent.getX() - shape.getTranslateX());
                            ((Rectangle) shape).setHeight(mouseEvent.getY() - shape.getTranslateY());
                        }
                        if (shape instanceof Circle){
                            double radius = Math.sqrt(Math.pow(((Circle)shape).getCenterX()-(mouseEvent.getX() - shape.getTranslateX()),2)+Math.pow(((Circle)shape).getCenterY()-(mouseEvent.getY() - shape.getTranslateY()),2));
                            ((Circle)shape).setRadius(radius);
                        }
                    }
                    if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                        shape.setVisible(true);
                        ShapeObj shapeObj=null;
                        switch(DataAccessor.getPrimitiveType()){
                            case "Line":
                                shapeObj = new LineObj(((Line)shape).getStartX(), ((Line)shape).getStartY());
                                ((LineObj)shapeObj).setLength(Math.abs(((Line)shape).getStartX()-((Line)shape).getEndX()));
                                break;
                            case "Circle":
                                shapeObj = new CircleObj(((Circle)shape).getCenterX(), ((Circle)shape).getCenterY(), ((Circle)shape).getRadius());
                                break;
                            case "Rectangle":
                                shapeObj = new RectangleObj(((Rectangle)shape).getX(), ((Rectangle)shape).getY(), ((Rectangle)shape).getHeight(), ((Rectangle)shape).getWidth());
                                break;
                        }
                        DataAccessor.getMapping().put(shapeObj, shape);
                        shape.setOnMousePressed(shapeOnMousePressedEventHandler);
                        shape.setOnMouseDragged(shapeOnMouseDraggedEventHandler);
                        shape.setOnMouseReleased(shapeOnMouseReleasedEventHandler);
                        solver.getChildren().add(shape);
                        resetToggleGroup();
                    }
                }
            }
        });

    }

    @FXML
    private void createPrimitiveCustom() {
        showFXML("/fxmls/PrimitiveSelectFXML.fxml", "Draw primitive");
        if (DataAccessor.isDraw()) {
            drawOnCanva();
        }
    }
    
    @FXML
    private void resetToggleGroup(){
        toggleGroup.selectToggle(null);
    }

    private void showFXML(String resource, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(DataAccessor.getPrimaryStage());
            stage.setScene(new Scene(root1));
            DataAccessor.setDraw(false);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void drawOnCanva() {
        ShapeObj shape = DataAccessor.getShapes().get(DataAccessor.getShapes().size() - 1);
        Shape fxShape = null;
        switch (DataAccessor.getPrimitiveType()) {
            case "Line":
                fxShape = new Line(shape.getxCoord(), shape.getyCoord(), shape.getxCoord() + ((LineObj) shape).getLength(), shape.getyCoord() + ((LineObj) shape).getLength());
                break;
            case "Circle":
                fxShape = new Circle(shape.getxCoord(), shape.getyCoord(), ((CircleObj) shape).getRadius());
                break;
            case "Rectangle":
                fxShape = new Rectangle(shape.getxCoord(), shape.getyCoord(), ((RectangleObj) shape).getWidth(), ((RectangleObj) shape).getHeight());
                break;
        }
        fxShape.setStrokeWidth(5);
        fxShape.setOnMousePressed(shapeOnMousePressedEventHandler);
        fxShape.setOnMouseDragged(shapeOnMouseDraggedEventHandler);
        fxShape.setOnMouseReleased(shapeOnMouseReleasedEventHandler);
        fxShape.toFront();
        DataAccessor.getMapping().put(shape, fxShape);
        solver.getChildren().add(fxShape);

    }

    EventHandler<MouseEvent> shapeOnMousePressedEventHandler
            = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (t.getButton() == MouseButton.SECONDARY) {
                Shape shape = (Shape) t.getSource();
                DataAccessor.getMapping().entrySet().stream().filter((entry) -> (entry.getValue().equals(shape))).forEachOrdered((entry) -> {
                    DataAccessor.setAnalyzedPrimitive(entry.getKey());
                });
                if (t.getSource() instanceof Circle) {
                    DataAccessor.setPrimitiveType("Circle");
                } else if (t.getSource() instanceof Line) {
                    DataAccessor.setPrimitiveType("Line");
                } else if (t.getSource() instanceof Rectangle) {
                    DataAccessor.setPrimitiveType("Rectangle");
                }
                showFXML("/fxmls/ChangePrimitiveFXML.fxml", "Change primitive");

            } else {
                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();
                orgTranslateX = ((Shape) (t.getSource())).getTranslateX();
                orgTranslateY = ((Shape) (t.getSource())).getTranslateY();
                ((Shape) (t.getSource())).toFront();
            }
        }
    };

    //obsługa przesuwania wierzchołków poprzez myszkę
    EventHandler<MouseEvent> shapeOnMouseDraggedEventHandler
            = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
            ((Shape) (t.getSource())).setTranslateX(newTranslateX);
            ((Shape) (t.getSource())).setTranslateY(newTranslateY);
        }
    };

    EventHandler<MouseEvent> shapeOnMouseReleasedEventHandler
            = (MouseEvent t) -> {
                if (((t.getSource())) instanceof Line) {
                    Line line = ((Line) (t.getSource()));
                    line.setStartX(line.getStartX() + line.getTranslateX());
                    line.setStartY(line.getStartY() + line.getTranslateY());
                    line.setEndX(line.getEndX() + line.getTranslateX());
                    line.setEndY(line.getEndY() + line.getTranslateY());
                    DataAccessor.getMapping().entrySet().stream().filter((entry) -> (entry.getValue().equals(line))).forEachOrdered((entry) -> {
                        LineObj lineObj = (LineObj) entry.getKey();
                        lineObj.setxCoord(line.getStartX());
                        lineObj.setyCoord(line.getStartY());
                        entry.setValue(line);
                        DataAccessor.getMapping().replace(lineObj, entry.getValue());
                    });

                }
                if (((t.getSource())) instanceof Circle) {
                    Circle circle = ((Circle) (t.getSource()));
                    circle.setCenterX(circle.getCenterX() + circle.getTranslateX());
                    circle.setCenterY(circle.getCenterY() + circle.getTranslateY());
                    DataAccessor.getMapping().entrySet().stream().filter((entry) -> (entry.getValue().equals(circle))).forEachOrdered((entry) -> {
                        CircleObj circleObj = (CircleObj) entry.getKey();
                        circleObj.setxCoord(circle.getCenterX());
                        circleObj.setyCoord(circle.getCenterY());
                        entry.setValue(circle);
                        DataAccessor.getMapping().replace(circleObj, entry.getValue());
                    });

                }
                if (((t.getSource())) instanceof Rectangle) {
                    Rectangle rectangle = ((Rectangle) (t.getSource()));
                    rectangle.setX(rectangle.getX() + rectangle.getTranslateX());
                    rectangle.setY(rectangle.getY() + rectangle.getTranslateY());
                    DataAccessor.getMapping().entrySet().stream().filter((entry) -> (entry.getValue().equals(rectangle))).forEachOrdered((entry) -> {
                        RectangleObj rectangleObj = (RectangleObj) entry.getKey();
                        rectangleObj.setxCoord(rectangle.getX());
                        rectangleObj.setyCoord(rectangle.getY());
                        entry.setValue(rectangle);
                        DataAccessor.getMapping().replace(rectangleObj, entry.getValue());
                    });

                }
                ((Shape) t.getSource()).setTranslateX(0);
                ((Shape) t.getSource()).setTranslateY(0);
            };
}
