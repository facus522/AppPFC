package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.Serializable;

/**
 * Created by Facundo Salmerón on 14/6/2018.
 */

public class MenuEspecialActivity extends AppCompatActivity implements Serializable{

    private UsuarioDto usuarioLogueado;
    private CardView encuestasDisponibles;
    private CardView informacionNoticias;
    private CardView administrarEncuestas;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_especial_activity);
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        toolbar = findViewById(R.id.titulo_especial);
        toolbar.setTitle("BIENVENIDO " + usuarioLogueado.getNombre().toUpperCase());

        encuestasDisponibles = findViewById(R.id.encuestasDisponiblesEspecial);
        informacionNoticias = findViewById(R.id.informacionNoticiasEspecial);
        administrarEncuestas = findViewById(R.id.administrarEncuestas);

        informacionNoticias.setOnClickListener(view -> {
            Intent info_intent = new Intent(MenuEspecialActivity.this, InfoNoticiasEspecialActivity.class).putExtra("usuario", usuarioLogueado);
            MenuEspecialActivity.this.startActivity(info_intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        encuestasDisponibles.setOnClickListener(view -> {
            Intent admin_intent = new Intent(MenuEspecialActivity.this, ResponderEncuestaEspecialActivity.class).putExtra("usuario", usuarioLogueado);
            MenuEspecialActivity.this.startActivity(admin_intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        administrarEncuestas.setOnClickListener(view -> {
            Intent admin_intent = new Intent(MenuEspecialActivity.this, AdministrarEncuestasActivity.class).putExtra("usuario", usuarioLogueado);
            MenuEspecialActivity.this.startActivity(admin_intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuEspecialActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("¿Desea cerrar su sesión?");
        alertDialog.setNegativeButton("Aceptar", (dialog, which) -> {
            dialog.cancel();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        alertDialog.setPositiveButton("Cancelar", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }
}
