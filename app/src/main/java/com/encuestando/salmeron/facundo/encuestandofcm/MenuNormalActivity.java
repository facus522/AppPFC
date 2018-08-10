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

        toolbar = (Toolbar) findViewById(R.id.titulo_normal);
        toolbar.setTitle("BIENVENIDO " + usuarioLogueado.getNombreUsuario().toUpperCase());

        encuestasDisponibles = (CardView) findViewById(R.id.encuestasDisponiblesNormal);
        informacionNoticias = (CardView) findViewById(R.id.informacionNoticiasNormal);

        informacionNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent info_intent = new Intent(MenuNormalActivity.this, InfoNoticiasNormalActivity.class);
                MenuNormalActivity.this.startActivity(info_intent);
            }
        });
    }
}
