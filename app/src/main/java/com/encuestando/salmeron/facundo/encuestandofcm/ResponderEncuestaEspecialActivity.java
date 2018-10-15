package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ResponderEncuestaEspecialActivity extends AppCompatActivity implements HttpAsyncTaskInterface, EncuestaEspecialRecyclerViewAdapter.ItemClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EncuestaEspecialRecyclerViewAdapter adapter;
    private ArrayList<ListaEncuestaDto> encuestas = new ArrayList<>();
    private HttpAsyncTask httpAsyncTask;

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

        String url = "http://192.168.0.107:8080/EncuestasFCM/encuestas/getAll";
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

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void cargarEncuestas(String result) {
        String encuestasJSON = result;
        if (encuestasJSON != null && !encuestasJSON.isEmpty()) {
            encuestas.addAll(JSONConverterUtils.JSONEncuestasConverter(result));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
