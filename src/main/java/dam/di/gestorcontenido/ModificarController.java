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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Clase controladora de la vista Modificar.fxml
 *
 * @author Axel
 */
public class ModificarController implements Initializable {

    @FXML
    private TextField tfDesafio;
    @FXML
    private Label lblDesafio;
    @FXML
    private Label lblTitulo;
    @FXML
    private TextField tfTitulo;
    @FXML
    private Label lblDescripcion;
    @FXML
    private TextArea tfDescripcionExp;
    @FXML
    private Label lblCiudad;
    @FXML
    private Label lblCoordenadas;
    @FXML
    private TextField tfLongitud;
    @FXML
    private TextField tfLatitud;
    @FXML
    private Button btnAnteriorExperiencia;
    @FXML
    private Button btnSiguienteExperiencia;
    @FXML
    private Button btnBuscar;
    @FXML
    private Label lblDescripcion1;
    @FXML
    private TextArea tfDescripcionDes;
    @FXML
    private Label lblCiudad1;
    @FXML
    private TextField tfCiudadDes;
    @FXML
    private CheckBox chkCultura;
    @FXML
    private CheckBox chkArte;
    @FXML
    private CheckBox chkOcio;
    @FXML
    private CheckBox chkGastronomia;
    @FXML
    private Button btnModDesafio;
    @FXML
    private Button btnModExperiencia;
    @FXML
    private TextField tfDireccionExp;
    @FXML
    private Button btnImagen;
    @FXML
    private ImageView ivExperiencia;
    
