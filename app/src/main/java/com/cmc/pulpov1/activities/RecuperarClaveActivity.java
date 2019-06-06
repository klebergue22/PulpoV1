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

import com.cmc.pulpov1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class RecuperarClaveActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private EditText etCorreoE;
    private Button btnRecuperarC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_clave);
        atarComponentes();
        desplazarPantalla();


        btnRecuperarC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
                recuperar();
            }
        });
    }
    public void recuperar() {
        boolean respuestaValidacion;
        String valor = "Correo Electronico" + etCorreoE.getText().toString();
       /* Toast.makeText(this,
                "Valor:" + valor, Toast.LENGTH_LONG).show();*/
       respuestaValidacion=validarCampos();
       if(respuestaValidacion){
           recuperarCuentaFirebase();
       }



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
                    //Toast.makeText(MainActivity.this,"Keyboard is showing",Toast.LENGTH_LONG).show();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Subir la línea de guía
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.30f); // 7% // range: 0 <-> 1
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

    private void recuperarCuentaFirebase(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("es");
        auth.sendPasswordResetEmail(etCorreoE.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RecuperarClaveActivity.this, "Se ha enviado un correo para que pueda restablecer la clave", Toast.LENGTH_SHORT).show();
                            Log.d("PULPOLOG", "Se ha enviado un correo para que pueda restablecer la clave");
                        } else {
                            Toast.makeText(RecuperarClaveActivity.this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                            Log.w("PULPOLOG", "excepcion" + task.getException().getClass().getCanonicalName() + " " + task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(RecuperarClaveActivity.this, "El formato del correo electrónico no es la correcta", Toast.LENGTH_SHORT).show();
                                Log.e("PULPOLOG", "El formato del correo electrónico no es la correcta" + task.getException().getClass().getCanonicalName() + " " + task.getException());
                            } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(RecuperarClaveActivity.this, "El usuario no se encuentra registrado en el sistema", Toast.LENGTH_SHORT).show();
                                Log.e("PULPOLOG", "El usuario no se encuentra registrado en el sistema" + task.getException().getClass().getCanonicalName() + " " + task.getException());


                            } else {
                                Log.w("PULPOLOG", "excepcion" + task.getException().getClass().getCanonicalName());
                                Toast.makeText(RecuperarClaveActivity.this, "Excepción", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                });


    }


    private void atarComponentes() {
        constraintLayout = findViewById(R.id.rootview);
        etCorreoE = findViewById(R.id.etMail);
        btnRecuperarC = findViewById(R.id.btnRecuperar);
    }

    private boolean validarCampos() {
        boolean correcto = true;

        if (etCorreoE.getText() != null && etCorreoE.getText().toString().isEmpty()) {
            etCorreoE.requestFocus();
            etCorreoE.setError("El correo es obligatorio");
            correcto = false;
        }
        return correcto;
    }
}
