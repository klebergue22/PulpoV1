package com.cmc.pulpov1.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Partido;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PartidoAdapter extends ArrayAdapter<Partido> {

    private Context context;
    private List<Partido> partidos;
    private TextView tvEquipo1;
    private TextView tvEquipo2;
    private TextView tvHora;
    private TextView tvMinuto;
    private TextView tvFecha;
    private TextView tvCategoria;
    private Date fechaI;


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
        tvHora = viewItem.findViewById(R.id.tvValorHora);
        tvHora.setText(partidoActual.getHora());
        tvMinuto = viewItem.findViewById(R.id.tvValorMinuto);
        tvMinuto.setText(partidoActual.getMinuto());
        tvFecha = viewItem.findViewById(R.id.tvFecha);
        fechaI=ParseFecha(partidoActual.getFecha());
        Log.d(Rutas.TAG,"El valor de la fecha I es "+fechaI);





        tvMinuto.setText(partidoActual.getFecha());
        tvCategoria = viewItem.findViewById(R.id.tvCategoria);
        tvCategoria.setText(partidoActual.getCategoria());

        return viewItem;
    }

    public static Date ParseFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
            Log.d(Rutas.TAG, " fecha es  PArse fecha " + fechaDate);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return fechaDate;
    }


}
