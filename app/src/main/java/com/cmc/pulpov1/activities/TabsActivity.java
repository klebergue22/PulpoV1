package com.cmc.pulpov1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.adapters.ViewPagerAdapter;
import com.cmc.pulpov1.entities.Rol;

import java.util.Map;


public class TabsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private String codigoCategoria;
    private String codigoTorneo;
    private String mail;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_activity_main);


        codigoCategoria = PulpoSingleton.getInstance().getCodigoCategoria();
        codigoTorneo = PulpoSingleton.getInstance().getCodigoTorneo();

        Log.w("PULPOLOG", "EL VALOR DEL CODIGO TORNEO ES " + codigoTorneo);
        Log.w("PULPOLOG", "EL VALOR DEL CODIGO CATEGORIA ES " + codigoCategoria);

        Toast.makeText(TabsActivity.this, "El VALOR DEL CODIGO DEL TORNEO ES " + codigoTorneo + "Valor CATEGORIA" + codigoCategoria, Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navNuevoEquipo();
            }
        });
        if (puedeCrearEquipo()) {
            fab.show();


        } else {
            fab.hide();
        }
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public boolean puedeCrearEquipo() {
        if (PulpoSingleton.getInstance().getUsuarioLogueado() == null) {
            return false;
        }
        if (PulpoSingleton.getInstance().getUsuarioLogueado().getRoles() == null) {
            return false;
        }

        Log.w("PULPOLOG", "valor del codigo del torneo en el IF " + codigoTorneo);

        if (codigoTorneo.equals(PulpoSingleton.getInstance().getCodigoTorneo()))
            Log.w("PULPOLOG", "VALOR DENTRO DEL IF " + codigoTorneo);

        Map<String, Rol> roles = PulpoSingleton.getInstance().getUsuarioLogueado().getRoles();
        for (String clave : roles.keySet()) {
            Rol rol = roles.get(clave);
            if (("2".equals(rol.getIdRol())) && (codigoTorneo.equals(rol.getIdTorneo()))) {
                return true;
            }
        }

        return false;
    }


    private void navNuevoEquipo() {
        Intent intent = new Intent(this, CalendarioActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


}
