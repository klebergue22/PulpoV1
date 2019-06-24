package com.cmc.pulpov1.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.activities.GlideApp;
import com.cmc.pulpov1.entities.Equipo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EquipoAdapter extends ArrayAdapter<Equipo> {
    //SE crea el contexto y el tipo de objeto que va a manejar el adapter
    private Context context;
    private List<Equipo> equipos;
    private TextView tvNombre;
    private TextView tvCategoria;
    private TextView tvHora;
    private TextView tvMin;
    private ImageView iV2;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageReference;
    //Genero el constructor contexto cambio argumento y


    public EquipoAdapter(Context context, List<Equipo> equipos) {
        super(context, 0, equipos);
       equipos=new ArrayList<Equipo>();
       equipos= PulpoSingleton.getInstance().getEquipos();
        this.context = context;
        this.equipos = equipos;

        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("equipos");
        Log.d("LogPulpo.TAG","CONSTURCTOR DEL ADAPTER EL VALOR DE LOS EQUIPOS ES "+equipos.size());

    }

    @Override
    public int getCount() {

        return equipos.size();

    }
//eliminar la anotacion de androidx
    //Eliminar las anotaciones @androidx.annotation.Nullable @androidx.annotation.NonNull

    @Override
    public Equipo getItem(int position) {
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
            viewItem = LayoutInflater.from(context).inflate(R.layout.item_equipo, parent, false);
        Log.d("LogPulpo.TAG","eL VALOR DEL GETVIEW EQUIPOS"+equipos.size());
        //Se setea la posicion del equipo actual
        Equipo equipoActual = equipos.get(position);

        //Atar los componentes
        tvNombre = viewItem.findViewById(R.id.tvNombreEq);
        tvCategoria = viewItem.findViewById(R.id.tvCategoriaEq);
       // iV2 = viewItem.findViewById(R.id.iV2);


        imageReference = storageReference.child(equipoActual.getId());
        //Se setea los valores del nombre y categorìa
        tvCategoria.setText(equipoActual.getCategoria());
        tvNombre.setText(equipoActual.getNombreEquipo());
        GlideApp.with(context /* context */)
                .load(imageReference).error(R.drawable.pulpologo)
                .into(iV2);
        return viewItem;
    }
}
