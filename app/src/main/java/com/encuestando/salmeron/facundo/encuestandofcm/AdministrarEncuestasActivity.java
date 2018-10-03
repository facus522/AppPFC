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

public class AdministrarEncuestasActivity extends AppCompatActivity implements HttpAsyncTaskInterface, Serializable{

    private UsuarioDto usuarioLogueado;
    private Toolbar toolbar;
    private FloatingActionButton agregarEncuestaBoton;
    private HttpAsyncTask httpAsyncTask;
    private List<ListaEncuestaDto> listaEncuestas = new ArrayList<>();
    private ListView listViewEncuestas;
    private ArrayList<PreguntaDto> preguntasAbrir = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrar_encuestas_activity);
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");

        listViewEncuestas = findViewById(R.id.listaEncuestas);

        toolbar = findViewById(R.id.titulo_administrar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdministrarEncuestasActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        agregarEncuestaBoton = findViewById(R.id.boton_agregar_encuesta);
        agregarEncuestaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent agregar_intent = new Intent(AdministrarEncuestasActivity.this, NuevaEncuestaActivity.class).putExtra("usuario", usuarioLogueado);
                agregar_intent.putExtra("preguntas", new ArrayList<PreguntaDto>());
                startActivityForResult(agregar_intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        String url = "http://192.168.0.107:8080/EncuestasFCM/encuestas/getAll";
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.CARGAR_ENCUESTAS.getCodigo());
        httpAsyncTask.setHttpAsyncTaskInterface(AdministrarEncuestasActivity.this);
        try {
            String receivedData = httpAsyncTask.execute(url).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        if (listaEncuestas.size() > 0){
            List<Map<String, String>> crsList = new ArrayList<Map<String,String>>();
            for (ListaEncuestaDto dto : listaEncuestas){
                Map<String, String> aug = new HashMap<String, String>();
                aug.put("tituloEncuesta", dto.getTitulo());
                aug.put("descripcionEncuesta", dto.getDescripcion());
                crsList.add(aug);
            }

            String[] keys = {"tituloEncuesta","descripcionEncuesta"};
            int[] widgetIds = {R.id.titulo_cargar_encuesta, R.id.subtitulo_cargar_encuesta};
            SimpleAdapter crsAdapter = new SimpleAdapter(AdministrarEncuestasActivity.this, crsList, R.layout.encuesta_item, keys,widgetIds){
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
            listViewEncuestas.setAdapter(crsAdapter);

        } else {
            Toast.makeText(AdministrarEncuestasActivity.this, "Actualmente no se encuentran cargadas Encuestas.", Toast.LENGTH_LONG).show();
        }

        listViewEncuestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdministrarEncuestasActivity.this);
                alertDialog.setTitle("Atención");
                alertDialog.setIcon(R.drawable.ic_action_error);
                alertDialog.setMessage("¿Qué acción desea realizar?");
                alertDialog.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        dialogInterface.cancel();
                        String url = "http://192.168.0.107:8080/EncuestasFCM/encuestas/disableEncuesta?idEncuesta=" + listaEncuestas.get(i).getId() + "&idUsuario=" + usuarioLogueado.getId();
                        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.ELIMINAR_ENCUESTA.getCodigo());
                        httpAsyncTask.setHttpAsyncTaskInterface(AdministrarEncuestasActivity.this);
                        try {
                            String receivedData = httpAsyncTask.execute(url).get();
                        } catch (ExecutionException | InterruptedException ei) {
                            ei.printStackTrace();
                        }
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(AdministrarEncuestasActivity.this, "Encuesta eliminada correctamente!", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertDialog.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        String urlAbrir = "http://192.168.0.107:8080/EncuestasFCM/encuestas/openEncuesta?idEncuesta=" + listaEncuestas.get(i).getId();
                        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.OPEN_ENCUESTA.getCodigo());
                        httpAsyncTask.setHttpAsyncTaskInterface(AdministrarEncuestasActivity.this);
                        try {
                            String receivedData = httpAsyncTask.execute(urlAbrir).get();
                        } catch (ExecutionException | InterruptedException ei) {
                            ei.printStackTrace();
                        }


                        Intent modificar_intent = new Intent(AdministrarEncuestasActivity.this, ModificarEncuestaActivity.class).putExtra("usuario", usuarioLogueado);
                        modificar_intent.putExtra("preguntas", preguntasAbrir);
                        modificar_intent.putExtra("tituloGuardar", listaEncuestas.get(i).getTitulo());
                        modificar_intent.putExtra("descripcionGuardar", listaEncuestas.get(i).getDescripcion());
                        startActivityForResult(modificar_intent, 1);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public void cargarEncuestas(String result) {
        String encuestasJSON = result;
        if (encuestasJSON != null && !encuestasJSON.isEmpty()) {
            listaEncuestas = JSONConverterUtils.JSONEncuestasConverter(result);
        }
    }

    @Override
    public void abrirEncuesta(String result) {
        String encuestasJSON = result;
        if (encuestasJSON != null && !encuestasJSON.isEmpty()) {
            preguntasAbrir = JSONConverterUtils.JSONAbrirEncuestasConverter(result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED){
            finish();
            startActivity(getIntent());
            Toast.makeText(AdministrarEncuestasActivity.this, "La encuesta ha sido creada correctamente!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
