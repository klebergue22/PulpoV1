package com.cmc.pulpov1.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.adapters.PartidosAdapter;
import com.cmc.pulpov1.entities.Partido;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ResultadoActivity extends AppCompatActivity {
    private Button btnGuardar;
    private TextView equipo1;
    private TextView equipo2;
    private EditText valor1;
    private EditText valor2;
    private List<Partido> partidos;
    private Partido partidoSeleccionado;
    private FirebaseDatabase database;
    private DatabaseReference refResultado;
    private PartidosAdapter pad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        pad = new PartidosAdapter(getApplicationContext(), partidos);
        atarComponentes();
//        Log.d(Rutas.TAG,"El valor del numero de partido es "+PulpoSingleton.getInstance().getNumeroFechaP().toString());

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
                finish();
            }
        });
    }

    public void atarComponentes() {
        database = FirebaseDatabase.getInstance();
        partidoSeleccionado = PulpoSingleton.getInstance().getPartido();
        partidos = PulpoSingleton.getInstance().getPartidos();
        equipo1 = findViewById(R.id.tvEquip1);
        equipo1.setText(partidoSeleccionado.getEquipoUno());
        equipo2 = findViewById(R.id.tvEquip2);
        equipo2.setText(partidoSeleccionado.getEquipoDos());
        valor1 = findViewById(R.id.etValor1);
        valor1.setText(partidoSeleccionado.getPuntosEquipoUno());
        valor2 = findViewById(R.id.etValor2);
        valor2.setText(partidoSeleccionado.getPuntosEquiDos());
        btnGuardar = findViewById(R.id.btnGuardarResultadoA);
    }

    public void guardar() {
        for (int i = 0; i < partidos.size(); i++) {
            if (partidoSeleccionado.equals(partidos.get(i))) {
                partidoSeleccionado.setPuntosEquipoUno(valor1.getText().toString());
                PulpoSingleton.getInstance().setPartido(partidoSeleccionado);
                partidoSeleccionado.setPuntosEquiDos(valor2.getText().toString());
                partidos.remove(i);
                partidos.add(i, partidoSeleccionado);
            }
        }
        refResultado = database.getReference(Rutas.CALENDARIO)
                .child(Rutas.ROOT_TORNEOS)
                .child(PulpoSingleton.getInstance().getCodigoTorneo())
                .child(PulpoSingleton.getInstance().getNumeroFechaP())
                .child(partidoSeleccionado.getId());
        refResultado.child("puntosEquipoUno").setValue(valor1.getText().toString());
        refResultado.child(("puntosEquiDos")).setValue(valor2.getText().toString());
        PulpoSingleton.getInstance().setPartidoFirabaseListener();


    }


}
