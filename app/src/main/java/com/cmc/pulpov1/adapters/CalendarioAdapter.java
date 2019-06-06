package com.cmc.pulpov1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Equipo;

import java.util.List;

public class CalendarioAdapter extends ArrayAdapter<Equipo> {

    private TextView tvEquipo1;
    private TextView tvEquipo2;
        private Context context;
    private List<Equipo> equipos;


    public CalendarioAdapter(Context context, List<Equipo> equipos) {
        super(context, 0, equipos);
        this.context = context;
        this.equipos = equipos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewItem = convertView;
        if (viewItem == null)
            viewItem = LayoutInflater.from(context).inflate(R.layout.item_calendario, parent, false);
        Equipo equipoActual = equipos.get(position);
        tvEquipo1 = viewItem.findViewById(R.id.tvEquipo);
        tvEquipo1.setText(equipoActual.getNombreEquipo());


        return viewItem;
    }


    @Override
    public Equipo getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
