package com.cmc.pulpov1.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.activities.CalendarioActivity;
import com.cmc.pulpov1.entities.Partido;

public class PartidoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView tvHora;
    private TextView tvMinuto;
    private TextView tvEquipo1;
    private TextView tvEquipo2;


    public PartidoHolder(View itemView) {
        super(itemView);
        tvHora = itemView.findViewById(R.id.tvValorHora);
        tvMinuto = itemView.findViewById(R.id.tvValorMinuto);
        tvEquipo1 = itemView.findViewById(R.id.txEquipo1);
        tvEquipo2 = itemView.findViewById(R.id.txEquipo2);
        itemView.setOnClickListener(this);
    }

    public void bind(Partido p) {
       tvHora.setText(p.getHora());
        tvMinuto.setText(p.getMinuto());
        tvEquipo1.setText(p.getEquipoUno());
        tvEquipo2.setText(p.getEquipoDos());
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
