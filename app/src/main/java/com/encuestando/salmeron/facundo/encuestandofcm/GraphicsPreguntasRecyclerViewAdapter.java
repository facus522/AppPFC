package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

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
                //ViewHolderChoice viewHolderChoice = (ViewHolder2) holder;
                break;
            case 2:
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
                viewHolderEscala.textViewTitle.setText(preguntaRecyclerEscala.getId() + ". " + preguntaRecyclerEscala.getDescripcion());
                viewHolderEscala.promedio.setText(String.format("%.2f", promedioEscala) + "/" + preguntaRecyclerEscala.getMaximaEscala());
                break;
            default: break;
        }

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

        public ViewHolderChoice(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titulo_grafico_choice);
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