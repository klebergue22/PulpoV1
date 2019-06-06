package com.cmc.pulpov1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cmc.pulpov1.IdentificadorUtils;
import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Jugador;
import com.cmc.pulpov1.entities.Rol;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

public class JugadorAdapter extends ArrayAdapter<Jugador> {
    private Context context;
    private List<Jugador> jugadores;
    private TextView tvNombreJugador;
    private TextView tvApellidoJugador;
    private TextView tvEstado;
    private Button btnAprobar;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private String mailC;

    public JugadorAdapter(Context context, List<Jugador> jugadores) {
        super(context, 0, jugadores);
        this.context = context;
        this.jugadores = jugadores;
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public int getCount() {
        return jugadores.size();
    }

    @Override
    public Jugador getItem(int position) {
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
            viewItem = LayoutInflater.from(context).inflate(R.layout.item_jugador, parent, false);
            final Jugador jugadorActual = jugadores.get(position);
            tvNombreJugador = viewItem.findViewById(R.id.tvNombreJugador);
            tvApellidoJugador = viewItem.findViewById(R.id.tvApellidoJugador);
            tvEstado = viewItem.findViewById(R.id.tvEstado);

            btnAprobar = viewItem.findViewById(R.id.btnAprobar);
            tvNombreJugador.setText(jugadorActual.getPrimerNombre());
            tvApellidoJugador.setText(jugadorActual.getPrimerApellido());
            tvEstado.setText(jugadorActual.getEstado());


            Map<String, Rol> roles = PulpoSingleton.getInstance().getUsuarioLogueado().getRoles();

            if (roles==null) {
                btnAprobar.setVisibility(View.INVISIBLE);

            } else {

                for (String clave : roles.keySet()) {
                    Rol rol = roles.get(clave);
                    if ((rol != null) && ((jugadorActual.getEstado().equals("A"))
                            && (rol.getIdRol().equals("2"))
                            && (rol.getIdTorneo().equals(PulpoSingleton.getInstance().getCodigoTorneo())))) {
                        btnAprobar.setVisibility(View.INVISIBLE);
                    } else {
                        btnAprobar.setVisibility(View.VISIBLE);
                    }
                }
            }
            btnAprobar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PulpoSingleton.getInstance().setJugador(jugadorActual);
                    mailC=IdentificadorUtils.crearIdentificacionMail(jugadorActual.getMailJugador());
                    PulpoSingleton.getInstance().setMail(mailC);
                    Log.d("PULPOLOG", "EL JUGADOR ES **** "+jugadorActual.getMailJugador());
                    Log.d("PULPOLOG", "EL Mail es  **** "+mailC);
                    Log.d("PULPOLOG", "EL VALOR DEL PATH ES " + Rutas.ROOT_TORNEOS + PulpoSingleton.getInstance().getCodigoTorneo()
                            + Rutas.CATEGORIAS + PulpoSingleton.getInstance().getCodigoCategoria() + Rutas.EQUIPOS + PulpoSingleton.getInstance().getCodigoEquipo() +
                            Rutas.MIEMBROS + PulpoSingleton.getInstance().getMail());
                    aprobarJugador();


                }
            });
        }
        return viewItem;
    }

    private void aprobarJugador() {
        //1.-Referencia al arbol
        DatabaseReference refJugador = database.getReference(Rutas.ROOT_TORNEOS).child(PulpoSingleton.getInstance().getCodigoTorneo())
                .child(Rutas.CATEGORIAS).child(PulpoSingleton.getInstance().getCodigoCategoria())
                .child(Rutas.EQUIPOS).child(PulpoSingleton.getInstance().getCodigoEquipo())
                .child(Rutas.MIEMBROS).child(PulpoSingleton.getInstance().getMail()).child(Rutas.ESTADO);

        refJugador.setValue("A");



        //2.-Crear el listener
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        refJugador.addChildEventListener(childEventListener);

        //ubicarse en el estado del jugador (dentro de miembros del equipo)
        //cambiar el estado a A



    }
}
