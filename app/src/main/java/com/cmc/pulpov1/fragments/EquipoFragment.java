package com.cmc.pulpov1.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.google.firebase.database.FirebaseDatabase;

public class EquipoFragment extends Fragment {
    private FirebaseDatabase database;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.equipo_fragment,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();

    Log.d("LogPulpo.TAG","se dispara onViewCreated EquipoFragment");

        recyclerView=(RecyclerView)view.findViewById(R.id.equipo_rv);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(PulpoSingleton.getInstance().getEquiposAdapter());







    }


}
