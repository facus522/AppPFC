package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class PreguntaNumericaActivity extends AppCompatActivity implements Serializable{

    private Toolbar toolbar;
    private TextInputLayout pregunta;
    private CardView agregar;
    private CardView volver;
    private Boolean modificando;
    private Long idPregunta;
    private TextView tvButton;
    private Integer idPreguntaPersistida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregunta_numerica_activity);
        toolbar = findViewById(R.id.pregunta_numerica_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        modificando = (Boolean) getIntent().getSerializableExtra("modificando");
        idPreguntaPersistida = (Integer) getIntent().getSerializableExtra("idPreguntaPersistida");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaNumericaActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        pregunta = findViewById(R.id.pregunta_numerica);
        if (modificando) {
            pregunta.getEditText().setText((String) getIntent().getSerializableExtra("preguntaNumerica"));
            idPregunta = (Long) getIntent().getSerializableExtra("idPreguntaModificar");
            tvButton = findViewById(R.id.texto_crear_modificar_numerica);
            tvButton.setText("Modificar");
        }
        agregar = findViewById(R.id.agrega_numerica_button);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregunta.setError(null);
                if (pregunta.getEditText().getText().toString().isEmpty()){
                    pregunta.setError("La pregunta no puede estar vacía");
                } else if (pregunta.getEditText().getText().length() > pregunta.getCounterMaxLength()){
                    pregunta.setError("Se superó la cantidad máxima permitida de caracteres.");
                } else {
                    PreguntaDto dto = new PreguntaDto();
                    dto.setDescripcion(pregunta.getEditText().getText().toString());
                    dto.setTipoPregunta(TipoPreguntaEnum.RESPUESTA_NUMERICA);
                    dto.setId(modificando ? idPregunta : null);
                    dto.setPreguntaModificada(modificando ? Boolean.TRUE : Boolean.FALSE);
                    dto.setIdPersistido(idPreguntaPersistida);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("pregunta", dto);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
        volver = findViewById(R.id.volver_numerica_button);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posibleSalida();
            }
        });

        if (savedInstanceState != null){
            String preguntaNumerica = savedInstanceState.getString("preguntaNumerica");

            if (pregunta!= null){
                pregunta.getEditText().setText(preguntaNumerica);
            }
        }

    }

    @Override
    public void onBackPressed() {
        posibleSalida();
    }

    private void posibleSalida(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreguntaNumericaActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                PreguntaNumericaActivity.this.finish();
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
        outState.putString("preguntaNumerica", pregunta.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

}
