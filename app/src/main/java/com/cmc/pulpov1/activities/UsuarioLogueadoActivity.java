package com.cmc.pulpov1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cmc.pulpov1.R;

public class UsuarioLogueadoActivity extends AppCompatActivity {
    private Button btnCrearT;
    private Button btnCrearEq;
    private Button btnCrearJu;
    private Button btnCrearP;
    private Button btnVerT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_logueado);
        atarComponentes();
        btnCrearT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                crearTorneo();
            }
        });
        btnCrearEq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearEquipo();
            }
        });

        btnCrearJu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearJugador();
            }
        });
        btnCrearP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPartido();
            }
        });
        btnVerT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verTorneos();
            }
        });

    }

    public void crearTorneo() {
        Intent intent = new Intent(this, GestionTorneoActivity.class);
        startActivity(intent);
    }

    public void crearEquipo() {
        Intent intent = new Intent(this, CrearEquipoActivity.class);
        startActivity(intent);
    }

    public void crearJugador() {
        Intent intent = new Intent(this, RegistroJugadorActivity.class);
        startActivity(intent);
    }

    public void crearPartido() {
        Intent intent = new Intent(this, RegistroFechasActivity.class);
        startActivity(intent);
    }
    public void verTorneos() {
        Intent intent = new Intent(this, ListaDeTorneosActivity.class);
        startActivity(intent);
    }

    public void atarComponentes() {

        btnCrearT = findViewById(R.id.btnCrearT);
        btnCrearEq = findViewById(R.id.btnCrearEq);
        btnCrearJu = findViewById(R.id.btnCrearJu);
        btnCrearP = findViewById(R.id.btnCrearP);
        btnVerT=findViewById(R.id.btnVerListaT);

    }

}
