package com.example.percorsi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.percorsi.R;
import com.example.percorsi.fragment.RouteInfoFragment;
import com.example.percorsi.fragment.RouteMapFragment;

public class RoutePagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "AdapterTabPercorso";

    private static final int ROUTE_INFO_TAB = 0;
    private static final int ROUTE_MAP_TAB = 1;
    public static final int TAB_NUMBER = 2;

    private Context context;

    public RoutePagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        this.context = container.getContext();
        return super.instantiateItem(container, position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case ROUTE_INFO_TAB:
                Log.d(TAG, "Selezionata Tab: RouteInfoFragment");
                return new RouteInfoFragment();
            case ROUTE_MAP_TAB:
                Log.d(TAG, "Selezionata Tab: RouteMapFragment");
                return new RouteMapFragment();
            default:
                Log.d(TAG, "Errore nella selezione della Tab");
                return new RouteInfoFragment();
        }
    }

    @Override
    public int getCount() {
        return TAB_NUMBER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case ROUTE_INFO_TAB:
                return context.getText(R.string.route_info_tab_title);
            case ROUTE_MAP_TAB:
                return context.getText(R.string.route_map_tab_title);
            default:
                Log.d(TAG, "Errore nella creazione dei titoli delle Tab");
                return "";
        }
    }
}
