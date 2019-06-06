package com.cmc.pulpov1.holder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Persona;

public class PersonaHolder extends RecyclerView.ViewHolder {
    private TextView tvN;
    private TextView tvA;


    public PersonaHolder(View itemView){
        super(itemView);
        tvN=itemView.findViewById(R.id.tVNombre);
        tvA=itemView.findViewById(R.id.tVApellido);
    }

    public void bind(Persona p){
        tvN.setText(p.getNombre());
        tvA.setText(p.getApellido());
    }

}
