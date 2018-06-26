package com.encuestando.salmeron.facundo.encuestandofcm;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


/**
 * Created by Facundo Salmerón on 14/6/2018.
 */

public class HttpAsyncTask extends AsyncTask<String,Void,String> {

    private JSONConverter jsonConverter;
    private Integer nroWebService;
    private Object object;

    public HttpAsyncTask(Integer nroWebService, Object object) {
        this.jsonConverter = new JSONConverter();
        this.nroWebService = nroWebService;
        this.object = object;
    }

    @Override
    protected String doInBackground(String... url) {
        return GET(url[0]);
    }

    @Override
    protected void onPostExecute(String result){
        if(nroWebService.equals(0)){
            jsonConverter.loginUsuario(result, object);
        }
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static String GET (String url) {
        InputStream inputStream = null;
        String result = "";

        try {
            HttpClient httpClient = new DefaultHttpClient();
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

    public JSONConverter getJsonConverter() {
        return jsonConverter;
    }

    public void setJsonConverter(JSONConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    public Integer getNroWebService() {
        return nroWebService;
    }

    public void setNroWebService(Integer nroWebService) {
        this.nroWebService = nroWebService;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }



}