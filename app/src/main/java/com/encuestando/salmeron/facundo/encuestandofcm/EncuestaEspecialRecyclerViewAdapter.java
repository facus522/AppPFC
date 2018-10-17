package com.encuestando.salmeron.facundo.encuestandofcm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Facundo Salmerón on 15/10/2018.
 */

public class EncuestaEspecialRecyclerViewAdapter extends RecyclerView.Adapter<EncuestaEspecialRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ListaEncuestaDto> encuestas;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    EncuestaEspecialRecyclerViewAdapter(Context context, ArrayList<ListaEncuestaDto> data) {
        this.mInflater = LayoutInflater.from(context);
        this.encuestas = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_responder_especial, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = encuestas.get(position).getTitulo();
        String description = encuestas.get(position).getDescripcion();
        String dateCreation = "Creada por '" + encuestas.get(position).getUsuarioCreacion() + "' el: " + encuestas.get(position).getFechaCreacion();
        String solves = "Resuelta " + encuestas.get(position).getResoluciones() + " veces.";
        holder.titulo.setText(title);
        holder.descripcion.setText(description);
        holder.fecha.setText(dateCreation);
        holder.resoluciones.setText(solves);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return encuestas.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titulo;
        TextView descripcion;
        TextView fecha;
        TextView resoluciones;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.row_title_resolver_especial);
            descripcion = itemView.findViewById(R.id.row_descripcion_resolver_especial);
            fecha = itemView.findViewById(R.id.row_fecha_resolver_especial);
            resoluciones = itemView.findViewById(R.id.row_resoluciones_resolver_especial);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    ListaEncuestaDto getItem(int id) {
        return encuestas.get(id);
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