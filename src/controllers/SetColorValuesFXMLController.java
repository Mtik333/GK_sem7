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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import loadingfile.Convertion;

/**
 * FXML Controller class
 *
 * @author walendziukm
 */
public class SetColorValuesFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    public Label rcLabel;
    @FXML
    public Label gmLabel;
    @FXML
    public Label byLabel;
    @FXML
    public Label kLabel;
    @FXML
    public TextField rcText;
    @FXML
    public TextField gmText;
    @FXML
    public TextField byText;
    @FXML
    public TextField kText;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (DataAccessor.isIfRGB()){
            rcLabel.setText("R:");
            gmLabel.setText("G:");
            byLabel.setText("B:");
            kLabel.setText("K:");
            rcText.setText(String.valueOf(DataAccessor.getRgbValues().get("r")));
            gmText.setText(String.valueOf(DataAccessor.getRgbValues().get("g")));
            byText.setText(String.valueOf(DataAccessor.getRgbValues().get("b")));
            kLabel.setDisable(true);
        }
        else {
            rcLabel.setText("C:");
            gmLabel.setText("M:");
            byLabel.setText("Y:");
            kLabel.setText("K:");
            rcText.setText(String.valueOf(DataAccessor.getCmykValues().get("c")));
            gmText.setText(String.valueOf(DataAccessor.getCmykValues().get("m")));
            byText.setText(String.valueOf(DataAccessor.getCmykValues().get("y")));
            kText.setText(String.valueOf(DataAccessor.getCmykValues().get("k")));
            kLabel.setDisable(false);
        }
    }

    
    @FXML
    public void convertValues(ActionEvent event){
        if (DataAccessor.isIfRGB()){
            DataAccessor.getRgbValues().replace("r", Integer.valueOf(rcText.getText()));
            DataAccessor.getRgbValues().replace("g", Integer.valueOf(gmText.getText()));
            DataAccessor.getRgbValues().replace("b", Integer.valueOf(byText.getText()));
            Convertion.convertToCMYK();
        }
        else{
            DataAccessor.getCmykValues().replace("c", Integer.valueOf(rcText.getText()));
            DataAccessor.getCmykValues().replace("m", Integer.valueOf(gmText.getText()));
            DataAccessor.getCmykValues().replace("y", Integer.valueOf(byText.getText()));
            DataAccessor.getCmykValues().replace("k", Integer.valueOf(kText.getText()));
            Convertion.convertToRGB();
        }
        Stage stage = (Stage) rcText.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void dismiss(ActionEvent event) {
        Stage stage = (Stage) rcText.getScene().getWindow();
        stage.close();
    }
}
