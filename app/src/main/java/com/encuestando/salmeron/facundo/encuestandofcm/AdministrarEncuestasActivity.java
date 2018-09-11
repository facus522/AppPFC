package com.encuestando.salmeron.facundo.encuestandofcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AdministrarEncuestasActivity extends AppCompatActivity {

    private UsuarioDto usuarioLogueado;
    private Toolbar toolbar;

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
    }
}
