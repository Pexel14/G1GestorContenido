/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package dam.di.gestorcontenido;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
 * Clase controladora para la vista de Eliminar.fxml
 *
 * @author Axel
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
    private Bucket bucket;
    private String nombreImagen;

    /**
     * Inicializa el punto de entrada de las dependencias de Firebase.
     * También inicializa las referencias de 'desafios' y 'experiencias' de la base de datos.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if(FirebaseApp.getApps().isEmpty()){
                FileInputStream credenciales = new FileInputStream("credenciales.json");
                //Construir las opciones de Firebase
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(credenciales))
                        .setDatabaseUrl("https://pipas-pruebas-default-rtdb.firebaseio.com/")
                        .setStorageBucket("pipas-pruebas.firebasestorage.app/")
                    .build();

                FirebaseApp.initializeApp(options);
            }
            
            db = FirebaseDatabase.getInstance();

            refDef = db.getReference("desafios");
            refExp = db.getReference("experiencias");
            
            bucket = StorageClient.getInstance().bucket("pipas-pruebas.firebasestorage.app");

            listaExp = new ArrayList<>();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AniadirController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AniadirController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método para eliminar el desafío seleccionado y sus experiencias relacionadas junto a las imagenes guardadas en Firebase Storage
     * @param event 
     */
    @FXML
    private void handleEliminarDesafioAction(ActionEvent event) {
        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmar borrado de desafío?");
        confirmar.showAndWait().ifPresent(respuesta -> {
            Blob imagenAntigua;
            if (respuesta == ButtonType.OK) {
                refDef.child(desafio.getTitulo()).removeValue((DatabaseError de, DatabaseReference dr) -> {
                    listaExp.remove(posicion);
                    limpiarDatos();
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Desafío borrado con exito");
                    alerta.showAndWait();
                });
                for(String experiencia : desafio.getExperiencias().split(",")){
                    
                    imagenAntigua = bucket.get(nombreImagen);

                    if(imagenAntigua != null){
                        imagenAntigua.delete();
                    }
                    
                    refExp.child(experiencia).removeValue((DatabaseError de, DatabaseReference dr) -> {});
                }
                Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Experiencia borrada con exito");
                alerta.showAndWait();
            }
        });
    }

    /**
     * Método para eliminar la experiencia seleccionada junto a la imagen en Firebase Storage
     * @param event 
     */
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
                    
                    Blob imagenAntigua = bucket.get(nombreImagen);

                    if(imagenAntigua != null){
                        imagenAntigua.delete();
                    }
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Experiencia borrada con exito");
                    alerta.showAndWait();
                });
            }
        });
    }

    /**
     * Cambia la experiencia que se visualiza en los componentes de la interfaz
     * @param event 
     */
    @FXML
    private void handleAnteriorExperienciaAction(ActionEvent event) {
        if (posicion == 0) {
            posicion = listaExp.size() - 1;
        } else {
            posicion--;
        }

        insertarExperiencia(posicion);

    }

    /**
     * Cambia la experiencia que se visualiza en los componentes de la interfaz
     * @param event 
     */
    @FXML
    private void handleSiguienteExperienciaAction(ActionEvent event) {
        if (posicion == listaExp.size() - 1) {
            posicion = 0;
        } else {
            posicion++;
        }

        insertarExperiencia(posicion);

    }

    /**
     * Busca en la base de datos el desafío que ha introducido el usuario
     * @param event 
     */
    @FXML
    private void handleBtnBuscarAction(ActionEvent event) {
        String titulo = tfDesafio.getText();
        if (!titulo.isEmpty()) {
            refDef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    String tituloDes = null;
                    String experiencias = null;
                    long id;
                    boolean encontrado = false;
                    for (DataSnapshot snapshot : ds.getChildren()) {
                        tituloDes = snapshot.child("titulo").getValue(String.class);
                        if (tituloDes.equals(titulo)) {
                            experiencias = snapshot.child("experiencias").getValue(String.class);
                            id = snapshot.child("id").getValue(Long.class);
                            nombreImagen = "imagenes/" + titulo + id + ".png"; 
                            desafio = new Desafio(
                                    id,
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

    /**
     * Muestra los componentes de la interfaz
     * @param mostrar 
     */
    public void mostrarBotones(boolean mostrar) {
        btnAnteriorExperiencia.setDisable(!mostrar);
        btnEliminarDesafio.setDisable(!mostrar);
        btnEliminarExperiencia.setDisable(!mostrar);
        btnSiguienteExperiencia.setDisable(!mostrar);
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

    /**
     * Rellena una lista con las experiencias del desafío encontrado
     * @param experiencias Experiencias relacionadas al desafío
     */
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

    /**
     * Inserta la experiencia seleccionada en los componentes de la interfaz
     * @param index Posición de la experiencia
     */
    public void insertarExperiencia(int index) {
        tfTitulo.setText(listaExp.get(index).getTitulo());
        tfDescripcion.setText(listaExp.get(index).getDescripcion());
        tfCiudad.setText(listaExp.get(index).getDireccion());
        tfLatitud.setText(listaExp.get(index).getCoordenadas().split(",")[0]);
        tfLongitud.setText(listaExp.get(index).getCoordenadas().split(",")[1]);
    }

    /**
     * Limpia los datos de la interfaz
     */
    public void limpiarDatos() {
        tfTitulo.setText("");
        tfDescripcion.setText("");
        tfCiudad.setText("");
        tfLatitud.setText("");
        tfLongitud.setText("");
    }

    /**
     * Quita la experiencia eliminada del desafío
     * @param id_exp ID de la experiencia eliminada
     */
    private void actualizarDesafio(long id_exp) {
        if(desafio.getExperiencias().contains(String.valueOf(id_exp))){
            String experiencias = "";
            String [] aux = desafio.getExperiencias().split(",");
            
            for (int i = 0; i < aux.length; i++) {
                if(!aux[i].equals(String.valueOf(id_exp))){
                    experiencias += aux[i];
                }
            }
            
            if(!experiencias.isEmpty()){
                experiencias = experiencias.substring(0, experiencias.length() -1);
            }
            
            desafio.setExperiencias(experiencias);
            
            CompletableFuture.runAsync(() ->{
                try {
                    ApiFuture<Void>future =  refDef.child(desafio.getTitulo()).setValueAsync(desafio);
                    future.get();

                } catch (InterruptedException ex) {
                    Logger.getLogger(AniadirController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(AniadirController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
        }
    }

}
