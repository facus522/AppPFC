package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InfoNoticiasEspecialActivity extends AppCompatActivity implements HttpAsyncTaskInterface{

    private ListView listViewInfoNoticias;
    private List<InfoNoticiaDto> infoNoticiasDto = new ArrayList<InfoNoticiaDto>();
    private HttpAsyncTask httpAsyncTask;
    private String urlInfo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_noticias_especial_activity);
        listViewInfoNoticias = (ListView) findViewById(R.id.listaInfoNoticiasEspecial);
        toolbar = (Toolbar) findViewById(R.id.toolbar_info_especial);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoNoticiasEspecialActivity.this.finish();
            }
        });

        String url = "http://192.168.0.107:8080/EncuestasFCM/infoNoticias/getAll";
        httpAsyncTask = new HttpAsyncTask(2);
        httpAsyncTask.setHttpAsyncTaskInterface(InfoNoticiasEspecialActivity.this);
        try {
            String receivedData = httpAsyncTask.execute(url).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        if (infoNoticiasDto.size() > 0){
            List<Map<String, String>> crsList = new ArrayList<Map<String,String>>();
            for (InfoNoticiaDto dto : infoNoticiasDto){
                Map<String, String> aug = new HashMap<String, String>();
                aug.put("tituloInfo", dto.getTitulo());
                aug.put("descripcionInfo", dto.getDescripcion());
                crsList.add(aug);
            }

            String[] keys = {"tituloInfo","descripcionInfo"};
            int[] widgetIds = {R.id.titulo_info_normal, R.id.subtitulo_info_normal};
            SimpleAdapter crsAdapter = new SimpleAdapter(InfoNoticiasEspecialActivity.this, crsList, R.layout.info_noticias_item, keys,widgetIds);




            listViewInfoNoticias.setAdapter(crsAdapter);

        } else{
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoNoticiasEspecialActivity.this);
            alertDialog.setTitle("Sin Datos");
            alertDialog.setMessage("Actualmente no se encuentran cargadas Informaciones y Noticias.");
            alertDialog.setIcon(R.drawable.ic_action_error);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    InfoNoticiasEspecialActivity.this.finish();
                }
            });
            alertDialog.show();
        }

        listViewInfoNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                urlInfo = "";
                if (!infoNoticiasDto.get(i).getUrl().substring(0,4).equals("http")){
                    urlInfo += "http://" + infoNoticiasDto.get(i).getUrl();
                } else{
                    urlInfo = infoNoticiasDto.get(i).getUrl();
                }

                Intent web_info_intent = new Intent(InfoNoticiasEspecialActivity.this, WebViewNormalActivity.class).putExtra("urlInfo", urlInfo);
                InfoNoticiasEspecialActivity.this.startActivity(web_info_intent);
            }
        });

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
