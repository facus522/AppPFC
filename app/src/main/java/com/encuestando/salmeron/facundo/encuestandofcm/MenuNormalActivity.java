package com.encuestando.salmeron.facundo.encuestandofcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Facundo Salmerón on 14/6/2018.
 */

public class MenuNormalActivity extends AppCompatActivity implements Serializable{

    private TextView titulo;
    private UsuarioDto usuarioLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_normal_activity);
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        titulo = (TextView) findViewById(R.id.titulo_normal);
        titulo.setText("BIENVENIDO " + usuarioLogueado.getNombreUsuario().toUpperCase());
    }
}
