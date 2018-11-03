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
                usuarioDto.setNombreUsuario(json.getString("nombreUsuario"));
                usuarioDto.setNombre(json.getString("nombre"));
                usuarioDto.setApellido(json.getString("apellido"));
                usuarioDto.setDni(Integer.parseInt(json.getString("dni")));
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
                dto.setUsuarioCreacion(aux.getString("usuario"));
                dto.setFechaCreacion(aux.getString("fecha"));
                dto.setResoluciones(aux.getString("resoluciones"));
                dto.setGeolocalizada(Boolean.valueOf(aux.getString("geolocalizada")));
                result.add(dto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static ArrayList<PreguntaDto> JSONAbrirEncuestasConverter(String jsonString){
        ArrayList<PreguntaDto> result = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("preguntas");
            for (int i=0; i<jsonArray.length();i++){
                PreguntaDto dto = new PreguntaDto();
                dto.setDescripcion((String) jsonArray.getJSONObject(i).get("descripcionPregunta"));
                Integer idTipoRespuesta = (Integer) jsonArray.getJSONObject(i).get("idTipoRespuesta");
                dto.setTipoPregunta(TipoPreguntaEnum.getEnum(idTipoRespuesta));
                dto.setIdPersistido((Integer) jsonArray.getJSONObject(i).get("idPregunta"));
                dto.setMaximaEscala((Integer) jsonArray.getJSONObject(i).get("numeroEscala"));
                dto.setPreguntaPersistida(Boolean.TRUE);
                dto.setId(new Long(i+1L));
                JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("respuesta");
                ArrayList<RespuestaDto> listaRespuestas = new ArrayList<>();
                for (int j=0; j<jsonArray2.length(); j++){
                    RespuestaDto rta = new RespuestaDto();
                    rta.setDescripcion((String) jsonArray2.getJSONObject(j).get("descripcionRespuesta"));
                    rta.setIdPersistido((Integer) jsonArray2.getJSONObject(j).get("idRespuesta"));
                    listaRespuestas.add(rta);
                }
                dto.setRespuestas(listaRespuestas);
                result.add(dto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Map<Boolean,ArrayList<PreguntaDto>> JSONAbrirEncuestasValidarConverter(String jsonString){
        Map<Boolean, ArrayList<PreguntaDto>> mapa = new HashMap<>();
        ArrayList<PreguntaDto> result = new ArrayList<>();
        Boolean respondida = Boolean.FALSE;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            respondida = (Boolean) jsonObject.get("respondida");

            if (!respondida){
                JSONArray jsonArray = jsonObject.getJSONArray("preguntas");
                for (int i=0; i<jsonArray.length();i++){
                    PreguntaDto dto = new PreguntaDto();
                    dto.setDescripcion((String) jsonArray.getJSONObject(i).get("descripcionPregunta"));
                    Integer idTipoRespuesta = (Integer) jsonArray.getJSONObject(i).get("idTipoRespuesta");
                    dto.setTipoPregunta(TipoPreguntaEnum.getEnum(idTipoRespuesta));
                    dto.setIdPersistido((Integer) jsonArray.getJSONObject(i).get("idPregunta"));
                    dto.setMaximaEscala((Integer) jsonArray.getJSONObject(i).get("numeroEscala"));
                    dto.setPreguntaPersistida(Boolean.TRUE);
                    dto.setId(new Long(i+1L));
                    JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("respuesta");
                    ArrayList<RespuestaDto> listaRespuestas = new ArrayList<>();
                    for (int j=0; j<jsonArray2.length(); j++){
                        RespuestaDto rta = new RespuestaDto();
                        rta.setDescripcion((String) jsonArray2.getJSONObject(j).get("descripcionRespuesta"));
                        rta.setIdPersistido((Integer) jsonArray2.getJSONObject(j).get("idRespuesta"));
                        listaRespuestas.add(rta);
                    }
                    dto.setRespuestas(listaRespuestas);
                    result.add(dto);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mapa.put(respondida, result);

        return mapa;
    }

}
