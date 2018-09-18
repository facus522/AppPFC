package com.encuestando.salmeron.facundo.encuestandofcm;

import java.io.Serializable;

public class PreguntaDto implements Serializable{

    private Long id;
    private String descripcion;
    private TipoPreguntaEnum tipoPregunta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoPreguntaEnum getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(TipoPreguntaEnum tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }
}
