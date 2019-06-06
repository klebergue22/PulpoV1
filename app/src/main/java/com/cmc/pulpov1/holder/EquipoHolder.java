package com.cmc.pulpov1.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.activities.ListaJugadoresActivity;
import com.cmc.pulpov1.entities.Equipo;

public class EquipoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView tvN;
    private TextView tvCat;


    public EquipoHolder(View itemView) {
        super(itemView);
        tvN = itemView.findViewById(R.id.tvNombreEq);
        tvCat = itemView.findViewById(R.id.tvCategoriaEq);
        itemView.setOnClickListener(this);


    }

    public void bind(Equipo e) {
        tvN.setText(e.getNombreEquipo());
        tvCat.setText(e.getCategoria());
    }


    @Override
    public void onClick(View v) {
        int pos = getAdapterPosition();
        Log.d("PULPOLOG", "la posicion es  " + pos+"EquipoHolder");
   Intent intent = new Intent(v.getContext(), ListaJugadoresActivity.class);

        Equipo equipoSeleccionado=PulpoSingleton.getInstance().getEquipos().get(pos);

        PulpoSingleton.getInstance().setNombreEquipo(equipoSeleccionado.getNombreEquipo());
        PulpoSingleton.getInstance().setCodigoEquipo(equipoSeleccionado.getId());

        v.getContext().startActivity(intent);

    }


}
