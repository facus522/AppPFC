package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Facundo Salmerón on 15/10/2018.
 */

public class ResponderEncuestaEspecialActivity extends AppCompatActivity implements HttpAsyncTaskInterface, EncuestaEspecialRecyclerViewAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UsuarioDto usuarioLogueado;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EncuestaEspecialRecyclerViewAdapter adapter;
    private ArrayList<ListaEncuestaDto> encuestas = new ArrayList<>();
    private ArrayList<PreguntaDto> preguntasAbrir = new ArrayList<>();
    private ArrayList<ResultadoDto> resultados = new ArrayList<>();
    private HttpAsyncTask httpAsyncTask;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean scrollEnabled;
    private ImageButton ayuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.responder_encuesta_especial_activity);
        toolbar = findViewById(R.id.title_resolver_especial);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResponderEncuestaEspecialActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ayuda = findViewById(R.id.ayuda_menu);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaEspecialActivity.this, AlertDialog.THEME_HOLO_DARK);
                alertDialog.setTitle("Información");
                alertDialog.setMessage("Las encuestas que contienen la etiqueta de 'Geolocalizadas' guardarán en base de datos la ubicación del encuestado.");
                alertDialog.setIcon(R.drawable.ic_action_error);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_responder_especial);
        swipeRefreshLayout.setOnRefreshListener(this);

        String url = getResources().getString(R.string.urlWS) + "/encuestas/getAll";
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.CARGAR_ENCUESTAS.getCodigo());
        httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
        try {
            String receivedData = httpAsyncTask.execute(url).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        if (encuestas.size() < 1){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaEspecialActivity.this);
            alertDialog.setTitle("Sin Datos");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Actualmente no se encuentran cargadas Encuestas.");
            alertDialog.setIcon(R.drawable.ic_action_error);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ResponderEncuestaEspecialActivity.this.finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
            alertDialog.show();
        }

        recyclerView = findViewById(R.id.recycler_resolver_especial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EncuestaEspecialRecyclerViewAdapter(this, encuestas);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ?
                                0 : recyclerView.getChildAt(0).getTop();

                boolean newScrollEnabled =
                        (dx == 0 && topRowVerticalPosition >= 0) ?
                                true : false;

                if (null != ResponderEncuestaEspecialActivity.this.swipeRefreshLayout && scrollEnabled != newScrollEnabled) {
                    // Start refreshing....
                    ResponderEncuestaEspecialActivity.this.swipeRefreshLayout.setEnabled(newScrollEnabled);
                    scrollEnabled = newScrollEnabled;
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        finish();
        startActivity(getIntent());
        Toast.makeText(ResponderEncuestaEspecialActivity.this, "Actualizado!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaEspecialActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("¿Qué acción desea realizar?");
        if (Integer.parseInt(encuestas.get(position).getResoluciones()) > 0){
            alertDialog.setNegativeButton("Ver Estadísticas", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i2) {
                    dialogInterface.cancel();

                    String urlAbrirGraficos = getResources().getString(R.string.urlWS) + "/encuestas/openEncuesta?idEncuesta=" + encuestas.get(position).getId();
                    httpAsyncTask = new HttpAsyncTask(WebServiceEnum.OPEN_ENCUESTA.getCodigo());
                    httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
                    try {
                        String receivedData = httpAsyncTask.execute(urlAbrirGraficos).get();
                    } catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }

                    String urlTraer = getResources().getString(R.string.urlWS) + "/resultados/getResultados?idEncuesta=" + encuestas.get(position).getId();
                    httpAsyncTask = new HttpAsyncTask(WebServiceEnum.RESULTADOS_ENCUESTA.getCodigo());
                    httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
                    try {
                        String receivedData = httpAsyncTask.execute(urlTraer).get();
                    } catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }

                    Intent graphics_intent = new Intent(ResponderEncuestaEspecialActivity.this, GraphicsActivity.class).putExtra("usuario", usuarioLogueado);
                    graphics_intent.putExtra("idEncuestaPersistida", encuestas.get(position).getId());
                    graphics_intent.putExtra("preguntas", preguntasAbrir);
                    graphics_intent.putExtra("resultados", resultados);
                    graphics_intent.putExtra("tituloEncuesta", encuestas.get(position).getTitulo());
                    graphics_intent.putExtra("descripcionEncuesta", encuestas.get(position).getDescripcion());
                    graphics_intent.putExtra("isGeolocalizada", encuestas.get(position).getGeolocalizada());
                    startActivityForResult(graphics_intent, 1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

        alertDialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.setPositiveButton("Responder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i2) {
                String urlAbrir = getResources().getString(R.string.urlWS) + "/encuestas/openEncuesta?idEncuesta=" + encuestas.get(position).getId();
                httpAsyncTask = new HttpAsyncTask(WebServiceEnum.OPEN_ENCUESTA.getCodigo());
                httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
                try {
                    String receivedData = httpAsyncTask.execute(urlAbrir).get();
                } catch (ExecutionException | InterruptedException ei) {
                    ei.printStackTrace();
                }

                Intent responder_intent = new Intent(ResponderEncuestaEspecialActivity.this, ResolviendoEncuestaEspecialActivity.class).putExtra("usuario", usuarioLogueado);
                responder_intent.putExtra("idEncuestaPersistida", encuestas.get(position).getId());
                responder_intent.putExtra("preguntas", preguntasAbrir);
                responder_intent.putExtra("tituloEncuesta", encuestas.get(position).getTitulo());
                responder_intent.putExtra("descripcionEncuesta", encuestas.get(position).getDescripcion());
                responder_intent.putExtra("isGeolocalizada", encuestas.get(position).getGeolocalizada());
                startActivityForResult(responder_intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            finish();
            startActivity(getIntent());
            Toast.makeText(ResponderEncuestaEspecialActivity.this, "La encuesta ha sido respondida correctamente!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cargarEncuestas(String result) {
        String encuestasJSON = result;
        if (encuestasJSON != null && !encuestasJSON.isEmpty()) {
            encuestas.addAll(JSONConverterUtils.JSONEncuestasConverter(result));
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
    public void traerResultadosEncuesta(String result) {
        String encuestasJSON = result;
        if (encuestasJSON != null && !encuestasJSON.isEmpty()) {
            resultados = JSONConverterUtils.JSONTraerResultadosEncuestaConverter(result);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
