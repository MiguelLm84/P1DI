package com.miguel_lm.myapplicationtodo.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miguel_lm.myapplicationtodo.R;
import com.miguel_lm.myapplicationtodo.core.Tarea;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ListenerTareas {

    private List<Tarea> listaTareas;
    private AdapterTareas adapterTareas;
    private LinearLayout toolBar;
    private Tarea tareaAmodificar;
    List<Tarea> listaTareasSeleccionadas;
    ArrayList<Tarea> listaTareasFinalizadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();

        toolBar = findViewById(R.id.toolbar);

        listaTareas = new ArrayList<>();
        listaTareas.add(new Tarea("Ir al cine", new Date(), new Date()));
        listaTareas.add(new Tarea("Hacer deporte", new Date(), new Date()));
        listaTareas.add(new Tarea("Ir al taller", new Date(), new Date()));

        RecyclerView recyclerViewTareas = findViewById(R.id.recyclerViewTareas);
        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(this));
        adapterTareas = new AdapterTareas(listaTareas, this);
        recyclerViewTareas.setAdapter(adapterTareas);

        toolBar.setVisibility(View.GONE);
        listaTareasSeleccionadas = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Date fechaActual = new Date();
        listaTareasFinalizadas = new ArrayList<>();

        for (Tarea tarea : listaTareas) {

            if (tarea.getFecha().compareTo(fechaActual) < 0) {
                listaTareasFinalizadas.add(tarea);
            }

        }

        if (!listaTareasFinalizadas.isEmpty()) {
            mostrarTareasCaducadas(listaTareasFinalizadas);
        }

    }

    @Override
    public void seleccionarTarea(Tarea tarea) {

        if (tarea.isTareaSeleccionada()) {
            listaTareasSeleccionadas.add(tarea);
        } else {
            listaTareasSeleccionadas.remove(tarea);
        }
        toolBar.setVisibility(listaTareasSeleccionadas.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.accionCrearTarea) {
            crearTarea();
        } else if (item.getItemId() == R.id.accionModificar) {
            accionEscogerYModificar();
        } else if (item.getItemId() == R.id.accionEliminar) {
            accionEscogerYEliminar();
        }
        return super.onOptionsItemSelected(item);
    }

    private void crearTarea() {
        crearOModificarTarea(null);
    }

    private void crearOModificarTarea(final Tarea tareaAmodificar) {

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        final View dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_crear_tarea, null);
        build.setView(dialogLayout);
        final AlertDialog dialog = build.create();

        final EditText edTxtTarea = dialogLayout.findViewById(R.id.edTxt_tarea);
        final TextView tvFecha = dialogLayout.findViewById(R.id.tv_fecha);
        final TextView tvHora = dialogLayout.findViewById(R.id.tv_hora);
        final Button buttonAceptar = dialogLayout.findViewById(R.id.btn_aceptar);
        final Button buttonCancelar = dialogLayout.findViewById(R.id.btn_cancelar);

        final Calendar calendar = Calendar.getInstance();

        if (tareaAmodificar != null) {
            edTxtTarea.setText(tareaAmodificar.getTitulo());
            tvFecha.setText((CharSequence) tareaAmodificar.getFechaTexto());
            tvHora.setText((CharSequence) tareaAmodificar.getHoraTexto());
        }

        tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tareaAmodificar != null) {
                    calendar.setTime(tareaAmodificar.getFecha());
                }

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                final DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMMM 'de' yyyy", Locale.getDefault());
                        tvFecha.setText(formatoFecha.format(calendar.getTime()));
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        tvHora.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tareaAmodificar != null) {
                    calendar.setTime(tareaAmodificar.getHora());
                }

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                final TimePickerDialog tpd = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hora, int minutos) {

                        calendar.set(Calendar.HOUR_OF_DAY, hora);
                        calendar.set(Calendar.MINUTE, minutos);

                        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        tvHora.setText(formatoHora.format(calendar.getTime()));
                    }
                }, hour, minutes, true);
                tpd.show();
            }
        });

        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titulo = edTxtTarea.getText().toString();

                if(edTxtTarea.getText().toString().length()<=0){
                    Toast.makeText(MainActivity.this, "Debe escoger una fecha", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tareaAmodificar == null) {
                    Tarea nuevaTarea = new Tarea(titulo, calendar.getTime(), calendar.getTime());
                    listaTareas.add(nuevaTarea);
                    Toast.makeText(MainActivity.this, "Evento añadido.", Toast.LENGTH_SHORT).show();
                }
                else {
                    tareaAmodificar.modificar(titulo, calendar.getTime(), calendar.getTime());
                    Toast.makeText(MainActivity.this, "Evento modificado.", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                adapterTareas.notifyDataSetChanged();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void accionEscogerYModificar() {

        AlertDialog.Builder builderDialogEscogerTareas = new AlertDialog.Builder(MainActivity.this);
        builderDialogEscogerTareas.setIcon(R.drawable.pencil);
        builderDialogEscogerTareas.setTitle("Modificar Tarea");

        final String[] arrayTareasAMostrar = new String[listaTareas.size()];
        for (int i = 0; i < listaTareas.size(); i++) {
            arrayTareasAMostrar[i] = "\n· TAREA: " + listaTareas.get(i).getTitulo() + "\n· FECHA:  " + listaTareas.get(i).getFechaTextoCorta() + "\n· HORA:  " + listaTareas.get(i).getHoraTexto();
        }
        builderDialogEscogerTareas.setSingleChoiceItems(arrayTareasAMostrar, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int posicionElementoSeleccionado) {
                tareaAmodificar = listaTareas.get(posicionElementoSeleccionado);
            }
        });
        builderDialogEscogerTareas.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int i) {

                if (tareaAmodificar == null) {
                    Toast.makeText(MainActivity.this, "Debe escoger una tarea", Toast.LENGTH_SHORT).show();
                }
                else {
                    crearOModificarTarea(tareaAmodificar);
                }
            }
        });
        builderDialogEscogerTareas.setNegativeButton("Cancelar", null);
        builderDialogEscogerTareas.create().show();
    }

    private void accionEscogerYEliminar() {

        AlertDialog.Builder builderEliminar = new AlertDialog.Builder(MainActivity.this);
        builderEliminar.setIcon(R.drawable.remove_symbol);
        builderEliminar.setTitle("Eliminar elementos");

        final ArrayList<String> listaTareasAeliminar = new ArrayList<>();
        String[] arrayTareas = new String[listaTareas.size()];
        final boolean[] tareasSeleccionadas = new boolean[listaTareas.size()];
        for (int i = 0; i < listaTareas.size(); i++){
            arrayTareas[i] = "\n· TAREA: " + listaTareas.get(i).getTitulo() + "\n· FECHA:  " + listaTareas.get(i).getFechaTextoCorta() + "\n· HORA:  " + listaTareas.get(i).getHoraTexto();
        }
        builderEliminar.setMultiChoiceItems(arrayTareas, tareasSeleccionadas, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indiceSeleccionado, boolean isChecked) {
                tareasSeleccionadas[indiceSeleccionado] = isChecked;
                String tareasParaEliminar = "\n· TAREA: " + listaTareas.get(indiceSeleccionado).getTitulo() + "\n· FECHA:  " + listaTareas.get(indiceSeleccionado).getFechaTextoCorta() + "\n· HORA:  " + listaTareas.get(indiceSeleccionado).getHoraTexto() + "\n";
                listaTareasAeliminar.add(tareasParaEliminar);
            }
        });

        builderEliminar.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                AlertDialog.Builder builderEliminar_Confirmar = new AlertDialog.Builder(MainActivity.this);
                builderEliminar_Confirmar.setIcon(R.drawable.exclamation);
                builderEliminar_Confirmar.setTitle("¿Eliminar los elementos?");
                String tareasPorBorra = null;
                for(int i=0;i<listaTareasAeliminar.size();i++){
                    tareasPorBorra = listaTareasAeliminar.get(i);
                }
                builderEliminar_Confirmar.setMessage(tareasPorBorra);
                builderEliminar_Confirmar.setNegativeButton("Cancelar", null);
                builderEliminar_Confirmar.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        for (int i = listaTareas.size() - 1; i >= 0; i--) {
                            if (tareasSeleccionadas[i]) {
                                listaTareas.remove(i);
                            }
                        }
                        Toast.makeText(MainActivity.this, "Tareas eliminadas correctamente", Toast.LENGTH_SHORT).show();
                        MainActivity.this.adapterTareas.notifyDataSetChanged();
                    }
                });
                builderEliminar_Confirmar.create().show();
                dialog.dismiss();
            }
        });

        builderEliminar.setNegativeButton("Cancelar",null);
        builderEliminar.create().show();
    }

    public void onClickFABNuevaTarea(View view) {
        crearTarea();
    }

    public void onClickToolbarEliminar(View view) {

        AlertDialog.Builder builderEliminar = new AlertDialog.Builder(MainActivity.this);
        builderEliminar.setIcon(R.drawable.remove_symbol);
        builderEliminar.setTitle("Eliminar elementos");
        String msj = null;
        final ArrayList<String> tareasParaEliminar = new ArrayList<>();
        String[] arrayTareas = new String[listaTareas.size()];
        for (int i = 0; i < listaTareas.size(); i++){
            arrayTareas[i] = "\n· TAREA: " + listaTareas.get(i).getTitulo() + "\n· FECHA:  " + listaTareas.get(i).getFechaTextoCorta() + "\n· HORA:  " + listaTareas.get(i).getHoraTexto();
            tareasParaEliminar.add(arrayTareas[i]);
            for(int j = 0; j<tareasParaEliminar.size(); j++ );
                 msj =tareasParaEliminar.get(i);
        }
        builderEliminar.setMessage(msj);
        builderEliminar.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                AlertDialog.Builder builderEliminar_Confirmar = new AlertDialog.Builder(MainActivity.this);
                builderEliminar_Confirmar.setIcon(R.drawable.exclamation);
                builderEliminar_Confirmar.setTitle("¿Eliminar los elementos?");
                String tareasPorBorra = null;
                for(int i=0;i<tareasParaEliminar.size();i++){
                    tareasPorBorra = tareasParaEliminar.get(i);
                }
                builderEliminar_Confirmar.setMessage(tareasPorBorra);
                builderEliminar_Confirmar.setNegativeButton("Cancelar", null);
                builderEliminar_Confirmar.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        for (int i = tareasParaEliminar.size() - 1; i >= 0; i--) {
                            tareasParaEliminar.remove(i);
                            }
                        Toast.makeText(MainActivity.this, "Tareas eliminadas correctamente", Toast.LENGTH_SHORT).show();
                        MainActivity.this.adapterTareas.notifyDataSetChanged();
                    }
                });
                builderEliminar_Confirmar.create().show();
            }
        });

        builderEliminar.setNegativeButton("Cancelar",null);
        builderEliminar.create().show();
    }

    public void onClickToolbarModificar(View view) {

        AlertDialog.Builder builderDialogEscogerTareas = new AlertDialog.Builder(MainActivity.this);
        builderDialogEscogerTareas.setIcon(R.drawable.pencil);
        builderDialogEscogerTareas.setTitle("Modificar Tarea");

        String tareasParaModificar = null;
        final String[] arrayTareasAMostrar = new String[listaTareas.size()];
        for (int i = 0; i < listaTareas.size(); i++) {
            arrayTareasAMostrar[i] = "\n· TAREA: " + listaTareas.get(i).getTitulo() + "\n· FECHA:  " + listaTareas.get(i).getFechaTextoCorta() + "\n· HORA:  " + listaTareas.get(i).getHoraTexto();
            tareasParaModificar = arrayTareasAMostrar[i];
        }
        builderDialogEscogerTareas.setMessage(tareasParaModificar);
        builderDialogEscogerTareas.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int i) {

                crearOModificarTarea(tareaAmodificar);
            }
        });
        builderDialogEscogerTareas.setNegativeButton("Cancelar", null);
        builderDialogEscogerTareas.create().show();
    }

    public void onClickToolbarSalir(View view) {

        for (Tarea tarea : listaTareasSeleccionadas)
            tarea.setTareaSeleccionada(false);

        listaTareasSeleccionadas.clear();
        toolBar.setVisibility(View.GONE);
        adapterTareas.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "Salir sin seleccionar", Toast.LENGTH_SHORT).show();
    }

    private void mostrarTareasCaducadas(final List<Tarea> listaTareasFinalizadas) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.remove_symbol);
        builder.setTitle("Tareas Finalizadas");

        String listaTareasParaBorrar = null;
        String[] arrayTareas = new String[listaTareasFinalizadas.size()];
        final boolean[] tareasSeleccionadas = new boolean[listaTareasFinalizadas.size()];
        for (int i = 0; i < listaTareasFinalizadas.size(); i++) {
            arrayTareas[i] = "\n· TAREA: " + listaTareasFinalizadas.get(i).getTitulo() + "\n· FECHA:  " + listaTareasFinalizadas.get(i).getFechaTextoCorta() + "\n· HORA:  " + listaTareasFinalizadas.get(i).getHoraTexto();
            listaTareasParaBorrar =  arrayTareas[i];
        }
        builder.setMultiChoiceItems(arrayTareas, tareasSeleccionadas, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                tareasSeleccionadas[i] = isChecked;
            }
        });

        final String finalListaTareasParaBorrar = listaTareasParaBorrar;
        builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.remove_symbol);
                builder.setTitle("¿Eliminar el elemento?");
                builder.setMessage(finalListaTareasParaBorrar);

                AlertDialog.Builder builderEliminar_Confirmar = new AlertDialog.Builder(MainActivity.this);
                builderEliminar_Confirmar.setIcon(R.drawable.exclamation);
                builderEliminar_Confirmar.setMessage("¿Eliminar los elementos?");
                builderEliminar_Confirmar.setNegativeButton("Cancelar", null);
                builderEliminar_Confirmar.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        for (int i = listaTareasFinalizadas.size() - 1; i >= 0; i--) {
                            if (tareasSeleccionadas[i]) {
                                listaTareas.remove(listaTareasFinalizadas.get(i));
                            }
                        }
                        Toast.makeText(MainActivity.this, "Tareas eliminadas correctamente", Toast.LENGTH_SHORT).show();
                        MainActivity.this.adapterTareas.notifyDataSetChanged();
                    }
                });
                builderEliminar_Confirmar.create().show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();

    }
}





