package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Facundo Salmerón on 02/11/2018.
 */

public class ResponderEncuestaNormalActivity extends AppCompatActivity implements HttpAsyncTaskInterface, EncuestaNormalRecyclerViewAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UsuarioDto usuarioLogueado;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EncuestaNormalRecyclerViewAdapter adapter;
    private ArrayList<ListaEncuestaDto> encuestas = new ArrayList<>();
    private ArrayList<PreguntaDto> preguntasAbrir = new ArrayList<>();
    private HttpAsyncTask httpAsyncTask;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean scrollEnabled;
    private ImageButton ayuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.responder_encuesta_normal_activity);
        toolbar = findViewById(R.id.title_resolver_normal);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResponderEncuestaNormalActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ayuda = findViewById(R.id.ayuda_menu);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaNormalActivity.this, AlertDialog.THEME_HOLO_DARK);
                alertDialog.setTitle("Información");
                alertDialog.setMessage("Las encuestas que contienen la etiqueta de 'Geolocalizadas' guardarán en base de datos la ubicación del encuestado.\nSu finalidad a la hora de analizar los resultados, es tener una respuesta zonal de las mismas.");
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

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_responder_normal);
        swipeRefreshLayout.setOnRefreshListener(this);

        String url = getResources().getString(R.string.urlWS) + "/encuestas/getAll";
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.CARGAR_ENCUESTAS.getCodigo());
        httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaNormalActivity.this);
        try {
            String receivedData = httpAsyncTask.execute(url).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        if (encuestas.size() < 1) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaNormalActivity.this);
            alertDialog.setTitle("Sin Datos");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Actualmente no se encuentran cargadas Encuestas.");
            alertDialog.setIcon(R.drawable.ic_action_error);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ResponderEncuestaNormalActivity.this.finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
            alertDialog.show();
        }

        recyclerView = findViewById(R.id.recycler_resolver_normal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EncuestaNormalRecyclerViewAdapter(this, encuestas);
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

                if (null != ResponderEncuestaNormalActivity.this.swipeRefreshLayout && scrollEnabled != newScrollEnabled) {
                    // Start refreshing....
                    ResponderEncuestaNormalActivity.this.swipeRefreshLayout.setEnabled(newScrollEnabled);
                    scrollEnabled = newScrollEnabled;
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        finish();
        startActivity(getIntent());
        Toast.makeText(ResponderEncuestaNormalActivity.this, "Actualizado!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (encuestas.get(position).getIsEdadRestriccion() != 0){
            if (encuestas.get(position).getIsEdadRestriccion() > usuarioLogueado.getEdad()){
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaNormalActivity.this, AlertDialog.THEME_HOLO_DARK);
                alertDialog.setTitle("Error de validación");
                alertDialog.setMessage("Usted no puede responder la siguient encuesta, ya que está restringida hacia personas mayores de " + encuestas.get(position).getIsEdadRestriccion() + " años de edad.");
                alertDialog.setIcon(R.drawable.ic_action_error);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return;
            }
        }

        if (encuestas.get(position).getIsSexoRestriccion() != 0){
            if (!encuestas.get(position).getIsSexoRestriccion().equals(usuarioLogueado.getSexo())){
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaNormalActivity.this, AlertDialog.THEME_HOLO_DARK);
                alertDialog.setTitle("Error de validación");
                alertDialog.setMessage("Usted no puede responder la siguiente encuesta, ya que está restringida hacia personas del sexo " + (encuestas.get(position).getIsSexoRestriccion().equals(1) ? "masculino." : "femenino."));
                alertDialog.setIcon(R.drawable.ic_action_error);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return;
            }
        }

        String urlAbrir = getResources().getString(R.string.urlWS) + "/encuestas/openEncuestaValidar?idEncuesta="
                + encuestas.get(position).getId()
                + "&idUsuario=" + usuarioLogueado.getId();
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.OPEN_ENCUESTA_VALIDAR.getCodigo());
        httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaNormalActivity.this);
        try {
            String receivedData = httpAsyncTask.execute(urlAbrir).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        if (preguntasAbrir != null) {
            Intent responder_intent = new Intent(ResponderEncuestaNormalActivity.this, ResolviendoEncuestaNormalActivity.class).putExtra("usuario", usuarioLogueado);
            responder_intent.putExtra("idEncuestaPersistida", encuestas.get(position).getId());
            responder_intent.putExtra("preguntas", preguntasAbrir);
            responder_intent.putExtra("tituloEncuesta", encuestas.get(position).getTitulo());
            responder_intent.putExtra("descripcionEncuesta", encuestas.get(position).getDescripcion());
            responder_intent.putExtra("isGeolocalizada", encuestas.get(position).getGeolocalizada());
            startActivityForResult(responder_intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaNormalActivity.this, AlertDialog.THEME_HOLO_DARK);
            alertDialog.setTitle("Error de validación");
            alertDialog.setMessage("Usted ya ha respondido esta encuesta, por lo cual no puede volver a realizarlo.");
            alertDialog.setIcon(R.drawable.ic_action_error);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            finish();
            startActivity(getIntent());
            Toast.makeText(ResponderEncuestaNormalActivity.this, "La encuesta ha sido respondida correctamente!", Toast.LENGTH_LONG).show();
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
    public void abrirEncuestaValidar(String result) {
        String encuestasJSON = result;
        if (encuestasJSON != null && !encuestasJSON.isEmpty()) {
            Map<Boolean, ArrayList<PreguntaDto>> mapa = JSONConverterUtils.JSONAbrirEncuestasValidarConverter(result);
            ArrayList<PreguntaDto> dtosFALSE = mapa.get(Boolean.FALSE);
            preguntasAbrir = dtosFALSE != null ? dtosFALSE : null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
