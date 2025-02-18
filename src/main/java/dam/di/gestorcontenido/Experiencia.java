/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dam.di.gestorcontenido;

/**
 * Representa una experiencia
 * Contiene el ID, título, descripción, dirección, imagen y coordenadas de una experiencia
 * @author ignac
 */
public class Experiencia {
    private long id;
    private String titulo;
    private String descripcion;
    private String direccion;
    private String imagen;
    private String coordenadas;

    /**
     * Constructor para crear una instancia de Experiencia
     * 
     * @param id Identificador único de la experiencia
     * @param titulo Título de la experiencia
     * @param descripcion Descripción de la experiencia
     * @param direccion Dirección donde se encuentra la experiencia
     * @param imagen Imagen de la experiencia
     * @param coordenadas Coordenadas donde se encuentra la experiencia
     */
    public Experiencia(long id, String titulo, String descripcion, String direccion, String imagen, String coordenadas) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.imagen = imagen;
        this.coordenadas = coordenadas;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
}
