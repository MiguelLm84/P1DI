package com.miguel_lm.myapplicationtodo.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tarea {
    private String titulo;
    private Date fecha;
    private Date hora;
    private boolean tareaSeleccionada;

    public Tarea(String titulo, Date fecha, Date hora) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.hora = hora;
        tareaSeleccionada = false;
    }

    public String getTitulo() {

        return titulo;
    }

    public Date getFecha() {

        return fecha;
    }

    public String getFechaTexto() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMMM 'de' yyyy ", Locale.getDefault());
        return formatoFecha.format(fecha);
    }

    public String getFechaTextoCorta() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
        return formatoFecha.format(fecha);
    }

    public Date getHora(){
        return hora;
    }

    public String getHoraTexto(){
        SimpleDateFormat formatoFecha = new SimpleDateFormat("HH:mm", Locale.getDefault() );
        return formatoFecha.format(hora);
    }

    public String toStringTarea(){

        return "\n· TITULO: "+getTitulo()+"\n\n· FECHA: "+getFechaTexto()+"\n\n· HORA: "+getHoraTexto();
    }

    public void modificar(String titulo, Date fecha, Date hora) {

        this.titulo = titulo;
        this.fecha = fecha;
        this.hora = hora;
    }

    public boolean isTareaSeleccionada() {
        return tareaSeleccionada;
    }

    public void setTareaSeleccionada(boolean tareaSeleccionada) {
        this.tareaSeleccionada = tareaSeleccionada;
    }
}