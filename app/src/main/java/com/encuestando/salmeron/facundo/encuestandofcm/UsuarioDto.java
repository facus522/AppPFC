package com.encuestando.salmeron.facundo.encuestandofcm;

import java.io.Serializable;

/**
 * Created by fsalmeron on 17/6/2018.
 */

public class UsuarioDto implements Serializable{
    private Boolean exito;
    private Integer tipoUsuario;
    private Integer id;
    private Integer sexo;
    private Integer edad;
    private String nombreUsuario;
    private String error;
    private Integer nroError;

    public void setExito(Boolean exito) {
        this.exito = exito;
    }

    public Integer getNroError() {
        return nroError;
    }

    public void setNroError(Integer nroError) {
        this.nroError = nroError;
    }

    public Boolean getExito() {
        return exito;

    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSexo() {
        return sexo;
    }

    public void setSexo(Integer sexo) {
        this.sexo = sexo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }
}
