package com.example.percorsi.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Classe singleton che definisce le preferenze dell'utente nell'applicazione.
 */
public class AppPreferencesManager {
    private static final String TAG = "PreferenzeAppSingleton";

    private static final String APP_PREFERENCES = "Preferenze";

    private static final String SORTING = "Ordinamento";
    private static final int SORT_BY_MOST_RECENT = 0;
    private static final int SORT_BY_LEAST_RECENT = 1;
    private static final int SORT_BY_DISTANCE = 2;
    private static final int SORT_BY_SPEED = 3;
    private static final int SORT_BY_DURATION = 4;

    //TODO: valori inseriti per il testing, da eliminare in seguito
    public static final int SORT_BY_NAME = -1;
    public static final int SORT_BY_DOUBLE = -2;

    private AppPreferencesManager(){}

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static int getSortingPreference(Context context){
        Log.d(TAG, "getSortingPreference chiamato");
        return getSharedPreferences(context).getInt(SORTING, SORT_BY_MOST_RECENT);
    }

    public static void setSortingPreference(Context context, int sortingPreference){
        Log.d(TAG, "setSortingPreference chiamato");
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(SORTING, sortingPreference);
        editor.apply();
    }
}
