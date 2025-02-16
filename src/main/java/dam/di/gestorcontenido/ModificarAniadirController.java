/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package dam.di.gestorcontenido;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author ignac
 */
public class ModificarAniadirController implements Initializable {

    @FXML
    private TextField tfTituloDes;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextArea taDescripcionDes;
    @FXML
    private CheckBox chkCultura;
    @FXML
    private CheckBox chkArte;
    @FXML
    private CheckBox chkOcio;
    @FXML
    private CheckBox chkGastronomia;
    @FXML
    private TextField tfTituloExp;
    @FXML
    private TextArea taDescripcionExp;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfLatitud;
    @FXML
    private TextField tfLongitud;
    @FXML
    private ImageView ivExperiencia;
    @FXML
    private Button btnAniadirExp;
    @FXML
    private Button btnAniadirDesafio;
    @FXML
    private Button btnCrearDes;
    @FXML
    private Button btnImagen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleBtnAniadirExpAction(ActionEvent event) {
    }

    @FXML
    private void handleBtnAniadirDesafioAction(ActionEvent event) {
    }

    @FXML
    private void handleBtnCrearDesafioAction(ActionEvent event) {
    }

    @FXML
    private void handleInsertarImagenAction(ActionEvent event) {
    }
    
}
