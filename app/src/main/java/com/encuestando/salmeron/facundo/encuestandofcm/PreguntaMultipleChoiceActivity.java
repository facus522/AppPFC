package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Facundo Salmerón on 19/9/2018.
 */

public class PreguntaMultipleChoiceActivity extends AppCompatActivity implements Serializable, RespuestaNuevaRecyclerViewAdapter.ItemClickListener {

    private Toolbar toolbar;
    private TextInputLayout pregunta;
    private CardView agregar;
    private CardView volver;
    private FloatingActionButton agregarRespuestaBoton;
    private RecyclerView recyclerView;
    private ArrayList<RespuestaDto> respuestas;
    private RespuestaNuevaRecyclerViewAdapter adapter;
    private Boolean modificando;
    private Long idPregunta;
    private TextView tvButton;
    private Integer idPreguntaPersistida;
    private ArrayList<RespuestaDto> respuestasEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregunta_multiple_choice_activity);
        respuestas = new ArrayList<>();
        pregunta = findViewById(R.id.pregunta_multiple_choice);
        modificando = (Boolean) getIntent().getSerializableExtra("modificando");
        idPreguntaPersistida = (Integer) getIntent().getSerializableExtra("idPreguntaPersistida");
        if (modificando) {
            pregunta.getEditText().setText((String) getIntent().getSerializableExtra("preguntaChoice"));
            respuestas = (ArrayList) getIntent().getSerializableExtra("respuestasChoice");
            respuestasEliminar = (ArrayList<RespuestaDto>) getIntent().getSerializableExtra("respuestasEliminar");
            idPregunta = (Long) getIntent().getSerializableExtra("idPreguntaModificar");
            tvButton = findViewById(R.id.texto_crear_modificar_choice);
            tvButton.setText("Modificar");
        }
        if (savedInstanceState != null){
            String preguntaMultipleChoice = savedInstanceState.getString("preguntaMultipleChoice");
            ArrayList<RespuestaDto> listaRespuestas = savedInstanceState.getParcelableArrayList("respuestasMultipleChoice");

            if (preguntaMultipleChoice != null){
                pregunta.getEditText().setText(preguntaMultipleChoice);
            }

            if (listaRespuestas != null && !listaRespuestas.isEmpty()){
                respuestas = listaRespuestas;
            }
        }
        toolbar = findViewById(R.id.pregunta_multiple_choice_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaMultipleChoiceActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        recyclerView = findViewById(R.id.recycler_respuestas_choice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RespuestaNuevaRecyclerViewAdapter(this, respuestas, R.layout.row_respuesta_nueva);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        agregar = findViewById(R.id.agrega_multiple_choice_button);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregunta.setError(null);
                if (pregunta.getEditText().getText().toString().isEmpty()){
                    pregunta.setError("La pregunta no puede estar vacía");
                } else if (pregunta.getEditText().getText().length() > pregunta.getCounterMaxLength()){
                    pregunta.setError("Se superó la cantidad máxima permitida de caracteres.");
                } else if (respuestas.size() < 2) {
                    pregunta.setError("Deben agregarse al menos 2 respuestas!!");
                } else {
                    PreguntaDto dto = new PreguntaDto();
                    dto.setDescripcion(pregunta.getEditText().getText().toString());
                    dto.setTipoPregunta(TipoPreguntaEnum.MULTIPLE_CHOICE);
                    dto.setRespuestas(respuestas);
                    dto.setId(modificando ? idPregunta : null);
                    dto.setPreguntaModificada(modificando ? Boolean.TRUE : Boolean.FALSE);
                    dto.setIdPersistido(idPreguntaPersistida);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("pregunta", dto);
                    returnIntent.putExtra("rtasEliminar", respuestasEliminar);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
        volver = findViewById(R.id.volver_multiple_choice_button);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posibleSalida();
            }
        });

        agregarRespuestaBoton = findViewById(R.id.boton_agregar_respuesta);
        agregarRespuestaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText rta = new TextInputEditText(PreguntaMultipleChoiceActivity.this);
                rta.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                rta.setHint("Respuesta");
                AlertDialog d = new AlertDialog.Builder(PreguntaMultipleChoiceActivity.this)
                        .setView(rta)
                        .setTitle("Ingrese una opción:")
                        .setPositiveButton("Aceptar", null)
                        .setNegativeButton("Cancelar", null).create();
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button accept = d.getButton(AlertDialog.BUTTON_POSITIVE);
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                rta.setError(null);
                                if (rta.getText().toString().isEmpty()){
                                    rta.setError("La respuesta no puede estar vacía");
                                } else{
                                    RespuestaDto rtaDto = new RespuestaDto();
                                    rtaDto.setDescripcion(rta.getText().toString());
                                    respuestas.add(rtaDto);
                                    d.dismiss();
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(PreguntaMultipleChoiceActivity.this, "Respuesta agregada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Button cancel = d.getButton(AlertDialog.BUTTON_NEGATIVE);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                d.dismiss();
                            }
                        });
                    }
                });
                d.show();
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreguntaMultipleChoiceActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Qué acción desea realizar sobre la respuesta: '" + respuestas.get(position).getDescripcion() + "'.");
        alertDialog.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (modificando && respuestas.get(position).getIdPersistido() != null && respuestas.get(position).getIdPersistido() != -1){
                    respuestasEliminar.add(respuestas.get(position));
                }
                respuestas.remove(position);
                dialog.dismiss();
                adapter.notifyDataSetChanged();
                Toast.makeText(PreguntaMultipleChoiceActivity.this, "Respuesta eliminada correctamente", Toast.LENGTH_SHORT).show();
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
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                TextInputEditText rta = new TextInputEditText(PreguntaMultipleChoiceActivity.this);
                rta.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                rta.setHint("Respuesta");
                rta.setText(adapter.getItem(position).getDescripcion());
                AlertDialog d = new AlertDialog.Builder(PreguntaMultipleChoiceActivity.this)
                        .setView(rta)
                        .setTitle("Ingrese una opción:")
                        .setPositiveButton("Aceptar", null)
                        .setNegativeButton("Cancelar", null).create();
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button accept = d.getButton(AlertDialog.BUTTON_POSITIVE);
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                rta.setError(null);
                                if (rta.getText().toString().isEmpty()){
                                    rta.setError("La respuesta no puede estar vacía");
                                } else{
                                    RespuestaDto dtoRta = new RespuestaDto();
                                    dtoRta.setDescripcion(rta.getText().toString());
                                    if (modificando && respuestas.get(position).getIdPersistido() != null && respuestas.get(position).getIdPersistido() != -1){
                                        dtoRta.setRespuestaModificada(Boolean.TRUE);
                                        dtoRta.setIdPersistido(respuestas.get(position).getIdPersistido());
                                    }
                                    respuestas.set(position, dtoRta);
                                    d.dismiss();
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(PreguntaMultipleChoiceActivity.this, "Respuesta modificada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Button cancel = d.getButton(AlertDialog.BUTTON_NEGATIVE);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                d.dismiss();
                            }
                        });
                    }
                });
                d.show();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        posibleSalida();
    }

    private void posibleSalida(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreguntaMultipleChoiceActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                PreguntaMultipleChoiceActivity.this.finish();
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("preguntaMultipleChoice", pregunta.getEditText().getText().toString());
        outState.putParcelableArrayList("respuestasMultipleChoice", respuestas);
        super.onSaveInstanceState(outState);
    }


}
