package com.encuestando.salmeron.facundo.encuestandofcm;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Facundo Salmerón on 3/10/2018.
 */

public class RespuestaDto implements Serializable, Parcelable {
    private String descripcion;
    private Integer idPersistido;
    private Boolean respuestaModificada = Boolean.FALSE;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdPersistido() {
        return idPersistido;
    }

    public void setIdPersistido(Integer idPersistido) {
        this.idPersistido = idPersistido;
    }

    public Boolean getRespuestaModificada() {
        return respuestaModificada;
    }

    public void setRespuestaModificada(Boolean respuestaModificada) {
        this.respuestaModificada = respuestaModificada;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (idPersistido != null){
            parcel.writeInt(idPersistido.intValue());
        } else {
            parcel.writeInt(-1);
        }
        if (descripcion != null){
            parcel.writeString(descripcion);
        }
    }

    private RespuestaDto(Parcel in) {
        idPersistido = in.readInt();
        descripcion = in.readString();
    }

    public RespuestaDto() {
        // Normal actions performed by class, since this is still a normal object!
    }

    public static final Parcelable.Creator<RespuestaDto> CREATOR = new Parcelable.Creator<RespuestaDto>(){
        @Override
        public RespuestaDto createFromParcel(Parcel in) {
            return new RespuestaDto(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public RespuestaDto[] newArray(int size) {
            return new RespuestaDto[size];
        }
    };
}
