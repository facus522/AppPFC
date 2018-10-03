package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.Serializable;
import java.util.ArrayList;

public class ModificarEncuestaActivity extends AppCompatActivity implements HttpAsyncTaskInterface, Serializable, PreguntaNuevaRecyclerViewAdapter.ItemClickListener {

    private Toolbar toolbar;
    private UsuarioDto usuarioLogueado;
    private FloatingActionButton multipleChoiceButton;
    private FloatingActionButton unicaButton;
    private FloatingActionButton numericaButton;
    private FloatingActionButton textualButton;
    private FloatingActionButton escalaButton;
    private FloatingActionMenu actionMenu;
    private ArrayList<PreguntaDto> preguntas;
    private RecyclerView recyclerView;
    private PreguntaNuevaRecyclerViewAdapter adapter;
    private CardView modificar;
    private CardView volver;
    private TextInputLayout titulo;
    private TextInputLayout descripcion;
    private String tituloGuardar;
    private String descripcionGuardar;
    private HttpAsyncTask httpAsyncTaskEncuesta;
    private HttpAsyncTask httpAsyncTaskPregunta;
    private HttpAsyncTask httpAsyncTaskRespuesta;
    private Integer idEncuestaAsignado;
    private Integer idPreguntaAsignado;
    private Integer idRespuestaAsignado;
    private TextView cargandoModificar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_encuesta_activity);
        preguntas = (ArrayList<PreguntaDto>) getIntent().getSerializableExtra("preguntas");
        tituloGuardar = (String) getIntent().getSerializableExtra("tituloGuardar");
        descripcionGuardar = (String) getIntent().getSerializableExtra("descripcionGuardar");
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        cargandoModificar = findViewById(R.id.cargandoModificarEncuesta);
        cargandoModificar.setVisibility(View.GONE);
        toolbar = findViewById(R.id.titulo_modificar_encuesta);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_FIRST_USER);
                ModificarEncuestaActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        modificar = findViewById(R.id.modificar_encuesta_button);
        volver = findViewById(R.id.volver_modificar_encuesta_button);
        titulo = findViewById(R.id.titulo_encuesta_modificar);
        descripcion = findViewById(R.id.descripcion_encuesta_modificar);
        titulo.getEditText().setText(tituloGuardar);
        descripcion.getEditText().setText(descripcionGuardar);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posibleSalida();
            }
        });
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickModificarEncuesta();
            }
        });
        recyclerView = findViewById(R.id.recycler_encuesta_modificar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PreguntaNuevaRecyclerViewAdapter(this, preguntas);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        actionMenu = findViewById(R.id.boton_agregar_pregunta);
        multipleChoiceButton = findViewById(R.id.multiple_choice_button);
        multipleChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_multiple_choice = new Intent(ModificarEncuestaActivity.this, PreguntaMultipleChoiceActivity.class).putExtra("modificando", false);
                startActivityForResult(agregar_multiple_choice, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        unicaButton = findViewById(R.id.unica_button);
        unicaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_unica_opcion = new Intent(ModificarEncuestaActivity.this, PreguntaUnicaOpcionActivity.class).putExtra("modificando", false);
                startActivityForResult(agregar_unica_opcion, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        numericaButton = findViewById(R.id.numerica_button);
        numericaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_numerica = new Intent(ModificarEncuestaActivity.this, PreguntaNumericaActivity.class).putExtra("modificando", false);
                startActivityForResult(agregar_numerica, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        textualButton = findViewById(R.id.textual_button);
        textualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_textual = new Intent(ModificarEncuestaActivity.this, PreguntaTextualActivity.class).putExtra("modificando", false);
                startActivityForResult(agregar_textual, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        escalaButton = findViewById(R.id.escala_button);
        escalaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_escala = new Intent(ModificarEncuestaActivity.this, PreguntaEscalaActivity.class).putExtra("modificando", false);
                startActivityForResult(agregar_escala, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        if (savedInstanceState != null){
            String tituloNuevo = savedInstanceState.getString("tituloNuevo");
            String descripcionNuevo = savedInstanceState.getString("descripcionNuevo");
            if (tituloNuevo!= null){
                titulo.getEditText().setText(tituloNuevo);
            }
            if (descripcionNuevo!=null){
                descripcion.getEditText().setText(descripcionNuevo);
            }
        }
    }

    private void onClickModificarEncuesta(){

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tituloNuevo", titulo.getEditText().getText().toString());
        outState.putString("descripcionNuevo", descripcion.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    private String reemplazarEspacios(String valor){
        String aux = valor.replace(" ", "%20");
        aux = aux.replace("\n", "%20");
        return aux;
    }

    @Override
    public void onBackPressed() {
        if (actionMenu.isOpened()){
            actionMenu.close(true);
        }else{
            posibleSalida();
        }

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void posibleSalida(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModificarEncuestaActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos modificados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                setResult(RESULT_FIRST_USER);
                ModificarEncuestaActivity.this.finish();
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
}
