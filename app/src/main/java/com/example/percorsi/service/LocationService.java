package com.example.percorsi.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.percorsi.R;
import com.example.percorsi.activity.RouteActivity;
import com.example.percorsi.fragment.RouteMapFragment;
import com.example.percorsi.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Service che fornisce aggiornamenti sulla Location anche tramite notifiche foreground.
 *
 * Parte del codice presa dal repository degli sviluppatori su Github:
 * <a href="https://github.com/android/location-samples/blob/master/LocationUpdatesForegroundService">
 *      Repository Android Developers</a>
 */
public class LocationService extends Service {
    private static final String TAG = "ServicePosizione";

    private static final String PACKAGE_NAME = "com.example.percorsi.service";
    public static final String ACTION_NAME = PACKAGE_NAME + ".broadcast";
    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME + ".started_from_notification";

    private static final int NOTIFICATION_ID = 1973;
    private static final String CHANNEL_ID = "channel_01";

    private final IBinder locationBinder = new LocalBinder();
    private boolean changingConfiguration = false;

    private Location currentLocation = null;

    private static final int FIVE_METERS = 5;
    private static final int TWO_SECONDS = 2000;

    private LocationRequest locationRequest = null;
    private FusedLocationProviderClient fusedLocationClient = null;
    private LocationCallback locationCallback = null;

    private NotificationManager notificationManager = null;
    private Handler locationServiceHandler = null;


    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Chiamato onCreate sul Service");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        locationServiceHandler = new Handler(handlerThread.getLooper());

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "LocationService partito");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false);

        if (startedFromNotification){
            removeLocationUpdates();
            stopSelf();
        }

        //Non ricreare il Service una volta che è stato distrutto
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        changingConfiguration = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Chiamato onBind sul Service");
        stopForeground(true);
        changingConfiguration = false;
        return locationBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "Chiamato onRebind sul Service");
        stopForeground(true);
        changingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "Chiamato onUnbind sul Service");

        if(changingConfiguration && Utils.requestingLocationUpdates(this)){
            Log.d(TAG, "LocationService spostato in foreground");
            startForeground(NOTIFICATION_ID, getNotification());
        }

        return true;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Chiamato onDestroy sul Service");
        locationServiceHandler.removeCallbacksAndMessages(null);
    }

    //TODO: spostare questo metodo in un fragment o activity che possa mostrare il dialog
    private void openLocationSettings(){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS disattivato")
                    .setCancelable(false)
                    .setMessage("Per registrare il tuo Percorso è necessario il GSP. Per favore, attivalo")
                    .setPositiveButton("Apri impostazioni", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent openLocationSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            openLocationSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(openLocationSettingsIntent);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isGPSPermissionGranted(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void createLocationRequest(){
        Log.d(TAG, "Chiamato createLocationRequest");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(TWO_SECONDS)
                .setFastestInterval(TWO_SECONDS/2)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void requestLocationUpdates() {
        Log.i(TAG, "Richiesti aggiornamenti sulla Location");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), LocationService.class));
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Impossibile rimuovere gli update sulla location. Permessi GPS rimossi: " + unlikely);
        }
    }

    public void removeLocationUpdates(){
        Log.d(TAG, "Aggiornamenti sulla Location rimossi");

        try {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException e) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Impossibile rimuovere gli update sulla location. Permessi GPS rimossi: " + e);
        }
    }

    private void getLastLocation(){
        Log.d(TAG, "Chiamato getLastLocation");
        try {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                currentLocation = task.getResult();
                            }else {
                                Log.d(TAG, "Errore nell'ottenimento della posizione");
                            }
                        }
                    });
        } catch (SecurityException e) {
            Log.d(TAG, "Permessi sulla localizzazione revocati: " + e);
        }
    }

    private void onNewLocation(Location location){
        Log.d(TAG, "Nuova location: " + location);

        currentLocation = location;
        Intent intent =  new Intent(ACTION_NAME);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        if (serviceIsRunningInForeground(this)){
            notificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, LocationService.class);

        CharSequence text = Utils.getLocationText(currentLocation);
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //TODO: controllare che nell'Intent non si debba passare il fragment invece dell'activity (?)
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, RouteActivity.class), 0);

        //TODO: aggiornare il Builder aggiungendo il ChannelId (stringa) al costruttore
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_launcher_foreground, getString(R.string.launch_activity), activityPendingIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.remove_location_updates),
                        servicePendingIntent)
                .setContentText(text)
                .setContentTitle(Utils.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationBuilder.setChannelId(CHANNEL_ID);
        }

        return notificationBuilder.build();
    }

    public boolean serviceIsRunningInForeground(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(getClass().getName().equals(service.service.getClassName())){
                if(service.foreground) return true;
            }
        }
        return false;
    }
}
