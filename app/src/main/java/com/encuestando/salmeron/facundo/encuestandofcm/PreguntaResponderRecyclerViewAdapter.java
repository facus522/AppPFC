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

import java.util.ArrayList;

/**
 * Created by Facundo Salmerón on 17/10/2018.
 */

public class PreguntaResponderRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<PreguntaDto> preguntas;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    PreguntaResponderRecyclerViewAdapter(Context context, ArrayList<PreguntaDto> data) {
        this.mInflater = LayoutInflater.from(context);
        this.preguntas = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ViewHolder2(mInflater.inflate(R.layout.row_choice_preguntas_respuestas_responder_activity, parent, false));
            case 2:
                return new ViewHolder2(mInflater.inflate(R.layout.row_unica_preguntas_respuestas_responder_activity, parent, false));
            case 3:
                return new ViewHolder(mInflater.inflate(R.layout.row_numerica_preguntas_respuestas_responder_activity, parent, false));
            case 4:
                return new ViewHolder(mInflater.inflate(R.layout.row_textual_preguntas_respuestas_responder_activity, parent, false));
            case 5:
                return new ViewHolder(mInflater.inflate(R.layout.row_escala_preguntas_respuestas_responder_activity, parent, false));
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
                ViewHolder2 viewHolderChoice = (ViewHolder2) holder;
                PreguntaDto preguntaRecyclerChoice = preguntas.get(position);
                viewHolderChoice.textViewPregunta.setText(preguntaRecyclerChoice.getId() + ". " + preguntaRecyclerChoice.getDescripcion());
                viewHolderChoice.linearLayout.removeAllViews();
                for (RespuestaDto rta : preguntaRecyclerChoice.getRespuestas()){
                    CheckBox checkBox = new CheckBox(mInflater.getContext());
                    checkBox.setText(rta.getDescripcion());
                    viewHolderChoice.linearLayout.addView(checkBox);
                }
                break;
            case 2:
                ViewHolder2 viewHolderUnica = (ViewHolder2) holder;
                PreguntaDto preguntaRecyclerUnica = preguntas.get(position);
                viewHolderUnica.textViewPregunta.setText(preguntaRecyclerUnica.getId() + ". " + preguntaRecyclerUnica.getDescripcion());
                viewHolderUnica.linearLayout.removeAllViews();
                RadioGroup radioGroup = new RadioGroup(mInflater.getContext());
                for (RespuestaDto rta : preguntaRecyclerUnica.getRespuestas()){
                    RadioButton radioButton = new RadioButton(mInflater.getContext());
                    radioButton.setText(rta.getDescripcion());
                    radioGroup.addView(radioButton);
                }
                viewHolderUnica.linearLayout.addView(radioGroup);
                break;
            case 3:
                ViewHolder viewHolderNumerica = (ViewHolder) holder;
                PreguntaDto preguntaRecyclerNumerica = preguntas.get(position);
                viewHolderNumerica.textViewPregunta.setText(preguntaRecyclerNumerica.getId() + ". " + preguntaRecyclerNumerica.getDescripcion());
                break;
            case 4:
                ViewHolder viewHolderTextual = (ViewHolder) holder;
                PreguntaDto preguntaRecyclerTextual = preguntas.get(position);
                viewHolderTextual.textViewPregunta.setText(preguntaRecyclerTextual.getId() + ". " + preguntaRecyclerTextual.getDescripcion());
                break;
            case 5:
                ViewHolder viewHolderEscala = (ViewHolder) holder;
                PreguntaDto preguntaRecyclerEscala = preguntas.get(position);
                viewHolderEscala.textViewPregunta.setText(preguntaRecyclerEscala.getId() + ". " + preguntaRecyclerEscala.getDescripcion());
                viewHolderEscala.scaleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            numberPickerDialog(preguntaRecyclerEscala.getMaximaEscala(), viewHolderEscala.scaleEditText);
                        }
                    }
                });

                viewHolderEscala.scaleEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        numberPickerDialog(preguntaRecyclerEscala.getMaximaEscala(), viewHolderEscala.scaleEditText);
                    }
                });

                break;
            default: break;
        }

    }

    private void numberPickerDialog(Integer maxValue, EditText scaleText){
        NumberPicker np = new NumberPicker(mInflater.getContext());
        np.setMinValue(1);
        np.setMaxValue(maxValue);
        np.setValue(scaleText.getText().toString().isEmpty() ? maxValue : Integer.parseInt(scaleText.getText().toString()));

        AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext()).setView(np);
        builder.setTitle("Máximo valor de escala");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scaleText.setText(String.valueOf(np.getValue()));
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return preguntas.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewPregunta;
        EditText editText;
        EditText scaleEditText;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewPregunta = itemView.findViewById(R.id.textView_pregunta_responder_encuesta);
            editText = itemView.findViewById(R.id.editText_responder_encuesta);
            scaleEditText = itemView.findViewById(R.id.scale_editText_responder_encuesta);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewPregunta;
        LinearLayout linearLayout;

        public ViewHolder2(View itemView) {
            super(itemView);
            textViewPregunta = itemView.findViewById(R.id.textView_pregunta_responder_encuesta);
            linearLayout = itemView.findViewById(R.id.linear_layout_respuestas);
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