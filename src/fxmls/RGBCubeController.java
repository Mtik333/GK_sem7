/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmls;

import cubegenerator.GenerateRGBCube;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Mateusz
 */
public class RGBCubeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        anchorPane.getChildren().add(GenerateRGBCube.generateRGBCube());
    }

    @FXML
    public void test(ActionEvent event) {
        PerspectiveCamera camera = new PerspectiveCamera(false);
        // Add a Rotation Animation to the Camera
        anchorPane.getScene().setCamera(camera);
        RotateTransition rotation = new RotateTransition(Duration.seconds(5), camera);
        rotation.setCycleCount(Animation.INDEFINITE);
        rotation.setFromAngle(0);
        rotation.setToAngle(360);
        rotation.setAutoReverse(false);
        rotation.setAxis(Rotate.Y_AXIS);
        rotation.play();
    }

}
