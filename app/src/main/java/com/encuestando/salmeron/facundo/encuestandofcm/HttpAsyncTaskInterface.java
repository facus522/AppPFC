package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 4/7/2018.
 */

public interface HttpAsyncTaskInterface {
    default void loginUsuario(String result){} //nroWebService = 0;
    default void registerUsuario(String result){} //nroWebService = 1;
    default void cargarInfoNoticias(String result){} //nroWebService = 2;
    default void eliminarInfoNoticia(String result){} //nroWebService = 3;
    default void crearInfoNoticica(String result){} //nroWebService = 4;
    default void crearEncuesta(String result){} //nroWebService = 5;
    default void crearPregunta(String result){} //nroWebService = 6;
    default void crearRespuesta(String result){} //nroWebService = 7;
    default void cargarEncuestas(String result){} //nroWebService = 8;
    default void eliminarEncuesta(String result){} //nroWebService = 9;
    default void abrirEncuesta(String result){} //nroWebService = 10;
    default void eliminarPregunta(String result){} //nroWebService = 11;
    default void eliminarRespuesta(String result){} //nroWebService = 12;
    default void modificarPregunta(String result){} //nroWebService = 13;
    default void modificarRespuesta(String result){} //nroWebService = 14;
    default void modificarEncuesta(String result){} //nroWebService = 15;
    default void contestarPregunta(String result){} //nroWebService = 16;
    default void incrementarResultado(String result){} //nroWebService = 17;
    default void abrirEncuestaValidar(String result){} //nroWebService = 18;
    default void traerResultadosEncuesta(String result){} //nroWebService = 19;
    default void habilitarEncuesta(String result){} //nroWebService = 20;
    default void inhabilitarEncuesta(String result){} //nroWebService = 21;
}
