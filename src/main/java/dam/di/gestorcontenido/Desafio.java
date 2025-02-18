/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dam.di.gestorcontenido;

/**
 *
 * @author ignac
 */
public class Desafio {
    private long id;
    private String titulo;
    private String descripcion;
    private String ciudad;
    private String etiquetas;
    private String experiencias;

    public Desafio(long id, String titulo, String descripcion, String ciudad, String etiquetas, String experiencias) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.etiquetas = etiquetas;
        this.experiencias = experiencias;
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

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getExperiencias() {
        return experiencias;
    }

    public void setExperiencias(String experiencias) {
        this.experiencias = experiencias;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
