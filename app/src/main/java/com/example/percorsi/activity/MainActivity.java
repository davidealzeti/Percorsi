package com.example.percorsi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.percorsi.R;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ActivityPrincipale";

    private Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);

        setupToolbar();
    }

    private void setupToolbar(){
        Log.d(TAG, "Creazione della toolbar");
        this.mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
        TODO: impostare dei metodi per la gestione dei click sul menu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.sorting_icon:
                Log.d(TAG, "Cliccato menu: Ordinamento");
                return true;
            case R.id.option_menu_icon:
                Log.d(TAG, "Cliccato menu: Opzioni");
                return true;
            case R.id.app_info_icon:
                Log.d(TAG, "Cliccato menu: Informazioni");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
