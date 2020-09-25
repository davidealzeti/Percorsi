package com.example.percorsi.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.percorsi.R;
import com.example.percorsi.adapter.RouteListAdapter;
import com.example.percorsi.model.Route;
import com.example.percorsi.persistence.RouteManager;
import com.example.percorsi.service.LocationService;
import com.example.percorsi.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

/**
 * Classe che contiene la Mappa che mostra il Percorso dell'utente.
 */
public class RouteMapFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "MappaPercorso";

    private static final double DONE = Route.DONE;

    private MapView mapView;
    private GoogleMap gMap;

    private Route clickedRoute = null;
    private Location currentLocation = null;

    private LocationService locationService = null;
    private UpdateLocationReceiver locationReceiver = null;
    private boolean isBound = false;

    private FloatingActionButton stopRouteButton = null;

    private static final int DEFAULT_ZOOM = 17;
    private static final LatLng DEFAULT_LOCATION = new LatLng(44,45);

    public RouteMapFragment() {
        // Required empty public constructor
    }

    private final ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "Service connesso");
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            locationService = binder.getService();
            locationService.requestLocationUpdates();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Service disconnesso");
            locationService = null;
            isBound = false;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Chiamato onCreate");
        super.onCreate(savedInstanceState);

        retrieveRouteInfo();
        Log.d(TAG, clickedRoute.toString());

        if (clickedRoute.getDone() != DONE) {
            locationReceiver = new UpdateLocationReceiver();
            getActivity().bindService(new Intent(getContext(), LocationService.class), boundServiceConnection,
                    Context.BIND_AUTO_CREATE);
        }else Log.d(TAG, "Service di localizzazione non connesso perchè percorso già completato");
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Chiamato onStart");
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        //locationService.requestLocationUpdates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Chiamato onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_route_map, container, false);

        retrieveAndSetStopRouteButton(rootView);

        mapView = rootView.findViewById(R.id.route_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "Chiamato onMapReady");
                gMap = googleMap;
                gMap.setMinZoomPreference(DEFAULT_ZOOM);
                try {
                    if (clickedRoute.getDone() == DONE){
                        //clickedRoute.setLocationsArray(RouteManager.getInstance(getContext()).getLocationList(clickedRoute));

                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(clickedRoute.getLocationsArray().get(0).getLatitude(),
                                clickedRoute.getLocationsArray().get(0).getLongitude()), DEFAULT_ZOOM));
                        clickedRoute.addPolylineToGoogleMap(gMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "Chiamato onResume");
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(locationReceiver,
                new IntentFilter(LocationService.ACTION_NAME));
    }

    @Override
    public void onPause() {
        Log.d(TAG, "Chiamato onPause");
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(locationReceiver);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "Chiamato onStop");
        if(isBound){
            getActivity().unbindService(boundServiceConnection);
            isBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(getContext()).
                unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "Chiamato onDestroyView");
        super.onDestroyView();
        if (locationService != null) {
            locationService.removeLocationUpdates();
            isBound = false;
        }
    }

    private void retrieveRouteInfo(){
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null){
            this.clickedRoute = bundle.getParcelable(RouteListAdapter.ROUTE_ITEM);
            this.clickedRoute = RouteManager.getInstance(getContext()).getRouteWithName(this.clickedRoute.getName());
        }
        else Log.d(TAG, "Il bundle risulta essere vuoto");
    }


    @SuppressLint("RestrictedApi")
    private void retrieveAndSetStopRouteButton(View view){
        stopRouteButton = view.findViewById(R.id.stop_route_floating_button);
        if (RouteManager.getInstance(getContext()).getRouteWithName(clickedRoute.getName()).getDone() == DONE){
            Log.d(TAG, "Il percorso selezionato è già stato completato");
            stopRouteButton.setVisibility(View.INVISIBLE);
        }

        Log.d(TAG, "Percorso selezionato: " + RouteManager.getInstance(getContext()).getRouteWithName(clickedRoute.getName()).toString());

        stopRouteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"MissingPermission", "RestrictedApi"})
            @Override
            public void onClick(View v) {
                if (locationService != null){
                    Log.d(TAG, "Bottone stopRouteButton premuto: aggiornamenti sulla posizione rimossi");
                    locationService.removeLocationUpdates();
                    isBound = false;
                    //TODO: la posizione sulla mappa dell'utente non deve cambiare una volta premuto il bottone
                    gMap.setMyLocationEnabled(false);
                    gMap.getUiSettings().setMyLocationButtonEnabled(false);
                    stopRouteButton.setVisibility(View.INVISIBLE);
                    clickedRoute.setStopDate(new Date());
                    RouteManager.getInstance(getContext()).setStartLatitudeAndLongitude(clickedRoute, clickedRoute.getLocationsArray().get(0).getLatitude(),
                            clickedRoute.getLocationsArray().get(0).getLongitude());
                    RouteManager.getInstance(getContext()).setAverageSpeedAndAccuracy(clickedRoute, clickedRoute.getAverageSpeed(), clickedRoute.getAverageAccuracy());
                    //RouteManager.getInstance(getContext()).setLocationList(clickedRoute, clickedRoute.getLocationsArray());
                    RouteManager.getInstance(getContext()).setDone(clickedRoute.getName(), Route.DONE);
                }
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES) && stopRouteButton != null) {
            stopRouteButton.setEnabled(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }
    }

    private class UpdateLocationReceiver extends BroadcastReceiver{
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            currentLocation = intent.getParcelableExtra(LocationService.EXTRA_LOCATION);
            if (isBound) {
                updateLocationUI();
                setMapViewToUserLocation(currentLocation);
                clickedRoute.addLocationToLocationsArray(currentLocation);
                clickedRoute.addPolylineToGoogleMap(gMap);
            }else{
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
            }

            if (!isGPSEnabled()) Toast.makeText(getContext(), "Attiva il GPS per favore", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void updateLocationUI(){
        Log.d(TAG, "Chiamato updateLocationUI");
        try {
            if (!gMap.isMyLocationEnabled()) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Errore nel metodo updateLocationUI: " + e);
        }
    }

    private void setMapViewToUserLocation(Location location){
        Log.d(TAG, "Chiamato setMapViewToUserLocation");
        if (gMap != null){
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(),
                            location.getLongitude()), DEFAULT_ZOOM));
        }
    }

    private boolean isGPSEnabled(){
        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (NullPointerException e) {
            Log.e(TAG, "LocationManager non esiste: " + e);
            return false;
        }
    }
}
