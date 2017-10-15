/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.DataAccessor;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import loadingfile.LoadFiles;
import shapes.*;

/**
 *
 * @author Mateusz
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ImageView myImage;
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
    }

    @FXML
    private void loadPPMFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Load PPM P3/P6 file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PPM P3/P6 files", "*.ppm")
        );
        File file = fileChooser.showOpenDialog(solver.getScene().getWindow());
        if (file != null) {

            Image image = LoadFiles.fetchHeader(file);
            myImage.setFitHeight(LoadFiles.height);
            myImage.setFitWidth(LoadFiles.width);
            myImage.setImage(image);

            BufferedImage bimage = new BufferedImage((int)image.getWidth(), (int)image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            // Draw the image on to the buffered image
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(bimage, 0, 0, null);
            bGr.dispose();
            DataAccessor.setImage(bimage);
        }
    }

    @FXML
    private void loadJPEGFile() throws FileNotFoundException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Load PPM P3/P6 file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG/JPEG files", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(solver.getScene().getWindow());
        if (file != null) {
            FileInputStream fis = new FileInputStream(file);
//            int one;
//            int two;
//            one = ((int) fis.read() & 0xff);
//            two = ((int) fis.read() & 0xff);
//            if (one == 255 && two == 216) {
            BufferedImage src = null;
            Iterator<ImageReader> it = ImageIO.getImageReadersByMIMEType("image/jpeg");
            ImageReader reader = it.next();
            ImageInputStream iis = ImageIO.createImageInputStream(fis);
            reader.setInput(iis, false, false);
            src = reader.read(0);
            DataAccessor.setImageMetadata(reader.getImageMetadata(0));
            DataAccessor.setImage(src);
            //BufferedImage image = ImageIO.read(file);
            myImage.setFitHeight(src.getHeight());
            myImage.setFitWidth(src.getWidth());
            myImage.setImage(SwingFXUtils.toFXImage(src, null));
            //}

        }
    }

    @FXML
    private void saveAsJPEG() throws IOException {
        FileChooser fileChooser1 = new FileChooser();
        fileChooser1.setTitle("Zapisz obraz");
        fileChooser1.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG/JPEG images", "*.jpg")
        );
        File file1 = fileChooser1.showSaveDialog(solver.getScene().getWindow());
        if (file1 != null && myImage.getImage() != null) {
            showFXML("/fxmls/ImageQualityFXML.fxml", "Image quality");
            Iterator iter = ImageIO.getImageWritersByMIMEType("image/jpeg");
            ImageWriter writer = (ImageWriter) iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(DataAccessor.getJpegQuality());
            FileImageOutputStream output = new FileImageOutputStream(file1);
            writer.setOutput(output);
            IIOImage image = new IIOImage((RenderedImage) DataAccessor.getImage(), null, DataAccessor.getImageMetadata());
            writer.write(null, image, iwp);
            writer.dispose();
        }
    }

    @FXML
    private void createPrimitiveCustom() {
        showFXML("/fxmls/PrimitiveSelectFXML.fxml", "Draw primitive");
        if (DataAccessor.isDraw()) {
            drawOnCanva();
        }
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
