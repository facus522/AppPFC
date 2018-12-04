package com.encuestando.salmeron.facundo.encuestandofcm;

import java.io.Serializable;

public class RespuestaJson implements Serializable{
    private Integer id;
    private String descripcion;
    private TipoRespuestaJson tipoRespuesta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoRespuestaJson getTipoRespuesta() {
        return tipoRespuesta;
    }

    public void setTipoRespuesta(TipoRespuestaJson tipoRespuesta) {
        this.tipoRespuesta = tipoRespuesta;
    }
}
