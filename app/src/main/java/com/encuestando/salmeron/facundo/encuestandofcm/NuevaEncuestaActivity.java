package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class NuevaEncuestaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private UsuarioDto usuarioLogueado;
    private FloatingActionButton multipleChoiceButton;
    private FloatingActionButton unicaButton;
    private FloatingActionButton numericaButton;
    private FloatingActionButton textualButton;
    private FloatingActionMenu actionMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_encuesta_activity);
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
                Toast.makeText(NuevaEncuestaActivity.this, "Multiple Choice.", Toast.LENGTH_LONG).show();
            }
        });
        unicaButton = findViewById(R.id.unica_button);
        unicaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                Toast.makeText(NuevaEncuestaActivity.this, "Respuesta Unica.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(NuevaEncuestaActivity.this, "Respuesta Textual.", Toast.LENGTH_LONG).show();
            }
        });
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
}
