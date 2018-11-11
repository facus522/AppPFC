package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class GraphicsActivity extends AppCompatActivity implements GraphicsPreguntasRecyclerViewAdapter.ItemClickListener {

    private Toolbar toolbar;
    private ImageButton ayuda;
    private TextView titulo;
    private TextView descripcion;
    private String tituloEncuesta;
    private String descripcionEncuesta;
    private UsuarioDto usuarioLogueado;
    private RecyclerView recyclerView;
    private GraphicsPreguntasRecyclerViewAdapter adapter;
    private ArrayList<PreguntaDto> preguntas;
    private ArrayList<ResultadoDto> resultados;
    private Float edadPromedio;
    private Float masculinoPromedio;
    private TextView edad;
    private TextView sexo;
    private CardView volver;
    private Boolean isGeolocalizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphics_activity);

        tituloEncuesta = (String) getIntent().getSerializableExtra("tituloEncuesta");
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        descripcionEncuesta = (String) getIntent().getSerializableExtra("descripcionEncuesta");
        preguntas = (ArrayList<PreguntaDto>) getIntent().getSerializableExtra("preguntas");
        resultados = (ArrayList<ResultadoDto>) getIntent().getSerializableExtra("resultados");
        isGeolocalizada = (Boolean) getIntent().getSerializableExtra("isGeolocalizada");

        divideResultadosPreguntas();

        toolbar = findViewById(R.id.title_graphics);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphicsActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ayuda = findViewById(R.id.ayuda_menu);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(GraphicsActivity.this, AlertDialog.THEME_HOLO_DARK);
                alertDialog.setTitle("Información");
                alertDialog.setMessage("En esta sección encontrará estadísticas relacionadas a los resultados de la encuesta, así como tambien el promedio de edad y sexo de los encuestados.\nPara preguntas tipo 'Múltiple Choice' se presenta un gráfico de torta con los promedios.\nPara preguntas de tipo 'Respuesta Única' se presenta un gráfico de barras con los promedios.\nAquellas preguntas de tipo 'Textual' no contarán con estadísticas.\nLas preguntas 'Numéricas' y de tipo 'Escala' presentan un número promedio de los resultados.\nSi la encuesta es Geolocalizada al presionar la misma se mostrará un mapa con las respuestas.");
                alertDialog.setIcon(R.drawable.ic_action_error);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        titulo = findViewById(R.id.titulo_encuesta_graphics);
        titulo.setText(tituloEncuesta);
        descripcion = findViewById(R.id.descripcion_encuesta_graphics);
        descripcion.setText(descripcionEncuesta);
        edad = findViewById(R.id.edad_promedio_encuesta_graphics);
        sexo = findViewById(R.id.sexo_promedio_encuesta_graphics);
        volver = findViewById(R.id.volver_graphics);
        volver.setOnClickListener((View view) -> {
                    GraphicsActivity.this.finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
        );

        promedioEdad();

        edad.setText("Edad promedio: " + String.format("%.2f", edadPromedio));
        sexo.setText("Masculino: " + String.format("%.2f", masculinoPromedio) + "%, Femenino: " + String.format("%.2f", 100.00 - masculinoPromedio) + "%");

        recyclerView = findViewById(R.id.recycler_graphics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GraphicsPreguntasRecyclerViewAdapter(this, preguntas);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onItemClick(View view, int position) {
        if (isGeolocalizada){
            Intent maps_intent = new Intent(GraphicsActivity.this, ResultMapsActivity.class);
            maps_intent.putExtra("resultados", preguntas.get(position).getResultadoDtos());
            maps_intent.putExtra("respuestas", preguntas.get(position).getRespuestas());
            startActivityForResult(maps_intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void promedioEdad() {
        Float edades = 0.0f;
        Float mascu = 0.0f;
        for (ResultadoDto dto : resultados) {
            edades += dto.getEdad();
            if (dto.getSexo().equals(SexoEnum.MASCULINO.getCodigo())) {
                mascu++;
            }
        }
        edades /= resultados.size();
        mascu = mascu * 100 / resultados.size();

        edadPromedio = edades;
        masculinoPromedio = mascu;
    }


    private void divideResultadosPreguntas() {
        for (PreguntaDto dto : preguntas) {
            for (RespuestaDto rta : dto.getRespuestas()) {
                for (ResultadoDto rtado : resultados) {
                    if (rta.getIdPersistido().equals(rtado.getIdRespuesta())) {
                        dto.getResultadoDtos().add(rtado);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
