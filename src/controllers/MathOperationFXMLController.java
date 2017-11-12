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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mateusz
 */
public class MathOperationFXMLController implements Initializable {

    @FXML
    public ComboBox mathOperation;
    @FXML
    public TextField rField;
    @FXML
    public TextField bField;
    @FXML
    public TextField gField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mathOperation.getItems().addAll("Sum", "Subtract", "Multiply", "Divide");
        rField.setText(String.valueOf(1));
        gField.setText(String.valueOf(1));
        bField.setText(String.valueOf(1));
    }

    @FXML
    public void dismiss(ActionEvent event) {
        DataAccessor.rgbValues.put("r", Integer.parseInt(rField.getText()));
        DataAccessor.rgbValues.put("g", Integer.parseInt(gField.getText()));
        DataAccessor.rgbValues.put("b", Integer.parseInt(bField.getText()));
        switch (mathOperation.getSelectionModel().getSelectedIndex()) {
            case 0:
                DataAccessor.setMathOperation("Sum");
                break;
            case 1:
                DataAccessor.setMathOperation("Subtract");
                break;
            case 2:
                DataAccessor.setMathOperation("Multiply");
                break;
            case 3:
                DataAccessor.setMathOperation("Divide");
                break;

        }
        Stage stage = (Stage) gField.getScene().getWindow();
        stage.close();
    }
}
