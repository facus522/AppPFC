package com.encuestando.salmeron.facundo.encuestandofcm;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Facundo Salmerón on 15/9/2018.
 */

public class PreguntaDto implements Serializable{

    private Long id;
    private String descripcion;
    private TipoPreguntaEnum tipoPregunta;
    private Integer maximaEscala;
    private ArrayList<RespuestaDto> respuestas;
    private Boolean activo = Boolean.TRUE;
    private Integer idPersistido;
    private Boolean preguntaPersistida = Boolean.FALSE;
    private Boolean preguntaModificada = Boolean.FALSE;
    private ArrayList<ResultadoDto> resultadoDtos = new ArrayList<>();

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

    public Integer getMaximaEscala() {
        return maximaEscala;
    }

    public void setMaximaEscala(Integer maximaEscala) {
        this.maximaEscala = maximaEscala;
    }

    public ArrayList<RespuestaDto> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<RespuestaDto> respuestas) {
        this.respuestas = respuestas;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getIdPersistido() {
        return idPersistido;
    }

    public void setIdPersistido(Integer idPersistido) {
        this.idPersistido = idPersistido;
    }

    public Boolean getPreguntaPersistida() {
        return preguntaPersistida;
    }

    public void setPreguntaPersistida(Boolean preguntaPersistida) {
        this.preguntaPersistida = preguntaPersistida;
    }

    public Boolean getPreguntaModificada() {
        return preguntaModificada;
    }

    public void setPreguntaModificada(Boolean preguntaModificada) {
        this.preguntaModificada = preguntaModificada;
    }

    public ArrayList<ResultadoDto> getResultadoDtos() {
        return resultadoDtos;
    }

    public void setResultadoDtos(ArrayList<ResultadoDto> resultadoDtos) {
        this.resultadoDtos = resultadoDtos;
    }
}
