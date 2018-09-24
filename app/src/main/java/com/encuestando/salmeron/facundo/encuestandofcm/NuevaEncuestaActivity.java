package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class
NuevaEncuestaActivity extends AppCompatActivity implements Serializable{

    private Toolbar toolbar;
    private UsuarioDto usuarioLogueado;
    private FloatingActionButton multipleChoiceButton;
    private FloatingActionButton unicaButton;
    private FloatingActionButton numericaButton;
    private FloatingActionButton textualButton;
    private FloatingActionButton escalaButton;
    private FloatingActionMenu actionMenu;
    private ArrayList<PreguntaDto> preguntas;
    private Long contadorPreguntas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_encuesta_activity);
        preguntas = (ArrayList<PreguntaDto>) getIntent().getSerializableExtra("preguntas");
        contadorPreguntas = (Long) getIntent().getSerializableExtra("contadorPreguntas");
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        toolbar = findViewById(R.id.titulo_nueva_encuesta);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevaEncuestaActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        actionMenu = findViewById(R.id.boton_agregar_pregunta);
        multipleChoiceButton = findViewById(R.id.multiple_choice_button);
        multipleChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_multiple_choice = new Intent(NuevaEncuestaActivity.this, PreguntaMultipleChoiceActivity.class);
                startActivityForResult(agregar_multiple_choice, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        unicaButton = findViewById(R.id.unica_button);
        unicaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_unica_opcion = new Intent(NuevaEncuestaActivity.this, PreguntaUnicaOpcionActivity.class);
                startActivityForResult(agregar_unica_opcion, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        numericaButton = findViewById(R.id.numerica_button);
        numericaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_numerica = new Intent(NuevaEncuestaActivity.this, PreguntaNumericaActivity.class);
                startActivityForResult(agregar_numerica, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        textualButton = findViewById(R.id.textual_button);
        textualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_textual = new Intent(NuevaEncuestaActivity.this, PreguntaTextualActivity.class);
                startActivityForResult(agregar_textual, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        escalaButton = findViewById(R.id.escala_button);
        escalaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Intent agregar_escala = new Intent(NuevaEncuestaActivity.this, PreguntaEscalaActivity.class);
                startActivityForResult(agregar_escala, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            PreguntaDto dto = (PreguntaDto) data.getSerializableExtra("pregunta");
            contadorPreguntas += 1;
            dto.setId(contadorPreguntas);
            preguntas.add(dto);
            finish();
            getIntent().putExtra("preguntas", preguntas);
            getIntent().putExtra("contadorPreguntas", contadorPreguntas);
            startActivity(getIntent());
            Toast.makeText(NuevaEncuestaActivity.this, "La pregunta ha sido añadida correctamente!", Toast.LENGTH_LONG).show();
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

    public Long getContadorPreguntas() {
        return contadorPreguntas;
    }

    public void setContadorPreguntas(Long contadorPreguntas) {
        this.contadorPreguntas = contadorPreguntas;
    }
}
