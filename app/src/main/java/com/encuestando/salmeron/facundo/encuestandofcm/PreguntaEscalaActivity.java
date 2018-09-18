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

import java.io.Serializable;

public class PreguntaEscalaActivity extends AppCompatActivity implements Serializable{

    private Toolbar toolbar;
    private TextInputLayout pregunta;
    private CardView agregar;
    private CardView volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregunta_escala_activity);
        toolbar = findViewById(R.id.pregunta_escala_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreguntaEscalaActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        pregunta = findViewById(R.id.pregunta_escala);
        agregar = findViewById(R.id.agrega_escala_button);
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
                    dto.setTipoPregunta(TipoPreguntaEnum.ESCALA);
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

        if (savedInstanceState != null){
            String preguntaEscala = savedInstanceState.getString("preguntaEscala");

            if (pregunta!= null){
                pregunta.getEditText().setText(preguntaEscala);
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
        super.onSaveInstanceState(outState);
    }

}
