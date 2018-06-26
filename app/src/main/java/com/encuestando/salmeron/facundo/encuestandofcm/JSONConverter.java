package com.encuestando.salmeron.facundo.encuestandofcm;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Facundo Salmerón on 18/6/2018.
 */

public class JSONConverter {

    public void loginUsuario(String jsonString, Object object){
        try {
            JSONObject json = new JSONObject(jsonString);
            UsuarioDto usuarioDto = new UsuarioDto();
            Boolean exito = Boolean.valueOf(json.getString("exito"));
            usuarioDto.setExito(exito);
            object = usuarioDto;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
