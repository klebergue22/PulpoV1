package com.cmc.pulpov1.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Equipo;
import com.cmc.pulpov1.holder.EquipoHolder;

import java.util.List;

public class EquipoRecyclerViewAdapter extends RecyclerView.Adapter<EquipoHolder> {
    private List<Equipo> equipos;
    public EquipoRecyclerViewAdapter(List<Equipo> equipos) {

        this.equipos = equipos;
    }

    @Override
    public EquipoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.equipo_item_rv,parent,false);
      return new EquipoHolder(view);

    }

    @Override
    public void onBindViewHolder(EquipoHolder equipoHolder, int position) {
        equipoHolder.bind(equipos.get(position));

    /*    equipoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListaJugadoresActivity.class);
                view.getContext().startActivity(intent);
            }
        });*/

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return equipos.size();
    }
}















