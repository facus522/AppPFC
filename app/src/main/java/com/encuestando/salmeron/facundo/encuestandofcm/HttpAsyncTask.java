package com.encuestando.salmeron.facundo.encuestandofcm;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


/**
 * Created by Facundo Salmerón on 14/6/2018.
 */

public class HttpAsyncTask extends AsyncTask<String,Void,String> {

    private Integer nroWebService;
    public HttpAsyncTaskInterface httpAsyncTaskInterface;

    public HttpAsyncTask(Integer nroWebService) {
        this.httpAsyncTaskInterface = null;
        this.nroWebService = nroWebService;
    }

    @Override
    protected String doInBackground(String... url) {
        String result = GET(url[0]);
        if(nroWebService.equals(WebServiceEnum.LOGIN_USER.getCodigo())){
            httpAsyncTaskInterface.loginUsuario(result);
        } else if (nroWebService.equals(WebServiceEnum.REGISTER_USER.getCodigo())){
            httpAsyncTaskInterface.registerUsuario(result);
        } else if (nroWebService.equals(WebServiceEnum.CARGAR_INFO.getCodigo())){
            httpAsyncTaskInterface.cargarInfoNoticias(result);
        } else if (nroWebService.equals(WebServiceEnum.ELIMINAR_INFO.getCodigo())){
            httpAsyncTaskInterface.eliminarInfoNoticia(result);
        } else if (nroWebService.equals(WebServiceEnum.CREAR_INFO.getCodigo())){
            httpAsyncTaskInterface.crearInfoNoticica(result);
        } else if (nroWebService.equals(WebServiceEnum.CREAR_ENCUESTA.getCodigo())){
            httpAsyncTaskInterface.crearEncuesta(result);
        } else if (nroWebService.equals(WebServiceEnum.CREAR_PREGUNTA.getCodigo())){
            httpAsyncTaskInterface.crearPregunta(result);
        } else if (nroWebService.equals(WebServiceEnum.CREAR_RESPUESTA.getCodigo())){
            httpAsyncTaskInterface.crearRespuesta(result);
        } else if (nroWebService.equals(WebServiceEnum.CARGAR_ENCUESTAS.getCodigo())){
            httpAsyncTaskInterface.cargarEncuestas(result);
        } else if (nroWebService.equals(WebServiceEnum.ELIMINAR_ENCUESTA.getCodigo())){
            httpAsyncTaskInterface.eliminarEncuesta(result);
        } else if (nroWebService.equals(WebServiceEnum.OPEN_ENCUESTA.getCodigo())){
            httpAsyncTaskInterface.abrirEncuesta(result);
        } else if (nroWebService.equals(WebServiceEnum.ELIMINAR_PREGUNTA.getCodigo())){
            httpAsyncTaskInterface.eliminarPregunta(result);
        } else if (nroWebService.equals(WebServiceEnum.ELIMINAR_RESPUESTA.getCodigo())){
            httpAsyncTaskInterface.eliminarRespuesta(result);
        } else if (nroWebService.equals(WebServiceEnum.MODIFICAR_PREGUNTA.getCodigo())){
            httpAsyncTaskInterface.modificarPregunta(result);
        } else if (nroWebService.equals(WebServiceEnum.MODIFICAR_RESPUESTA.getCodigo())){
            httpAsyncTaskInterface.modificarRespuesta(result);
        } else if (nroWebService.equals(WebServiceEnum.MODIFICAR_ENCUESTA.getCodigo())){
            httpAsyncTaskInterface.modificarEncuesta(result);
        }

        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public static String GET (String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "No funciona!";
        }
        catch(Exception e){
            Log.d("InputStream",e.getLocalizedMessage());
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    public Integer getNroWebService() {
        return nroWebService;
    }

    public void setNroWebService(Integer nroWebService) {
        this.nroWebService = nroWebService;
    }

    public HttpAsyncTaskInterface getHttpAsyncTaskInterface() {
        return httpAsyncTaskInterface;
    }

    public void setHttpAsyncTaskInterface(HttpAsyncTaskInterface httpAsyncTaskInterface) {
        this.httpAsyncTaskInterface = httpAsyncTaskInterface;
    }

}