package com.encuestando.salmeron.facundo.encuestandofcm;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Facundo Salmerón on 07/11/2018.
 */

public class GraphicsPreguntasRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<PreguntaDto> preguntas;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    GraphicsPreguntasRecyclerViewAdapter(Context context, ArrayList<PreguntaDto> data) {
        this.mInflater = LayoutInflater.from(context);
        this.preguntas = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ViewHolderChoice(mInflater.inflate(R.layout.row_choice_graphics_activity, parent, false));
            case 2:
                return new ViewHolderUnica(mInflater.inflate(R.layout.row_unica_graphics_activity, parent, false));
            case 3:
                return new ViewHolderNumerica(mInflater.inflate(R.layout.row_numerica_graphics_activity, parent, false));
            case 4:
                return new ViewHolderTextual(mInflater.inflate(R.layout.row_textual_graphics_activity, parent, false));
            case 5:
                return new ViewHolderEscala(mInflater.inflate(R.layout.row_escala_graphics_activity, parent, false));
            default:
                return null;
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        PreguntaDto pregunta = preguntas.get(position);

        switch (holder.getItemViewType()){
            case 1:
                ViewHolderChoice viewHolderChoice = (ViewHolderChoice) holder;
                PreguntaDto preguntaRecyclerChoice = preguntas.get(position);
                viewHolderChoice.textViewTitle.setText(preguntaRecyclerChoice.getId() + ". " + preguntaRecyclerChoice.getDescripcion());
                viewHolderChoice.pieChart.getDescription().setText("Promedio de respuestas");
                List<CargarGraficosDto> datosChoice = obtenerDatosGrafico(preguntaRecyclerChoice.getRespuestas(), preguntaRecyclerChoice.getResultadoDtos());
                List<String> listaChoice = new ArrayList<>();
                ArrayList<PieEntry> entriesChoice = new ArrayList<>();

                for (int i=0; i<datosChoice.size(); i++){
                    entriesChoice.add(new PieEntry(datosChoice.get(i).getPorcentaje(), datosChoice.get(i).getDescripcion()));
                    listaChoice.add(datosChoice.get(i).getDescripcion());
                }

                PieDataSet pieDataSet = new PieDataSet(entriesChoice, "");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData pieData = new PieData(pieDataSet);
                viewHolderChoice.pieChart.setData(pieData);
                viewHolderChoice.pieChart.setDrawSliceText(false);
                viewHolderChoice.pieChart.getLegend().setEnabled(false);

                viewHolderChoice.pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        if (e == null) {
                            return;
                        } else {
                            IMarker marker = new CustomMarkerView(mInflater.getContext(), R.layout.marker);
                            viewHolderChoice.pieChart.setMarker(marker);
                        }

                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

                viewHolderChoice.pieChart.animateY(2000);

                break;
            case 2:
                ViewHolderUnica viewHolderUnica = (ViewHolderUnica) holder;
                PreguntaDto preguntaRecyclerUnica = preguntas.get(position);
                viewHolderUnica.textViewTitle.setText(preguntaRecyclerUnica.getId() + ". " + preguntaRecyclerUnica.getDescripcion());
                viewHolderUnica.barChart.getDescription().setEnabled(false);
                viewHolderUnica.xAxis = viewHolderUnica.barChart.getXAxis();

                List<CargarGraficosDto> datos = obtenerDatosGrafico(preguntaRecyclerUnica.getRespuestas(), preguntaRecyclerUnica.getResultadoDtos());

                List<String> lista = new ArrayList<>();

                ArrayList<BarEntry> entries = new ArrayList<>();

                for (int i=0; i<datos.size(); i++){
                    entries.add(new BarEntry(i, datos.get(i).getPorcentaje(), datos.get(i).getDescripcion()));
                    lista.add(datos.get(i).getDescripcion());
                }

                viewHolderUnica.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                viewHolderUnica.xAxis.setLabelCount(entries.size());
                //viewHolderUnica.xAxis.setValueFormatter(new IndexAxisValueFormatter(lista));
                viewHolderUnica.barChart.getLegend().setEnabled(false);

                BarDataSet dataSet = new BarDataSet(entries, "");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                BarData data = new BarData(dataSet);

                viewHolderUnica.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        if (e == null) {
                            return;
                        } else {
                            IMarker marker = new CustomMarkerView(mInflater.getContext(), R.layout.marker);
                            viewHolderUnica.barChart.setMarker(marker);
                        }

                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

                viewHolderUnica.barChart.setData(data);
                viewHolderUnica.barChart.animateY(2000);

                YAxis ejeY = viewHolderUnica.barChart.getAxisLeft();
                ejeY.setAxisMinimum(0);
                ejeY.setAxisMaximum(100);
                break;
            case 3:
                ViewHolderNumerica viewHolderNumerica = (ViewHolderNumerica) holder;
                PreguntaDto preguntaRecyclerNumerica = preguntas.get(position);
                Float promedio = calcularPromedios(preguntaRecyclerNumerica.getResultadoDtos());
                viewHolderNumerica.textViewTitle.setText(preguntaRecyclerNumerica.getId() + ". " + preguntaRecyclerNumerica.getDescripcion());
                viewHolderNumerica.promedio.setText(String.format("%.2f", promedio));
                break;
            case 4:
                ViewHolderTextual viewHolderTextual = (ViewHolderTextual) holder;
                PreguntaDto preguntaRecyclerTextual = preguntas.get(position);
                viewHolderTextual.textViewTitle.setText(preguntaRecyclerTextual.getId() + ". " + preguntaRecyclerTextual.getDescripcion());
                break;
            case 5:
                ViewHolderEscala viewHolderEscala = (ViewHolderEscala) holder;
                PreguntaDto preguntaRecyclerEscala = preguntas.get(position);
                Float promedioEscala = calcularPromedios(preguntaRecyclerEscala.getResultadoDtos());
                if (promedioEscala < (preguntas.get(position).getMaximaEscala() / 2.0f)) {
                    viewHolderEscala.promedio.setBackground(viewHolderEscala.itemView.getResources().getDrawable(R.drawable.escala_baja_circle));
                }
                viewHolderEscala.textViewTitle.setText(preguntaRecyclerEscala.getId() + ". " + preguntaRecyclerEscala.getDescripcion());
                viewHolderEscala.promedio.setText(String.format("%.2f", promedioEscala) + "/" + preguntaRecyclerEscala.getMaximaEscala());
                break;
            default: break;
        }

    }

    private List<CargarGraficosDto> obtenerDatosGrafico(ArrayList<RespuestaDto> respuestas, ArrayList<ResultadoDto> resultados){
        List<CargarGraficosDto> cargas = new ArrayList<>();
        for (RespuestaDto rta : respuestas) {
            CargarGraficosDto carga = new CargarGraficosDto();
            Integer totalRespuestas = 0;
            for (ResultadoDto rdo : resultados) {
                if (rta.getIdPersistido().equals(rdo.getIdRespuesta())){
                    totalRespuestas++;
                }
            }
            carga.setIdRespuesta(rta.getIdPersistido());
            carga.setDescripcion(rta.getDescripcion());
            carga.setPorcentaje(totalRespuestas * 100.00f / resultados.size());
            cargas.add(carga);
        }
        return cargas;
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    private Float calcularPromedios(ArrayList<ResultadoDto> resultados){
        Float result = 0f;
        for (ResultadoDto dto : resultados){
            result += Float.parseFloat(dto.getDescripcion());
        }
        result /= resultados.size();
        return result;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolderChoice extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private PieChart pieChart;

        public ViewHolderChoice(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titulo_grafico_choice);
            pieChart = itemView.findViewById(R.id.piechart_unica);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public class ViewHolderUnica extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private BarChart barChart;
        private XAxis xAxis;

        public ViewHolderUnica(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titulo_grafico_unica);
            barChart = itemView.findViewById(R.id.barchart_unica);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public class ViewHolderNumerica extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private TextView promedio;

        public ViewHolderNumerica(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titulo_grafico_numerica);
            promedio = itemView.findViewById(R.id.promedio_numerica);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public class ViewHolderTextual extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;

        public ViewHolderTextual(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titulo_grafico_textual);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public class ViewHolderEscala extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewTitle;
        private TextView promedio;

        public ViewHolderEscala(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titulo_grafico_escala);
            promedio = itemView.findViewById(R.id.promedio_escala);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    @Override
    public int getItemViewType(int position) {
        PreguntaDto pregunta = getItem(position);
        return pregunta.getTipoPregunta().getCodigo();
    }

    // convenience method for getting data at click position
    PreguntaDto getItem(int id) {
        return preguntas.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}