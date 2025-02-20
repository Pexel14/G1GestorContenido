/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package dam.di.gestorcontenido;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * Clase controladora para la vista Aniadir.fxml
 *
 * @author Axel
 */
public class AniadirController implements Initializable {

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
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblTituloExp;
    @FXML
    private Label lblDescripcion;
    @FXML
    private Label lblDireccion;
    @FXML
    private Label lblCoordenadas;
    
    /**
     * Instancia de la base de datos en Firebase Database
     */
    private FirebaseDatabase db;
    
    /**
     * Referencia al nodo 'desafios' en la base de datos
     */
    private DatabaseReference refDef;
    
    /**
     * Referencia al nodo 'experiencias' en la base de datos
     */
    private DatabaseReference refExp;
    
    /**
     * Objeto de tipo Desafio
     */
    private Desafio desafio;
    
    /**
     * Lista de experiencias
     */
    private ArrayList<Experiencia> listaExp;
    
    /**
     * Nombre de la imagen almacenada en Firebase Storage
     */
    private String nombreImagen;
    
    /**
     * Imagen inicial del botón
     */
    private Image imagenDefault;
    
    /**
     * Bucket que contiene imágenes en Firebase Storage
     */
    private Bucket bucket;
    
    /**
     * Identificador de la experiencia
     */
    private long id_exp;

