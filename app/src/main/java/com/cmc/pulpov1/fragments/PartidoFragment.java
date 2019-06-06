package com.cmc.pulpov1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Partido;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;


public class PartidoFragment extends Fragment {
    private FirebaseDatabase database;
    RecyclerView recyclerView;
    private List<Partido> partidos;
    private Partido p1;
    private Partido p2;
    private Partido p3;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.partido_fragment, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date fecha=new Date();
        p1=(new Partido("E1","E2","0","0","14","05","eq1_eq2"));
      //  partidos=new ArrayList<Partido>();
     //   partidos.add(p1);
      // PartidoRecyclerViewAdapter adapter =new PartidoRecyclerViewAdapter(PulpoSingleton.getInstance().getPartidos());
        recyclerView = (RecyclerView) view.findViewById(R.id.partido_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(PulpoSingleton.getInstance().getPartidoAdapter());



    }
}
