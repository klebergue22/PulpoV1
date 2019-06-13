package com.cmc.pulpov1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmc.pulpov1.entities.Partido;

import java.util.List;

public class ResultadoActivity extends AppCompatActivity {
    private Button btnGuardar;
    private TextView equipo1;
    private TextView equipo2;
    private EditText valor1;
    private EditText valor2;
    private List<Partido> partidos;
    private Partido partidoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        partidoSeleccionado=PulpoSingleton.getInstance().getPartido();
        partidos=PulpoSingleton.getInstance().getPartidos();
        equipo1=findViewById(R.id.tvEquip1);
        equipo1.setText(partidoSeleccionado.getEquipoUno());
        equipo2=findViewById(R.id.tvEquip2);
        equipo2.setText(partidoSeleccionado.getEquipoDos());
        valor1=findViewById(R.id.etValor1);
        valor1.setText(partidoSeleccionado.getPuntosEquipoUno());
        valor2=findViewById(R.id.etValor2);
        valor2.setText(partidoSeleccionado.getPuntosEquiDos());
        btnGuardar=findViewById(R.id.btnGuardarResultadoA);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
                finish();
            }
        });
    }

    public void guardar(){
        for(Partido p:partidos){
            if(partidoSeleccionado.equals(p)){
                p.setPuntosEquipoUno(valor1.getText().toString());
                p.setPuntosEquiDos(valor2.getText().toString());
            }
        }


    }
}
