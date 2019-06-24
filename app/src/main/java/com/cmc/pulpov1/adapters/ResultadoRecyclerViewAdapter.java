package com.cmc.pulpov1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.Partido;
import com.cmc.pulpov1.holder.ResultadoHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ResultadoRecyclerViewAdapter extends RecyclerView.Adapter<ResultadoHolder> {
    private List<Partido> partidos;
    private Partido partidoActual;
    private Context context;
 //   private View.OnClickListener listener;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    public ResultadoRecyclerViewAdapter.ResultadoAdapterListener onClickListener;



    public ResultadoRecyclerViewAdapter(List<Partido> partidos,ResultadoAdapterListener listener) {

        this.partidos = partidos;
        this.onClickListener=listener;
    }

    @Override
    public ResultadoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resultado_item_rv, parent, false);
     //   view.setOnClickListener(this);

        return new ResultadoHolder(view,onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultadoHolder resultadoHolder, int position) {
        resultadoHolder.bind(partidos.get(position));
        final Partido partidoActual = PulpoSingleton.getInstance().getPartidos().get(position);

        Log.d("LogPulpo.TAG","Holder de resultados  "+partidos.size());
        Log.d("LogPulpo.TAG","INGRESA EN EL METODO ONCLICK RV DEL BOTON"+partidoActual.toString());
//        Log.d(Rutas.TAG,"El valor del numero de partido es "+PulpoSingleton.getInstance().getNumeroFechaP().toString());
        database = FirebaseDatabase.getInstance();
       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference refResultado = database.
                getReference(Rutas.CALENDARIO).
                child(Rutas.ROOT_TORNEOS).
                child(PulpoSingleton.getInstance().getCodigoTorneo()).
                child(Rutas.CATEGORIAS).
                child(PulpoSingleton.getInstance().getCodigoCategoria()).
                child(Rutas.FECHA);

        if (partidoActual != null) {
            partidos.add(partidoActual);
            PulpoSingleton.getInstance().setPartidos(partidos);

            refResultado.setValue(partidoActual);
            Log.d(Rutas.TAG,"eL VALOR FUE ACTUALIZADO***********" +    partidoActual.toString()+                "");
        } else {
            Toast.makeText(context, "El partido no se pudo actualizar", Toast.LENGTH_LONG).show();
        }*/


    }




    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return partidos.size();
    }






    public interface ResultadoAdapterListener {

        void guardarResultado(View v, int position, int puntosEquipo1, int puntosEquipo2);


    }

}