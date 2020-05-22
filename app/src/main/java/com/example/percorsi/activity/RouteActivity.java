package com.example.percorsi.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.percorsi.R;
import com.example.percorsi.adapter.RoutePagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * Classe che permette di visualizzare o effettuare un Percorso.
 */
public class RouteActivity extends AppCompatActivity {
    private static final String TAG = "ActivityPercorso";

    private Toolbar toolbar;
    private RoutePagerAdapter pagerAdapter = null;
    private ViewPager viewPager = null;
    private TabLayout tabLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity_with_tabs);

        setupToolbar();
        setupViewPager(getApplicationContext());
        setupTabLayout();

    }

    private void setupToolbar(){
        Log.d(TAG, "Creazione della Toolbar");
        this.toolbar = findViewById(R.id.toolbar_with_tabs);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupViewPager(Context context){
        Log.d(TAG, "Creazione del ViewPager");
        pagerAdapter = new RoutePagerAdapter(getSupportFragmentManager(), context);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(RoutePagerAdapter.TAB_NUMBER);
        viewPager.setAdapter(pagerAdapter);

        //TODO: implementare i listener sul cambio di pagina (tab) o di stato
    }

    private void setupTabLayout(){
        Log.d(TAG, "Creazione del TabLayout");
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }
}
