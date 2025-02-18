/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package dam.di.gestorcontenido;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ignac
 */
public class MainController implements Initializable {

    @FXML
    private Button btnAniadir;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnSalir;
    @FXML
    private AnchorPane contenido;
    @FXML
    private Label lblTituloMain;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            File archivo = new File("src/main/java/dam/di/gestorcontenido/Aniadir.fxml");
            AnchorPane inicio = FXMLLoader.load(archivo.toURI().toURL());
            contenido.getChildren().setAll(inicio);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    


    @FXML
    private void handleSalirButton(ActionEvent event) {
    }

    @FXML
    private void handleAniadirAction(ActionEvent event) {
        try {
            File archivo = new File("src/main/java/dam/di/gestorcontenido/Aniadir.fxml");
            AnchorPane aniadir = FXMLLoader.load(archivo.toURI().toURL());
            contenido.getChildren().setAll(aniadir);
            Stage stage = (Stage) contenido.getScene().getWindow();
            stage.getIcons().clear();
            //Image icon = new Image(getClass().getResourceAsStream("img/iconoPer.png"));
            //stage.getIcons().add(icon);
            stage.setTitle("Crear Desafío");
            lblTituloMain.setText("Crear Desafío");
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleModificarButton(ActionEvent event) {
        try {
            File archivo = new File("src/main/java/dam/di/gestorcontenido/Modificar.fxml");
            AnchorPane modificar = FXMLLoader.load(archivo.toURI().toURL());
            contenido.getChildren().setAll(modificar);
            Stage stage = (Stage) contenido.getScene().getWindow();
            stage.getIcons().clear();
            //Image icon = new Image(getClass().getResourceAsStream("img/iconoPer.png"));
            //stage.getIcons().add(icon);
            stage.setTitle("Modificar Desafío");
            lblTituloMain.setText("Modificar Desafío");
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleEliminarButton(ActionEvent event) {
        try {
            File archivo = new File("src/main/java/dam/di/gestorcontenido/Eliminar.fxml");
            AnchorPane eliminar = FXMLLoader.load(archivo.toURI().toURL());
            contenido.getChildren().setAll(eliminar);
            Stage stage = (Stage) contenido.getScene().getWindow();
            stage.getIcons().clear();
            //Image icon = new Image(getClass().getResourceAsStream("img/iconoPer.png"));
            //stage.getIcons().add(icon);
            stage.setTitle("Eliminar Desafío");
            lblTituloMain.setText("Eliminar Desafío");
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
