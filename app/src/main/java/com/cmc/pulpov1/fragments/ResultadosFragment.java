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

import java.util.ArrayList;
import java.util.List;

public class ResultadosFragment extends Fragment {
    RecyclerView recyclerView;
    private List<Partido> partidos;
    private Button btnPrueba;




    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.resultados_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        partidos=new ArrayList<Partido>();

        recyclerView=(RecyclerView)view.findViewById(R.id.resultado_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(PulpoSingleton.getInstance().getResultadoAdapter());

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partidos = PulpoSingleton.getInstance().getPartidos();
                Log.d(Rutas.TAG,"El valor del OBJETO PARTIDOS ES "+partidos.toString());
                btnPrueba=(Button) view.findViewById(R.id.btnGuardarResultados);
                btnPrueba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


            }
        });

    }
}