    /**
     * Inicializa el punto de entrada de las dependencias de Firebase.
     * También inicializa las referencias de 'desafios' y 'experiencias' de la base de datos junto al Bucket para subir imágenes.
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
            imagenDefault = ivExperiencia.getImage();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AniadirController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AniadirController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * Método para crear un desafío
     * @param event 
     */
    @FXML
    private void handleBtnCrearDesafioAction(ActionEvent event) {
        String titulo = tfTituloDes.getText().trim();
        if (!titulo.isEmpty()) {
            System.out.println("PASA POR AQUI 1");
            refDef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
            System.out.println("PASA POR AQUI 2");
                    String tituloDes = null;
                    boolean encontrado = false;
                    
                    for (DataSnapshot snapshot : ds.getChildren()) {
                        tituloDes = snapshot.child("titulo").getValue(String.class);
                        if (tituloDes.equals(titulo)) {
                            encontrado = true;
                        }
                    }

                    if (!encontrado) {
                                    System.out.println("PASA POR AQUI 3");
                        String descripcion = taDescripcionDes.getText().trim();

                        if (!descripcion.isEmpty()) {
                            String ciudad = tfCiudad.getText().trim();

                            if (!ciudad.isEmpty()) {
                                            System.out.println("PASA POR AQUI 3.5");
                                ArrayList<String> listaEtiquetas = new ArrayList<>();

                                if (chkArte.isSelected()) {
                                    listaEtiquetas.add(chkArte.getText());
                                }

                                if (chkCultura.isSelected()) {
                                    listaEtiquetas.add(chkCultura.getText());
                                }

                                if (chkGastronomia.isSelected()) {
                                    listaEtiquetas.add(chkGastronomia.getText());
                                }

                                if (chkOcio.isSelected()) {
                                    listaEtiquetas.add(chkOcio.getText());
                                }
                                String etiquetas = "";
                                if (!listaEtiquetas.isEmpty()) {
                                                System.out.println("PASA POR AQUI 4");
                                    for (String etiqueta : listaEtiquetas) {
                                        etiquetas += etiqueta + ",";
                                    }
                                    etiquetas = etiquetas.substring(0, etiquetas.length() - 1);

                                    long id = 0;
                                    for (DataSnapshot dataSnapshot : ds.getChildren()) {
                                        id = dataSnapshot.child("id").getValue(Integer.class);
                                    }
                                    
                                    nombreImagen = "imagenes/" + titulo;
                                    
                                    desafio = new Desafio(id, titulo, descripcion, ciudad, etiquetas, "");
                                    System.out.println("PASA POR AQUI 5");
                                    mostrarExperiencias(true);
                                    mostrarMensaje("Añada experiencias al desafío", 0);
                                    
                                } else {
                                    mostrarMensaje("Es necesario escoger etiquetas para el desafío", 1);
                                }
                                
                            } else {
                                mostrarMensaje("Añada una ciudad al desafío", 1);
                            }

                        } else {
                            mostrarMensaje("Debe añadir una descripción", 1);
                        }

                    } else {
                        mostrarMensaje("Ya existe un desafío con este título", 2);
                        
                    }

                }

                @Override
                public void onCancelled(DatabaseError de) {

                }
            });
        } else {
            mostrarMensaje("Necesitas introducir un titulo al desafío a añadir", 1);
        }
    }

    /**
     * Método para añadir experiencias al desafío
     * @param event 
     */
    @FXML
    private void handleBtnAniadirExpAction(ActionEvent event) {
        String titulo = tfTituloExp.getText().trim();
        
        if(!titulo.isEmpty()){
            
                String descripcion = taDescripcionExp.getText().trim();
                
                if(!descripcion.isEmpty()){
                    
                    String direccion = tfDireccion.getText().trim();
                    
                    if(!direccion.isEmpty()){
                        
                        String latitud = tfLatitud.getText().trim();
                        String longitud = tfLongitud.getText().trim();
                        
                        if(!latitud.isEmpty() || !longitud.isEmpty()){
                            String coordenadas = latitud + "," + longitud;
                            
                            
                            Blob blob = bucket.get(nombreImagen);
                            System.out.println("IMAGEN AÑADIR: " + nombreImagen);
                            
                            nombreImagen = nombreImagen.substring(0, nombreImagen.lastIndexOf("_"));
                            if(blob != null){
                                
                                if(listaExp.isEmpty()){
                                    refExp.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot ds) {
                                            long id = 0;
                                            
                                            for (DataSnapshot snapshot : ds.getChildren()) {
                                                id = snapshot.child("id").getValue(Long.class);
                                            }
                                            id++;
                                            id_exp = id;
                                            listaExp.add(new Experiencia(id, titulo, descripcion, direccion, blob.getMediaLink(), coordenadas));
                                            btnAniadirDesafio.setVisible(true);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError de) {

                                        }
                                    });
                                } else {
                                    long id = listaExp.get(listaExp.size() - 1).getId() + 1;
                                    System.out.println("IMAGEN: " + blob.getMediaLink());
                                    listaExp.add(new Experiencia(id, titulo, descripcion, direccion, blob.getMediaLink(), coordenadas));
                                }
                                mostrarMensaje("Experiencia añadida con exito", 0);
                                limpiarCamposExp();
                            } else {
                                mostrarMensaje("Debes añadir una imágen", 1);
                            }
                            
                        } else {
                            mostrarMensaje("Debes rellenar la latitud y la longitud", 1);
                        }
                        
                    } else {
                        mostrarMensaje("Introduce la dirección", 1);
                    }
                    
                } else {
                    mostrarMensaje("Introduce una descripción", 1);
                }
                
        } else {
            mostrarMensaje("Es necesario que la experiencia tenga título", 1);
        }
    }

    /**
     * Método para subir el desafío y las experiencias a la base de datos en Firebase
     * @param event 
     */
    @FXML
    private void handleBtnAniadirDesafioAction(ActionEvent event) {
        String experiencias = "";
            for (int i = 0; i < listaExp.size(); i++) {
                if(i == listaExp.size() - 1){
                    experiencias += String.valueOf(listaExp.get(i).getId());                    
                } else {
                    experiencias += String.valueOf(listaExp.get(i).getId()) + ",";
                }
                System.out.println("EXPERIENCIAS DESAFIO: " + experiencias);
            }
            
            desafio.setExperiencias(experiencias);
        
        CompletableFuture.runAsync(() ->{
            try {
                ApiFuture<Void>future =  refDef.child(desafio.getTitulo()).setValueAsync(desafio);
                future.get();
                
                ApiFuture<Void> future2;
                for(Experiencia exp : listaExp){
                    future2 = refExp.child(String.valueOf(exp.getId())).setValueAsync(exp);
                    Thread.sleep(3000);
                    future2.get();
                }
                
                mostrarExperiencias(false);
                limpiarCamposDesafio();
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Desafio creado con exito");
                alerta.showAndWait();
                
            } catch (InterruptedException ex) {
                Logger.getLogger(AniadirController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(AniadirController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Método para subir una imágen a Firebase Storage
     * @param event 
     */
    @FXML
    private void handleInsertarImagenAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecciona una imagen");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("imagenes","*.png"));
        
        File img = fc.showOpenDialog(null);
        
        if(img != null){
            if(!listaExp.isEmpty()){
                id_exp = listaExp.get(listaExp.size()-1).getId();
            }
            
            nombreImagen += "_" + id_exp + ".png";
            
            System.out.println("IMAGEN AÑADIR 2: " + nombreImagen);
            
            ivExperiencia.setImage(new Image(img.toURI().toString()));

            Bucket.BlobWriteOption precondition = Bucket.BlobWriteOption.doesNotExist();
            
             try {
                 bucket.create(nombreImagen, new FileInputStream(img),  "image/png", precondition);
             } catch (IOException ex) {
                 Logger.getLogger(AniadirController.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
    }

    /**
     * Método para habilitar añadir experiencias
     * @param mostrar 
     */
    public void mostrarExperiencias(boolean mostrar) {
        lblTituloExp.setVisible(mostrar);
        lblTitulo.setVisible(mostrar);
        tfTituloExp.setVisible(mostrar);
        lblDescripcion.setVisible(mostrar);
        taDescripcionExp.setVisible(mostrar);
        lblCoordenadas.setVisible(mostrar);
        lblDireccion.setVisible(mostrar);
        tfDireccion.setVisible(mostrar);
        tfLatitud.setVisible(mostrar);
        tfLongitud.setVisible(mostrar);
        btnAniadirExp.setVisible(mostrar);
        btnCrearDes.setDisable(mostrar);
        tfTituloDes.setDisable(mostrar);
        tfCiudad.setDisable(mostrar);
        taDescripcionDes.setDisable(mostrar);
        chkArte.setDisable(mostrar);
        chkOcio.setDisable(mostrar);
        chkGastronomia.setDisable(mostrar);
        chkCultura.setDisable(mostrar);
        btnImagen.setVisible(mostrar);
    }

    /**
     * Método para reiniciar los campos al añadir una experiencia
     */
    public void limpiarCamposExp() {
        tfTituloExp.setText("");
        taDescripcionExp.setText("");
        tfDireccion.setText("");
        ivExperiencia.setImage(imagenDefault);
        tfLatitud.setText("");
        tfLongitud.setText("");
    }

    /**
     * Método para mostrar mensajes emergentes
     * @param mensaje Mensaje que se va a mostrar en el Alert
     * @param tipo Sirve para indicar si el Alert va a ser informativo o de error
     */
    public void mostrarMensaje(String mensaje, int tipo){
        Alert alerta;
        if(tipo == 1){
            alerta = new Alert(Alert.AlertType.ERROR, mensaje);
        } else {
            alerta = new Alert(Alert.AlertType.INFORMATION, mensaje);
        }
        alerta.showAndWait();
    }

    /**
     * Limpia los campos del desafíos
     */
    private void limpiarCamposDesafio() {
        tfCiudad.setText("");
        tfTituloDes.setText("");
        taDescripcionDes.setText("");
        chkArte.setSelected(false);
        chkOcio.setSelected(false);
        chkGastronomia.setSelected(false);
        chkCultura.setSelected(false);
    }
    
}