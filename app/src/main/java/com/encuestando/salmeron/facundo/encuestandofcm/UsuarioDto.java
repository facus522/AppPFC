package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by fsalmeron on 17/6/2018.
 */

public class UsuarioDto {
    private Boolean exito;
    private Integer tipoUsuario;
    private Integer id;
    private Integer sexo;
    private String nombreUsuario;

    public Boolean isExito() {
        return exito;
    }

    public void setExito(Boolean exito) {
        this.exito = exito;
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
}
