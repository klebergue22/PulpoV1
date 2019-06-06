package com.cmc.pulpov1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.adapters.JugadorAdapter;
import com.cmc.pulpov1.entities.Jugador;
import com.cmc.pulpov1.entities.Torneo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListaJugadoresActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private ArrayAdapter<Jugador> adpJ;
    private List<Jugador> jugadores;
    private ListView lvJugadores;


    private Button btncrearJugador;
    private Button btnSerParteEquipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jugadores);
        database = FirebaseDatabase.getInstance();
        atarComponentes();
        escucharJugadores();
        // tomarPosicion();

        //Log.d("PULPOLOG", "vALOR DEL MAIL EN EL SINGLETON" + PulpoSingleton.getInstance().getMailN());


        jugadores = new ArrayList<Jugador>();

        btncrearJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navCrearTorneo();
            }
        });
        btnSerParteEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navSerParteEquipo();

            }
        });
        adpJ = new JugadorAdapter(this,jugadores);
        lvJugadores.setAdapter(adpJ);


    }

    private void navCrearTorneo() {
        Intent intent = new Intent(this, GestionTorneoActivity.class);
        startActivity(intent);
    }

    public void navSerParteEquipo() {
        Intent intent = new Intent(this, PertenecerEquipoActivity.class);
        startActivity(intent);
    }


    private void atarComponentes() {
        lvJugadores = findViewById(R.id.lvJugador);
        btncrearJugador = findViewById(R.id.btnCrearJugador);
        btnSerParteEquipo = findViewById(R.id.btnSerParteEquipo);


    }

    private void escucharJugadores() {
        //1.-Referencia al arbol
        DatabaseReference refJugadores = database.getReference(Rutas.ROOT_TORNEOS).child(PulpoSingleton.getInstance()
                                                 .getCodigoTorneo())
                                                 .child(Rutas.CATEGORIAS)
                                                 .child(PulpoSingleton.getInstance().getCodigoCategoria())
                                                 .child(Rutas.EQUIPOS).child(PulpoSingleton.getInstance().getCodigoEquipo())
                .child(Rutas.MIEMBROS);;

        Log.d("PULPOLOG","Valor del la Categoria en el  Path"+PulpoSingleton.getInstance().getCodigoCategoria());

        Log.d("PULPOLOG","Valor del Path"+Rutas.ROOT_TORNEOS
                                                   +PulpoSingleton.getInstance().getCodigoTorneo()+Rutas.CATEGORIAS+PulpoSingleton.getInstance().getCodigoCategoria()+Rutas.EQUIPOS+PulpoSingleton.getInstance().getCodigoEquipo());

        //2.-Crear el listener
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             //   Log.d("PULPOLOG", "Se agrega Jugador " + dataSnapshot.getKey());
                boolean repetido = false;
                for (Jugador jugador : jugadores) {
                    if (jugador.equals(dataSnapshot.getKey())) {
                        repetido = true;
                    }
                }
                if (!repetido) {
                    Jugador  jugador=dataSnapshot.getValue(Jugador.class);
                    jugadores.add(jugador);
                    adpJ.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            //    Log.d("PULPOLOG", "Se borra jugador " + dataSnapshot.getKey());
                int posicionRepetido = -1;
                for (int i = 0; i < jugadores.size(); i++) {
                    if (jugadores.get(i).equals(dataSnapshot.getKey())) {
                        posicionRepetido = i;
                    }
                }
                if (posicionRepetido != -1) {
                    jugadores.remove(posicionRepetido);
                    adpJ.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refJugadores.addChildEventListener(childEventListener);
    }

  /* private void tomarPosicion(){
        lvJugadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //jugadorSeleccionado=jugadores.get(position);
            }
        });
    }*/


}
