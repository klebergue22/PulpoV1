package com.cmc.pulpov1.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Partido;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ResultadoAdapter extends ArrayAdapter<Partido>  {

    private Context context;
    private List<Partido> partidos;

    private EditText etPuntosE1;
    private EditText etPuntosE2;
    private Button btnGuardarResultado;



    public ResultadoAdapter(Context context, List<Partido> partidos) {
        super(context, 0, partidos);
        this.context = context;
        this.partidos = partidos;

    }


    @Override
    public int getCount() {
        return partidos.size();
    }

    @Override
    public Partido getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View viewItem = convertView;
        if (viewItem == null)
            viewItem = LayoutInflater.from(context).inflate(R.layout.item_partido, parent, false);
        final Partido partidoActual = partidos.get(position);
        etPuntosE1 = viewItem.findViewById(R.id.txtPtosE01);
        partidoActual.setPuntosEquipoUno(etPuntosE1.getText().toString());
        etPuntosE2 = viewItem.findViewById(R.id.txtPtosE02);
        partidoActual.setPuntosEquiDos(etPuntosE2.getText().toString());
        Log.d("PULPOLOG","VALOR DEL LOG "+partidoActual.toString());
        partidos.set(position,partidoActual);
        PulpoSingleton.getInstance().setPartidos(partidos);
        btnGuardarResultado = viewItem.findViewById(R.id.btnGuardarResultados);

                btnGuardarResultado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

            }
        });

        return viewItem;
    }
}
