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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.cmc.pulpov1.IdentificadorUtils;
import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Jugador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistroJugadorActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private EditText etCedula;
    private EditText etPrimerNombre;
    private EditText etPrimerApellido;
    private EditText etSegundoNombre;
    private EditText etSegundoApellido;
    private EditText etFechaNacimiento;
    private EditText etMailJ;
    private ConstraintLayout constraintLayout;
    private Button btnCrearJudador;
    private Jugador jugador;
    private String mailc;
    private Date fechaN;
    private int anio;
    private int mes;
    private int dia;
    private SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat sdfCompleto=new SimpleDateFormat("dd MMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_jugador);
        atarComponentes();
        database= FirebaseDatabase.getInstance();
        etMailJ.setText(PulpoSingleton.getInstance().getMailN());
        mailc=PulpoSingleton.getInstance().getMail();
        etMailJ.setEnabled(false);

        jugador=PulpoSingleton.getInstance().getJugador();

        if(jugador!=null) {
            etCedula.setText(jugador.getCedula());
            etPrimerNombre.setText(jugador.getPrimerNombre());
            etPrimerApellido.setText(jugador.getPrimerApellido());
            etSegundoNombre.setText(jugador.getSegundoNombre());
            etSegundoApellido.setText(jugador.getSegundoApellido());
            if (jugador.getFechaNacimiento() != null) {
                etFechaNacimiento.setText(sdfCompleto.format(jugador.getFechaNacimiento()));
            }
        }
        desplazarPantalla();
        btnCrearJudador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearJugador();
            }
        });
        etFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                anio = mcurrentDate.get(Calendar.YEAR);
                mes = mcurrentDate.get(Calendar.MONTH);
                dia = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog mDatePicker = new DatePickerDialog

                        (RegistroJugadorActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                int month_k = selectedmonth + 1;
                                //armar la fecha con los valores de anio mes y dia que selecciona
                                final Calendar c = Calendar.getInstance();
                                c.set(selectedyear,selectedmonth,selectedday);
                                fechaN=c.getTime();
                                etFechaNacimiento.setText(sdfCompleto.format(fechaN));
                            }
                        }, anio, mes, dia);

                mDatePicker.setTitle("Seleccione una fecha");
                mDatePicker.getDatePicker();
               mDatePicker.show();
            }

        });

    }

    public void insertarJugador() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refJugador = database.getReference(Rutas.JUGADORES).child(mailc);
        if (jugador== null) {
            jugador=new Jugador();
        }
        jugador.setMailJugador(PulpoSingleton.getInstance().getMail());
        jugador.setCedula(etCedula.getText().toString());
        jugador.setPrimerNombre(etPrimerNombre.getText().toString());
        jugador.setPrimerApellido(etPrimerApellido.getText().toString());
        jugador.setSegundoNombre(etSegundoNombre.getText().toString());
        jugador.setSegundoApellido(etSegundoApellido.getText().toString());
        if(fechaN == null){
            try {
                fechaN = sdfCompleto.parse(etFechaNacimiento.getText().toString());
            }catch (Exception e){
                Log.e("PULPOLOG","Error al convertir la fehca");
        }
        }
        jugador.setFechaNacimiento(fechaN);
        refJugador.setValue(jugador);//si es exitoso o no
        PulpoSingleton.getInstance().setJugador(jugador);
        finish();
    }

    private void desplazarPantalla() {
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.0001f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                } else {
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

    public void atarComponentes() {
        etMailJ=findViewById(R.id.etMailJ);
        etMailJ.setText(PulpoSingleton.getInstance().getMailN());
        etCedula = findViewById(R.id.etCedula);
        etPrimerNombre = findViewById(R.id.etPrimerNombre);
        etPrimerApellido = findViewById(R.id.etPrimerApellido);
        etSegundoNombre = findViewById(R.id.etSegundoNombre);
        etSegundoApellido = findViewById(R.id.etSegundoApellido);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        btnCrearJudador = findViewById(R.id.btnCrearJ);
        constraintLayout = findViewById(R.id.rootview);

        etMailJ.setEnabled(false);


    }

    public void crearJugador() {
        boolean resultadoValidacion;
        resultadoValidacion = validarCampos();
        if (resultadoValidacion) {
            insertarJugador();
        }
    }

    public boolean validarCampos() {
        boolean correcto = true;
      Log.w("PULPOLOG", "ingreso al Metodo validarCampos() ");
        if (etCedula.getText() != null && etCedula.getText().toString().isEmpty()) {
            etCedula.requestFocus();
            etCedula.setError("La cédula es obligatorio");
            correcto = false;
        }
        if (etPrimerNombre.getText() != null && etPrimerNombre.getText().toString().isEmpty()) {

            etPrimerNombre.requestFocus();
            etPrimerNombre.setError("El nombre  es obligatorio");
            correcto = false;
        }
        if (etPrimerApellido.getText() != null && etPrimerApellido.getText().toString().isEmpty()) {
            Log.w("PULPOLOG", "ingreso al Metodo Valicadion de año ");

            etPrimerApellido.requestFocus();
            etPrimerApellido.setError("El apellido es obligatorio");
            correcto = false;

        }
        if (etFechaNacimiento.getText() != null && etFechaNacimiento.getText().toString().isEmpty()) {
            etFechaNacimiento.requestFocus();
            etFechaNacimiento.setError("La fecha de nacimiento es obligatorio");
            correcto = false;
        }
        if (etMailJ.getText() != null && etMailJ.getText().toString().isEmpty()) {
            etFechaNacimiento.requestFocus();
            etFechaNacimiento.setError("La fecha de nacimiento es obligatorio");
            correcto = false;
        }
        return correcto;
    }






}
