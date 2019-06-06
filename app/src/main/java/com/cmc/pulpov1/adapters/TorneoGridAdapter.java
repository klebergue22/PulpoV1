package com.cmc.pulpov1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.activities.GlideApp;
import com.cmc.pulpov1.entities.Torneo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class TorneoGridAdapter extends BaseAdapter {

    //Se crea el contexto y el timp de objeto que va a manejar el adapter
    private Context context;
    private List<Torneo> torneos;
    private TextView tvNombre;
    private TextView tvAnio;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    private ImageView imgView;

    public TorneoGridAdapter(Context context, List<Torneo> torneos) {
        this.context = context;
        this.torneos = torneos;
        storage=FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("torneos");


    }

    @Override
    public int getCount() {
        return torneos.size();
    }

    @Override
    public Object getItem(int position) {
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
            //Cambiar el inflate para el layout que se hace referencia
            viewItem = LayoutInflater.from(context).inflate(R.layout.torneo_item_grid, parent, false);
        //Se setea la posicion del torneo actual
        Torneo torneoActual = torneos.get(position);

        //atar los componentes
        tvNombre=viewItem.findViewById(R.id.tvNombreG);
        tvAnio=viewItem.findViewById(R.id.tvAnioG);
        imgView=viewItem.findViewById(R.id.imgView);

        //Setear los valores de nombre y anio


        imageReference=storageReference.child(torneoActual.getId());

        tvNombre.setText(torneoActual.getNombreTorneo());
        tvAnio.setText(torneoActual.getAnio()+"");
        GlideApp.with(context /* context */)
                .load(imageReference).error(R.drawable.pulpologo)
                .into(imgView);

        return viewItem;
    }
}
