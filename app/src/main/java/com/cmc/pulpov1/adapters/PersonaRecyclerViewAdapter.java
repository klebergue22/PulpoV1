package com.cmc.pulpov1.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Persona;
import com.cmc.pulpov1.holder.PersonaHolder;

import java.util.List;

/**
 * Created by anupamchugh on 05/10/16.
 */

public class PersonaRecyclerViewAdapter extends RecyclerView.Adapter<PersonaHolder> {

    private List<Persona>personas;




    public PersonaRecyclerViewAdapter(List<Persona> personas) {
        this.personas = personas;
    }

    @NonNull
    @Override
    public PersonaHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.persona_item_rv, parent, false);
        return new PersonaHolder(view);
    }



    @Override
    public void onBindViewHolder(PersonaHolder personaHolder, int position) {
        personaHolder.bind(personas.get(position));

    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }
}
