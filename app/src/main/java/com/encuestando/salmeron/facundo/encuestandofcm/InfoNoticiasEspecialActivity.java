package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
    private FloatingActionButton agregarInfoBoton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_noticias_especial_activity);
        listViewInfoNoticias = (ListView) findViewById(R.id.listaInfoNoticiasEspecial);
        agregarInfoBoton = (FloatingActionButton) findViewById(R.id.boton_agregar_info);
        toolbar = (Toolbar) findViewById(R.id.toolbar_info_especial);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoNoticiasEspecialActivity.this.finish();
            }
        });

        agregarInfoBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent agregar_info_intent = new Intent(InfoNoticiasEspecialActivity.this, CrearInfoActivity.class);
                InfoNoticiasEspecialActivity.this.startActivity(agregar_info_intent);
            }
        });

        String url = "http://192.168.0.107:8080/EncuestasFCM/infoNoticias/getAll";
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.CARGAR_INFO.getCodigo());
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
            Toast.makeText(InfoNoticiasEspecialActivity.this, "Actualmente no se encuentran cargadas Informaciones y Noticias.", Toast.LENGTH_LONG).show();
        }

        listViewInfoNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoNoticiasEspecialActivity.this);
                alertDialog.setTitle("Confirme opción a realizar");
                alertDialog.setMessage("¿Desea ingresar a la información o eliminarla?");
                alertDialog.setPositiveButton("Ver URL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                alertDialog.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        String url = "http://192.168.0.107:8080/EncuestasFCM/infoNoticias/removeInfo?idInfo=" + infoNoticiasDto.get(i).getId();
                        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.ELIMINAR_INFO.getCodigo());
                        httpAsyncTask.setHttpAsyncTaskInterface(InfoNoticiasEspecialActivity.this);
                        try {
                            String receivedData = httpAsyncTask.execute(url).get();
                        } catch (ExecutionException | InterruptedException ei) {
                            ei.printStackTrace();
                        }
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(InfoNoticiasEspecialActivity.this, "Información eliminada correctamente!", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
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
    public void eliminarInfoNoticia(String result) {

    }

    @Override
    public void loginUsuario(String result) {

    }

    @Override
    public void registerUsuario(String result) {

    }
}
