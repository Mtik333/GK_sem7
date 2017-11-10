/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import bezier.BezierCurve;
import binarization.BinariizationMethods;
import data.DataAccessor;
import filters.MathOperationFilter;
import filters.Zajecia2;
import filters.Zajecia3;
import filters.Zajecia4;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import loadingfile.Convertion;
import loadingfile.LoadFiles;
import shapes.*;
import transformations.Transformations;

/**
 *
 * @author Mateusz
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label rLabel;
    @FXML
    private Label gLabel;
    @FXML
    private Label bLabel;
    @FXML
    private Label cLabel;
    @FXML
    private Label mLabel;
    @FXML
    private Label yLabel;
    @FXML
    private Label kLabel;
    @FXML
    private Box rgbBox;
    @FXML
    private VBox myVBox;
    @FXML
    private HBox myHBox;
    @FXML
    private Canvas myImage;
    @FXML
    private Canvas bezierCanva;
    @FXML
    private Pane solver;
    @FXML
    private Rectangle rgbRectangle;
    @FXML
    private Rectangle cmykRectangle;
    @FXML
    private ImageView myImageView1;
    @FXML
    private Pane myCanvaScrollPane;
    
    @FXML
    private Canvas bezierCanva1;
    @FXML
    private Pane myCanvaScrollPane1;

    private Zajecia2 zajecia2 = new Zajecia2();
    private Zajecia3 zajecia3 = new Zajecia3();
    private Zajecia4 zajecia4 = new Zajecia4();
    private MathOperationFilter math = new MathOperationFilter();
    private BinariizationMethods bmethods = new BinariizationMethods();
    private BezierCurve bezierCurve = new BezierCurve();
    private Transformations transform = new Transformations();
    private GraphicsContext gc;
    
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
        DataAccessor.initializeRGBCMYKMaps();
        Convertion.convertToRGB();
        setValuesForLabels();
        rgbRectangle.setFill(Color.rgb(DataAccessor.getRgbValues().get("r"), DataAccessor.getRgbValues().get("g"), DataAccessor.getRgbValues().get("b")));
        cmykRectangle.setFill(Color.rgb(DataAccessor.getRgbValues().get("r"), DataAccessor.getRgbValues().get("g"), DataAccessor.getRgbValues().get("b")));
        gc = bezierCanva1.getGraphicsContext2D();
        bezier();
        polygons();
//        Polygon polygon = new Polygon();
//        polygon.getPoints().addAll(new Double[]{
//            100.0, 100.0,
//            200.0, 100.0,
//            200.0, 200.0,
//            150.0, 200.0
//        });
//        DataAccessor.setPolygon(polygon);
//        //myCanvaScrollPane1.getChildren().add(polygon);
//        //transform.translatePointsByVector(50,150);
//        transform.scaleByPointAndFactor(50,150,2);
//        //transform.rotateByPointAndAngle(50.0, 150.0, 90.0);
//       
//        myCanvaScrollPane1.getChildren().add(polygon);
    }
    
    private void polygons(){
        bezierCanva1.setOnMouseClicked((MouseEvent event) ->{
            //gc.clearRect(0, 0, 900, 480);
            int x = (int) event.getX();
            int y = (int) event.getY();
            Circle c = new Circle(x, y, 5);
            DataAccessor.getPolygonPoints().add(c);
            myCanvaScrollPane1.getChildren().add(c);
        });
    }
    
    @FXML
    private void createPolygon(){
        double[] xpoints = new double[DataAccessor.getPolygonPoints().size()];
        double[] ypoints = new double[DataAccessor.getPolygonPoints().size()];
        for (int i=0; i<DataAccessor.getPolygonPoints().size(); i++){
            xpoints[i]=DataAccessor.getPolygonPoints().get(i).getCenterX();
            ypoints[i]=DataAccessor.getPolygonPoints().get(i).getCenterY();
        }
        gc.fillPolygon(xpoints, ypoints, DataAccessor.getPolygonPoints().size());
    }
    
    @FXML
    private void translateVector(){
        
    }
    
    @FXML
    private void rotate(){
        
    }
    
    @FXML
    private void scale(){
        
    }
    
    private void bezier(){
        GraphicsContext gc = bezierCanva.getGraphicsContext2D();
        bezierCanva.setOnMouseClicked((MouseEvent event) ->{
            gc.clearRect(0, 0, 900, 480);
            int x = (int) event.getX();
            int y = (int) event.getY();
            Circle c = new Circle(x, y, 5);
            DataAccessor.getControlPoints().add(c);
            myCanvaScrollPane.getChildren().add(c);
            c.setOnMouseDragged((MouseEvent mouseEvent) -> {
                gc.clearRect(0, 0, 900, 480);
                Circle clicked = (Circle) mouseEvent.getSource();
                clicked.setCenterX(mouseEvent.getX());
                clicked.setCenterY(mouseEvent.getY());
                bezierCurve.calculateBezierCurve();
                DataAccessor.getBezierPoints().forEach((circle) -> {
                    gc.fillRect(circle.getCenterX(), circle.getCenterY(), 1, 1);
                });
            });
            gc.clearRect(0, 0, 900, 480);
            bezierCurve.calculateBezierCurve();
            DataAccessor.getBezierPoints().forEach((circle) -> {
                gc.fillRect(circle.getCenterX(), circle.getCenterY(), 1, 1);
            });
        });
    }
    
    @FXML
    private void resetCanva(){
        GraphicsContext gc = bezierCanva.getGraphicsContext2D();
        gc.clearRect(0, 0, 900, 480);
        DataAccessor.getBezierPoints().clear();
        DataAccessor.getControlPoints().clear();
        myCanvaScrollPane.getChildren().clear();
        myCanvaScrollPane.getChildren().add(bezierCanva);
    }
    
    @FXML
    private void fuzzyMinimumError(){

    }
    
    @FXML
    private void minimumError(){
        bmethods.minimumErrorVerify();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void entropySelection(){
        bmethods.entropySelection();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void meanIterativeSelection(){
        bmethods.meanIterativeSelection();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void percentBlackSelection(){
        bmethods.percentBlackDialog();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void manualBinarization(){
        zajecia3.binaryzacjaManualDialog();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void stretchHistogram(){
        zajecia2.stretchHistogramDialog();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void equalizeHistogram(){
        zajecia2.equalizeHistogram();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void showHistogram(){
        zajecia2.showHistogramDialog();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void mathOperation(){
        showFXML("/fxmls/MathOperationFXML.fxml", "Math");
        math.mathOperation();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void brightnessDialog(){
        zajecia2.brightnessDialog();
        
    }
    
    @FXML
    private void makeMonochrome(){
        zajecia3.naSzaro();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void linearFilter(){
        zajecia4.matrixDialog();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void medianFilter(){
        zajecia4.medianaDialog();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void kuwaharFilter(){
        zajecia4.kuwaharFilter();
        myImageView1 = DataAccessor.getImageView();
    }
    
    @FXML
    private void createHSVCone() {
        showFXML("/fxmls/HSVCone.fxml", "HSV cone");
    }

    @FXML
    private void createRGBCube() {
        showFXML("/fxmls/RGBCube.fxml", "RGB cube");
    }

    @FXML
    private void changeColorRGB() {
        DataAccessor.setIfRGB(true);
        showFXML("/fxmls/SetColorValuesFXML.fxml", "Set color");
        rgbRectangle.setFill(Color.rgb(DataAccessor.getRgbValues().get("r"), DataAccessor.getRgbValues().get("g"), DataAccessor.getRgbValues().get("b")));
        cmykRectangle.setFill(Color.rgb(DataAccessor.getRgbValues().get("r"), DataAccessor.getRgbValues().get("g"), DataAccessor.getRgbValues().get("b")));
        setValuesForLabels();
    }

    @FXML
    private void changeColorCMYK() {
        DataAccessor.setIfRGB(false);
        showFXML("/fxmls/SetColorValuesFXML.fxml", "Set color");
        rgbRectangle.setFill(Color.rgb(DataAccessor.getRgbValues().get("r"), DataAccessor.getRgbValues().get("g"), DataAccessor.getRgbValues().get("b")));
        cmykRectangle.setFill(Color.rgb(DataAccessor.getRgbValues().get("r"), DataAccessor.getRgbValues().get("g"), DataAccessor.getRgbValues().get("b")));
        setValuesForLabels();
    }

    @FXML
    private void setValuesForLabels() {
        rLabel.setText("R: " + String.valueOf(DataAccessor.getRgbValues().get("r")));
        gLabel.setText("G: " + String.valueOf(DataAccessor.getRgbValues().get("g")));
        bLabel.setText("B: " + String.valueOf(DataAccessor.getRgbValues().get("b")));
        cLabel.setText("C: " + String.valueOf(DataAccessor.getCmykValues().get("c")));
        mLabel.setText("M: " + String.valueOf(DataAccessor.getCmykValues().get("m")));
        yLabel.setText("Y: " + String.valueOf(DataAccessor.getCmykValues().get("y")));
        kLabel.setText("K: " + String.valueOf(DataAccessor.getCmykValues().get("k")));
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
            if (image == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(DataAccessor.getFetchError());
                alert.showAndWait();
                DataAccessor.setFetchError(null);
                return;
            }
            myImage.setHeight(LoadFiles.height);
            myImage.setWidth(LoadFiles.width);
            myImage.getGraphicsContext2D().drawImage(image, 0, 0);

            BufferedImage bimage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.OPAQUE);
            // Draw the image on to the buffered image
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(bimage, 0, 0, null);
            bGr.dispose();
            DataAccessor.setImage(bimage);
//
//            Iterator<ImageReader> it = ImageIO.getImageReadersByMIMEType("image/bmp");
//            ImageReader reader = it.next();
//            ImageInputStream iis = ImageIO.createImageInputStream(file);
//            reader.setInput(iis, false, false);
//            DataAccessor.setImageMetadata(reader.getImageMetadata(0));

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
            DataAccessor.setIsGray(false);
            DataAccessor.setMinimalDistribution(0);
            DataAccessor.setThresholdForIterations(128);
            Image image2 = new Image(file.toURI().toString());
            myImageView1.setImage(image2);
            DataAccessor.setImageView(myImageView1);
            
//            FileInputStream fis = new FileInputStream(file);
//            BufferedImage src = null;
//            Iterator<ImageReader> it = ImageIO.getImageReadersByMIMEType("image/jpeg");
//            ImageReader reader = it.next();
//            ImageInputStream iis = ImageIO.createImageInputStream(fis);
//            reader.setInput(iis, false, false);
//            src = reader.read(0);
//            DataAccessor.setImageMetadata(reader.getImageMetadata(0));
//            DataAccessor.setImage(src);
//            //BufferedImage image = ImageIO.read(file);
//            Image image = new Image(new FileInputStream(file.getAbsoluteFile()));
//            myImageView1.setImage(image);
//            DataAccessor.setImageView(myImageView1);
//            myImage.setHeight(LoadFiles.height);
//            myImage.setWidth(LoadFiles.width);
//            myImage.getGraphicsContext2D().drawImage(image, 0, 0);

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
        if (file1 != null) {
            showFXML("/fxmls/ImageQualityFXML.fxml", "Image quality");
            WritableImage wi = new WritableImage((int) myImage.getWidth(), (int) myImage.getHeight());
            myImage.snapshot(null, wi);
            BufferedImage copyImg = SwingFXUtils.fromFXImage(wi, null);
            BufferedImage image = new BufferedImage(copyImg.getWidth(), copyImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            image.getGraphics().drawImage(copyImg, 0, 0, null);
            Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(DataAccessor.getJpegQuality());
            FileImageOutputStream output = new FileImageOutputStream(file1);
            writer.setOutput(output);
            IIOImage image2 = new IIOImage(image, null, DataAccessor.getImageMetadata());
            writer.write(null, image2, iwp);
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
