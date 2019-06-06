package com.cmc.pulpov1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Torneo;

import java.util.List;

public class TorneoAdapter extends ArrayAdapter<Torneo> {
    // se crea el contexto y el tipo de objeto que va a manejar el adapter
    private Context context;
    private List<Torneo> torneos;
    private TextView tvNombre;
    private TextView tvAnio;
    //se genera el constructor con el contexto se cabia los argumentos y se pone en context context y la lista
    //en supoer se aumenta el valor 0 al recurso

    public TorneoAdapter(Context context, List<Torneo> torneos) {
        super(context, 0, torneos);
        this.context = context;
        this.torneos = torneos;
    }
    //eliminar la anotacion de androidx
    //Eliminar las anotaciones @androidx.annotation.Nullable @androidx.annotation.NonNull

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewItem = convertView;
        if (viewItem == null)
            //Cambiar el inflate para el layout que se hace referencia
            viewItem = LayoutInflater.from(context).inflate(R.layout.item_torneo, parent, false);
        //Se setea la posicion del torneo actual
        Torneo torneoActual = torneos.get(position);

        //atar los componentes
        tvNombre=viewItem.findViewById(R.id.tvNombre);
        tvAnio=viewItem.findViewById(R.id.tvAnio);

        //Setear los valores de nombre y anio

        tvNombre.setText(torneoActual.getNombreTorneo());
        tvAnio.setText(torneoActual.getAnio()+"");

        return viewItem;
    }


}
