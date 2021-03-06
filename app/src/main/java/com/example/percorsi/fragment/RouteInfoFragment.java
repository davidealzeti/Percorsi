package com.example.percorsi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.percorsi.R;
import com.example.percorsi.adapter.RouteListAdapter;
import com.example.percorsi.model.Route;
import com.example.percorsi.persistence.AppPreferencesManager;
import com.example.percorsi.persistence.RouteManager;
import com.example.percorsi.service.LocationService;

import java.text.DecimalFormat;

/**
 * Classe che mostra le informazioni e i dati di un Percorso specifico selezionato dall'utente
 * tra la lista dei Percorsi disponibili.
 */
public class RouteInfoFragment extends Fragment {
    private static final String TAG = "InfoPercorso";

    private UpdateRouteInfoReceiver routeInfoReceiver = null;
    private Route clickedRoute = null;

    private TextView routeNameTextView, meansOfTransportTextView, startLatTextView, startLonTextView;
    private TextView routeDateTextView, routeLengthTextView, averageSpeedTextView, averageAccuracyTextView;

    public RouteInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: aggiunto UpdateRouteInfoReceiver");
        routeInfoReceiver = new UpdateRouteInfoReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(routeInfoReceiver,
                new IntentFilter(LocationService.ACTION_NAME));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_route_info, container, false);


        if (savedInstanceState == null){
            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null){
                this.clickedRoute = bundle.getParcelable(RouteListAdapter.ROUTE_ITEM);
                AppPreferencesManager.setMeansOfTransport(getContext(), clickedRoute.getMeansOfTransport());
            }
            else Log.d(TAG, "Il bundle arrivato alla RouteActivity risulta essere vuoto");
        }

        setupUI(rootView);
        setupRouteInfo(clickedRoute);

        return rootView;
    }

    private void setupUI(View view){
        routeNameTextView = view.findViewById(R.id.route_name_text_view);
        meansOfTransportTextView = view.findViewById(R.id.means_of_transport_text_view);
        startLatTextView = view.findViewById(R.id.route_start_lat_text_view);
        startLonTextView = view.findViewById(R.id.route_start_lon_text_view);
        routeDateTextView = view.findViewById(R.id.route_date_text_view);
        averageSpeedTextView = view.findViewById(R.id.route_avg_speed_text_view);
        averageAccuracyTextView = view.findViewById(R.id.route_avg_accuracy_text_view);
    }

    private void setupRouteInfo(Route route){
        if (route != null){
            Log.d(TAG, "Impostazione del testo sulle TextView");
            routeNameTextView.setText(route.getName());
            meansOfTransportTextView.setText(route.getMeansOfTransport());
            startLatTextView.setText(String.valueOf(RouteManager.getInstance(getContext()).getRouteWithName(route.getName()).getStartLatitude()));
            startLonTextView.setText(String.valueOf(RouteManager.getInstance(getContext()).getRouteWithName(route.getName()).getStartLongitude()));
            routeDateTextView.setText(route.getFormattedDate());

            String speed = formatDouble(RouteManager.getInstance(getContext()).getRouteWithName(route.getName()).getAverageSpeed()) + " km/h";
            averageSpeedTextView.setText(speed);
            averageAccuracyTextView.setText(formatDouble(RouteManager.getInstance(getContext()).getRouteWithName(route.getName()).getAverageAccuracy()));
        }
        else Log.d(TAG, "Il percorso cliccato sulla lista risulta essere null");
    }

    private class UpdateRouteInfoReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Message received with location");
            Location currentLocation = intent.getParcelableExtra(LocationService.EXTRA_LOCATION);
            clickedRoute.addLocationToLocationsArray(currentLocation);
            startLatTextView.setText(String.valueOf(currentLocation.getLatitude()));
            startLonTextView.setText((String.valueOf(currentLocation.getLongitude())));

            String speed = formatDouble(currentLocation.getSpeed()) + " km/h";
            averageSpeedTextView.setText(speed);

            averageAccuracyTextView.setText(String.valueOf(currentLocation.getAccuracy()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: rimosso UpdateRouteInfoReceiver");
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(routeInfoReceiver);
    }

    private String formatDouble(double num){
        return new DecimalFormat("##.##").format(num);
    }
}