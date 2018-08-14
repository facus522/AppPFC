package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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
    private String urlInfo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_noticias_normal_activity);
        listViewInfoNoticias = (ListView) findViewById(R.id.listaInfoNoticias);
        toolbar = (Toolbar) findViewById(R.id.toolbar_info_normal);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoNoticiasNormalActivity.this.finish();
            }
        });

        String url = "http://192.168.0.107:8080/EncuestasFCM/infoNoticias/getAll";
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.CARGAR_INFO.getCodigo());
        httpAsyncTask.setHttpAsyncTaskInterface(InfoNoticiasNormalActivity.this);
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
            SimpleAdapter crsAdapter = new SimpleAdapter(InfoNoticiasNormalActivity.this, crsList, R.layout.info_noticias_item, keys,widgetIds);




            listViewInfoNoticias.setAdapter(crsAdapter);

        } else{
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

        listViewInfoNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                urlInfo = infoNoticiasDto.get(i).getUrl();
                Intent web_info_intent = new Intent(InfoNoticiasNormalActivity.this, WebViewNormalActivity.class).putExtra("urlInfo", urlInfo);
                InfoNoticiasNormalActivity.this.startActivity(web_info_intent);
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
    public void crearInfoNoticica(String result) {

    }

    @Override
    public void loginUsuario(String result) {

    }

    @Override
    public void registerUsuario(String result) {

    }

    @Override
    public void eliminarInfoNoticia(String result) {

    }
}
