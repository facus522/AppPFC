package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facu Salmerón 6/8/2018.
 */

public class InfoNoticiaDto {

    private Integer id;
    private String titulo;
    private String descripcion;
    private String url;

    public String getTitulo() {
        return titulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
