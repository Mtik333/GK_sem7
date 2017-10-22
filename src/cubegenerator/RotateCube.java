/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubegenerator;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 *
 * @author Mateusz
 */
public class RotateCube extends Group {
        public Translate t  = new Translate();
        public Translate p  = new Translate();
        public Translate ip = new Translate();
        public Rotate rx = new Rotate();
        { rx.setAxis(Rotate.X_AXIS); }
        public Rotate ry = new Rotate();
        { ry.setAxis(Rotate.Y_AXIS); }
        public Rotate rz = new Rotate();
        { rz.setAxis(Rotate.Z_AXIS); }
        public Scale s = new Scale();
        public RotateCube() { super(); getTransforms().addAll(t, p, rx, rz, ry, s, ip); }
    }
