package com.example.percorsi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.example.percorsi.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Classe che rappresenta lo stato degli update sulla posizione tramite
 * SharedPreferences.
 * Parte del codice presa dal repository degli sviluppatori su Github:
 * <a href="https://github.com/android/location-samples/blob/master/LocationUpdatesForegroundService">
 *     Repository Android Developers</a>
 */
public class Utils {
    private static final String TAG = "UtilsLocationUpdates";

    public static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates";

    private static SharedPreferences getLocationUpdatesPreferences(Context context){
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public static boolean requestingLocationUpdates(Context context){
        Log.d(TAG, "Chiamato requestingLocationUpdates");
        return getLocationUpdatesPreferences(context).getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates){
        Log.d(TAG, "Chiamato setRequestingLocationUpdates con valore: " + requestingLocationUpdates);
        final SharedPreferences.Editor editor = getLocationUpdatesPreferences(context).edit();
        editor.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    public static String getLocationText(Location location){
        return location == null ? "Location sconosciuta" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context){
        return context.getString(R.string.location_updated, DateFormat.getDateTimeInstance().format(new Date()));
    }

}
