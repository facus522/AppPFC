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
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class PreguntaUnicaOpcionActivity extends AppCompatActivity implements Serializable, RespuestaNuevaRecyclerViewAdapter.ItemClickListener {

    private Toolbar toolbar;
    private TextInputLayout pregunta;
    private CardView agregar;
    private CardView volver;
    private FloatingActionButton agregarRespuestaBoton;
    private RecyclerView recyclerView;
    private ArrayList<String> respuestas;
    private RespuestaNuevaRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregunta_unica_opcion_activity);
        respuestas = new ArrayList<>();
        pregunta = findViewById(R.id.pregunta_respuesta_unica);
        if (savedInstanceState != null){
            String preguntaRespuestaUnica = savedInstanceState.getString("preguntaRespuestaUnica");
            ArrayList<String> listaRespuestas = savedInstanceState.getStringArrayList("respuestasUnicas");

            if (preguntaRespuestaUnica != null){
                pregunta.getEditText().setText(preguntaRespuestaUnica);
            }

            if (listaRespuestas != null && !listaRespuestas.isEmpty()){
                respuestas = listaRespuestas;
            }
        }
        toolbar = findViewById(R.id.pregunta_respuesta_unica_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaUnicaOpcionActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        recyclerView = findViewById(R.id.recycler_respuestas_unicas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RespuestaNuevaRecyclerViewAdapter(this, respuestas, R.layout.row_respuesta_radio_button);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        agregar = findViewById(R.id.agrega_respuesta_unica_button);
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
                    dto.setTipoPregunta(TipoPreguntaEnum.RESPUESTA_UNICA);
                    dto.setRespuestas(respuestas);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("pregunta", dto);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
        volver = findViewById(R.id.volver_respuesta_unica_button);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posibleSalida();
            }
        });

        agregarRespuestaBoton = findViewById(R.id.boton_agregar_respuesta_unica);
        agregarRespuestaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText rta = new TextInputEditText(PreguntaUnicaOpcionActivity.this);
                rta.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                rta.setHint("Respuesta");
                AlertDialog d = new AlertDialog.Builder(PreguntaUnicaOpcionActivity.this)
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
                                    respuestas.add(rta.getText().toString());
                                    d.dismiss();
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(PreguntaUnicaOpcionActivity.this, "Respuesta agregada correctamente", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreguntaUnicaOpcionActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Qué desea realizar sobre la respuesta: '" + respuestas.get(position) + "'.");
        alertDialog.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                respuestas.remove(position);
                dialog.dismiss();
                adapter.notifyDataSetChanged();
                Toast.makeText(PreguntaUnicaOpcionActivity.this, "Respuesta eliminada correctamente", Toast.LENGTH_SHORT).show();
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
                TextInputEditText rta = new TextInputEditText(PreguntaUnicaOpcionActivity.this);
                rta.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                rta.setHint("Respuesta");
                rta.setText(adapter.getItem(position));
                AlertDialog d = new AlertDialog.Builder(PreguntaUnicaOpcionActivity.this)
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
                                    respuestas.set(position, rta.getText().toString());
                                    d.dismiss();
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(PreguntaUnicaOpcionActivity.this, "Respuesta modificada correctamente", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreguntaUnicaOpcionActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                PreguntaUnicaOpcionActivity.this.finish();
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
        outState.putString("preguntaRespuestaUnica", pregunta.getEditText().getText().toString());
        outState.putStringArrayList("respuestasUnicas", respuestas);
        super.onSaveInstanceState(outState);
    }


}
