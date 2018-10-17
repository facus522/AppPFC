package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Facundo Salmerón on 6/8/2018.
 */

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
        listViewInfoNoticias = findViewById(R.id.listaInfoNoticias);
        toolbar = findViewById(R.id.toolbar_info_normal);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoNoticiasNormalActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
            SimpleAdapter crsAdapter = new SimpleAdapter(InfoNoticiasNormalActivity.this, crsList, R.layout.info_noticias_item, keys,widgetIds){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    if (position%2 == 1){
                        view.setBackgroundColor(getResources().getColor(R.color.impares));
                    } else{
                        view.setBackgroundColor(getResources().getColor(R.color.pares));
                    }
                    return view;
                }
            };

            listViewInfoNoticias.setAdapter(crsAdapter);

        } else{
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoNoticiasNormalActivity.this);
            alertDialog.setTitle("Sin Datos");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Actualmente no se encuentran cargadas Informaciones y Noticias.");
            alertDialog.setIcon(R.drawable.ic_action_error);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    InfoNoticiasNormalActivity.this.finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void cargarInfoNoticias(String result) {
        String infoNoticiasJSON = result;
        if (infoNoticiasJSON != null && !infoNoticiasJSON.isEmpty()) {
            infoNoticiasDto = JSONConverterUtils.JSONInfoNoticiasConverter(result);
        }
    }
}
