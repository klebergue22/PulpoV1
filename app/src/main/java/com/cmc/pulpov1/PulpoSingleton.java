package com.cmc.pulpov1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.cmc.pulpov1.adapters.EquipoRecyclerViewAdapter;
import com.cmc.pulpov1.adapters.PartidoRecyclerViewAdapter;
import com.cmc.pulpov1.adapters.PartidosAdapter;
import com.cmc.pulpov1.adapters.ResultadoRecyclerViewAdapter;
import com.cmc.pulpov1.entities.Equipo;
import com.cmc.pulpov1.entities.Fecha;
import com.cmc.pulpov1.entities.Jugador;
import com.cmc.pulpov1.entities.Partido;
import com.cmc.pulpov1.entities.Rol;
import com.cmc.pulpov1.entities.UsuarioRol;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PulpoSingleton {

    private static PulpoSingleton instancia;

    private static FirebaseDatabase database;
    private String codigoTorneo;
    private String codigoCategoria;
    private String codigoEquipo;
    private String codigoPartido;
    private String mail;
    private String mailN;
    private String nombreTorneo;
    private String nombreEquipo;
    private String categoria;
    private String numeroFechaP = null;
    private boolean estadoCedula;
    private List<Equipo> equipos;
    private List<Partido> partidos;
    private List<Rol> roles;
    private List<Fecha> fechas;
    private Jugador jugador;
    private Partido partido;
    private List<Jugador>jugadores;
    private static Context context;
    private String tipo;


    private List<Partido> resultadoPartido;


    private static DatabaseReference equiposDBReference;
    private static DatabaseReference partidosDBReference;
    private static DatabaseReference resultadosDBReference;
    private EquipoRecyclerViewAdapter equiposAdapter;
    private PartidoRecyclerViewAdapter partidoAdapter;
    private ResultadoRecyclerViewAdapter resultadoAdapter;
    private PartidosAdapter partidosAdapter;
    private UsuarioRol usuarioLogueado;

    public static PulpoSingleton getInstance() {
        if (instancia == null) {
            instancia = new PulpoSingleton();
            database = FirebaseDatabase.getInstance();


        }
        return instancia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public boolean isEstadoCedula() {
        return estadoCedula;
    }

    public void setEstadoCedula(boolean estadoCedula) {
        this.estadoCedula = estadoCedula;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {

        this.context = context.getApplicationContext();
    }

    public PartidosAdapter getPartidosAdapter() {
        return partidosAdapter;
    }

    public void setPartidosAdapter(PartidosAdapter partidosAdapter) {
        this.partidosAdapter = partidosAdapter;
    }

    public String getNumeroFechaP() {

        return numeroFechaP;
    }

    public List<Fecha> getFechas() {
        return fechas;
    }

    public void setFechas(List<Fecha> fechas) {
        this.fechas = fechas;
    }

    public void setNumeroFechaP(String numeroFechaP) {
        this.numeroFechaP = numeroFechaP;
    }

    public String getCodigoPartido() {
        return codigoPartido;
    }

    public void setCodigoPartido(String codigoPartido) {
        this.codigoPartido = codigoPartido;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public String getMailN() {
        return mailN;
    }

    public void setMailN(String mailN) {
        this.mailN = mailN;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public void setCodigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
    }

    public Jugador getJugador() {

        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {

        this.categoria = categoria;
    }

    public UsuarioRol getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(UsuarioRol usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    private PulpoSingleton() {

    }

    public String getCodigoTorneo() {
        return codigoTorneo;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {

        if (!mail.equals(this.mail)) {
            this.mail = mail;
            Log.v("PULPOLOG", "Mail__codigo" + mail);

        }
    }

    public void setCodigoTorneo(String codigoTorneo) {
        //  Log.v("PULPOLOG", "codigo torneo" + codigoTorneo);
        if (!codigoTorneo.equals(this.codigoTorneo)) {
            this.codigoTorneo = codigoTorneo;
            codigoCategoria = null;
        }
    }

    public void setCodigoCategoria(final String codigoCategoria) {
        // Log.v("PULPOLOG", "codigo categoria" + codigoCategoria);
        if (!codigoCategoria.equals(this.codigoCategoria)) {
            this.codigoCategoria = codigoCategoria;
            equipos = new ArrayList<Equipo>();
            partidos = new ArrayList<Partido>();
            resultadoPartido = new ArrayList<Partido>();

            partidoAdapter = new PartidoRecyclerViewAdapter(partidos);
            equiposAdapter = new EquipoRecyclerViewAdapter(equipos);
            //  partidosAdapter = new PartidosAdapter(context, partidos);
            resultadoAdapter = new ResultadoRecyclerViewAdapter(partidos, new ResultadoRecyclerViewAdapter.ResultadoAdapterListener() {
                @Override
                public void guardarResultado(View v, int position, int puntosEquipo1, int intPuntosEquipo2) {
                    Log.d("PULPOLOG", "SMO llama al boton!!");
                    //1. Con la posicion recuperar el partido de la lista de partidos
                    partido = partidos.get(position);
                    Log.d(Rutas.TAG, "El partido recuperado es " + partido.toString());
                    //2. ACTUALIZAR (NO INSERTAR!!!! ACTUALIZAR) los puntos del equipo1 y del equipo2 del equipo seleccionado
                    // --- Armar la ruta hasta llegar al valor que se quiere modificar osea hasta el atributo que guarda los puntos de cada equipo
                    Log.d(Rutas.TAG, "El valor del codigoTorneo es " + codigoTorneo);
                    resultadosDBReference = database.
                            getReference(Rutas.CALENDARIO).
                            child(Rutas.ROOT_TORNEOS).
                            child(codigoTorneo).
                            child(Rutas.CATEGORIAS).
                            child(codigoCategoria).
                            child(Rutas.FECHA).
                            child(partido.getId().toString());


                    Log.d(Rutas.TAG, "El valor de la ruta es " + resultadosDBReference.toString());


                    // -- Para armar la ruta, se tiene todo lo necesario en el objeto partido de la lista, como se tiene la posición elegida, se puede recuperar el partido
                    // -- Con los datos del partido se ARMA correctamente la ruta y luego se MODIFICA solo los dos atributos que cambian usando los valores que llegan en el método

                    partido.setPuntosEquipoUno(Integer.toString(puntosEquipo1));
                    partido.setPuntosEquiDos(Integer.toString(intPuntosEquipo2));

                    Log.d(Rutas.TAG, "El partido seteado nuevo es ****" + partido.toString());
                    resultadosDBReference.setValue(partido);


                }


            });

            setEquiposFirabaseListener();
            setPartidoFirabaseListener();
            //   setResultadosFirabaseListener();

        }
    }


    public void setRoles(Rol rol) {
        Log.d("PULPOLOG", "rol guardado" + rol);

        roles.add(rol);

    }


    public EquipoRecyclerViewAdapter getEquiposAdapter() {
        return equiposAdapter;
    }

    public PartidoRecyclerViewAdapter getPartidoAdapter() {
        return partidoAdapter;
    }

    public void setPartidoAdapter(PartidoRecyclerViewAdapter partidoAdapter) {
        this.partidoAdapter = partidoAdapter;
    }

    public ResultadoRecyclerViewAdapter getResultadoAdapter() {
        return resultadoAdapter;
    }

    public void setResultadoAdapter(ResultadoRecyclerViewAdapter resultadoAdapter) {
        this.resultadoAdapter = resultadoAdapter;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public List<Rol> getRoles() {
        return roles;

    }

    public void setEquiposFirabaseListener() {
        //1.-Referencia al arbol
        equiposDBReference = database.
                getReference(Rutas.ROOT_TORNEOS).
                child(codigoTorneo).
                child(Rutas.CATEGORIAS).
                child(codigoCategoria).
                child(Rutas.EQUIPOS);

        //2.-Crear el listener
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("PULPOLOG", "childAdded" + dataSnapshot.getKey());
                boolean repetido = false;
                for (Equipo equipo : equipos) {
                    //SE COMPARA CONTRA EL ID  Para ver si es repetido
                    if (equipo.getId().equals(dataSnapshot.getKey())) {
                        repetido = true;
                    }
                }
                if (!repetido) {
                    //Se recupera el objeto completo
                    Equipo equipo = dataSnapshot.getValue(Equipo.class);
                    equipos.add(equipo);
                    equiposAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PULPOLOG", "Se borra equipo " + dataSnapshot.getKey());
                int posicionRepetido = -1;
                for (int i = 0; i < equipos.size(); i++) {
                    //se compara contra el id
                    if (equipos.get(i).getId().equals(dataSnapshot.getKey())) {
                        posicionRepetido = i;
                    }
                }
                if (posicionRepetido != -1) {
                    equipos.remove(posicionRepetido);
                    equiposAdapter.notifyDataSetChanged();
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
        equiposDBReference.addChildEventListener(childEventListener);
    }


    public void setPartidoFirabaseListener() {


        Log.d(Rutas.TAG, "El valor del codigo del torneo es SetPArtidoFirebase:::: " + codigoTorneo);
        Log.d(Rutas.TAG, "*************El valor del codigo del numero de fecha es SetPArtidoFirebase:::: " + numeroFechaP);
        Log.d(Rutas.TAG, "El valor del codigo del partido es SetPArtidoFirebase:::: " + codigoPartido);
        //1.-Referencia al arbol
        partidosDBReference = database.
                getReference(Rutas.CALENDARIO).
                child(Rutas.ROOT_TORNEOS).
                child(codigoTorneo)
                .child("1")
        ;


        Log.d("PULPOLOG", "PATH en Partidos>>" + partidosDBReference.getPath());
        //2.-Crear el listener
        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("PULPOLOG", "onChildAdded de partido ....... datasnapshot" + dataSnapshot);
                partido = dataSnapshot.getValue(Partido.class);
                partidos.add(partido);
//                partidosAdapter.notifyDataSetChanged();
                partidoAdapter.notifyDataSetChanged();
                resultadoAdapter.notifyDataSetChanged();


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
                    partidoAdapter.notifyDataSetChanged();
                    resultadoAdapter.notifyDataSetChanged();
                    //     partidosAdapter.notifyDataSetChanged();
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
