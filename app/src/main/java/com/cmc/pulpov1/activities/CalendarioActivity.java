package com.cmc.pulpov1.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cmc.pulpov1.LogPulpo;
import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.adapters.CalendarioAdapter;
import com.cmc.pulpov1.adapters.PartidoRecyclerViewAdapter;
import com.cmc.pulpov1.entities.Equipo;
import com.cmc.pulpov1.entities.Partido;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarioActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private ConstraintLayout constraintLayout;
    private EditText etFechaPartido;
    private TextView tvEquipo1;
    private TextView tvEquipo2;
    private TextView tvNumeroFecha;
    private Button btnRegistrar;
    private Spinner spHora;
    private Spinner spMiunuto;
    private TextView tvFecha;
    private Date fechaProgramacion;
    //EEEE/MMMM/yyyy
    //dd MMM yyyy
    //dow mon dd hh:mm:ss zzz yyyy
    //dow, MMM dd yyyy
    //"EEEE, MMMM d 'at' hh:mm a 'in the y"
    //EEE, MMM d, ''yy

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfCompleto = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    private int anio;
    private int mes;
    private int dia;
    private List<String> hora;
    private List<String> minutos;
    private ArrayAdapter<String> adpH;
    private ArrayAdapter<String> adpM;
    private Equipo equipo1Seleccionado;
    private Equipo equipo2Seleccionado;
    private List<Equipo> equipo1;
    private List<Equipo> equipo2;
    private ListView lvEquipoUno;
    private ListView lvEquipoDos;
    private ArrayAdapter<Equipo> adpE1;
    private ArrayAdapter<Equipo> adpE2;
    private String fechaMod;
    private String codPartido;
    private Partido partido;
    private String horaP;
    private String minP;
    private PartidoRecyclerViewAdapter partidoAdapter;
    //   private List<Partido> partidos;
    private DatabaseReference refFecha;
    private String numFecha;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        Intent intent = getIntent();
        numFecha=intent.getStringExtra("numFecha");
        Log.d(LogPulpo.TAG,"El valor que se recupera es "+numFecha);

        atarComponentes();

        cargarEquipos();
        cargarSpinnerHora();
        desplazarPantalla();
        tomarPosicionEquipos();
        //partidos = new ArrayList<Partido>();

        database = FirebaseDatabase.getInstance();
        adpE1 = new CalendarioAdapter(getApplicationContext(), equipo1);
        lvEquipoUno.setAdapter(adpE1);
        adpE2 = new CalendarioAdapter(getApplicationContext(), equipo2);
        lvEquipoDos.setAdapter(adpE2);
        codPartido = equipo1Seleccionado.getNombreEquipo() + "_" + equipo2Seleccionado.getNombreEquipo();
        Log.d("LogPulpo.TAG", "codigo del partido ON CREATE" + codPartido);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarFecha();
                navIrListaPartidos();

            }
        });

        lvEquipoUno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                equipo1Seleccionado = equipo1.get(position);
                tvEquipo1.setText(equipo1Seleccionado.getNombreEquipo());


            }
        });

        lvEquipoDos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                equipo2Seleccionado = equipo2.get(position);
                tvEquipo2.setText(equipo2Seleccionado.getNombreEquipo());

            }
        });

        etFechaPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                anio = mcurrentDate.get(Calendar.YEAR);
                mes = mcurrentDate.get(Calendar.MONTH);
                dia = mcurrentDate.get(Calendar.DAY_OF_WEEK);


                final DatePickerDialog mDatePicker = new DatePickerDialog
                        (CalendarioActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                int month_k = selectedmonth + 1;
                                //armar la fecha con los valores de anio mes y dia que selecciona
                                final Calendar c = Calendar.getInstance();
                                c.set(selectedyear, selectedmonth, selectedday);
                                fechaProgramacion = c.getTime();
                                etFechaPartido.setText(sdfCompleto.format(fechaProgramacion));
                                fechaMod = sdfCompleto.format(fechaProgramacion);

                                Log.d("LogPulpo.TAG", "Valor de la fecha para el ingreso en el nodo" + fechaMod);
                            }
                        }, anio, mes, dia);

                mDatePicker.setTitle("Seleccione una fecha");
                mDatePicker.getDatePicker();
                mDatePicker.show();
            }
        });
    }

    public void atarComponentes() {
        partido = new Partido();
        constraintLayout = findViewById(R.id.rootview);
        lvEquipoUno = findViewById(R.id.lvEquipo1);
        lvEquipoDos = findViewById(R.id.lvEquipo2);
        tvEquipo1 = findViewById(R.id.tvEquipo1);
        tvEquipo2 = findViewById(R.id.tvEquipo2);
        tvNumeroFecha = findViewById(R.id.tvnumFecha);
        tvNumeroFecha.setText(PulpoSingleton.getInstance().getNumeroFechaP());
        spHora = findViewById(R.id.spHora);
        spMiunuto = findViewById(R.id.spMinuto);
        etFechaPartido = findViewById(R.id.etFecha);
        btnRegistrar = findViewById(R.id.btnRegistrarFecha);

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
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.6f); // 7% // range: 0 <-> 1
                    android.transition.TransitionManager.beginDelayedTransition(constraintLayout);
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

    public void cargarSpinnerHora() {
        hora = new ArrayList<String>();
        hora.add("1");
        hora.add("2");
        hora.add("3");
        hora.add("4");
        hora.add("5");
        hora.add("6");
        hora.add("7");
        hora.add("8");
        hora.add("9");
        hora.add("10");
        hora.add("11");
        hora.add("12");
        hora.add("13");
        hora.add("14");
        hora.add("15");
        hora.add("16");
        hora.add("17");
        hora.add("18");
        hora.add("19");
        hora.add("20");
        hora.add("21");
        hora.add("22");
        hora.add("23");
        hora.add("24");
        minutos = new ArrayList<String>();
        adpH = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hora);
        spHora.setAdapter(adpH);
        minutos = new ArrayList<String>();
        minutos.add("00");
        minutos.add("05");
        minutos.add("10");
        minutos.add("15");
        minutos.add("20");
        minutos.add("30");
        minutos.add("35");
        minutos.add("40");
        minutos.add("45");
        minutos.add("50");
        minutos.add("55");
        adpM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutos);
        spMiunuto.setAdapter(adpM);
    }

    private void tomarPosicionEquipos() {
        lvEquipoUno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                equipo1Seleccionado = equipo1.get(position);
            }
        });

        lvEquipoDos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                equipo2Seleccionado = equipo2.get(position);
            }
        });
    }

    private void cargarEquipos() {
        equipo1 = new ArrayList<Equipo>();
        equipo1 = PulpoSingleton.getInstance().getEquipos();
        equipo2 = new ArrayList<Equipo>();
        equipo2 = PulpoSingleton.getInstance().getEquipos();
        equipo1Seleccionado = new Equipo();
        equipo2Seleccionado = new Equipo();


    }

    private boolean validarValores() {
        boolean valor = false;
        if ((tvNumeroFecha.getText().toString() == null)) {
            Toast.makeText(getApplicationContext(), "Ingrese un número de fecha ", Toast.LENGTH_LONG).show();
            tvNumeroFecha.requestFocus();
            tvNumeroFecha.setError("El valor es obligatorio");
            valor = false;
        }

        if ((tvEquipo1.getText().toString() != null) && (tvEquipo2.getText().toString() != null)) {
            codPartido = equipo1Seleccionado.getNombreEquipo().toString() + "_" + equipo2Seleccionado.getNombreEquipo().toString();
            partido.setId(codPartido);
            PulpoSingleton.getInstance().setCodigoPartido(codPartido);
            valor = true;
        } else {


            Toast.makeText(getApplicationContext(), "Seleccione un equipo", Toast.LENGTH_LONG).show();
            valor = false;
        }

        return valor;
    }

    private void guardarFecha() {
        if (validarValores()) {
            //PulpoSingleton.getInstance().setNumeroFechaP(tvNumeroFecha.getText().toString());
            PulpoSingleton.getInstance().setNumeroFechaP(numFecha);
            Log.d(LogPulpo.TAG, "El valor del numero de fecha del calendario es######  " + PulpoSingleton.getInstance().getNumeroFechaP());
            Log.d(LogPulpo.TAG, "El valor del codPartido dentro de guardar fecha es " + codPartido.toString());
            horaP = spHora.getSelectedItem().toString();
            minP = spMiunuto.getSelectedItem().toString();
            partido.setEquipoUno(equipo1Seleccionado.getNombreEquipo());
            partido.setEquipoDos(equipo2Seleccionado.getNombreEquipo());
            partido.setPuntosEquipoUno("0");
            partido.setPuntosEquiDos("0");
            partido.setHora(horaP);
            partido.setMinuto(minP);
            partido.setFecha(fechaMod);
            partido.setCategoria(PulpoSingleton.getInstance().getCodigoCategoria());
            refFecha = database.getReference(Rutas.CALENDARIO)
                    .child(Rutas.ROOT_TORNEOS)
                    .child(PulpoSingleton.getInstance().getCodigoTorneo())
                    .child(PulpoSingleton.getInstance().getNumeroFechaP())
                    .child(PulpoSingleton.getInstance().getCodigoPartido())
            ;


            refFecha.setValue(partido);

            Log.d(LogPulpo.TAG, "El valor del codigo del numero de fecha es " + PulpoSingleton.getInstance().getNumeroFechaP());

            //      partidos=PulpoSingleton.getInstance().getPartidos();
            //    Log.d(LogPulpo.TAG,"El numero de partidos que tiene la lista es/////// "+partidos.size());
            //   partidos.add(partido);
            PulpoSingleton.getInstance().setCodigoPartido(codPartido);
            Log.d(LogPulpo.TAG, "El valor del numero del codigo del partido es " + PulpoSingleton.getInstance().getCodigoPartido());
            PulpoSingleton.getInstance().setNumeroFechaP(tvNumeroFecha.getText().toString());


            //Agrego el Listener luego de tener insertado un partido

           /* if (partidos != null) {
                PulpoSingleton.getInstance().setPartidoFirabaseListener();
                PulpoSingleton.getInstance().setPartidos(partidos);
            }*/

            Toast.makeText(this, "Se registro el Partido " + partido.getEquipoUno().toString() + partido.getEquipoDos().toString(), Toast.LENGTH_SHORT).show();
            Log.d("LogPulpo.TAG", "Se registro el partido");

            // partidoAdapter = new PartidoRecyclerViewAdapter(partidos);
            // PulpoSingleton.getInstance().setPartidoAdapter(partidoAdapter);


        } else {
            Toast.makeText(this, "No se registro el partido " + partido.getEquipoUno().toString() + partido.getEquipoDos().toString(), Toast.LENGTH_SHORT).show();
            Log.d("LogPulpo.TAG", "Error al registrar el partido");
        }


    }

    public void navIrListaPartidos() {
        Intent intent = new Intent(this, ListaPartidosActivity.class);
        intent.putExtra("numFecha",numFecha);
        Log.d(LogPulpo.TAG,"El valor enviado en crearFechas hacia la lista de fechas es  es  "+numFecha);
        PulpoSingleton.getInstance().setNumeroFechaP(numFecha);

        startActivity(intent);
    }


}
