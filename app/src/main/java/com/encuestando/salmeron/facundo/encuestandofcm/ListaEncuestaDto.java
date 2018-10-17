package com.encuestando.salmeron.facundo.encuestandofcm;

import java.io.Serializable;

/**
 * Created by Facundo Salmerón on 30/9/2018.
 */

public class ListaEncuestaDto implements Serializable{

    private Integer id;
    private String titulo;
    private String descripcion;
    private String fechaCreacion;
    private String resoluciones;
    private String usuarioCreacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getResoluciones() {
        return resoluciones;
    }

    public void setResoluciones(String resoluciones) {
        this.resoluciones = resoluciones;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }
}
