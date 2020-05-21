package com.example.percorsi.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.percorsi.R;
import com.example.percorsi.adapter.RouteListAdapter;
import com.example.percorsi.model.Route;
import com.example.percorsi.persistence.RouteManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

/**
 * La classe RouteListFragment mostra la lista degli elementi Route generati
 * dall'utente, i quali rappresentano i Percorsi svolti.
 */
public class RouteListFragment extends Fragment {
    private static final String TAG = "FragmentListaPercorsi";

    private RecyclerView routeListRecyclerView = null;
    private LinearLayoutManager routeListLayoutManager = null;
    private RouteListAdapter routeListAdapter = null;

    private ArrayList<Route> routeList = null;

    private FloatingActionButton addRouteButton = null;

    public RouteListFragment() {
        Log.d(TAG, "Costruttore RouteListFragment chiamato");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_route_list, container, false);

        setupRouteList(rootView);

        addRouteButton = rootView.findViewById(R.id.add_route_floating_button);

        addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cliccato floatingButton per l'aggiunta di un nuovo percorso");
                Route route = new Route("Test", new Random().nextDouble(), new Random().nextDouble());
                RouteManager.getInstance().addRoute(route);

                routeListAdapter.notifyDataSetChanged();
                routeListRecyclerView.scrollToPosition(routeList.size()-1);
            }
        });

        return rootView;
    }

    private void setupRouteList(View v){
        Log.d(TAG, "Chiamato setupRouteList");
        routeListRecyclerView = v.findViewById(R.id.route_recycler_view);

        routeListLayoutManager = new LinearLayoutManager(getActivity());
        routeListLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        /*
            TODO: modificare l'inserimento dall'alto per adattarlo dinamicamente al riordinamento
                    della lista tramite l'apposito bottone nella MainActivity
         */
        routeListLayoutManager.setReverseLayout(true);
        routeListLayoutManager.setStackFromEnd(true);

        routeListLayoutManager.scrollToPosition(0);

        routeListRecyclerView.setLayoutManager(routeListLayoutManager);
        routeListRecyclerView.setHasFixedSize(true);

        this.routeList = RouteManager.getInstance().getRouteList();
        routeListAdapter = new RouteListAdapter(routeList);
        routeListRecyclerView.setAdapter(routeListAdapter);

    }
}
