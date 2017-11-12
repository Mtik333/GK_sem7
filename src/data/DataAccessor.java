/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javax.imageio.metadata.IIOMetadata;
import shapes.ShapeObj;

/**
 *
 * @author Mateusz
 */
public class DataAccessor {

    private static String primitiveType = "Line";
    private static Stage primaryStage; //widok UI
    private static List<ShapeObj> shapes;
    private static Map<ShapeObj, Shape> mapping;
    private static ShapeObj analyzedPrimitive;
    private static Shape drawnShape;
    private static float jpegQuality = 1f;
    private static BufferedImage image;
    private static String fetchError;
    public static Map<String, Integer> rgbValues = new HashMap<>(3);
    public static Map<String, Integer> cmykValues = new HashMap<>(4);
    public static boolean ifRGB = true;
    public static ImageView imageView;
    public static String mathOperation;
    public static boolean isGray = false;
    public static int thresholdForIterations = 128;
    public static double minimalDistribution;
    public static List<Circle> controlPoints = new ArrayList<>();
    public static List<Circle> bezierPoints = new ArrayList<>();
    public static List<Circle> polygonPoints = new ArrayList<>();
    public static List<List<Circle>> myPolygons = new ArrayList<>();
    public static Polygon polygon;
    public static Circle changePoint;

    public static Circle getChangePoint() {
        return changePoint;
    }

    public static void setChangePoint(Circle changePoint) {
        DataAccessor.changePoint = changePoint;
    }

    public static List<List<Circle>> getMyPolygons() {
        return myPolygons;
    }

    public static void setMyPolygons(List<List<Circle>> myPolygons) {
        DataAccessor.myPolygons = myPolygons;
    }

    public static List<Circle> getPolygonPoints() {
        return polygonPoints;
    }

    public static void setPolygonPoints(List<Circle> polygonPoints) {
        DataAccessor.polygonPoints = polygonPoints;
    }

    public static Polygon getPolygon() {
        return polygon;
    }

    public static void setPolygon(Polygon polygon) {
        DataAccessor.polygon = polygon;
    }

    public static List<Circle> getControlPoints() {
        return controlPoints;
    }

    public static void setControlPoints(List<Circle> controlPoints) {
        DataAccessor.controlPoints = controlPoints;
    }

    public static List<Circle> getBezierPoints() {
        return bezierPoints;
    }

    public static void setBezierPoints(List<Circle> bezierPoints) {
        DataAccessor.bezierPoints = bezierPoints;
    }

    public static double getMinimalDistribution() {
        return minimalDistribution;
    }

    public static void setMinimalDistribution(double minimalDistribution) {
        DataAccessor.minimalDistribution = minimalDistribution;
    }

    public static int getThresholdForIterations() {
        return thresholdForIterations;
    }

    public static void setThresholdForIterations(int thresholdForIterations) {
        DataAccessor.thresholdForIterations = thresholdForIterations;
    }

    public static boolean isIsGray() {
        return isGray;
    }

    public static void setIsGray(boolean isGray) {
        DataAccessor.isGray = isGray;
    }

    public static String getMathOperation() {
        return mathOperation;
    }

    public static void setMathOperation(String mathOperation) {
        DataAccessor.mathOperation = mathOperation;
    }

    public static ImageView getImageView() {
        return imageView;
    }

    public static void setImageView(ImageView imageView) {
        DataAccessor.imageView = imageView;
    }

    public static Map<String, Integer> getRgbValues() {
        return rgbValues;
    }

    public static void setRgbValues(Map<String, Integer> rgbValues) {
        DataAccessor.rgbValues = rgbValues;
    }

    public static Map<String, Integer> getCmykValues() {
        return cmykValues;
    }

    public static void setCmykValues(Map<String, Integer> cmykValues) {
        DataAccessor.cmykValues = cmykValues;
    }

    public static boolean isIfRGB() {
        return ifRGB;
    }

    public static void setIfRGB(boolean ifRGB) {
        DataAccessor.ifRGB = ifRGB;
    }

    public static String getFetchError() {
        return fetchError;
    }

    public static void setFetchError(String fetchError) {
        DataAccessor.fetchError = fetchError;
    }

    public static BufferedImage getImage() {
        return image;
    }

    public static void setImage(BufferedImage image) {
        DataAccessor.image = image;
    }

    public static float getJpegQuality() {
        return jpegQuality;
    }

    public static void setJpegQuality(float jpegQuality) {
        DataAccessor.jpegQuality = jpegQuality;
    }

    public static IIOMetadata getImageMetadata() {
        return imageMetadata;
    }

    public static void setImageMetadata(IIOMetadata imageMetadata) {
        DataAccessor.imageMetadata = imageMetadata;
    }
    private static IIOMetadata imageMetadata;

    public static Shape getDrawnShape() {
        return drawnShape;
    }

    public static void setDrawnShape(Shape drawnShape) {
        DataAccessor.drawnShape = drawnShape;
        if (drawnShape != null) {
            drawnShape.setDisable(false);
        }
    }
    private static boolean draw = false;

    public static List<ShapeObj> getShapes() {
        return shapes;
    }

    public static void setShapes(List<ShapeObj> shapes) {
        DataAccessor.shapes = shapes;
    }

    public static Map<ShapeObj, Shape> getMapping() {
        return mapping;
    }

    public static void setMapping(Map<ShapeObj, Shape> mapping) {
        DataAccessor.mapping = mapping;
    }

    public static boolean isDraw() {
        return draw;
    }

    public static void setDraw(boolean draw) {
        DataAccessor.draw = draw;
    }

    public static ShapeObj getAnalyzedPrimitive() {
        return analyzedPrimitive;
    }

    public static void setAnalyzedPrimitive(ShapeObj analyzedPrimitive) {
        DataAccessor.analyzedPrimitive = analyzedPrimitive;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        DataAccessor.primaryStage = primaryStage;
    }

    public static String getPrimitiveType() {
        return primitiveType;
    }

    public static void setPrimitiveType(String primitiveType) {
        DataAccessor.primitiveType = primitiveType;
    }

    public static void initializeRGBCMYKMaps() {
        DataAccessor.cmykValues = new HashMap<>();
        cmykValues.put("c", 0);
        cmykValues.put("m", 0);
        cmykValues.put("y", 0);
        cmykValues.put("k", 0);
        DataAccessor.rgbValues = new HashMap<>();
        rgbValues.put("r", 255);
        rgbValues.put("g", 255);
        rgbValues.put("b", 255);
    }
}
