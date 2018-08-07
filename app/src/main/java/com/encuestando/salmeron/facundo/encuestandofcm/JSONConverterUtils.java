package com.encuestando.salmeron.facundo.encuestandofcm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public static String JSONUsuarioRegisterConverter(String jsonString){
        String resultado = "";
        try {
            JSONObject json = new JSONObject(jsonString);
            Boolean exito = Boolean.valueOf(json.getString("exito"));
            if (!exito){
                resultado = json.getString("error");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static List<InfoNoticiaDto> JSONInfoNoticiasConverter(String jsonString){
        List<InfoNoticiaDto> result = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i=0; i<jsonArray.length();i++){
                JSONObject aux = jsonArray.getJSONObject(i);
                InfoNoticiaDto dto = new InfoNoticiaDto();
                dto.setTitulo(aux.getString("titulo"));
                dto.setDescripcion(aux.getString("descripcion"));
                dto.setUrl(aux.getString("url"));
                result.add(dto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
