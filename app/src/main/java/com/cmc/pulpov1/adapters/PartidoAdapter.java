package com.cmc.pulpov1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Partido;

import java.util.List;

public class PartidoAdapter extends ArrayAdapter<Partido> {

    private Context context;
    private List<Partido> partidos;
    private TextView tvEquipo1;
    private TextView tvEquipo2;
    private TextView tvHora;
    private TextView tvMinuto;




    public PartidoAdapter(Context context, List<Partido> partidos) {
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
        Partido partidoActual = partidos.get(position);
        tvEquipo1 = viewItem.findViewById(R.id.txEquipo1);
        tvEquipo1.setText(partidoActual.getEquipoUno());
        tvEquipo2 = viewItem.findViewById(R.id.txEquipo2);
        tvEquipo2.setText(partidoActual.getEquipoDos());
        tvHora=viewItem.findViewById(R.id.tvValorHora);
        tvHora.setText(partidoActual.getHora());
        tvMinuto=viewItem.findViewById(R.id.tvValorMinuto);
        tvMinuto.setText(partidoActual.getMinuto());
        return viewItem;
    }





}
