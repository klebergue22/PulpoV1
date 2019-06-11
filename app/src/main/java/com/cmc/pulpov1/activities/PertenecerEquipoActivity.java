package com.cmc.pulpov1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Jugador;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PertenecerEquipoActivity extends AppCompatActivity {

    private String nombreTorneo;
    private String nombreCategoria;
    private String nombreEquipo;
    private TextView tvNombreTorneo;
    private TextView tvCategoria;
    private TextView tvNombreEquipo;
    private Button btnGuardar;
    private Jugador jugador;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_pertenecer_equipo);
        nombreTorneo = PulpoSingleton.getInstance().getNombreTorneo();
        nombreCategoria = PulpoSingleton.getInstance().getCodigoCategoria();
        nombreEquipo = PulpoSingleton.getInstance().getNombreEquipo();
        tvNombreTorneo = findViewById(R.id.tvNombreTorneo);
        tvCategoria = findViewById(R.id.tvCategoriaFecha);
        tvNombreEquipo = findViewById(R.id.tvNombreEquipo);
        btnGuardar = findViewById(R.id.btnGuardar);
        tvNombreEquipo.setText(nombreEquipo);
        tvCategoria.setText(nombreCategoria);
        tvNombreTorneo.setText(nombreTorneo);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });
    }

    public void guardar() {
        jugador = PulpoSingleton.getInstance().getJugador();

        if (jugador == null) {
            navCrearJugador();
        } else {
            jugador.setEstado("P");
                 DatabaseReference refEquipo = database.
                    getReference(Rutas.ROOT_TORNEOS).child(PulpoSingleton.getInstance().getCodigoTorneo())
                    .child(Rutas.CATEGORIAS).child(PulpoSingleton.getInstance().getCodigoCategoria())
                    .child(Rutas.EQUIPOS).child(PulpoSingleton.getInstance().getCodigoEquipo())
                    .child(Rutas.MIEMBROS).child(PulpoSingleton.getInstance().getMail());
            refEquipo.setValue(jugador);
        }
        finish();
    }

    public void navCrearJugador() {
        Intent intent = new Intent(this, RegistroJugadorActivity.class);
        startActivity(intent);
    }
}
