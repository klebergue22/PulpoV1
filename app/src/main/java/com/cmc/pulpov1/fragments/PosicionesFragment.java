package com.cmc.pulpov1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmc.pulpov1.R;

public class PosicionesFragment extends Fragment {

    private TextView tV04;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.posiciones_fragment, container, false);
        tV04=view.findViewById(R.id.tV04);
        return view;
    }
}
