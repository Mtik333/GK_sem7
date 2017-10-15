/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javax.imageio.metadata.IIOMetadata;
import shapes.ShapeObj;

/**
 *
 * @author Mateusz
 */
public class DataAccessor {
    private static String primitiveType="Line";
    private static Stage primaryStage; //widok UI
    private static List<ShapeObj> shapes;
    private static Map<ShapeObj, Shape> mapping;
    private static ShapeObj analyzedPrimitive;
    private static Shape drawnShape;
    private static float jpegQuality=1f;
    private static BufferedImage image;
    private static String fetchError;

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
        if (drawnShape!=null)
            drawnShape.setDisable(false);
    }
    private static boolean draw=false;

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
    
}
