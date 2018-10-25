package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Facundo Salmerón on 16/10/2018.
 */

public class ResolviendoEncuestaEspecialActivity extends AppCompatActivity implements PreguntaResponderRecyclerViewAdapter.ItemClickListener {

    private Toolbar toolbar;
    private String tituloEncuesta;
    private String descripcionEncuesta;
    private TextView titulo;
    private TextView descripcion;
    private TextView cargandoErrores;
    private RecyclerView recyclerView;
    private PreguntaResponderRecyclerViewAdapter adapter;
    private ArrayList<PreguntaDto> preguntas;
    private CardView enviar;
    private CardView volver;
    private TextInputLayout edad;
    private RadioGroup sexo_radioGroup;
    private RadioButton sexo_radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolviendo_encuesta_especial_activity);
        toolbar = findViewById(R.id.title_resolviendo_especial);
        tituloEncuesta = (String) getIntent().getSerializableExtra("tituloEncuesta");
        descripcionEncuesta = (String) getIntent().getSerializableExtra("descripcionEncuesta");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResolviendoEncuestaEspecialActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        titulo = findViewById(R.id.titulo_encuesta_resolver);
        descripcion = findViewById(R.id.descripcion_encuesta_resolver);
        cargandoErrores = findViewById(R.id.cargandoErroresResponderEspecial);
        cargandoErrores.setVisibility(View.GONE);

        edad = findViewById(R.id.edad_resolviendo);

        enviar = findViewById(R.id.enviar_resolviendo_button);
        volver = findViewById(R.id.volver_resolviendo_button);

        volver.setOnClickListener((View view) ->
                posibleSalida()
        );
        enviar.setOnClickListener((View v) ->
                onClickResponderEncuesta()
        );

        sexo_radioGroup = findViewById(R.id.sexo_encuestado);
        sexo_radioGroup.setOnCheckedChangeListener((RadioGroup radioGroup, int i) ->
                sexo_radioButton = findViewById(i)
        );

        titulo.setText(tituloEncuesta);
        descripcion.setText(descripcionEncuesta);

        preguntas = (ArrayList<PreguntaDto>) getIntent().getSerializableExtra("preguntas");
        recyclerView = findViewById(R.id.recycler_resolviendo_especial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PreguntaResponderRecyclerViewAdapter(this, preguntas);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void onClickResponderEncuesta(){
        edad.setError(null);
        cargandoErrores.setTextColor(getResources().getColor(R.color.colorAccent));
        if (sexo_radioButton == null || edad.getEditText().getText().toString().isEmpty()) {
            cargandoErrores.setText("Debe completar todos los campos y responder todas las preguntas!");
            cargandoErrores.setVisibility(View.VISIBLE);
            edad.setError(edad.getEditText().getText().toString().isEmpty() ? "Debe completar la edad!!" : null);
        } else {

            if (!evaluarRespuestas()){
                cargandoErrores.setText("Debe completar todos los campos y responder todas las preguntas!");
                cargandoErrores.setVisibility(View.VISIBLE);
            } else{
                cargandoErrores.setVisibility(View.VISIBLE);
                cargandoErrores.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                cargandoErrores.setText("Cargando...");
                new Thread( () -> {
                        runOnUiThread( () -> {
                            persistirRespuestas();
                        });
                }).start();
                cargandoErrores.setVisibility(View.GONE);
            }
        }
    }

    private void persistirRespuestas(){
        for (RecyclerView.ViewHolder vh : adapter.getViewHolders()){
            switch (vh.getItemViewType()){
                case 1:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder2 viewHolderChoicer = (PreguntaResponderRecyclerViewAdapter.ViewHolder2) vh;
                    ArrayList<Integer> respuestasChecked = getRespuestasChecked(viewHolderChoicer.getListaCheckbox());
                    //LOGICA DE PERSISTIR
                    break;
                case 2:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder2 viewHolderUnica = (PreguntaResponderRecyclerViewAdapter.ViewHolder2) vh;
                    Integer respuestaTildada = getRespuestaTildada(viewHolderUnica.getListaRadios());
                    //LOGICA DE PERSISTIR
                    break;
                case 5:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder viewHolderScale = (PreguntaResponderRecyclerViewAdapter.ViewHolder) vh;
                    String rtaEscala = viewHolderScale.getEditText().getText().toString();
                    //LOGICA DE PERSISTIR
                    break;
                default:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder viewHolder = (PreguntaResponderRecyclerViewAdapter.ViewHolder) vh;
                    String rtaTextNum = viewHolder.getEditText().getText().toString();
                    //LOGICA DE PERSISTIR
                    break;
            }
        }
    }

    private ArrayList<Integer> getRespuestasChecked(ArrayList<CheckBox> checkBoxes){
        ArrayList<Integer> rtas = new ArrayList<>();
        for (CheckBox cb : checkBoxes){
            if (cb.isChecked()){
                rtas.add(cb.getId());
            }
        }
        return rtas;
    }

    private Integer getRespuestaTildada(ArrayList<RadioButton> radioButtons){
        Integer rta = null;
        for (RadioButton rb : radioButtons){
            if (rb.isChecked()){
                rta = rb.getId();
                break;
            }
        }
        return rta;
    }

    private boolean evaluarRespuestas(){
        for (RecyclerView.ViewHolder vh : adapter.getViewHolders()){
            switch (vh.getItemViewType()){
                case 1:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder2 viewHolderChoicer = (PreguntaResponderRecyclerViewAdapter.ViewHolder2) vh;
                    if (!evaluarRespuestasCheckbox(viewHolderChoicer.getListaCheckbox())) return false;
                    break;
                case 2:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder2 viewHolderUnica = (PreguntaResponderRecyclerViewAdapter.ViewHolder2) vh;
                    if (!evaluarRespuestasRadios(viewHolderUnica.getListaRadios())) return false;
                    break;
                case 5:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder viewHolderScale = (PreguntaResponderRecyclerViewAdapter.ViewHolder) vh;
                    if (viewHolderScale.getScaleEditText().getText().toString().isEmpty()) return false;
                    break;
                default:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder viewHolder = (PreguntaResponderRecyclerViewAdapter.ViewHolder) vh;
                    if (viewHolder.getEditText().getText().toString().isEmpty()) return false;
                    break;
            }
        }
        return true;
    }

    private boolean evaluarRespuestasCheckbox(ArrayList<CheckBox> checkBoxes){
        for (CheckBox cb : checkBoxes){
            if (cb.isChecked()){
                return true;
            }
        }
        return false;
    }

    private boolean evaluarRespuestasRadios(ArrayList<RadioButton> radioButtons){
        for (RadioButton rb : radioButtons){
            if (rb.isChecked()){
                return true;
            }
        }
        return false;
    }

    private void posibleSalida(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResolviendoEncuestaEspecialActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todas las respuestas!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ResolviendoEncuestaEspecialActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        alertDialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        posibleSalida();
    }
}
