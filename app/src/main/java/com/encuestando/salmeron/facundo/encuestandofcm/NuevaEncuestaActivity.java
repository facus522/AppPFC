package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

public class
NuevaEncuestaActivity extends AppCompatActivity implements HttpAsyncTaskInterface, Serializable, PreguntaNuevaRecyclerViewAdapter.ItemClickListener {

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
    private CardView crear;
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
    private TextView cargandoCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_encuesta_activity);
        preguntas = (ArrayList<PreguntaDto>) getIntent().getSerializableExtra("preguntas");
        tituloGuardar = (String) getIntent().getSerializableExtra("tituloGuardar");
        descripcionGuardar = (String) getIntent().getSerializableExtra("descripcionGuardar");
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        cargandoCrear = findViewById(R.id.cargandoCrearNueva);
        cargandoCrear.setVisibility(View.GONE);
        toolbar = findViewById(R.id.titulo_nueva_encuesta);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevaEncuestaActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        crear = findViewById(R.id.crear_nueva_encuesta_button);
        volver = findViewById(R.id.volver_nueva_encuesta_button);
        titulo = findViewById(R.id.titulo_encuesta_nueva);
        descripcion = findViewById(R.id.descripcion_encuesta_nueva);
        titulo.getEditText().setText(tituloGuardar);
        descripcion.getEditText().setText(descripcionGuardar);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posibleSalida();
            }
        });
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCrearEncuesta();
            }
        });
        recyclerView = findViewById(R.id.recycler_encuesta_nueva);
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
                Intent agregar_multiple_choice = new Intent(NuevaEncuestaActivity.this, PreguntaMultipleChoiceActivity.class).putExtra("modificando", false);
                startActivityForResult(agregar_multiple_choice, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        unicaButton = findViewById(R.id.unica_button);
        unicaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_unica_opcion = new Intent(NuevaEncuestaActivity.this, PreguntaUnicaOpcionActivity.class).putExtra("modificando", false);
                startActivityForResult(agregar_unica_opcion, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        numericaButton = findViewById(R.id.numerica_button);
        numericaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_numerica = new Intent(NuevaEncuestaActivity.this, PreguntaNumericaActivity.class).putExtra("modificando", false);
                startActivityForResult(agregar_numerica, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        textualButton = findViewById(R.id.textual_button);
        textualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_textual = new Intent(NuevaEncuestaActivity.this, PreguntaTextualActivity.class).putExtra("modificando", false);
                startActivityForResult(agregar_textual, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        escalaButton = findViewById(R.id.escala_button);
        escalaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_escala = new Intent(NuevaEncuestaActivity.this, PreguntaEscalaActivity.class).putExtra("modificando", false);
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

    private void onClickCrearEncuesta(){
        titulo.setError(null);
        descripcion.setError(null);
        if (titulo.getEditText().getText().toString().isEmpty()){
            titulo.setError("El título no puede estar vacío!");
            return;
        } else if (titulo.getEditText().getText().length() > titulo.getCounterMaxLength()){
            titulo.setError("Se superó la cantidad máxima permitida de caracteres.");
            return;
        }

        if (descripcion.getEditText().getText().length() > descripcion.getCounterMaxLength()){
            descripcion.setError("Se superó la cantidad máxima permitida de caracteres.");
            return;
        }

        if (preguntas.size() < 1){
            descripcion.setError("Debe agregarse al menos una pregunta.");
            return;
        }

        cargandoCrear.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String urlEncuesta = "http://192.168.0.107:8080/EncuestasFCM/encuestas/saveEncuesta?titulo=" + reemplazarEspacios(titulo.getEditText().getText().toString())
                                + "&descripcion=" + reemplazarEspacios(descripcion.getEditText().getText().toString()) + "&idUsuario=" + usuarioLogueado.getId();
                        httpAsyncTaskEncuesta = new HttpAsyncTask(WebServiceEnum.CREAR_ENCUESTA.getCodigo());
                        httpAsyncTaskEncuesta.setHttpAsyncTaskInterface(NuevaEncuestaActivity.this);
                        try{
                            String receivedDataEncuesta = httpAsyncTaskEncuesta.execute(urlEncuesta).get();
                        }
                        catch (ExecutionException | InterruptedException ei){
                            ei.printStackTrace();
                        }

                        if (idEncuestaAsignado == null) {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NuevaEncuestaActivity.this, AlertDialog.THEME_HOLO_DARK);
                            alertDialog.setTitle("Error de Conexión");
                            alertDialog.setMessage("Verifique su conexión a Internet! \n\nSi el problema persiste se trata de un error interno en la base de datos.");
                            alertDialog.setIcon(R.drawable.ic_action_error);
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alertDialog.show();
                        } else {
                            for (PreguntaDto pregunta: preguntas){
                                String urlPregunta = "http://192.168.0.107:8080/EncuestasFCM/preguntas/savePregunta?descripcion=" + reemplazarEspacios(pregunta.getDescripcion())
                                        + "&idEncuesta=" + idEncuestaAsignado + "&numeroEscala=" + (pregunta.getMaximaEscala() != null ? pregunta.getMaximaEscala() : "")
                                        + "&idTipoRespuesta=" + pregunta.getTipoPregunta().getCodigo() + "&idUsuario=" + usuarioLogueado.getId();
                                httpAsyncTaskPregunta = new HttpAsyncTask(WebServiceEnum.CREAR_PREGUNTA.getCodigo());
                                httpAsyncTaskPregunta.setHttpAsyncTaskInterface(NuevaEncuestaActivity.this);
                                try{
                                    String receivedDataPregunta = httpAsyncTaskPregunta.execute(urlPregunta).get();
                                }
                                catch (ExecutionException | InterruptedException ei){
                                    ei.printStackTrace();
                                }

                                if (pregunta.getRespuestas() != null && !pregunta.getRespuestas().isEmpty() && idPreguntaAsignado != null){
                                    for (RespuestaDto rta : pregunta.getRespuestas()){
                                        String urlRespuesta = "http://192.168.0.107:8080/EncuestasFCM/respuestas/saveRespuesta?descripcion=" + reemplazarEspacios(rta.getDescripcion())
                                                + "&idPregunta=" + idPreguntaAsignado + "&idTipoRespuesta=" + pregunta.getTipoPregunta().getCodigo()
                                                + "&idUsuario=" + usuarioLogueado.getId();
                                        httpAsyncTaskRespuesta = new HttpAsyncTask(WebServiceEnum.CREAR_RESPUESTA.getCodigo());
                                        httpAsyncTaskRespuesta.setHttpAsyncTaskInterface(NuevaEncuestaActivity.this);
                                        try{
                                            String receivedDataRta = httpAsyncTaskRespuesta.execute(urlRespuesta).get();
                                        }
                                        catch (ExecutionException | InterruptedException ei){
                                            ei.printStackTrace();
                                        }
                                        idRespuestaAsignado = null;
                                    }
                                }

                                idPreguntaAsignado = null;
                            }
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NuevaEncuestaActivity.this, AlertDialog.THEME_HOLO_DARK);
                            alertDialog.setMessage("¡La encuesta ha sido creada correctamente!");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                }
                            });
                            alertDialog.show();
                        }
                        cargandoCrear.setVisibility(View.GONE);
                    }
                });
            }
        }).start();


    }

    private String reemplazarEspacios(String valor){
        String aux = valor.replace(" ", "%20");
        aux = aux.replace("\n", "%20");
        return aux;
    }

    @Override
    public void crearEncuesta(String result) {
        String createEncuestaJSON = result;
        if (createEncuestaJSON != null && !createEncuestaJSON.isEmpty()) {
            idEncuestaAsignado = JSONConverterUtils.JSONCreateEncuestaConverter(result);
        }
    }

    @Override
    public void crearPregunta(String result) {
        String createPreguntaJSON = result;
        if (createPreguntaJSON != null && !createPreguntaJSON.isEmpty()) {
            idPreguntaAsignado = JSONConverterUtils.JSONCreateEncuestaConverter(result);
        }
    }

    @Override
    public void crearRespuesta(String result) {
        String createPreguntaJSON = result;
        if (createPreguntaJSON != null && !createPreguntaJSON.isEmpty()) {
            idRespuestaAsignado = JSONConverterUtils.JSONCreateEncuestaConverter(result);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tituloNuevo", titulo.getEditText().getText().toString());
        outState.putString("descripcionNuevo", descripcion.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            PreguntaDto dto = (PreguntaDto) data.getSerializableExtra("pregunta");
            boolean isModificacion = false;
            if (dto.getId() != null){
                reemplazarPreguntaModificada(dto);
                isModificacion = true;
            } else {
                dto.setId(new Long(preguntas.size() + 1));
                preguntas.add(dto);
            }
            finish();
            getIntent().putExtra("preguntas", preguntas);
            getIntent().putExtra("tituloGuardar", titulo.getEditText().getText().toString());
            getIntent().putExtra("descripcionGuardar", descripcion.getEditText().getText().toString());
            startActivity(getIntent());
            if (isModificacion){
                Toast.makeText(NuevaEncuestaActivity.this, "La pregunta ha sido modificada correctamente!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(NuevaEncuestaActivity.this, "La pregunta ha sido añadida correctamente!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void reemplazarPreguntaModificada(PreguntaDto dto){
        for (PreguntaDto p : preguntas){
            if (p.getId().equals(dto.getId())){
                p.setDescripcion(dto.getDescripcion());
                p.setMaximaEscala(dto.getMaximaEscala());
                p.setRespuestas(dto.getRespuestas());
            }
        }
    }

    private void posibleSalida(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NuevaEncuestaActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                NuevaEncuestaActivity.this.finish();
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
        if (actionMenu.isOpened()){
            actionMenu.close(true);
        }else{
            posibleSalida();
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NuevaEncuestaActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("¿Qué acción desea realizar?");
        alertDialog.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePregunta(adapter.getItem(position));
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
            public void onClick(DialogInterface dialogInterface, int i) {
                openModificacionPregunta(adapter.getItem(position));
            }
        });
        alertDialog.show();
    }

    private void deletePregunta(PreguntaDto dto){
        for (PreguntaDto p : preguntas){
            if (p.getId().equals(dto.getId())){
                p.setActivo(Boolean.FALSE);
            }else if (p.getId() > dto.getId()){
                p.setId(p.getId()-1);
            }
        }
        for (PreguntaDto p : preguntas){
            if (!p.getActivo()){
                preguntas.remove(p);
                break;
            }
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(NuevaEncuestaActivity.this, "La pregunta ha sido eliminada correctamente!", Toast.LENGTH_LONG).show();
    }

    private void openModificacionPregunta(PreguntaDto dto) {
        switch (dto.getTipoPregunta().getCodigo()){
            case 1:
                actionMenu.close(true);
                Intent agregar_multiple_choice = new Intent(NuevaEncuestaActivity.this, PreguntaMultipleChoiceActivity.class);
                agregar_multiple_choice.putExtra("modificando", true);
                agregar_multiple_choice.putExtra("preguntaChoice", dto.getDescripcion());
                agregar_multiple_choice.putExtra("idPreguntaModificar", dto.getId());
                agregar_multiple_choice.putExtra("respuestasChoice", dto.getRespuestas());
                startActivityForResult(agregar_multiple_choice, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 2:
                actionMenu.close(true);
                Intent agregar_unica_opcion = new Intent(NuevaEncuestaActivity.this, PreguntaUnicaOpcionActivity.class);
                agregar_unica_opcion.putExtra("modificando", true);
                agregar_unica_opcion.putExtra("preguntaUnica", dto.getDescripcion());
                agregar_unica_opcion.putExtra("idPreguntaModificar", dto.getId());
                agregar_unica_opcion.putExtra("respuestasUnica", dto.getRespuestas());
                startActivityForResult(agregar_unica_opcion, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 3:
                actionMenu.close(true);
                Intent agregar_numerica = new Intent(NuevaEncuestaActivity.this, PreguntaNumericaActivity.class);
                agregar_numerica.putExtra("modificando", true);
                agregar_numerica.putExtra("preguntaNumerica", dto.getDescripcion());
                agregar_numerica.putExtra("idPreguntaModificar", dto.getId());
                startActivityForResult(agregar_numerica, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 4:
                actionMenu.close(true);
                Intent agregar_textual = new Intent(NuevaEncuestaActivity.this, PreguntaTextualActivity.class);
                agregar_textual.putExtra("modificando", true);
                agregar_textual.putExtra("preguntaTextual", dto.getDescripcion());
                agregar_textual.putExtra("idPreguntaModificar", dto.getId());
                startActivityForResult(agregar_textual, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 5:
                actionMenu.close(true);
                Intent agregar_escala = new Intent(NuevaEncuestaActivity.this, PreguntaEscalaActivity.class);
                agregar_escala.putExtra("modificando", true);
                agregar_escala.putExtra("preguntaEscala", dto.getDescripcion());
                agregar_escala.putExtra("maximaEscala", dto.getMaximaEscala());
                agregar_escala.putExtra("idPreguntaModificar", dto.getId());
                startActivityForResult(agregar_escala, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            default:
                break;
        }
    }

}
