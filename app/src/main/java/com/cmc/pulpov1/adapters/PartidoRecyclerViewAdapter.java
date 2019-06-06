package com.cmc.pulpov1.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Partido;
import com.cmc.pulpov1.holder.PartidoHolder;

import java.util.List;

public class PartidoRecyclerViewAdapter extends RecyclerView.Adapter<PartidoHolder> {
    private List<Partido> partidos;
    public PartidoRecyclerViewAdapter(List<Partido> partidos) {
        this.partidos = partidos;
    }

    @Override
    public PartidoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.partido_item_rv, parent, false);
        return new PartidoHolder(view);
    }

    @Override
    public void onBindViewHolder(PartidoHolder partidoHolderHolder, int position) {
        partidoHolderHolder.bind(partidos.get(position));

        Log.d("PULPOLOG","onBindViewHolder "+partidos.size());

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return partidos.size();
    }



}