package com.encuestando.salmeron.facundo.encuestandofcm;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AdministrarEncuestasActivity extends AppCompatActivity {

    private UsuarioDto usuarioLogueado;
    private Toolbar toolbar;
    private FloatingActionButton agregarEncuestaBoton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrar_encuestas_activity);
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");

        toolbar = findViewById(R.id.titulo_administrar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdministrarEncuestasActivity.this.finish();
            }
        });

        agregarEncuestaBoton = findViewById(R.id.boton_agregar_encuesta);
        agregarEncuestaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent agregar_intent = new Intent(AdministrarEncuestasActivity.this, NuevaEncuestaActivity.class).putExtra("usuario", usuarioLogueado);
                startActivityForResult(agregar_intent, 1);
            }
        });
    }
}
