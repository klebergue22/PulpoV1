package com.cmc.pulpov1.activities;

import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Partido;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegistroFechasActivity extends AppCompatActivity {
    private Spinner spEquipoUno;
    private Spinner spEquipoDos;
    private EditText etFechaPartido;
    private Button btnCrearPartido;
    private ConstraintLayout constraintLayout;
    private Partido partido;
    private String id;
    private Date fechaP;
    private String fecha;
    private int anio;
    private int mes;
    private int dia;
    private List<String> equipos;
    private String valorEquipoUno;
    private String valorEquipoDos;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat sdfCompleto = new SimpleDateFormat("dd MMM yyyy");
    private FirebaseDatabase database;
    private ArrayAdapter<String> adp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_fechas);

        database = FirebaseDatabase.getInstance();

        atarComponentes();
        desplazarPantalla();
        escucharEquipos();
        equipos = new ArrayList<String>();
        equipos.add("LVP");
        equipos.add("BULLS");
        equipos.add("TIGERS");
        equipos.add("BLESSED");

        adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, equipos);
        spEquipoUno.setAdapter(adp);

        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, equipos);
        spEquipoDos.setAdapter(adp2);

        btnCrearPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarPartido();
            }
        });

        etFechaPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                anio = mcurrentDate.get(Calendar.YEAR);
                mes = mcurrentDate.get(Calendar.MONTH);
                dia = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog mDatePicker = new DatePickerDialog
                        (RegistroFechasActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Log.d("PULPOLOG", "selectedday" + selectedday);
                                Log.d("PULPOLOG", "selectedMonth" + selectedmonth);
                                Log.d("PULPOLOG", "selectedyear" + selectedyear);
                                //etFechaPartido.setText(new StringBuilder().append(selectedyear).append("/").append(selectedmonth + 1).append("/").append(selectedday));
                                Log.d("PULPOLOG", "Fecha Partido" + etFechaPartido.getText().toString());

                                //armar la fecha con los valores de anio mes y dia que selecciona

                                final Calendar c = Calendar.getInstance();
                                c.set(selectedyear, selectedmonth, selectedday);
                                fechaP = c.getTime();
                                etFechaPartido.setText(sdfCompleto.format(fechaP));

                                //fecha=(String)"  "+selectedyear+selectedmonth+selectedday;
                                Log.d("PULPOLOG", "Fecha armada" + fecha);
                                //fechai.parse(fecha);


                                Log.d("PULPOLOG", "Fecha inicio" + etFechaPartido.toString());

                            }
                        }, anio, mes, dia);

                mDatePicker.setTitle("Please select date");
                // TODO Hide Future Date Here
                mDatePicker.getDatePicker();

                // TODO Hide Past Date Here
                //  mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                mDatePicker.show();
            }

        });

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
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.0001f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                } else {
                    //Toast.makeText(MainActivity.this,"keyboard closed",Toast.LENGTH_LONG).show();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Regresar la línea de guía a la posición original
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.1f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        });
    }

    public void insertarPartido() {

        DatabaseReference myRef1 = database.getReference("partidos");

        //etAnios = Integer.parseInt(etAnio.getText().toString().trim());
        valorEquipoUno = spEquipoUno.getSelectedItem().toString();
        valorEquipoDos = spEquipoDos.getSelectedItem().toString();
        partido = new Partido();
        partido.setEquipoUno(valorEquipoUno);
        partido.setEquipoDos(valorEquipoDos);
        //partido.setFecha(fechaP);
        Log.d("PULPOLOG", "Torneo" + partido.toString());
        id = valorEquipoUno + "_" + valorEquipoDos;
        Log.d("PULPOLOG", "Torneo" + id);

        myRef1.child(sdf.format(fechaP)).child(id).setValue(partido);
        Toast.makeText(this, "Se inserto el Partido " + partido.getEquipoUno().toString() + partido.getEquipoDos().toString(), Toast.LENGTH_SHORT).show();
        Log.d("PULPOLOG", "Se inserto el partido");
    }

    public void ingresar() {
        boolean resultadoValidacion;
        Log.w("PULPOLOG", "Ingresa al Metodo ingresar()");
        // String salida = "Mail:" + etMail.getText().toString() + "Password:" + etPassword.getText().toString();
      /* Toast.makeText(this,
             "Valor:" + salida, Toast.LENGTH_LONG).show();*/
        resultadoValidacion = validarCampos();
        Log.w("PULPOLOG", "RESULTADO VALIDACION>>" + resultadoValidacion);
        if (resultadoValidacion) {
            insertarPartido();
            limpiarComponentes();
        }
    }

    public void atarComponentes() {
        spEquipoUno = findViewById(R.id.spEquipo1);
        spEquipoDos = findViewById(R.id.spEquipo2);
        etFechaPartido = findViewById(R.id.etFechaPartido);
        btnCrearPartido = findViewById(R.id.btnCrearPart);
        constraintLayout = findViewById(R.id.rootview);
    }

    public void limpiarComponentes() {

        // spEquipoUno.setAdapter(spn);
        // etEquipoDos.setText("");
        etFechaPartido.setText("");
    }

    public boolean validarCampos() {
        boolean correcto = true;
        Log.w("PULPOLOG", "ingreso al Metodo validarCampos() ");

        if (etFechaPartido.getText() != null && etFechaPartido.getText().toString().isEmpty()) {
            etFechaPartido.requestFocus();
            etFechaPartido.setError("La fecha del Partido es obligatorio");
            correcto = false;
        }

        return correcto;
    }

    private void escucharEquipos() {
        //1-referencia al arbol
        DatabaseReference refEquipos = database.getReference("equipos");
        //2-crear el listener
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("PULPOLOG", "Se agrega equipo " + dataSnapshot.getKey());
                boolean repetido = false;
                for (String equipo : equipos) {
                    if (equipo.equals(dataSnapshot.getKey())) {
                        repetido = true;
                    }
                }
                if (!repetido) {
                    equipos.add(dataSnapshot.getKey());
                    adp.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("PULPOLOG", "Se modifica equipo " + dataSnapshot.getKey());

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PULPOLOG", "Se borra equipo " + dataSnapshot.getKey());
                int posicionRepetido = -1;
                for (int i = 0; i < equipos.size(); i++) {
                    if (equipos.get(i).equals(dataSnapshot.getKey())) {
                        posicionRepetido = i;
                    }
                }
                if (posicionRepetido != -1) {
                    equipos.remove(posicionRepetido);
                    adp.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        //3-atar la referencia con el listener
        refEquipos.addChildEventListener(childEventListener);
    }


}
