package com.cmc.pulpov1.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Fecha;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CrearFechasActivity extends AppCompatActivity {
    private EditText etFecha;
    private EditText etDia1;
    private EditText etDia2;
    private EditText etDia3;
    private EditText etCancha1;
    private EditText etCancha2;
    private Button btnGuardar;
    private Button btnPartidos;
    private ConstraintLayout constraintLayout;
    private Fecha fecha;
    private Date fechaSeleccionada;
    private List<Fecha> fechas;
    private List<String> numFecha;
    private ArrayAdapter<String> adpFecha;
    private Spinner spNumFecha;
    private SimpleDateFormat sdfCompleto = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    private int anio;
    private int mes;
    private int dia;
    private FirebaseDatabase database;
    private DatabaseReference refFechas;
    private String numeroFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_fechas);
        atarComponentes();
        cargarSpinnerNumFecha();
        fecha = new Fecha();
        desplazarPantalla();
        database = FirebaseDatabase.getInstance();

        etDia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                anio = mcurrentDate.get(Calendar.YEAR);
                mes = mcurrentDate.get(Calendar.MONTH);
                dia = mcurrentDate.get(Calendar.DAY_OF_WEEK);
                final DatePickerDialog mDatePicker1 = new DatePickerDialog
                        (CrearFechasActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                int month_k = selectedmonth + 1;
                                //armar la fecha con los valores de anio mes y dia que selecciona
                                final Calendar c = Calendar.getInstance();
                                c.set(selectedyear, selectedmonth, selectedday);
                                fechaSeleccionada = c.getTime();
                                fecha.setDia1(fechaSeleccionada);
                                etDia1.setText(sdfCompleto.format(fechaSeleccionada));
                                //fechaMod = sdfCompleto.format(fechaProgramacion);

                                //   Log.d("PULPOLOG", "Valor de la fecha para el ingreso en el nodo" + fechaMod);
                            }
                        }, anio, mes, dia);
                mDatePicker1.setTitle("Seleccione una fecha");
                mDatePicker1.getDatePicker();
                mDatePicker1.show();

            }
        });

        etDia2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                anio = mcurrentDate.get(Calendar.YEAR);
                mes = mcurrentDate.get(Calendar.MONTH);
                dia = mcurrentDate.get(Calendar.DAY_OF_WEEK);
                final DatePickerDialog mDatePicker2 = new DatePickerDialog
                        (CrearFechasActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                int month_k = selectedmonth + 1;
                                //armar la fecha con los valores de anio mes y dia que selecciona
                                final Calendar c = Calendar.getInstance();
                                c.set(selectedyear, selectedmonth, selectedday);
                                fechaSeleccionada = c.getTime();
                                fecha.setDia2(fechaSeleccionada);
                                etDia2.setText(sdfCompleto.format(fechaSeleccionada));
                                //fechaMod = sdfCompleto.format(fechaProgramacion);

                                //   Log.d("PULPOLOG", "Valor de la fecha para el ingreso en el nodo" + fechaMod);
                            }
                        }, anio, mes, dia);
                mDatePicker2.setTitle("Seleccione una fecha");
                mDatePicker2.getDatePicker();
                mDatePicker2.show();

            }
        });

        etDia3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                anio = mcurrentDate.get(Calendar.YEAR);
                mes = mcurrentDate.get(Calendar.MONTH);
                dia = mcurrentDate.get(Calendar.DAY_OF_WEEK);
                final DatePickerDialog mDatePicker3 = new DatePickerDialog
                        (CrearFechasActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                int month_k = selectedmonth + 1;
                                //armar la fecha con los valores de anio mes y dia que selecciona
                                final Calendar c = Calendar.getInstance();
                                c.set(selectedyear, selectedmonth, selectedday);
                                fechaSeleccionada = c.getTime();
                                fecha.setDia3(fechaSeleccionada);
                                etDia3.setText(sdfCompleto.format(fechaSeleccionada));
                                //fechaMod = sdfCompleto.format(fechaProgramacion);

                                //   Log.d("PULPOLOG", "Valor de la fecha para el ingreso en el nodo" + fechaMod);
                            }
                        }, anio, mes, dia);
                mDatePicker3.setTitle("Seleccione una fecha");
                mDatePicker3.getDatePicker();
                mDatePicker3.show();

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarFecha();
            }
        });
        btnPartidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navIrListaPartidos();

            }
        });
    }

    public void atarComponentes() {
        constraintLayout = findViewById(R.id.rootview);
        spNumFecha = findViewById(R.id.spNumFecha);
        etFecha = findViewById(R.id.etFecha);
        etDia1 = findViewById(R.id.etDia1);
        etDia2 = findViewById(R.id.etDia2);
        etDia3 = findViewById(R.id.etDia3);
        etCancha1 = findViewById(R.id.etCancha1);
        etCancha2 = findViewById(R.id.etCancha2);
        btnGuardar = findViewById(R.id.btnGuardarFecha);
        btnPartidos=findViewById(R.id.btnPartidos);

    }

    //Mueve la pantalla hacia arriba cuando se abre el teclado, regresa a la posición original cuando se cierra el teclado
    private void desplazarPantalla() {
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    //Toast.makeText(MainActivity.this,"Keyboard is showing",Toast.LENGTH_LONG).show();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Subir la línea de guía
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.001f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                } else {
                    //Toast.makeText(MainActivity.this,"keyboard closed",Toast.LENGTH_LONG).show();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Regresar la línea de guía a la posición original
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.2f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        });
    }

    public void cargarSpinnerNumFecha() {
        numFecha = new ArrayList<String>();
        numFecha.add("1");
        numFecha.add("2");
        numFecha.add("3");
        numFecha.add("4");
        numFecha.add("5");
        numFecha.add("6");
        numFecha.add("7");
        numFecha.add("8");
        numFecha.add("9");
        numFecha.add("10");
        numFecha.add("11");
        numFecha.add("12");
        numFecha.add("13");
        numFecha.add("14");
        numFecha.add("15");
        numFecha.add("16");
        numFecha.add("17");
        numFecha.add("18");
        numFecha.add("19");
        numFecha.add("20");
        numFecha.add("21");
        numFecha.add("22");
        numFecha.add("23");
        numFecha.add("24");


        adpFecha = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, numFecha);
        spNumFecha.setAdapter(adpFecha);
    }


    public void guardarFecha() {
        fechas = new ArrayList<Fecha>();
        etCancha1.setText(etCancha1.getText());
        etCancha2.setText(etCancha2.getText());
        fecha.setCancha1(etCancha1.getText().toString());
        fecha.setCancha2(etCancha2.getText().toString());
        fecha.setEstado("S");
        numeroFecha = spNumFecha.getSelectedItem().toString();
        refFechas = database.getReference(Rutas.FECHAS)
                .child(PulpoSingleton.getInstance().getCodigoTorneo())
                .child(numeroFecha);
        refFechas.setValue(fecha).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Se inserto correctamente la fecha",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"No se inserto correctamente la fecha",Toast.LENGTH_LONG).show();
            }
        });
        fechas.add(fecha);
        PulpoSingleton.getInstance().setFechas(fechas);
        PulpoSingleton.getInstance().setNumeroFechaP(numeroFecha);
        limpiar();



    }



    public void navIrListaPartidos() {
        Intent intent = new Intent(this, ListaPartidosActivity.class);
        intent.putExtra("numFecha",numeroFecha);
        Log.d(Rutas.TAG,"El valor enviado en crearFechas es nav irLista   "+numeroFecha);
        PulpoSingleton.getInstance().setNumeroFechaP(numeroFecha);
        startActivity(intent);
    }
    public void limpiar(){

        etDia1.setText("");
        etDia2.setText("");
        etDia3.setText("");
        etCancha1.setText("");
        etCancha2.setText("");

    }


}
