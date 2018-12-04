package com.encuestando.salmeron.facundo.encuestandofcm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EncuestaJson implements Serializable{

    private Integer id;
    private String titulo;
    private String descripcion;
    private Integer idUsuarioAlta;
    private Integer idUsuarioModificacion;
    private Boolean isGeolicalizada;
    private Integer isEdadRestriction;
    private Integer isSexoRestriction;
    private List<PreguntaJson> preguntas = new ArrayList<>();

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

    public Integer getIdUsuarioAlta() {
        return idUsuarioAlta;
    }

    public void setIdUsuarioAlta(Integer idUsuarioAlta) {
        this.idUsuarioAlta = idUsuarioAlta;
    }

    public Integer getIdUsuarioModificacion() {
        return idUsuarioModificacion;
    }

    public void setIdUsuarioModificacion(Integer idUsuarioModificacion) {
        this.idUsuarioModificacion = idUsuarioModificacion;
    }

    public Boolean getGeolicalizada() {
        return isGeolicalizada;
    }

    public void setGeolicalizada(Boolean geolicalizada) {
        isGeolicalizada = geolicalizada;
    }

    public Integer getIsEdadRestriction() {
        return isEdadRestriction;
    }

    public void setIsEdadRestriction(Integer isEdadRestriction) {
        this.isEdadRestriction = isEdadRestriction;
    }

    public Integer getIsSexoRestriction() {
        return isSexoRestriction;
    }

    public void setIsSexoRestriction(Integer isSexoRestriction) {
        this.isSexoRestriction = isSexoRestriction;
    }

    public List<PreguntaJson> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaJson> preguntas) {
        this.preguntas = preguntas;
    }
}
