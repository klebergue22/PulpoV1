package com.cmc.pulpov1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CategoriaActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private ArrayAdapter<String> adp;
    private List<String> categorias;
    private ListView lvCategorias;
    private String categoriaSeleccionada;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        database = FirebaseDatabase.getInstance();
        atarComponentes();
        categorias = new ArrayList<String>();
        categorias.add("Masculino");
        categorias.add("Femenino");
        categorias.add("Infantil");

        // escucharTorneos();
        tomarPosicion();

        //Instancio el adapter propio ya que estaba apuntando al adapter de android
        //adpT = new TorneoAdapter(this, torneos);
        //Seteo el adapter para pintar la lista
        adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categorias);
        lvCategorias.setAdapter(adp);

    }

    /*private void navCrearTorneo() {
        Intent intent = new Intent(this, GestionTorneoActivity.class);
        startActivity(intent);
    }

    private void navPerfilJugador() {
        Intent intent = new Intent(this, RegistroJugadorActivity.class);
        intent.putExtra("mail", mail);
        startActivity(intent);
    }*/



    private void atarComponentes() {
        lvCategorias = findViewById(R.id.lVCategoria);


    }

    private void tomarPosicion() {
        lvCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoriaSeleccionada = categorias.get(position);
              //  Log.d("PULPOLOG","el valor de la categoria seleccionada es "+categoriaSeleccionada+"CategoriaActivity");
             Toast.makeText(CategoriaActivity.this, "La categoria seleccionada es " + categoriaSeleccionada, Toast.LENGTH_SHORT).show();
                navCategoriaSeleccionada();
            }
        });
    }

    //envia un argumento al proximo intent (ListaEquiposActivity)
    private void navCategoriaSeleccionada() {
       // PulpoSingleton.getInstance().setCodigoTorneo(torneoSeleccionado.getId());
        PulpoSingleton.getInstance().setCodigoCategoria(categoriaSeleccionada);
       // Log.d("PULPOLOG","vALOR DE LA CATEGORIA**********"+ PulpoSingleton.getInstance().getCodigoCategoria()+"CategoriaActivity"  );

        //Intent intent = new Intent(this, TabsActivity.class);
        Intent intent = new Intent(this, TabsActivity.class);

        context=getApplicationContext();
        PulpoSingleton.getInstance().setContext(context);




        startActivity(intent);
    }
    /*

 public boolean puedeCrearEquipo(){
        if(PulpoSingleton.getInstance().getUsuarioLogueado()==null){
            return false;
        }
        if(PulpoSingleton.getInstance().getUsuarioLogueado().getRoles()==null){
            return false;
        }

        Map<String, Rol> roles=PulpoSingleton.getInstance().getUsuarioLogueado().getRoles();
        for(String clave:roles.keySet()){
            Rol rol=roles.get(clave);
            if("1".equals(rol.getIdRol())){
                return true;
            }
        }
        return false;
    }

     */



}
