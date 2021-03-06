package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Facundo Salmerón on 14/6/2018.
 */

public class MenuNormalActivity extends AppCompatActivity implements Serializable{

    private UsuarioDto usuarioLogueado;
    private CardView encuestasDisponibles;
    private CardView informacionNoticias;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_normal_activity);

        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");

        toolbar = findViewById(R.id.titulo_normal);
        toolbar.setTitle("BIENVENIDO " + usuarioLogueado.getNombre().toUpperCase());

        encuestasDisponibles = findViewById(R.id.encuestasDisponiblesNormal);
        informacionNoticias = findViewById(R.id.informacionNoticiasNormal);

        informacionNoticias.setOnClickListener(view -> {
            Intent info_intent = new Intent(MenuNormalActivity.this, InfoNoticiasNormalActivity.class);
            MenuNormalActivity.this.startActivity(info_intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        encuestasDisponibles.setOnClickListener(view -> {
            Intent admin_intent = new Intent(MenuNormalActivity.this, ResponderEncuestaNormalActivity.class).putExtra("usuario", usuarioLogueado);
            MenuNormalActivity.this.startActivity(admin_intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuNormalActivity.this);
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
