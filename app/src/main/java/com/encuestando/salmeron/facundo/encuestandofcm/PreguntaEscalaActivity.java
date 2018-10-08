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
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.Serializable;

public class PreguntaEscalaActivity extends AppCompatActivity implements Serializable{

    private Toolbar toolbar;
    private TextInputLayout pregunta;
    private TextInputLayout maxima;
    private CardView agregar;
    private CardView volver;
    private Boolean modificando;
    private Long idPregunta;
    private TextView tvButton;
    private Integer idPreguntaPersistida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregunta_escala_activity);
        toolbar = findViewById(R.id.pregunta_escala_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        modificando = (Boolean) getIntent().getSerializableExtra("modificando");
        idPreguntaPersistida = (Integer) getIntent().getSerializableExtra("idPreguntaPersistida");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaEscalaActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        pregunta = findViewById(R.id.pregunta_escala);
        maxima = findViewById(R.id.maxima_escala);
        if (modificando) {
            pregunta.getEditText().setText((String) getIntent().getSerializableExtra("preguntaEscala"));
            maxima.getEditText().setText(((Integer) getIntent().getSerializableExtra("maximaEscala")).toString());
            idPregunta = (Long) getIntent().getSerializableExtra("idPreguntaModificar");
            tvButton = findViewById(R.id.texto_crear_modificar_escala);
            tvButton.setText("Modificar");
        }
        agregar = findViewById(R.id.agrega_escala_button);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregunta.setError(null);
                maxima.setError(null);
                if (pregunta.getEditText().getText().toString().isEmpty() || maxima.getEditText().getText().toString().isEmpty()){
                    pregunta.setError(pregunta.getEditText().getText().toString().isEmpty() ? "La pregunta no puede estar vacía" : null);
                    maxima.setError(maxima.getEditText().getText().toString().isEmpty() ? "La escala máxima no puede estar vacía" : null);
                } else if (pregunta.getEditText().getText().length() > pregunta.getCounterMaxLength()){
                    pregunta.setError("Se superó la cantidad máxima permitida de caracteres.");
                } else {
                    PreguntaDto dto = new PreguntaDto();
                    dto.setDescripcion(pregunta.getEditText().getText().toString());
                    dto.setTipoPregunta(TipoPreguntaEnum.ESCALA);
                    dto.setMaximaEscala(Integer.valueOf(maxima.getEditText().getText().toString()));
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

        volver = findViewById(R.id.volver_escala_button);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posibleSalida();
            }
        });

        maxima.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    numberPickerDialog();
                }
            }
        });

        maxima.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPickerDialog();
            }
        });

        if (savedInstanceState != null){
            String preguntaEscala = savedInstanceState.getString("preguntaEscala");
            String maximaEscala = savedInstanceState.getString("maximaEscala");
            if (pregunta != null){
                pregunta.getEditText().setText(preguntaEscala);
            }
            if (maximaEscala != null){
                maxima.getEditText().setText(maximaEscala);
            }
        }

    }

    @Override
    public void onBackPressed() {
        posibleSalida();
    }

    private void posibleSalida(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreguntaEscalaActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                PreguntaEscalaActivity.this.finish();
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
        outState.putString("preguntaEscala", pregunta.getEditText().getText().toString());
        outState.putString("maximaEscala", maxima.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void numberPickerDialog(){
        NumberPicker np = new NumberPicker(this);
        np.setMinValue(2);
        np.setMaxValue(10);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(np);
        builder.setTitle("Máximo valor de escala");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                maxima.getEditText().setText(String.valueOf(np.getValue()));
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

}
