package com.encuestando.salmeron.facundo.encuestandofcm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Facundo Salmerón on 02/11/2018.
 */

public class EncuestaNormalRecyclerViewAdapter extends RecyclerView.Adapter<EncuestaNormalRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ListaEncuestaDto> encuestas;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    EncuestaNormalRecyclerViewAdapter(Context context, ArrayList<ListaEncuestaDto> data) {
        this.mInflater = LayoutInflater.from(context);
        this.encuestas = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_responder_normal, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = encuestas.get(position).getTitulo();
        String description = encuestas.get(position).getDescripcion();
        if (encuestas.get(position).getGeolocalizada()){
            holder.geo.setVisibility(View.VISIBLE);
        } else {
            holder.geo.setVisibility(View.GONE);
        }
        if (encuestas.get(position).getIsEdadRestriccion() != 0 || encuestas.get(position).getIsSexoRestriccion() != 0){
            holder.restriccion.setVisibility(View.VISIBLE);
            String aux = "Encuesta para ";
            String edad = "";
            String sexo = "";
            if (encuestas.get(position).getIsEdadRestriccion() != 0){
                edad = "mayores de " + encuestas.get(position).getIsEdadRestriccion() + " años";
            }
            if (encuestas.get(position).getIsSexoRestriccion() != 0){
                sexo = "personas de sexo " + (encuestas.get(position).getIsSexoRestriccion().equals(1) ? "masculino" : "femenino");
            }

            aux += edad;
            if (!edad.isEmpty() && !sexo.isEmpty()){
                aux+=" y ";
            }
            aux += sexo + ".";
            holder.restriccion.setText(aux);
        } else {
            holder.restriccion.setVisibility(View.GONE);
        }

        holder.titulo.setText(title);
        holder.descripcion.setText(description);
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
        TextView geo;
        TextView restriccion;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.row_title_resolver_normal);
            descripcion = itemView.findViewById(R.id.row_descripcion_resolver_normal);
            geo = itemView.findViewById(R.id.row_geolocalizada_resolver_normal);
            restriccion = itemView.findViewById(R.id.row_restricciones_resolver_normal);
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