    private FirebaseDatabase db;
    private DatabaseReference refDef;
    private DatabaseReference refExp;
    private Desafio desafio;
    private ArrayList<Experiencia> listaExp;
    private String nombreImagen;
    private Image imagenDefault;
    private String imagen;
    private Bucket bucket;
    private int posicion;

    
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
                        .setStorageBucket("pipas-pruebas.firebasestorage.app")
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
            Logger.getLogger(ModificarAniadirController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ModificarAniadirController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * Método para mover la lista mostrada de las experiencias para atrás
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
     * Método para mover la lista mostrada de las experiencias para adelante
     * @param event 
     */
    @FXML
    private void handleSiguienteExperienciaAction(ActionEvent event) {
        if (posicion == listaExp.size() -1) {
            posicion = 0;
        } else {
            posicion++;
        }

        insertarExperiencia(posicion);

    }

    /**
     * Método para buscar el desafío introducido por el usuario dentro de la base de datos
     * @param event 
     */
    @FXML
    private void handleBtnBuscarAction(ActionEvent event) {
        String titulo = tfDesafio.getText();
        if (!titulo.isEmpty()) {
            refDef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    boolean encontrado = false;
                    long id;
                    String tituloDes = null;
                    String experiencias = null;
                    String etiquetas;
                    String descripcion;
                    String ciudad;
                    for (DataSnapshot snapshot : ds.getChildren()) {
                        tituloDes = snapshot.child("titulo").getValue(String.class);
                        if (tituloDes.equals(titulo)) {
                            etiquetas = snapshot.child("etiquetas").getValue(String.class);
                            descripcion = snapshot.child("descripcion").getValue(String.class);
                            ciudad = snapshot.child("ciudad").getValue(String.class);
                            tfDescripcionDes.setText(descripcion);
                            tfCiudadDes.setText(ciudad);
                            
                            if(etiquetas.contains("Gastronomía")){
                                chkGastronomia.setSelected(true);
                            }

                            if(etiquetas.contains("Arte")){
                                chkArte.setSelected(true);
                            }

                            if(etiquetas.contains("Ocio")){
                                chkOcio.setSelected(true);
                            }

                            if(etiquetas.contains("Cultura")){
                                chkCultura.setSelected(true);
                            }                            
                            encontrado = true;
                            experiencias = snapshot.child("experiencias").getValue(String.class);
                            id = snapshot.child("id").getValue(Long.class);
                            desafio = new Desafio(
                                    id,
                                    tituloDes, 
                                    descripcion, 
                                    ciudad, 
                                    etiquetas, 
                                    experiencias
                            );
                            nombreImagen = titulo + id;
                        }
                    }

                    if (encontrado) {
                        habilitarBotones(false);
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
     * Método para añadir las modificaciones realizadas en el desafío a la base de datos
     * @param event 
     */
    @FXML
    private void handleBtnModDesafioAction(ActionEvent event) {
        String ciudad = tfCiudadDes.getText().trim();
        String descripcion = tfDescripcionDes.getText().trim();
        String etiquetas = "";
        
        if(!ciudad.isEmpty()){
            if(chkArte.isSelected()){
                etiquetas += "Arte,";
            }

            if(chkCultura.isSelected()){
                etiquetas += "Cultura,";
            }        

            if(chkGastronomia.isSelected()){
                etiquetas += "Gastronomía,";
            }

            if(chkOcio.isSelected()){
                etiquetas += "Ocio,";
            }

            if(!etiquetas.isEmpty()){
                etiquetas = etiquetas.substring(0, etiquetas.length() - 1);

                desafio.setCiudad(ciudad);
                desafio.setDescripcion(descripcion);
                desafio.setEtiquetas(etiquetas);
                
                CompletableFuture.runAsync(() ->{
                    try {
                        ApiFuture<Void>future =  refDef.child(desafio.getTitulo()).setValueAsync(desafio);
                        future.get();

                    } catch (InterruptedException ex) {
                        Logger.getLogger(ModificarAniadirController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ExecutionException ex) {
                        Logger.getLogger(ModificarAniadirController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Desafio modificado con éxito");
                alerta.showAndWait();
            }
            
        }
        
    }

    /**
     * Método para añadir las modificaciones de la experiencia seleccionada a la base de datos
     * @param event 
     */
    @FXML
    private void handleBtnModExperienciaAction(ActionEvent event) {
        String titulo = tfTitulo.getText();
        String descripcion = tfDescripcionExp.getText();
        String direccion = tfDireccionExp.getText();
        String latitud = tfLatitud.getText();
        String longitud = tfLongitud.getText();
        
        if(!titulo.isEmpty()){
            if(!descripcion.isEmpty()){
                if(!direccion.isEmpty()){
                    listaExp.get(posicion).setTitulo(titulo);
                    listaExp.get(posicion).setDescripcion(descripcion);
                    listaExp.get(posicion).setDireccion(direccion);
                    
                    if(!latitud.isEmpty() && !longitud.isEmpty()){
                        listaExp.get(posicion).setCoordenadas(latitud + "," +longitud);
                    }
                    
                    CompletableFuture.runAsync(() ->{
                        try {
                            ApiFuture<Void>future =  refExp.child(String.valueOf(listaExp.get(posicion).getId())).setValueAsync(listaExp.get(posicion));
                            future.get();

                        } catch (InterruptedException ex) {
                            Logger.getLogger(ModificarAniadirController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ExecutionException ex) {
                            Logger.getLogger(ModificarAniadirController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Experiencia modificada con éxito");
                    alerta.showAndWait();
                    
                }
            }
        }
    }
    
    /**
     * Método para rellenar una lista de experiencias con las experiencias del desafío buscado
     * @param experiencias Array con las experiencias de un desafío
     */
    public void rellenarContenido(String[] experiencias) {
        listaExp.clear();
        refExp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                String id = "";
                for (DataSnapshot snapshot : ds.getChildren()) {
                    id = snapshot.getKey();
                    System.out.println("ID exp: " + id);
                    System.out.println("Experiencias: " + Arrays.toString(experiencias));
                    for (int i = 0; i < experiencias.length; i++) {
                        if (id.equals(experiencias[i])) {
                            listaExp.add(new Experiencia(
                                    Long.parseLong(id),
                                    snapshot.child("titulo").getValue(String.class),
                                    snapshot.child("descripcion").getValue(String.class),
                                    snapshot.child("direccion").getValue(String.class),
                                    snapshot.child("imagen").getValue(String.class),
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
     * Método para introducir el contenido de una experiencia en los campos de la vista
     * @param index Índice para buscar en la lista de experiencias
     */
    public void insertarExperiencia(int index) {
        tfTitulo.setText(listaExp.get(index).getTitulo());
        tfDescripcionExp.setText(listaExp.get(index).getDescripcion());
        tfDireccionExp.setText(listaExp.get(index).getDireccion());
        imagen = listaExp.get(index).getImagen();
        ivExperiencia.setImage(new Image(imagen));
        tfLatitud.setText(listaExp.get(index).getCoordenadas().split(",")[0]);
        tfLongitud.setText(listaExp.get(index).getCoordenadas().split(",")[1]);
    }
    
    /**
     * Método para habilitar los componentes de la interfaz
     * @param habilitar 
     */
    public void habilitarBotones(boolean habilitar){
        tfCiudadDes.setDisable(habilitar);
        tfDireccionExp.setDisable(habilitar);
        tfDescripcionDes.setDisable(habilitar);
        tfDescripcionExp.setDisable(habilitar);
        tfLatitud.setDisable(habilitar);
        tfLongitud.setDisable(habilitar);
        tfTitulo.setDisable(habilitar);
        chkArte.setDisable(habilitar);
        chkCultura.setDisable(habilitar);
        chkGastronomia.setDisable(habilitar);
        chkOcio.setDisable(habilitar);
        btnAnteriorExperiencia.setDisable(habilitar);
        btnModDesafio.setDisable(habilitar);
        btnModExperiencia.setDisable(habilitar);
        btnSiguienteExperiencia.setDisable(habilitar);
        btnImagen.setDisable(habilitar);
    }

    /**
     * Método para subir una imagen a Firebase Storage, reemplazando la imagen antigua
     * @param event 
     */
    @FXML
    private void handleInsertarImagenAction(ActionEvent event) {
                 FileChooser fc = new FileChooser();
        fc.setTitle("Selecciona una imagen");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("imagenes","*.png"));
        
        File img = fc.showOpenDialog(null);
        
        if(img != null){
            
            String nuevoArchivo = nombreImagen + ".png";
            
            File destino = new File("src/main/java/dam/di/gestorcontenido/img/");
            
            File archivo = new File(destino, nuevoArchivo);
            
            ivExperiencia.setImage(new Image(archivo.toURI().toString()));

            Bucket.BlobWriteOption precondition = Bucket.BlobWriteOption.doesNotExist();
            
            Blob imagenAntigua = bucket.get(nuevoArchivo);
            
            if(imagenAntigua != null){
                imagenAntigua.delete();
            }
            
             try {
                bucket.create(nuevoArchivo, new FileInputStream(img), precondition);
             } catch (IOException ex) {
                 Logger.getLogger(ModificarAniadirController.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
    }
}
