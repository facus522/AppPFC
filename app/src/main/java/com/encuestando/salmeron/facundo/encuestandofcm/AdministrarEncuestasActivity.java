package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
 * Created by Facundo Salmerón on 11/9/2018.
 */

public class AdministrarEncuestasActivity extends AppCompatActivity implements HttpAsyncTaskInterface, Serializable, SwipeRefreshLayout.OnRefreshListener {

    private UsuarioDto usuarioLogueado;
    private Toolbar toolbar;
    private FloatingActionButton agregarEncuestaBoton;
    private HttpAsyncTask httpAsyncTask;
    private List<ListaEncuestaDto> listaEncuestas = new ArrayList<>();
    private ListView listViewEncuestas;
    private ArrayList<PreguntaDto> preguntasAbrir = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean scrollEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrar_encuestas_activity);
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");

        listViewEncuestas = findViewById(R.id.listaEncuestas);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_administrar);
        swipeRefreshLayout.setOnRefreshListener(this);

        listViewEncuestas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listViewEncuestas == null || listViewEncuestas.getChildCount() == 0) ?
                                0 : listViewEncuestas.getChildAt(0).getTop();

                boolean newScrollEnabled =
                        (firstVisibleItem == 0 && topRowVerticalPosition >= 0) ?
                                true : false;

                if (null != AdministrarEncuestasActivity.this.swipeRefreshLayout && scrollEnabled != newScrollEnabled) {
                    // Start refreshing....
                    AdministrarEncuestasActivity.this.swipeRefreshLayout.setEnabled(newScrollEnabled);
                    scrollEnabled = newScrollEnabled;
                }
            }
        });

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

        String url = getResources().getString(R.string.urlWS) + "/encuestas/getAll";
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.CARGAR_ENCUESTAS.getCodigo());
        httpAsyncTask.setHttpAsyncTaskInterface(AdministrarEncuestasActivity.this);
        try {
            String receivedData = httpAsyncTask.execute(url).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        if (listaEncuestas.size() > 0) {
            List<Map<String, String>> crsList = new ArrayList<Map<String, String>>();
            for (ListaEncuestaDto dto : listaEncuestas) {
                Map<String, String> aug = new HashMap<String, String>();
                aug.put("tituloEncuesta", dto.getTitulo());
                aug.put("descripcionEncuesta", dto.getDescripcion());
                crsList.add(aug);
            }

            String[] keys = {"tituloEncuesta", "descripcionEncuesta"};
            int[] widgetIds = {R.id.titulo_cargar_encuesta, R.id.subtitulo_cargar_encuesta};
            SimpleAdapter crsAdapter = new SimpleAdapter(AdministrarEncuestasActivity.this, crsList, R.layout.encuesta_item, keys, widgetIds) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    if (position % 2 == 1) {
                        view.setBackgroundColor(getResources().getColor(R.color.impares));
                    } else {
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
                        String url = getResources().getString(R.string.urlWS) + "/encuestas/disableEncuesta?idEncuesta=" + listaEncuestas.get(i).getId() + "&idUsuario=" + usuarioLogueado.getId();
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
                        String urlAbrir = getResources().getString(R.string.urlWS) + "/encuestas/openEncuesta?idEncuesta=" + listaEncuestas.get(i).getId();
                        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.OPEN_ENCUESTA.getCodigo());
                        httpAsyncTask.setHttpAsyncTaskInterface(AdministrarEncuestasActivity.this);
                        try {
                            String receivedData = httpAsyncTask.execute(urlAbrir).get();
                        } catch (ExecutionException | InterruptedException ei) {
                            ei.printStackTrace();
                        }


                        Intent modificar_intent = new Intent(AdministrarEncuestasActivity.this, ModificarEncuestaActivity.class).putExtra("usuario", usuarioLogueado);
                        modificar_intent.putExtra("idEncuestaPersistida", listaEncuestas.get(i).getId());
                        modificar_intent.putExtra("preguntas", preguntasAbrir);
                        modificar_intent.putExtra("preguntasEliminar", new ArrayList<PreguntaDto>());
                        modificar_intent.putExtra("respuestasEliminar", new ArrayList<RespuestaDto>());
                        modificar_intent.putExtra("tituloGuardar", listaEncuestas.get(i).getTitulo());
                        modificar_intent.putExtra("descripcionGuardar", listaEncuestas.get(i).getDescripcion());
                        modificar_intent.putExtra("geolocalizada", listaEncuestas.get(i).getGeolocalizada());
                        modificar_intent.putExtra("isSexoRestriccion", listaEncuestas.get(i).getIsSexoRestriccion());
                        modificar_intent.putExtra("isEdadRestriccion", listaEncuestas.get(i).getIsEdadRestriccion());
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onRefresh() {
        finish();
        startActivity(getIntent());
        Toast.makeText(AdministrarEncuestasActivity.this, "Actualizado!", Toast.LENGTH_LONG).show();
    }
}
