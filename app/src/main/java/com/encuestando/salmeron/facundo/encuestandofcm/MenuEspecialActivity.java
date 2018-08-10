package com.encuestando.salmeron.facundo.encuestandofcm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        toolbar = (Toolbar) findViewById(R.id.titulo_especial);
        toolbar.setTitle("BIENVENIDO " + usuarioLogueado.getNombreUsuario().toUpperCase());


        encuestasDisponibles = (CardView) findViewById(R.id.encuestasDisponiblesEspecial);
        informacionNoticias = (CardView) findViewById(R.id.informacionNoticiasEspecial);
        administrarEncuestas = (CardView) findViewById(R.id.administrarEncuestas);

        informacionNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent info_intent = new Intent(MenuEspecialActivity.this, InfoNoticiasEspecialActivity.class);
                MenuEspecialActivity.this.startActivity(info_intent);
            }
        });
    }
}
