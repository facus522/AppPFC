package com.encuestando.salmeron.facundo.encuestandofcm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PreguntaJson implements Serializable{

    private Integer id;
    private String descripcion;
    private Integer numeroEscala;
    private TipoRespuestaJson tipoRespuesta;
    private List<RespuestaJson> respuestas = new ArrayList<>();

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

    public Integer getNumeroEscala() {
        return numeroEscala;
    }

    public void setNumeroEscala(Integer numeroEscala) {
        this.numeroEscala = numeroEscala;
    }

    public TipoRespuestaJson getTipoRespuesta() {
        return tipoRespuesta;
    }

    public void setTipoRespuesta(TipoRespuestaJson tipoRespuesta) {
        this.tipoRespuesta = tipoRespuesta;
    }

    public List<RespuestaJson> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaJson> respuestas) {
        this.respuestas = respuestas;
    }
}
