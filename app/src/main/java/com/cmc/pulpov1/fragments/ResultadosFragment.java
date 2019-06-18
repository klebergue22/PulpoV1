package com.cmc.pulpov1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Partido;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ResultadosFragment extends Fragment {
    RecyclerView recyclerView;
    private List<Partido> partidos;
    private Button btnFinalizar;
    private String numFecha;
    private DatabaseReference refResultados;
    private FirebaseDatabase database;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resultados_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        partidos = new ArrayList<Partido>();
        partidos = PulpoSingleton.getInstance().getPartidos();
        Log.d(Rutas.TAG, "El valor del OBJETO PARTIDOS ES RESULTADOFRAGMENT " + partidos.toString());
        Log.d(Rutas.TAG,"El valor de la fecha es"+PulpoSingleton.getInstance().getNumeroFechaP());


        btnFinalizar = view.findViewById(R.id.btnFinalizar);

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Rutas.TAG, "se dio click en el boton***************"+PulpoSingleton.getInstance().getNumeroFechaP());
                numFecha=PulpoSingleton.getInstance().getNumeroFechaP();
                refResultados = database.getReference(Rutas.CALENDARIO)
                        .child(Rutas.ROOT_TORNEOS)
                        .child(PulpoSingleton.getInstance().getCodigoTorneo())
                        .child(PulpoSingleton.getInstance().getNumeroFechaP())
                        .child(PulpoSingleton.getInstance().getCodigoPartido())
                ;

                //queda pendiente por cambio de presentacion de incio se procede a cambiar la informacion anterior
                //refResultados.setValue(partido);





            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.resultado_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(PulpoSingleton.getInstance().getResultadoAdapter());

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partidos = PulpoSingleton.getInstance().getPartidos();
                Log.d(Rutas.TAG, "El valor del OBJETO PARTIDOS ES RESULTADOFRAGMENT " + partidos.toString());


            }
        });

    }
}
