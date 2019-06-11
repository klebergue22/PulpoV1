package com.cmc.pulpov1.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.adapters.PartidosAdapter;
import com.cmc.pulpov1.entities.Partido;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ListaPartidosActivity extends AppCompatActivity {
    private ListView lvPartidos;
    private Button btnAgregar;
    private ConstraintLayout constraintLayout;
    private ArrayAdapter<Partido> adpPartido;
    private List<Partido> partidos;
    private String numFecha;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private Context context;
    private static DatabaseReference partidosDBReference;
    private Partido partido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_partidos);
        database = FirebaseDatabase.getInstance();
        partidos = new ArrayList<Partido>();


        Intent intent = getIntent();
        numFecha = intent.getStringExtra("numFecha");

        Log.d(Rutas.TAG, "El valor de la numero de fecha es la siguiente antes de enviar " + numFecha);
        Toast.makeText(this, "El valor de la fecha seleccionada es " + numFecha, Toast.LENGTH_LONG).show();

        atarComponentes();

        desplazarPantalla();
        setPartidosFirabaseListener();

        adpPartido = new PartidosAdapter(getApplicationContext(), partidos);
        lvPartidos.setAdapter(adpPartido);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navFecha();

            }
        });
    }

    public void atarComponentes() {
        constraintLayout = findViewById(R.id.rootview);
        lvPartidos = findViewById(R.id.lvPartidos);
        btnAgregar = findViewById(R.id.btnAgregarPartidos);

    }

    private void desplazarPantalla() {
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    //Toast.makeText(MainActivity.this,"Keyboard is showing",Toast.LENGTH_LONG).show();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Subir la línea de guía
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.001f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                } else {
                    //Toast.makeText(MainActivity.this,"keyboard closed",Toast.LENGTH_LONG).show();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Regresar la línea de guía a la posición original
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.2f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        });
    }

    public void navFecha() {
        Intent intent = new Intent(this, CalendarioActivity.class);
        intent.putExtra("numFecha", numFecha);
        startActivity(intent);
    }

    public void setPartidosFirabaseListener() {


       // Log.d(Rutas.TAG, "El valor del codigo del torneo es SetPArtidoFirebase:::: " + codigoTorneo);
       // Log.d(Rutas.TAG, "*************El valor del codigo del numero de fecha es SetPArtidoFirebase:::: " + numeroFechaP);
        //Log.d(Rutas.TAG, "El valor del codigo del partido es SetPArtidoFirebase:::: " + codigoPartido);
        //1.-Referencia al arbol
        partidosDBReference = database.
                getReference(Rutas.CALENDARIO).
                child(Rutas.ROOT_TORNEOS).
                child(PulpoSingleton.getInstance().getCodigoTorneo())
                .child(PulpoSingleton.getInstance().getNumeroFechaP())
        ;


        Log.d("PULPOLOG", "PATH en Partidos>>" + partidosDBReference.getPath());
        //2.-Crear el listener
        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("PULPOLOG", "onChildAdded de partido ....... datasnapshot" + dataSnapshot);
                partido = dataSnapshot.getValue(Partido.class);
                partidos.add(partido);
                adpPartido.notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PULPOLOG", "Se borra Partido " + dataSnapshot.getKey());
                int posicionRepetido = -1;
                for (int i = 0; i < partidos.size(); i++) {
                    //se compara contra el id
                    if (partidos.get(i).getId().equals(dataSnapshot.getKey())) {
                        posicionRepetido = i;
                    }
                }
                if (posicionRepetido != -1) {
                    partidos.remove(posicionRepetido);
                    adpPartido.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        //Se descomenta el reotno del paso 3.- del listener
        partidosDBReference.addChildEventListener(childEventListener);

    }




}
