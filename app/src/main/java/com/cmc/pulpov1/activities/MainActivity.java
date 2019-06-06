package com.cmc.pulpov1.activities;

import android.content.Intent;
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
import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Jugador;
import com.cmc.pulpov1.entities.UsuarioRol;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText etMail = null;
    private EditText etPassword = null;
    private ConstraintLayout constraintLayout;
    private Button btnIngresar;
    private Button btnCrear;
    private Button btnRecuperar;
    private Button btnTorneo;
    private UsuarioRol usuarioRol;
    private String mailc;
    private DatabaseReference refUsuario;
    FirebaseDatabase database;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        atarComponentes();
        desplazarPantalla();
        mAuth = FirebaseAuth.getInstance();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingresar();
            }
        });
        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarRecuperarClave();
            }
        });
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarCrearCuenta();
            }
        });

    }


    public void ingresar() {
        boolean resultadoValidacion;
        resultadoValidacion = validarCampos();
        if (resultadoValidacion) {
            validarCuentaFirebase();
        }
    }


    public void validarCuentaFirebase() {
        mAuth.signInWithEmailAndPassword(etMail.getText().toString().trim(), etPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            recuperarRoles();
                            recuperarJugador();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(MainActivity.this, "El usuario o la contraseña no son correctos", Toast.LENGTH_SHORT).show();
                            }
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(MainActivity.this, "No se encuentra registrado en el sistema, por favor use la opción Regístrate", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });


    }


    public void navegarCrearCuenta() {
        Intent intent = new Intent(this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    public void navegarRecuperarClave() {
        Intent intent = new Intent(this, RecuperarClaveActivity.class);
        startActivity(intent);
    }

    public void navegarTorneosLogueado() {
        Intent intent = new Intent(this, ListaDeTorneosActivity.class);
        startActivity(intent);
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
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.20f); // 7% // range: 0 <-> 1
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

    private void atarComponentes() {
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        btnCrear = findViewById(R.id.btnCrear);
        // btnTorneo=findViewById(R.id.btnTorneo);
        constraintLayout = findViewById(R.id.rootview);
    }

    public boolean validarCampos() {
        boolean correcto = true;
        Log.w("PULPOLOG", "ingreso al Metodo validarCampos MainActivity() ");
        if (etMail.getText() != null && etMail.getText().toString().isEmpty()) {
            etMail.requestFocus();
            etMail.setError("El correo es obligatorio");
            correcto = false;
        }
        if (etPassword.getText() != null && etPassword.getText().toString().isEmpty()) {
            etPassword.requestFocus();
            etPassword.setError("La contraseña es obligatoria");
            correcto = false;
        }
        return correcto;
    }

    public void recuperarRoles() {
        mailc = IdentificadorUtils.crearIdentificacionMail(etMail.getText().toString());
        PulpoSingleton.getInstance().setMail(mailc);
        PulpoSingleton.getInstance().setMailN(etMail.getText().toString());
        database = FirebaseDatabase.getInstance();
        refUsuario = database.getReference(Rutas.USUARIOS).child(mailc);
        refUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarioRol = dataSnapshot.getValue(UsuarioRol.class);
                PulpoSingleton.getInstance().setUsuarioLogueado(usuarioRol);
                navegarTorneosLogueado();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void recuperarJugador() {
        database = FirebaseDatabase.getInstance();
        refUsuario = database.getReference(Rutas.JUGADORES).child(mailc);
        refUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Jugador jugador = dataSnapshot.getValue(Jugador.class);
                if (jugador != null) {
                    PulpoSingleton.getInstance().setJugador(jugador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
