package com.miguel_lm.myapplicationtodo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.miguel_lm.myapplicationtodo.R;
import com.miguel_lm.myapplicationtodo.core.Tarea;
import java.util.List;

public class AdapterTareas extends RecyclerView.Adapter<ViewHolderTarea> {

    private List<Tarea> listaTareas;
    private ListenerTareas listenerTareas;

    public AdapterTareas(List<Tarea> listaTareas, ListenerTareas listenerTareas) {
        this.listaTareas = listaTareas;
        this.listenerTareas = listenerTareas;
    }

    @NonNull
    @Override
    public ViewHolderTarea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new ViewHolderTarea(v, listenerTareas);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTarea holder, int position) {

        holder.mostrarTarea(listaTareas.get(position));
    }

    @Override
    public int getItemCount() {

        return listaTareas.size();
    }
}
