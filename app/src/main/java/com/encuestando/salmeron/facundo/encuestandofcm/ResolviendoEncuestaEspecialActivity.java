package com.encuestando.salmeron.facundo.encuestandofcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ResolviendoEncuestaEspecialActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String tituloEncuesta;
    private String descripcionEncuesta;
    private TextView titulo;
    private TextView descripcion;
    private TextView cargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolviendo_encuesta_especial_activity);
        toolbar = findViewById(R.id.title_resolviendo_especial);
        tituloEncuesta = (String) getIntent().getSerializableExtra("tituloEncuesta");
        descripcionEncuesta = (String) getIntent().getSerializableExtra("descripcionEncuesta");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResolviendoEncuestaEspecialActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        titulo = findViewById(R.id.titulo_encuesta_resolver);
        descripcion = findViewById(R.id.descripcion_encuesta_resolver);
        cargando = findViewById(R.id.cargandoResponderEspecial);
        cargando.setVisibility(View.GONE);

        titulo.setText(tituloEncuesta);
        descripcion.setText(descripcionEncuesta);
    }
}
