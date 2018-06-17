package com.encuestando.salmeron.facundo.encuestandofcm;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Facundo Salmerón on 14/6/2018.
 */

public class HttpAsyncTask extends AsyncTask<String, Void, String> {

    private Integer numeroWebService;
    private Object object;

    public HttpAsyncTask(Integer numeroWebService, Object object) {
        this.numeroWebService = numeroWebService;
        this.object = object;
    }

    @Override
    protected String doInBackground(String... urls) {
        return GET(urls[0]);
    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        switch (numeroWebService){
            case 1:
                try {
                    JSONObject json = new JSONObject(result); //Convierte String a JSONObject

                    String diaSemana = json.getJSONObject("forecast").
                            getJSONObject("simpleforecast").
                            getJSONArray("forecastday").
                            getJSONObject(0).
                            getJSONObject("date").getString("weekday_short");

                    String dia = json.getJSONObject("forecast").
                            getJSONObject("simpleforecast").
                            getJSONArray("forecastday").
                            getJSONObject(0).
                            getJSONObject("date").getString("day");

                    String mes = json.getJSONObject("forecast").
                            getJSONObject("simpleforecast").
                            getJSONArray("forecastday").
                            getJSONObject(0).
                            getJSONObject("date").getString("monthname_short");

                    String temperatura = json.getJSONObject("forecast").
                            getJSONObject("simpleforecast").
                            getJSONArray("forecastday").getJSONObject(0).getJSONObject("high").getString("celsius");

                    String ubicacion = json.getJSONObject("location").
                            getJSONObject("nearby_weather_stations").
                            getJSONObject("pws").
                            getJSONArray("station").getJSONObject(0).getString("city");
                    json.getJSONArray(String.valueOf(1)).getString(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }



    }
}
