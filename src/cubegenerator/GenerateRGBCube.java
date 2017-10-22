/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubegenerator;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Mateusz
 */
public class GenerateRGBCube {
    public static BilinearGradient frontWall = new BilinearGradient(Color.rgb(0, 255, 255), Color.rgb(255, 255, 255), Color.rgb(0, 255, 0), Color.rgb(255, 255, 0));
    public static BilinearGradient rightWall = new BilinearGradient(Color.rgb(255, 255, 255), Color.rgb(255, 0, 255), Color.rgb(255, 255, 0), Color.rgb(255, 0, 0));
    public static BilinearGradient topWall = new BilinearGradient(Color.rgb(0, 0, 255), Color.rgb(255, 0, 255), Color.rgb(0, 255, 255), Color.rgb(255, 255, 255));
    public static BilinearGradient backWall = new BilinearGradient(Color.rgb(255, 0, 255), Color.rgb(0, 0, 255), Color.rgb(255, 0, 0), Color.rgb(0, 0, 0));
    public static BilinearGradient leftWall = new BilinearGradient(Color.rgb(0, 0, 255), Color.rgb(0, 255, 255), Color.rgb(0, 0, 0), Color.rgb(0, 255, 0));
    public static BilinearGradient bottomWall = new BilinearGradient(Color.rgb(0, 255, 0), Color.rgb(255, 255, 0), Color.rgb(0, 0, 0), Color.rgb(255, 0, 0));
    
//    public static Stop[] stops1 = new Stop[]{new Stop(0, Color.rgb(0, 0, 255)), new Stop(1, Color.rgb(255, 255, 255))};
//    public static Stop[] stops2 = new Stop[]{new Stop(0, Color.rgb(0, 255, 0)), new Stop(1, Color.rgb(255, 255, 255))};
//    public static Stop[] stops3 = new Stop[]{new Stop(0, Color.rgb(255, 0, 0)), new Stop(1, Color.rgb(255, 255, 255))};
//    public static Stop[] stops4 = new Stop[]{new Stop(0, Color.rgb(0, 0, 0)), new Stop(1, Color.rgb(255, 255, 0))};
//    public static Stop[] stops5 = new Stop[]{new Stop(0, Color.rgb(0, 0, 0)), new Stop(1, Color.rgb(255, 0, 255))};
//    public static Stop[] stops6 = new Stop[]{new Stop(0, Color.rgb(0, 0, 0)), new Stop(1, Color.rgb(0, 255, 255))};
    
    public static Group generateRGBCube(){
        Group cube = new Group();
        double size = 255;
        Image frontImage = frontWall.getImage(255,255);
        Image rightImage = rightWall.getImage(255,255);
        Image topImage = topWall.getImage(255,255);
        Image backImage = backWall.getImage(255,255);
        Image leftImage = leftWall.getImage(255,255);
        Image bottomImage = bottomWall.getImage(255,255);
        cube.getChildren().addAll(
                RectangleBuilder.create() // back face
                .width(size).height(size)
                .fill(new ImagePattern(backImage))
                .translateX(-0.5 * size)
                .translateY(-0.5 * size)
                .translateZ(0.5 * size)
                .build(),
                RectangleBuilder.create() // bottom face
                .width(size).height(size)
                .fill(new ImagePattern(bottomImage))
                .translateX(-0.5 * size)
                .translateY(0)
                .rotationAxis(Rotate.X_AXIS)
                .rotate(-90)
                .build(),
                RectangleBuilder.create() // right face
                .width(size).height(size)
                .fill(new ImagePattern(leftImage))
                .translateX(-1 * size)
                .translateY(-0.5 * size)
                .rotationAxis(Rotate.Y_AXIS)
                .rotate(-90)
                .build(),
                RectangleBuilder.create() // left face
                .width(size).height(size)
                .fill(new ImagePattern(rightImage))
                .translateX(0)
                .translateY(-0.5 * size)
                .rotationAxis(Rotate.Y_AXIS)
                .rotate(-90)
                .build(),
                RectangleBuilder.create() // top face
                .width(size).height(size)
                .fill(new ImagePattern(topImage))
                .translateX(-0.5 * size)
                .translateY(-1 * size)
                .rotationAxis(Rotate.X_AXIS)
                .rotate(-90)
                .build(),
                RectangleBuilder.create() // front face
                .width(size).height(size)
                .fill(new ImagePattern(frontImage))
                .translateX(-0.5 * size)
                .translateY(-0.5 * size)
                .translateZ(-0.5 * size)
                .build());
        cube.getTransforms().addAll(new Rotate(45, Rotate.X_AXIS), new Rotate(45, Rotate.Y_AXIS));
        cube.setTranslateX(255);
        cube.setTranslateY(255);
       
        
        return cube;
    }
    
    private static LinearGradient generateGradient(Stop[] stops){
        LinearGradient linearGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        return linearGradient;
    }
}
