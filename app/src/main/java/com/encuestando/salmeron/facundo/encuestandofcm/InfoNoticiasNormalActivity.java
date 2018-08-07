package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InfoNoticiasNormalActivity extends AppCompatActivity implements HttpAsyncTaskInterface {

    private ListView listViewInfoNoticias;
    private List<InfoNoticiaDto> infoNoticiasDto = new ArrayList<InfoNoticiaDto>();
    private HttpAsyncTask httpAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_noticias_normal_activity);
        listViewInfoNoticias = (ListView) findViewById(R.id.listaInfoNoticias);

        String url = "http://192.168.0.107:8080/EncuestasFCM/infoNoticias/getAll";
        httpAsyncTask = new HttpAsyncTask(2);
        httpAsyncTask.setHttpAsyncTaskInterface(InfoNoticiasNormalActivity.this);
        try {
            String receivedData = httpAsyncTask.execute(url).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        if (infoNoticiasDto.size() > 0){
            HashMap<String, String> mapaInfoNoticias = new HashMap<>();
            for (InfoNoticiaDto dto : infoNoticiasDto){
                mapaInfoNoticias.put(dto.getTitulo(), dto.getDescripcion());
            }

            List<HashMap<String, String>> listaMapas = new ArrayList<>();
            SimpleAdapter adapter = new SimpleAdapter(this, listaMapas, R.layout.info_noticias_item, new String[]{"First Line", "Second Line"}, new int[]{R.id.titulo_info_normal, R.id.subtitulo_info_normal});

            Iterator it = mapaInfoNoticias.entrySet().iterator();
            while (it.hasNext()) {
                HashMap<String, String> resultMap = new HashMap<>();
                Map.Entry pair = (Map.Entry) it.next();
                resultMap.put("First Line", pair.getKey().toString());
                resultMap.put("Second Line", pair.getValue().toString());
                listaMapas.add(resultMap);
            }

            listViewInfoNoticias.setAdapter(adapter);
        } else{
            //Toast.makeText(InfoNoticiasNormalActivity.this, "Actualmente no existen informaciones y noticias cargadas!",Toast.LENGTH_LONG).show();
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoNoticiasNormalActivity.this);
            alertDialog.setTitle("Sin Datos");
            alertDialog.setMessage("Actualmente no se encuentran cargadas Informaciones y Noticias.");
            alertDialog.setIcon(R.drawable.ic_action_error);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    InfoNoticiasNormalActivity.this.finish();
                }
            });
            alertDialog.show();
        }


    }

    @Override
    public void cargarInfoNoticias(String result) {
        String infoNoticiasJSON = result;
        if (infoNoticiasJSON != null && !infoNoticiasJSON.isEmpty()) {
            infoNoticiasDto = JSONConverterUtils.JSONInfoNoticiasConverter(result);
        }
    }

    @Override
    public void loginUsuario(String result) {

    }

    @Override
    public void registerUsuario(String result) {

    }
}
