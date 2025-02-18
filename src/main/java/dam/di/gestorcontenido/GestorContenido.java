/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package dam.di.gestorcontenido;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase ejecutable
 * @author Axel
 */
public class GestorContenido extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            File archivo = new File("src/main/java/dam/di/gestorcontenido/Main.fxml");
            Parent loader = FXMLLoader.load(archivo.toURI().toURL());

            Scene scene = new Scene(loader);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException ex) {
            Logger.getLogger(GestorContenido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
