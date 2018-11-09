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
    private Boolean geolocalizada;
    private Integer isSexoRestriccion;
    private Integer isEdadRestriccion;
    private Boolean habilitada;

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

    public Boolean getGeolocalizada() {
        return geolocalizada;
    }

    public void setGeolocalizada(Boolean geolocalizada) {
        this.geolocalizada = geolocalizada;
    }

    public Integer getIsSexoRestriccion() {
        return isSexoRestriccion;
    }

    public void setIsSexoRestriccion(Integer isSexoRestriccion) {
        this.isSexoRestriccion = isSexoRestriccion;
    }

    public Integer getIsEdadRestriccion() {
        return isEdadRestriccion;
    }

    public void setIsEdadRestriccion(Integer isEdadRestriccion) {
        this.isEdadRestriccion = isEdadRestriccion;
    }

    public Boolean getHabilitada() {
        return habilitada;
    }

    public void setHabilitada(Boolean habilitada) {
        this.habilitada = habilitada;
    }
}
