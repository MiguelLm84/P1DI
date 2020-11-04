package com.miguel_lm.myapplicationtodo.ui;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.miguel_lm.myapplicationtodo.R;
import com.miguel_lm.myapplicationtodo.core.Tarea;

public class ViewHolderTarea extends RecyclerView.ViewHolder {

    private TextView textViewTarea_Fecha;
    private TextView textViewTarea_Titulo;
    private TextView textViewTarea_Hora;
    private CardView cardViewTarea;

    private ListenerTareas listenerTareas;

    public ViewHolderTarea(@NonNull View itemView, ListenerTareas listenerTareas) {
        super(itemView);
        this.listenerTareas = listenerTareas;

        textViewTarea_Fecha = itemView.findViewById(R.id.textViewTarea_Fecha);
        textViewTarea_Titulo = itemView.findViewById(R.id.textViewTarea_Titulo);
        textViewTarea_Hora = itemView.findViewById(R.id.textViewTarea_Hora);
        cardViewTarea = itemView.findViewById(R.id.cardViewTarea);
    }

    public void mostrarTarea(final Tarea tarea) {

        textViewTarea_Titulo.setText(tarea.getTitulo());
        textViewTarea_Fecha.setText(tarea.getFechaTexto());
        textViewTarea_Hora.setText(tarea.getHoraTexto());

        final String colorSeleccionado = "#AEEA00";
        final String colorNOSeleccionado = "#03DAC5";
        cardViewTarea.setCardBackgroundColor(Color.parseColor(tarea.isTareaSeleccionada() ? colorSeleccionado : colorNOSeleccionado));

        cardViewTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tarea.setTareaSeleccionada(!tarea.isTareaSeleccionada());
                cardViewTarea.setCardBackgroundColor(Color.parseColor(tarea.isTareaSeleccionada() ? colorSeleccionado : colorNOSeleccionado));

                listenerTareas.seleccionarTarea(tarea);
            }
        });

    }}
