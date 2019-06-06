package com.cmc.pulpov1.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cmc.pulpov1.fragments.EquipoFragment;
import com.cmc.pulpov1.fragments.PartidoFragment;
import com.cmc.pulpov1.fragments.PersonaFragment;
import com.cmc.pulpov1.fragments.ResultadosFragment;


/**
 * Created by Priyabrat on 21-08-2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {


        Fragment fragment = null;
        if (position == 0) {
            fragment = new EquipoFragment();
        } else if (position == 1) {
            fragment = new PartidoFragment();

        } else if (position == 2) {
            fragment = new ResultadosFragment();
        } else if (position == 3) {
            fragment = new PersonaFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Equipos";
        } else if (position == 1) {
            title = "Calendarios";
        } else if (position == 2) {
            title = "Resultados";
        } else if (position == 3) {
            title = "Posiciones";
        }
        return title;
    }
}
