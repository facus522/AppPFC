package com.encuestando.salmeron.facundo.encuestandofcm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class PreguntaNuevaRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<PreguntaDto> preguntas;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    PreguntaNuevaRecyclerViewAdapter(Context context, ArrayList<PreguntaDto> data) {
        this.mInflater = LayoutInflater.from(context);
        this.preguntas = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ViewHolder(mInflater.inflate(R.layout.row_preguntas_respuestas_nueva_activity, parent, false));
            case 2:
                return new ViewHolder(mInflater.inflate(R.layout.row_preguntas_respuestas_nueva_activity, parent, false));
            case 3:
                return new ViewHolder(mInflater.inflate(R.layout.row_preguntas_respuestas_nueva_activity, parent, false));
            case 4:
                return new ViewHolder(mInflater.inflate(R.layout.row_respuesta_nueva, parent, false));
            case 5:
                return new ViewHolder(mInflater.inflate(R.layout.row_preguntas_respuestas_nueva_activity, parent, false));
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
                ViewHolder viewHolder = (ViewHolder) holder;
                PreguntaDto preguntaRecycler = preguntas.get(position);
                viewHolder.textViewPregunta.setText(preguntaRecycler.getDescripcion());
                break;
            case 2:
                ViewHolder viewHolder1 = (ViewHolder) holder;
                PreguntaDto preguntaRecycler1 = preguntas.get(position);
                viewHolder1.textViewPregunta.setText(preguntaRecycler1.getDescripcion());
                break;
            case 3:
                ViewHolder viewHolder2 = (ViewHolder) holder;
                PreguntaDto preguntaRecycler2 = preguntas.get(position);
                viewHolder2.textViewPregunta.setText(preguntaRecycler2.getDescripcion());
                break;
            case 4:
                ViewHolder viewHolder3 = (ViewHolder) holder;
                PreguntaDto preguntaRecycler3 = preguntas.get(position);
                viewHolder3.textViewPregunta.setText(preguntaRecycler3.getDescripcion());
                break;
            case 5:
                ViewHolder viewHolder4 = (ViewHolder) holder;
                PreguntaDto preguntaRecycler4 = preguntas.get(position);
                viewHolder4.textViewPregunta.setText(preguntaRecycler4.getDescripcion());
                break;
            default: break;
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return preguntas.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewPregunta;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewPregunta = itemView.findViewById(R.id.respuesta_nueva_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        public ViewHolder2(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.respuesta_nueva_textview);
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