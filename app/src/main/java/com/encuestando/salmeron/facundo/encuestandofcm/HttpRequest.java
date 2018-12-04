package com.encuestando.salmeron.facundo.encuestandofcm;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pethoalpar on 4/16/2016.
 */
public class HttpRequest extends AsyncTask<HttpCall, String, String>{

    private static final String UTF_8 = "UTF-8";

    private Integer nroWebService;
    public HttpAsyncTaskInterface httpAsyncTaskInterface;

    public HttpRequest(Integer nroWebService, HttpAsyncTaskInterface httpAsyncTaskInterface) {
        this.nroWebService = nroWebService;
        this.httpAsyncTaskInterface = httpAsyncTaskInterface;
    }

    @Override
    protected String doInBackground(HttpCall... params) {
        HttpURLConnection urlConnection = null;
        HttpCall httpCall = params[0];
        StringBuilder response = new StringBuilder();
        try{
            String dataParams = getDataString(httpCall.getParams(), httpCall.getMethodType());
            URL url = new URL(httpCall.getMethodType() == HttpCall.GET ? httpCall.getUrl() + dataParams : httpCall.getUrl());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(httpCall.getMethodType() == HttpCall.GET ? "GET":"POST");
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            if(httpCall.getParams() != null && httpCall.getMethodType() == HttpCall.POST){
                /*OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
                writer.append(dataParams);
                writer.flush();
                writer.close();
                os.close();*/

                ObjectOutputStream os = new ObjectOutputStream(urlConnection.getOutputStream());
                os.writeObject(httpCall.getBody());
                os.close();
            }
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line ;
                BufferedReader br = new BufferedReader( new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null){
                    response.append(line);
                }
                if(nroWebService.equals(WebServiceEnum.LOGIN_USER.getCodigo())){
                    httpAsyncTaskInterface.loginUsuario(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.REGISTER_USER.getCodigo())){
                    httpAsyncTaskInterface.registerUsuario(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.CARGAR_INFO.getCodigo())){
                    httpAsyncTaskInterface.cargarInfoNoticias(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.ELIMINAR_INFO.getCodigo())){
                    httpAsyncTaskInterface.eliminarInfoNoticia(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.CREAR_INFO.getCodigo())){
                    httpAsyncTaskInterface.crearInfoNoticica(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.CREAR_ENCUESTA.getCodigo())){
                    httpAsyncTaskInterface.crearEncuesta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.CREAR_PREGUNTA.getCodigo())){
                    httpAsyncTaskInterface.crearPregunta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.CREAR_RESPUESTA.getCodigo())){
                    httpAsyncTaskInterface.crearRespuesta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.CARGAR_ENCUESTAS.getCodigo())){
                    httpAsyncTaskInterface.cargarEncuestas(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.ELIMINAR_ENCUESTA.getCodigo())){
                    httpAsyncTaskInterface.eliminarEncuesta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.OPEN_ENCUESTA.getCodigo())){
                    httpAsyncTaskInterface.abrirEncuesta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.ELIMINAR_PREGUNTA.getCodigo())){
                    httpAsyncTaskInterface.eliminarPregunta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.ELIMINAR_RESPUESTA.getCodigo())){
                    httpAsyncTaskInterface.eliminarRespuesta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.MODIFICAR_PREGUNTA.getCodigo())){
                    httpAsyncTaskInterface.modificarPregunta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.MODIFICAR_RESPUESTA.getCodigo())){
                    httpAsyncTaskInterface.modificarRespuesta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.MODIFICAR_ENCUESTA.getCodigo())){
                    httpAsyncTaskInterface.modificarEncuesta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.CONTESTAR_PREGUNTA.getCodigo())){
                    httpAsyncTaskInterface.contestarPregunta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.INCREMENTAR_RESULTADO.getCodigo())){
                    httpAsyncTaskInterface.incrementarResultado(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.OPEN_ENCUESTA_VALIDAR.getCodigo())){
                    httpAsyncTaskInterface.abrirEncuestaValidar(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.RESULTADOS_ENCUESTA.getCodigo())){
                    httpAsyncTaskInterface.traerResultadosEncuesta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.CARGAR_ENCUESTAS_HABILITADAS.getCodigo())){
                    httpAsyncTaskInterface.cargarEncuestas(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.HABILITAR_ENCUESTA.getCodigo())){
                    httpAsyncTaskInterface.habilitarEncuesta(response.toString());
                } else if (nroWebService.equals(WebServiceEnum.INHABILITAR_ENCUESTA.getCodigo())){
                    httpAsyncTaskInterface.inhabilitarEncuesta(response.toString());
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onResponse(s);
    }

    public void onResponse(String response){

    }

    private String getDataString(HashMap<String,String> params, int methodType) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String,String> entry : params.entrySet()){
            if (isFirst){
                isFirst = false;
                if(methodType == HttpCall.GET){
                    result.append("?");
                }
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), UTF_8));
        }
        return result.toString();
    }

    public HttpAsyncTaskInterface getHttpAsyncTaskInterface() {
        return httpAsyncTaskInterface;
    }

    public void setHttpAsyncTaskInterface(HttpAsyncTaskInterface httpAsyncTaskInterface) {
        this.httpAsyncTaskInterface = httpAsyncTaskInterface;
    }
}