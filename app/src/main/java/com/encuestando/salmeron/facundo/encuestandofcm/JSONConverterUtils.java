package com.encuestando.salmeron.facundo.encuestandofcm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<Integer, String> JSONUsuarioRegisterConverter(String jsonString){
        Map<Integer, String> resultado = new HashMap<>();
        try {
            JSONObject json = new JSONObject(jsonString);
            Boolean exito = Boolean.valueOf(json.getString("exito"));
            if (!exito){
                Integer idError = Integer.valueOf(json.getString("idError"));
                String error = json.getString("error");
                resultado.put(idError, error);
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
                dto.setId(Integer.parseInt(aux.getString("id")));
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

    public static Integer JSONCreateEncuestaConverter(String jsonString){
        Integer idAsignado = null;
        try {
            JSONObject json = new JSONObject(jsonString);
            idAsignado = Integer.valueOf(json.getString("idAsignado"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  idAsignado;
    }

    public static List<ListaEncuestaDto> JSONEncuestasConverter(String jsonString){
        List<ListaEncuestaDto> result = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            for (int i=0; i<jsonArray.length();i++){
                JSONObject aux = jsonArray.getJSONObject(i);
                ListaEncuestaDto dto = new ListaEncuestaDto();
                dto.setId(Integer.parseInt(aux.getString("id")));
                dto.setTitulo(aux.getString("titulo"));
                dto.setDescripcion(aux.getString("descripcion"));
                result.add(dto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

}
