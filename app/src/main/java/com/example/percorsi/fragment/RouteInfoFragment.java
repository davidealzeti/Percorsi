package com.example.percorsi.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.percorsi.R;
import com.example.percorsi.adapter.RouteListAdapter;
import com.example.percorsi.model.Route;

/**
 * Classe che mostra le informazioni e i dati di un Percorso specifico selezionato dall'utente
 * tra la lista dei Percorsi disponibili.
 */
public class RouteInfoFragment extends Fragment {
    private static final String TAG = "InfoPercorso";

    private Route clickedRoute = null;

    private TextView routeNameTextView, startLatTextView, startLonTextView;

    public RouteInfoFragment() {
        // Required empty public constructor
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
            }
            else Log.d(TAG, "Il bundle arrivato alla RouteActivity risulta essere vuoto");
        }

        setupUI(rootView);
        setupRouteInfo(clickedRoute);

        return rootView;
    }

    private void setupUI(View view){
        routeNameTextView = view.findViewById(R.id.route_name_text_view);
        startLatTextView = view.findViewById(R.id.route_start_lat_text_view);
        startLonTextView = view.findViewById(R.id.route_start_lon_text_view);
    }

    private void setupRouteInfo(Route route){
        if (route != null){
            Log.d(TAG, "Impostazione del testo sulle TextView");
            routeNameTextView.setText(route.getName());
            startLatTextView.setText(String.valueOf(route.getStartLatitude()));
            startLonTextView.setText(String.valueOf(route.getStartLongitude()));
        }
        else Log.d(TAG, "Il percorso cliccato sulla lista risulta essere null");
    }
}