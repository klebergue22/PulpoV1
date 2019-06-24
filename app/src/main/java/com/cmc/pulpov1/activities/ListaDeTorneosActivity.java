package com.cmc.pulpov1.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.cmc.pulpov1.LogPulpo;
import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.adapters.TorneoGridAdapter;
import com.cmc.pulpov1.entities.AdminPerfil;
import com.cmc.pulpov1.entities.Jugador;
import com.cmc.pulpov1.entities.Rol;
import com.cmc.pulpov1.entities.Torneo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListaDeTorneosActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private BaseAdapter adpT;
    private List<Torneo> torneos;
    private GridView gvTorneos;
    private boolean tipo;
    private Torneo torneoSeleccionado;
    private Button btnCrearTorneo;
    private Jugador jugador;
    private List<Jugador> jugadores;
    private AdminPerfil adminPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_torneos);
        database = FirebaseDatabase.getInstance();
        jugador = new Jugador();
        jugadores = new ArrayList<Jugador>();
        atarComponentes();
        recuperarJugador();
        tipo = true;
        if (puedeCrearTorneo()) {
            btnCrearTorneo.setVisibility(View.VISIBLE);
        } else {
            btnCrearTorneo.setVisibility(View.INVISIBLE);
        }
        escucharTorneos();
        tomarPosicion();
        //Instancio la lista de torneos
        torneos = new ArrayList<Torneo>();
        btnCrearTorneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navCrearTorneo();
            }
        });
        //Instancio el adapter propio ya que estaba apuntando al adapter de android
        //adpT = new TorneoAdapter(this, torneos);
        //Seteo el adapter para pintar la lista
        adpT = new TorneoGridAdapter(this, torneos);
        gvTorneos.setAdapter(adpT);
    }

    private void navPerfilJugador() {
        Intent intent = new Intent(this, RegistroJugadorActivity.class);
        intent.putExtra("paramTipoPerfil", Rutas.PRINCIPAL);
        startActivity(intent);
    }

    private void navPerfilAdicional1Jugador() {
        Intent intent = new Intent(this, RegistroJugadorActivity.class);
        intent.putExtra("paramTipoPerfil", Rutas.ADICIONAL1);
        startActivity(intent);
    }

    private void navPerfilAdicional2Jugador() {
        Intent intent = new Intent(this, RegistroJugadorActivity.class);
        intent.putExtra("paramTipoPerfil", Rutas.ADICIONAL2);
        startActivity(intent);
    }

    private void navCrearTorneo() {
        Intent intent = new Intent(this, GestionTorneoActivity.class);
        startActivity(intent);
    }

    private void navCrearNuevoJugador() {

        Intent intent = new Intent(this, RegistroJugadorActivity.class);
        intent.putExtra("tipo", tipo);
        startActivity(intent);
    }

    //envia un argumento al proximo intent (ListaEquiposActivity)
    private void navTorneoSeleccionado() {
        PulpoSingleton.getInstance().setCodigoTorneo(torneoSeleccionado.getId());
        //Intent intent = new Intent(this, TabsActivity.class);
        Intent intent = new Intent(this, CategoriaActivity.class);

        PulpoSingleton.getInstance().setNombreTorneo(torneoSeleccionado.getNombreTorneo());
        startActivity(intent);
    }


    private void escucharTorneos() {
        //1.-Referencia al arbol
        DatabaseReference refTorneo = database.getReference(Rutas.ROOT_TORNEOS);
        //2.-Crear el listener
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("LogPulpo.TAG", "Se agrega Torneo ListaTorneosActivity" + dataSnapshot.getKey());
                boolean repetido = false;
                for (Torneo torneo : torneos) {
                    //COMPARAR CONTRA EL ID Para ver si es repetido
                    if (torneo.getId().equals(dataSnapshot.getKey())) {
                        repetido = true;
                    }
                }
                if (!repetido) {
                    //Se recupera el objeto completo
                    Torneo torneo = dataSnapshot.getValue(Torneo.class);
                    torneos.add(torneo);

                    adpT.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("LogPulpo.TAG", "Se borra torneo ListaTorneosActivity " + dataSnapshot.getKey());
                int posicionRepetido = -1;
                for (int i = 0; i < torneos.size(); i++) {
                    //COMPARAR POR EL ID
                    if (torneos.get(i).getId().equals(dataSnapshot.getKey())) {
                        posicionRepetido = i;
                    }
                }
                if (posicionRepetido != -1) {
                    torneos.remove(posicionRepetido);
                    adpT.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        //Se descomenta el retorno del paso 3.-del listener
        refTorneo.addChildEventListener(childEventListener);
    }

    //Toma la posicion seleccionada
    private void tomarPosicion() {
        gvTorneos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                torneoSeleccionado = torneos.get(position);

                Toast.makeText(ListaDeTorneosActivity.this, "El torneo seleccionado es " + torneoSeleccionado.getId(), Toast.LENGTH_SHORT).show();
                navTorneoSeleccionado();
            }
        });
    }

    /*
     public void torneosLogueado() {
        Intent intent = new Intent(this, ListaDeTorneosActivity.class);
        intent.putExtra("prueba", etMail.getText().toString());
        Log.d("LogPulpo.TAG", "El valor del mail es " + etMail.getText().toString());
        startActivity(intent);
    }
     */

    private void atarComponentes() {
        gvTorneos = findViewById(R.id.baseGridView);
        //lvTorneos = findViewById(R.id.lvTorneo);
        //imgView=findViewById(R.id.imgView);
        btnCrearTorneo = findViewById(R.id.btnCrearTorneo);


    }

    public boolean puedeCrearTorneo() {
        if (PulpoSingleton.getInstance().getUsuarioLogueado() == null) {
            return false;
        }
        if (PulpoSingleton.getInstance().getUsuarioLogueado().getRoles() == null) {
            return false;
        }

        Map<String, Rol> roles = PulpoSingleton.getInstance().getUsuarioLogueado().getRoles();
        for (String clave : roles.keySet()) {
            Rol rol = roles.get(clave);
            if ("1".equals(rol.getIdRol())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        boolean valor = true;
        if (id == R.id.btnNuevoJugador) {
            navPerfilJugador();
            return valor;
        } else if (id == R.id.mnuAdicional) {
            navPerfilAdicional1Jugador();
            return valor;

        } else if (id == R.id.mnuAdicional2) {
            navPerfilAdicional2Jugador();
            return valor;
        }
        return valor;

        //return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);
        // Setting Dialog Title
        alertDialog.setTitle("Desea Salir?");
        // Setting Dialog Message
        alertDialog.setMessage("Esta seguro de salir de la aplicación ?");
        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.dialog_icon);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("SI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

    private void recuperarJugador() {
        // Get a reference to our posts
        database = FirebaseDatabase.getInstance();
        DatabaseReference refJugadores = database.getReference(Rutas.JUGADORES)
                .child(PulpoSingleton.getInstance().getMail());
        Log.d(LogPulpo.TAG, "El valor del path al recuperar es*********************** " + refJugadores.getPath());
        refJugadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminPerfil = dataSnapshot.getValue(AdminPerfil.class);
                PulpoSingleton.getInstance().setAdminPerfil(adminPerfil);
                if(adminPerfil==null){
                    adminPerfil=new AdminPerfil();
                    jugador=new Jugador();
                    PulpoSingleton.getInstance().setAdminPerfil(adminPerfil);
                    PulpoSingleton.getInstance().setJugador(jugador);
                }

                Log.d(LogPulpo.TAG, "Se recupera jugador" + adminPerfil);
                //Log.d(LogPulpo.TAG, "La lista de jugadores es " + jugadores.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
