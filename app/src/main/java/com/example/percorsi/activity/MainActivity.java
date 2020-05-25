package com.example.percorsi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.percorsi.R;
import com.example.percorsi.fragment.RouteListFragment;
import com.example.percorsi.persistence.AppPreferencesManager;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ActivityPrincipale";

    private Toolbar mainToolbar;

    private RouteListFragment routeListFragment = null;

    private int selectedSortingOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);

        this.routeListFragment = new RouteListFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container, routeListFragment).commit();

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

    //TODO: impostare dei metodi per la gestione dei click sul menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.sorting_icon:
                Log.d(TAG, "Cliccato menu: Ordinamento");
                openRouteListSortingDialog();
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

    //TODO: controllare se la chiamata a updateList() non debba essere fatta in background
    private void openRouteListSortingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] sortingOptions = this.getResources().getStringArray(R.array.sorting_options_array);

        builder.setTitle(R.string.sorting_dialog_title)
                .setSingleChoiceItems(sortingOptions, selectedSortingOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedSortingOption = which;
                        switch (which){
                            case 0:
                                AppPreferencesManager.setSortingPreference(getApplicationContext(),
                                        AppPreferencesManager.SORT_BY_NAME);
                                routeListFragment.updateList();
                                break;
                            case 1:
                                AppPreferencesManager.setSortingPreference(getApplicationContext(),
                                        AppPreferencesManager.SORT_BY_DOUBLE);
                                routeListFragment.updateList();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setPositiveButton(R.string.sorting_dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        AlertDialog sortingDialog = builder.create();
        sortingDialog.show();
    }
}
