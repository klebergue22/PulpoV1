package com.cmc.pulpov1.holder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmc.pulpov1.R;
import com.cmc.pulpov1.adapters.ResultadoRecyclerViewAdapter;
import com.cmc.pulpov1.entities.Partido;

public class ResultadoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private TextView tvEquipo1;
    private TextView tvEquipo2;
    private EditText tvPtosE1;
    private EditText tvPtosE2;
    private Button btnGuardarResultado;
    private View.OnClickListener listener;
    public ResultadoRecyclerViewAdapter.ResultadoAdapterListener onClickListener;



    public ResultadoHolder(View itemView, ResultadoRecyclerViewAdapter.ResultadoAdapterListener listener) {
        super(itemView);

        tvEquipo1 = itemView.findViewById(R.id.nombreEq01);
        tvEquipo2 = itemView.findViewById(R.id.nombreEq02);
        tvPtosE1=itemView.findViewById(R.id.txtPtosE01);
        tvPtosE2=itemView.findViewById(R.id.txtPtosE02);
        btnGuardarResultado=itemView.findViewById(R.id.btnGuardarResultados);
        this.onClickListener=listener;
        //itemView.setOnClickListener(this);


        btnGuardarResultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.guardarResultado(v,getAdapterPosition(),Integer.parseInt(tvPtosE1.getText().toString()),Integer.parseInt(tvPtosE2.getText().toString()));
            }
        });
    }

    public void bind(Partido p) {
        Log.d("PULPOLOG", "vALOR DEL PARTIDO" + p);


        tvEquipo1.setText(p.getEquipoUno());
        tvEquipo2.setText(p.getEquipoDos());
        tvPtosE1.setText(p.getPuntosEquipoUno());
        tvPtosE2.setText(p.getPuntosEquiDos());


    }


    @Override
    public void onClick(View v) {


    }
}
