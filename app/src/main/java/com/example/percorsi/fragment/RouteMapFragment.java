package com.example.percorsi.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.percorsi.R;
import com.example.percorsi.adapter.RouteListAdapter;
import com.example.percorsi.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Classe che contiene la Mappa che mostra il Percorso dell'utente.
 */
public class RouteMapFragment extends Fragment {
    private static final String TAG = "MappaPercorso";

    private MapView mapView;
    private GoogleMap gMap;

    private Route clickedRoute = null;

    public RouteMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_route_map, container, false);

        mapView = rootView.findViewById(R.id.route_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        retrieveRouteInfo();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;

                LatLng routePosition = new LatLng(44 + clickedRoute.getStartLongitude(), 45 + clickedRoute.getStartLongitude());
                gMap.addMarker(new MarkerOptions().position(routePosition).title("Marker Title").snippet("Marker Description"));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(routePosition).zoom(12).build();
                gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }

    private void retrieveRouteInfo(){
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null){
            this.clickedRoute = bundle.getParcelable(RouteListAdapter.ROUTE_ITEM);
        }
        else Log.d(TAG, "Il bundle risulta essere vuoto");
    }

}
