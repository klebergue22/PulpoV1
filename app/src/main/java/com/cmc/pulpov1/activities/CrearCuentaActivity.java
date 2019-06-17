package com.cmc.pulpov1.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmc.pulpov1.IdentificadorUtils;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Persona;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CrearCuentaActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private FirebaseAuth mAuth;// ...
    private EditText etNombre;
    private EditText etApellido;
    private EditText etCorreoE;
    private EditText etClave;
    private Button btnCrear;
    private String mailC;
    private Persona persona;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        persona = new Persona();
        atarComponentes();
        desplazarPantalla();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    public void registrar() {
        boolean resultadoValidacion;
        String registro = "Nombres: " + etNombre.getText().toString() + "Apellidos" + etApellido.getText().toString()
                + "Correo Electronico" + etCorreoE.getText().toString()
                + "Contraseña" + etClave.getText().toString();
       /* Toast.makeText(this,
                "Valor:" + registro, Toast.LENGTH_LONG).show();*/
        resultadoValidacion = validarCampos();
        if (resultadoValidacion) {
            crearCuentaFirebase();
            ingresarRol();

            // limpiarComponentes();
            finish();

        }
    }

    private void crearCuentaFirebase() {
        mAuth.createUserWithEmailAndPassword(etCorreoE.getText().toString().trim(), etClave.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CrearCuentaActivity.this, "Registro Creado con éxito", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();

                            //updateUI(user);
                        } else {
                            Log.w("PULPOLOG", "excepcion" + task.getException().getClass().getCanonicalName() + " " + task.getException()+"CrearCuentaActivity");

                            if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Log.e("PULPOLOG", "La contraseña debe tener al menos 6 caracteres"+"CrearCuentaActivity", task.getException());
                                Toast.makeText(CrearCuentaActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Log.w("PULPOLOG", "Ya existe un usuario registrado con ese correo"+"CrearCuentaActivity");
                                Toast.makeText(CrearCuentaActivity.this, "Ya existe un usuario registrado con ese correo", Toast.LENGTH_SHORT).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.w("PULPOLOG", "El formato del correo es incorrecto"+"CrearCuentaActivity");
                                Toast.makeText(CrearCuentaActivity.this, "El formato del correo es incorrecto", Toast.LENGTH_SHORT).show();
                            }

                            //  Log.w("PULPOLOG","excepcion"+task.getException().getClass().getCanonicalName());
                            else {
                                Log.e("PULPOLOG", "Error al crear el registro "+"CrearCuentaActivity", task.getException());
                                Toast.makeText(CrearCuentaActivity.this, "Error al crear el registro ", Toast.LENGTH_SHORT).show();
                            }
                            //updateUI(null);
                        }
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
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.2f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                } else {
                    //Toast.makeText(MainActivity.this,"keyboard closed",Toast.LENGTH_LONG).show();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Regresar la línea de guía a la posición original
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.40f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        });
    }

    private void ingresarRol() {


        //creo un objeto persona para poder realizar la insercion

        persona.setNombre(etNombre.getText().toString());
      //  Log.d("PULPOLOG", "NOMBRE PERSONA" + etNombre.getText().toString()+"CrearCuentaActivity");
        persona.setApellido(etApellido.getText().toString());
        // persona.setCorreo(etCorreoE.getText().toString())

        mailC = IdentificadorUtils.crearIdentificacionMail(etCorreoE.getText().toString());
      //  Log.d("PULPOLOG", "EL VALOR DEL CORREO ES " + mailC+"CrearCuentaActivity");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.
                getReference(Rutas.USUARIOS);
        myRef1.child(mailC).setValue(persona);


        Toast.makeText(this,
                "Se inserto el USUARIO EN EL ROL:" + persona.getNombre().toString(), Toast.LENGTH_LONG).show();
      //  Log.d("PULPOLOG", "Se inserto el USUARIO EN EL ROL"+"CrearCuentaActivity");

    }


    private void atarComponentes() {
        etNombre = findViewById(R.id.etNombres);
        etApellido = findViewById(R.id.etApellidos);
        etCorreoE = findViewById(R.id.etCorreo);
        etClave = findViewById(R.id.etClave);
        btnCrear = findViewById(R.id.btnCrear);
        constraintLayout = findViewById(R.id.rootview);

    }

    private void limpiarComponentes() {
        etNombre.setText("");
        etApellido.setText("");
        etCorreoE.setText("");
        etClave.setText("");
    }

    private boolean validarCampos() {
        boolean correcto = true;
        if (etNombre.getText() != null && etNombre.getText().toString().isEmpty()) {
            etNombre.requestFocus();
            etNombre.setError("El nombre es obligatorio");
            correcto = false;
        }
        if (etApellido.getText() != null && etApellido.getText().toString().isEmpty()) {
            etApellido.requestFocus();
            etApellido.setError("El apellido es obligatorio");
            correcto = false;
        }
        if (etCorreoE.getText() != null && etApellido.getText().toString().isEmpty()) {
            etCorreoE.requestFocus();
            etCorreoE.setError("La dirección de correo electrónico es obligatorio");
            correcto = false;
        }

        if (etClave.getText() != null && etClave.getText().toString().isEmpty()) {
            etClave.requestFocus();
            etClave.setError("La contraseña es obligatoria");
            correcto = false;
        }

        return correcto;


    }
}
