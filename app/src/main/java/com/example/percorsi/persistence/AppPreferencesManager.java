package com.example.percorsi.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.percorsi.model.Route;

/**
 * Classe singleton che definisce le preferenze dell'utente nell'applicazione.
 */
public class AppPreferencesManager {
    private static final String TAG = "PreferenzeAppSingleton";

    private static final String APP_PREFERENCES = "Preferenze";

    private static final String SORTING = "Ordinamento";
    public static final int SORT_BY_MOST_RECENT = 0;
    public static final int SORT_BY_LEAST_RECENT = 1;
    public static final int SORT_BY_ROUTE_LENGTH = 2;
    public static final int SORT_BY_AVERAGE_SPEED = 3;

    public static final int SORT_BY_NAME = 4;
    public static final int SORT_BY_DOUBLE = 5;

    private static final String MEANS_OF_TRANSPORT = "Mezzo di trasporto";

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

    public static String getMeansOfTransport(Context context){
        Log.d(TAG, "getMeansOfTransport chiamato");
        return getSharedPreferences(context).getString(MEANS_OF_TRANSPORT, Route.ON_FOOT);
    }

    public static void setMeansOfTransport(Context context, String meansOfTransport){
        Log.d(TAG, "setMeansOfTransport chiamato");
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(MEANS_OF_TRANSPORT, meansOfTransport);
        editor.apply();
    }
}
