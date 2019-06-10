package com.cmc.pulpov1.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.activities.CalendarioActivity;
import com.cmc.pulpov1.entities.Partido;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.cmc.pulpov1.adapters.PartidoAdapter.ParseFecha;

public class PartidoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView tvHora;
    private TextView tvMinuto;
    private TextView tvEquipo1;
    private TextView tvEquipo2;
    private TextView tvCategoria;
    private TextView tvFecha;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfCompleto = new SimpleDateFormat("dd MMM yyyy");
    private Date nuevaFecha;


    public PartidoHolder(View itemView) {
        super(itemView);
        tvHora = itemView.findViewById(R.id.tvValorHora);
        tvMinuto = itemView.findViewById(R.id.tvValorMinuto);
        tvEquipo1 = itemView.findViewById(R.id.txEquipo1);
        tvEquipo2 = itemView.findViewById(R.id.txEquipo2);
        tvCategoria = itemView.findViewById(R.id.tvCategoria);
        tvFecha=itemView.findViewById(R.id.tvFecha);
        itemView.setOnClickListener(this);
    }

    public void bind(Partido p) {
       tvHora.setText(p.getHora());
        tvMinuto.setText(p.getMinuto());
        tvEquipo1.setText(p.getEquipoUno());
        tvEquipo2.setText(p.getEquipoDos());
        tvCategoria.setText(p.getCategoria());
       // nuevaFecha = sdf.parse();
        nuevaFecha=ParseFecha(p.getFecha().toString());
        Log.d(Rutas.TAG,"El valor de la fecha es  PartidoHolder "+nuevaFecha);

       // p.setFecha(nuevaFecha);
        tvFecha.setText(p.getFecha());
    }


    @Override
    public void onClick(View v) {
        int pos = getAdapterPosition();
        Log.d("PULPOLOG", "la posicion en el HOLDER es  " + pos + "PartidoHolder");

        Intent intent = new Intent(v.getContext(), CalendarioActivity.class);

        Partido partidoSeleccionado = PulpoSingleton.getInstance().getPartidos().get(pos);

        PulpoSingleton.getInstance().setPartido(partidoSeleccionado);
        v.getContext().startActivity(intent);


    }


}
