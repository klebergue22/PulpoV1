package com.cmc.pulpov1.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.adapters.PersonaRecyclerViewAdapter;
import com.cmc.pulpov1.entities.Persona;

import java.util.ArrayList;
import java.util.List;

public class PersonaFragment extends Fragment {
    private List<Persona> personas;
    private Persona persona1;
    private Persona persona2;
    private Persona persona3;
    private Button btnRegistroFecha;


    RecyclerView recyclerView;


    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.persona_fragment,container,false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //personas quemadas
        persona1=new Persona("Jorge","Lopez","1715112395");
        persona2=new Persona("Lisa","Guerra","1715195395");
        persona3=new Persona("Mario","Galarza","1711525385");
        //instancio y agrego las personas
        personas=new ArrayList<>();
        personas.add(persona1);
        personas.add(persona2);
        personas.add(persona3);
        PersonaRecyclerViewAdapter adapter=new PersonaRecyclerViewAdapter(personas);
        recyclerView= (RecyclerView)view.findViewById(R.id.persona_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);




    }


}
