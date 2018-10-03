package com.encuestando.salmeron.facundo.encuestandofcm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class RespuestaNuevaRecyclerViewAdapter extends RecyclerView.Adapter<RespuestaNuevaRecyclerViewAdapter.ViewHolder> {

    private ArrayList<RespuestaDto> respuestas;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int filaRespuestas;

    // data is passed into the constructor
    RespuestaNuevaRecyclerViewAdapter(Context context, ArrayList<RespuestaDto> data, int filaRespuestas) {
        this.mInflater = LayoutInflater.from(context);
        this.respuestas = data;
        this.filaRespuestas = filaRespuestas;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(filaRespuestas, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String respuesta = respuestas.get(position).getDescripcion();
        holder.myTextView.setText(respuesta);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return respuestas.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.respuesta_nueva_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    RespuestaDto getItem(int id) {
        return respuestas.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public int getFilaRespuestas() {
        return filaRespuestas;
    }

    public void setFilaRespuestas(int filaRespuestas) {
        this.filaRespuestas = filaRespuestas;
    }
}