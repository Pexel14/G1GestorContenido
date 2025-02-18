/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package dam.di.gestorcontenido;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ignac
 */
public class EliminarController implements Initializable {

    @FXML
    private TextField tfDesafio;
    @FXML
    private Label lblDesafio;
    @FXML
    private Button btnEliminarDesafio;
    @FXML
    private Button btnEliminarExperiencia;
    @FXML
    private TextField tfTitulo;
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfLongitud;
    @FXML
    private TextField tfLatitud;
    @FXML
    private Button btnAnteriorExperiencia;
    @FXML
    private Button btnSiguienteExperiencia;

    private FirebaseDatabase db;
    private DatabaseReference refDef;
    private DatabaseReference refExp;
    private Desafio desafio;
    private ArrayList<Experiencia> listaExp;
    @FXML
    private Button btnBuscar;
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblDescripcion;
    @FXML
    private Label lblCiudad;
    @FXML
    private Label lblCoordenadas;

    private static int posicion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if(FirebaseApp.getApps().isEmpty()){
                FileInputStream credenciales = new FileInputStream("credenciales.json");
                //Construir las opciones de Firebase
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(credenciales))
                        .setDatabaseUrl("https://pipas-pruebas-default-rtdb.firebaseio.com/").build();

                FirebaseApp.initializeApp(options);
            }
            
            db = FirebaseDatabase.getInstance();

            refDef = db.getReference("desafios");
            refExp = db.getReference("experiencias");

            listaExp = new ArrayList<>();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ModificarAniadirController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ModificarAniadirController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleEliminarDesafioAction(ActionEvent event) {
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmar borrado de desafío?");
        confirmar.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                refDef.child(desafio.getTitulo()).removeValue((DatabaseError de, DatabaseReference dr) -> {
                    listaExp.remove(posicion);
                    limpiarDatos();
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Desafío borrado con exito");
                    alerta.showAndWait();
                });
                for(String experiencia : desafio.getExperiencias().split(",")){
                    refExp.child(experiencia).removeValue((DatabaseError de, DatabaseReference dr) -> {});
                }
                Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Experiencia borrada con exito");
                alerta.showAndWait();
            }
        });
    }

    @FXML
    private void handleEliminarExperienciaAction(ActionEvent event) {
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmar borrado de experiencia?");
        confirmar.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                refExp.child(String.valueOf(listaExp.get(posicion).getId())).removeValue((DatabaseError de, DatabaseReference dr) -> {
                    if(!listaExp.isEmpty()){
                        insertarExperiencia(0);
                    }
                    actualizarDesafio(listaExp.get(posicion).getId());
                    listaExp.remove(posicion);
                    limpiarDatos();
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Experiencia borrada con exito");
                    alerta.showAndWait();
                });
            }
        });
    }

    @FXML
    private void handleAnteriorExperienciaAction(ActionEvent event) {
        if (posicion == 0) {
            posicion = listaExp.size() - 1;
        } else {
            posicion--;
        }

        insertarExperiencia(posicion);

    }

    @FXML
    private void handleSiguienteExperienciaAction(ActionEvent event) {
        if (posicion == listaExp.size() - 1) {
            posicion = 0;
        } else {
            posicion++;
        }

        insertarExperiencia(posicion);

    }

    @FXML
    private void handleBtnBuscarAction(ActionEvent event) {
        String titulo = tfDesafio.getText();
        if (!titulo.isEmpty()) {
            refDef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    String tituloDes = null;
                    String experiencias = null;
                    boolean encontrado = false;
                    for (DataSnapshot snapshot : ds.getChildren()) {
                        tituloDes = snapshot.child("titulo").getValue(String.class);
                        if (tituloDes.equals(titulo)) {
                            experiencias = snapshot.child("experiencias").getValue(String.class);
                            
                            desafio = new Desafio(
                                    snapshot.child("id").getValue(Long.class),
                                    tituloDes, 
                                    snapshot.child("descripcion").getValue(String.class), 
                                    snapshot.child("ciudad").getValue(String.class), 
                                    snapshot.child("etiquetas").getValue(String.class), 
                                    experiencias
                            );
                            
                            encontrado = true;
                        }
                    }

                    if (encontrado) {
                        mostrarBotones(true);
                        rellenarContenido(experiencias.split(","));
                    } else {
                        Alert alerta = new Alert(Alert.AlertType.ERROR, "No se ha encontrado un desafío con ese titulo");
                        alerta.showAndWait();
                    }

                }

                @Override
                public void onCancelled(DatabaseError de) {

                }
            });
        }
    }

    public void mostrarBotones(boolean mostrar) {
        btnAnteriorExperiencia.setVisible(mostrar);
        btnEliminarDesafio.setVisible(mostrar);
        btnEliminarExperiencia.setVisible(mostrar);
        btnSiguienteExperiencia.setVisible(mostrar);
        lblDesafio.setVisible(mostrar);
        lblCiudad.setVisible(mostrar);
        lblCoordenadas.setVisible(mostrar);
        lblDesafio.setVisible(mostrar);
        lblTitulo.setVisible(mostrar);
        tfCiudad.setVisible(mostrar);
        tfDesafio.setVisible(mostrar);
        tfDescripcion.setVisible(mostrar);
        tfLatitud.setVisible(mostrar);
        tfLongitud.setVisible(mostrar);
        tfTitulo.setVisible(mostrar);
    }

    public void rellenarContenido(String[] experiencias) {
        refExp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                String id = "";
                for (DataSnapshot snapshot : ds.getChildren()) {
                    id = snapshot.getKey();
                    for (int i = 0; i < experiencias.length; i++) {
                        if (id.equals(experiencias[i])) {
                            listaExp.add(new Experiencia(
                                    Long.parseLong(id),
                                    snapshot.child("titulo").getValue(String.class),
                                    snapshot.child("descripcion").getValue(String.class),
                                    snapshot.child("direccion").getValue(String.class),
                                    "TODO",
                                    snapshot.child("coordenadas").getValue(String.class)
                            ));
                        }
                    }
                }
                if (!listaExp.isEmpty()) {
                    posicion = 0;
                    insertarExperiencia(posicion);
                }
            }

            @Override
            public void onCancelled(DatabaseError de) {

            }
        });
    }

    public void insertarExperiencia(int index) {
        tfTitulo.setText(listaExp.get(index).getTitulo());
        tfDescripcion.setText(listaExp.get(index).getDescripcion());
        tfCiudad.setText(listaExp.get(index).getDireccion());
        tfLatitud.setText(listaExp.get(index).getCoordenadas().split(",")[0]);
        tfLongitud.setText(listaExp.get(index).getCoordenadas().split(",")[1]);
    }

    public void limpiarDatos() {
        tfTitulo.setText("");
        tfDescripcion.setText("");
        tfCiudad.setText("");
        tfLatitud.setText("");
        tfLongitud.setText("");
    }

    private void actualizarDesafio(long id_exp) {
        
    }

}
