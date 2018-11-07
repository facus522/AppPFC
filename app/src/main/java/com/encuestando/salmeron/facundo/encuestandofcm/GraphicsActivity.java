package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphicsActivity extends AppCompatActivity {

    private BarChart barChart;
    private XAxis xAxis;
    private Toolbar toolbar;
    private ImageButton ayuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphics_activity);

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
                alertDialog.setMessage("En esta sección podrá encontrar estadísticas acerca de los resultados de la encuesta.\nSe proporcionan gráficos para todas las preguntas, excepto las de tipo 'Textual' ya que éstas son de libre desarrollo.\nPuede ampliar o reducir los graficos presionando sobre los mismos.");
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



        barChart = findViewById(R.id.graphics_barchart);
        barChart.getDescription().setEnabled(false);
        xAxis = barChart.getXAxis();
        List<String> lista = new ArrayList<>();

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 40, "Si"));
        lista.add("Si");
        entries.add(new BarEntry(1, 60, "No"));
        lista.add("No");

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(2);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(lista));

        BarDataSet dataSet = new BarDataSet(entries, "Respuestas");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.animateY(3000);

        YAxis ejeY = barChart.getAxisLeft();
        ejeY.setAxisMinimum(0);
        ejeY.setAxisMaximum(100);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
