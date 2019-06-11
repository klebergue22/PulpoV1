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

public class PartidosAdapter extends ArrayAdapter<Partido> {
    private  Context context;
    private List<Partido> partidosP ;
    private TextView tvEquipo1P;
    private TextView tvEquipo2P;
    private TextView tvCategoriaP;
    private TextView tvFechaP;
    private TextView tvHoraP;
    private TextView tvMinutoP;




    public PartidosAdapter(Context context, List<Partido> partidos) {
        super(context,0, partidos);
        this.context = context;
        this.partidosP = partidos;

    }




    @Override
    public int getCount() {
        return partidosP.size();
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
        if (viewItem == null) {
            viewItem = LayoutInflater.from(context).inflate(R.layout.partidos_item, parent, false);
            Partido partidoPactual = partidosP.get(position);

            tvEquipo1P = viewItem.findViewById(R.id.txEquipo1Fecha);
            tvEquipo1P.setText(partidoPactual.getEquipoUno());
            tvEquipo2P = viewItem.findViewById(R.id.txEquipo2Fecha);
            tvEquipo2P.setText(partidoPactual.getEquipoDos());
            tvFechaP = viewItem.findViewById(R.id.tvFechaFe);
            tvFechaP.setText(partidoPactual.getFecha());
            tvHoraP = viewItem.findViewById(R.id.tvValorHoraFecha);
            tvHoraP.setText(partidoPactual.getHora());
            tvMinutoP = viewItem.findViewById(R.id.tvValorMinutoFecha);
            tvMinutoP.setText(partidoPactual.getMinuto());
            tvCategoriaP = viewItem.findViewById(R.id.tvCategoriaFechas);
            tvCategoriaP.setText(partidoPactual.getCategoria());
        }
        return viewItem;
    }


}
