package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 4/7/2018.
 */

public interface HttpAsyncTaskInterface {
    void loginUsuario(String result); //nroWebService = 0;
    void registerUsuario(String result); //nroWebService = 1;
    void cargarInfoNoticias(String result); //nroWebService = 2;
}
