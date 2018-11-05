package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Facundo Salmerón on 12/8/2018.
 */


public class InfoNoticiasEspecialActivity extends AppCompatActivity implements HttpAsyncTaskInterface, Serializable{

    private ListView listViewInfoNoticias;
    private List<InfoNoticiaDto> infoNoticiasDto = new ArrayList<InfoNoticiaDto>();
    private HttpAsyncTask httpAsyncTask;
    private String urlInfo;
    private Toolbar toolbar;
    private FloatingActionButton agregarInfoBoton;
    private UsuarioDto usuarioLogueado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_noticias_especial_activity);
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        listViewInfoNoticias = findViewById(R.id.listaInfoNoticiasEspecial);
        agregarInfoBoton = findViewById(R.id.boton_agregar_info);
        toolbar = findViewById(R.id.toolbar_info_especial);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoNoticiasEspecialActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        agregarInfoBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent agregar_info_intent = new Intent(InfoNoticiasEspecialActivity.this, CrearInfoActivity.class).putExtra("usuario", usuarioLogueado);
                startActivityForResult(agregar_info_intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        String url = getResources().getString(R.string.urlWS) + "/infoNoticias/getAll";
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
            SimpleAdapter crsAdapter = new SimpleAdapter(InfoNoticiasEspecialActivity.this, crsList, R.layout.info_noticias_item, keys,widgetIds){
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
                        urlInfo = infoNoticiasDto.get(i).getUrl();
                        Intent web_info_intent = new Intent(InfoNoticiasEspecialActivity.this, WebViewNormalActivity.class).putExtra("urlInfo", urlInfo);
                        InfoNoticiasEspecialActivity.this.startActivity(web_info_intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
                alertDialog.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        String url = getResources().getString(R.string.urlWS) + "/infoNoticias/removeInfo?idInfo=" + infoNoticiasDto.get(i).getId();
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            finish();
            startActivity(getIntent());
            Toast.makeText(InfoNoticiasEspecialActivity.this, "La información ha sido añadida correctamente!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cargarInfoNoticias(String result) {
        String infoNoticiasJSON = result;
        if (infoNoticiasJSON != null && !infoNoticiasJSON.isEmpty()) {
            infoNoticiasDto = JSONConverterUtils.JSONInfoNoticiasConverter(result);
        }
    }

}
