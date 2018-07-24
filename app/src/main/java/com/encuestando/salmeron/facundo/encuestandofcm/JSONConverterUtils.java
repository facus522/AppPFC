package com.encuestando.salmeron.facundo.encuestandofcm;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Facundo Salmerón on 11/7/2018.
 */

public class JSONConverterUtils {
    public static UsuarioDto JSONUsuarioLoginConverter(String jsonString){
        UsuarioDto usuarioDto = new UsuarioDto();
        try {
            JSONObject json = new JSONObject(jsonString);
            Boolean exito = Boolean.valueOf(json.getString("exito"));
            usuarioDto.setExito(exito);
            if (exito){
                usuarioDto.setTipoUsuario(Integer.parseInt(json.getString("tipoUsuario")));
                usuarioDto.setId(Integer.parseInt(json.getString("id")));
                usuarioDto.setSexo(Integer.parseInt(json.getString("sexo")));
                usuarioDto.setNombreUsuario(json.getString("nombre"));
                usuarioDto.setEdad(Integer.parseInt(json.getString("edad")));
            } else{
                usuarioDto.setError(json.getString("error"));
                usuarioDto.setNroError(Integer.parseInt(json.getString("nroError")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  usuarioDto;
    }
}
