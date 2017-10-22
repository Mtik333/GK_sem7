/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import cubegenerator.GenerateRGBCube;
import cubegenerator.RotateCube;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Mateusz
 */
public class RGBCubeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private double mouseOldX, mouseOldY, mousePosX, mousePosY, mouseDeltaX, mouseDeltaY;
    
    @FXML
    private AnchorPane anchorPane;
    private Scene scene;
    private RotateCube rotateCube;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rotateCube = GenerateRGBCube.generateRGBCube();
        anchorPane.getChildren().add(rotateCube);
    }

    @FXML
    public void test(ActionEvent event) {
        scene = anchorPane.getScene();
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getX();
                mousePosY = me.getY();
                mouseDeltaX = mousePosX - mouseOldX;
                mouseDeltaY = mousePosY - mouseOldY;
                if (me.isAltDown() && me.isShiftDown() && me.isPrimaryButtonDown()) {
                    rotateCube.rz.setAngle(rotateCube.rz.getAngle() - mouseDeltaX);
                }
                else if (me.isAltDown() && me.isPrimaryButtonDown()) {
                    rotateCube.ry.setAngle(rotateCube.ry.getAngle() - mouseDeltaX);
                    rotateCube.rx.setAngle(rotateCube.rx.getAngle() + mouseDeltaY);
                }
                else if (me.isAltDown() && me.isSecondaryButtonDown()) {
                    double scale = rotateCube.s.getX();
                    double newScale = scale + mouseDeltaX*0.01;
                    rotateCube.s.setX(newScale); rotateCube.s.setY(newScale); rotateCube.s.setZ(newScale);
                }
                else if (me.isAltDown() && me.isMiddleButtonDown()) {
                    rotateCube.t.setX(rotateCube.t.getX() + mouseDeltaX);
                    rotateCube.t.setY(rotateCube.t.getY() + mouseDeltaY);
                }
            }
        });
        
    }

}
