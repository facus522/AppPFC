package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 4/7/2018.
 */

public interface HttpAsyncTaskInterface {
    default void loginUsuario(String result){}; //nroWebService = 0;
    default void registerUsuario(String result){}; //nroWebService = 1;
    default void cargarInfoNoticias(String result){}; //nroWebService = 2;
    default void eliminarInfoNoticia(String result){}; //nroWebService = 3;
    default void crearInfoNoticica(String result){}; //nroWebService = 4;
    default void crearEncuesta(String result){}; //nroWebService = 5;
    default void crearPregunta(String result){}; //nroWebService = 6;
    default void crearRespuesta(String result){}; //nroWebService = 7;
    default void cargarEncuestas(String result){}; //nroWebService = 8;
    default void eliminarEncuesta(String result){}; //nroWebService = 9;
}
