package com.encuestando.salmeron.facundo.encuestandofcm;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Facundo Salmerón on 7/11/2018.
 */

public class ResultadoDto implements Serializable, Parcelable {
    private String latitud;
    private String longitud;
    private Integer idRespuesta;
    private String descripcion;

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Integer getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(Integer idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (latitud != null){
            parcel.writeString(latitud);
        }

        if (longitud != null){
            parcel.writeString(longitud);
        }

        if (idRespuesta != null){
            parcel.writeInt(idRespuesta.intValue());
        } else {
            parcel.writeInt(-1);
        }

        if (descripcion != null){
            parcel.writeString(descripcion);
        }
    }

    private ResultadoDto(Parcel in) {
        latitud = in.readString();
        longitud = in.readString();
        idRespuesta = in.readInt();
        descripcion = in.readString();
    }

    public ResultadoDto() {
        // Normal actions performed by class, since this is still a normal object!
    }

    public static final Parcelable.Creator<ResultadoDto> CREATOR = new Parcelable.Creator<ResultadoDto>(){
        @Override
        public ResultadoDto createFromParcel(Parcel in) {
            return new ResultadoDto(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public ResultadoDto[] newArray(int size) {
            return new ResultadoDto[size];
        }
    };
}
