package com.encuestando.salmeron.facundo.encuestandofcm;

import java.io.Serializable;

public class TipoRespuestaJson implements Serializable {
    private String nombreTipoRespuesta;
    private Integer id;

    public String getNombreTipoRespuesta() {
        return nombreTipoRespuesta;
    }

    public void setNombreTipoRespuesta(String nombreTipoRespuesta) {
        this.nombreTipoRespuesta = nombreTipoRespuesta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoRespuestaJson(String nombreTipoRespuesta, Integer id) {
        this.nombreTipoRespuesta = nombreTipoRespuesta;
        this.id = id;
    }
}
