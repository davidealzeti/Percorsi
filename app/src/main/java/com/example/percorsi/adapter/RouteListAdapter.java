package com.example.percorsi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.percorsi.R;
import com.example.percorsi.activity.RouteActivity;
import com.example.percorsi.model.Route;
import com.example.percorsi.persistence.AppPreferencesManager;
import com.example.percorsi.persistence.RouteManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Classe che gestisce la lista dei Percorsi.
 */
public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {
    private static final String TAG = "AdapterListaPercorsi";

    public static final String ROUTE_ITEM = "Percorso";

    private Context context = null;
    private static RouteListAdapter instance = null;

    private List<Route> routeDataSet;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View v = null;

        public ViewHolder(View v){
            super(v);
            Log.d(TAG, "Chiamato il costruttore ViewHolder del RouteAdapter");

            this.v = v;

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Cliccato un elemento della lista dei Percorsi alla posizione: " + getAdapterPosition());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ROUTE_ITEM, routeDataSet.get(getAdapterPosition()));

                    Intent routeClickedIntent = new Intent(new Intent(v.getContext(), RouteActivity.class));
                    routeClickedIntent.putExtras(bundle);

                    v.getContext().startActivity(routeClickedIntent);
                }
            });
        }

        public void setText(String name, String meanOfTransport, String date){
            Log.d(TAG, "Aggiunto testo a un elemento della lista");
            TextView routeItemNameTextView = v.findViewById(R.id.route_item_name_text_view);
            TextView routeItemMeanOfTransport = v.findViewById(R.id.route_item_means_of_transport);
            TextView routeItemDateTextView = v.findViewById(R.id.route_item_date_text_view);

            routeItemNameTextView.setText(name);
            routeItemMeanOfTransport.setText(meanOfTransport);
            routeItemDateTextView.setText(date);
        }
    }

    /*
    public RouteListAdapter(ArrayList<Route> routeDataSet){
        Log.d(TAG, "Costruttore RouteAdapter chiamato");
        this.routeDataSet = routeDataSet;
    }
    */

    private RouteListAdapter(Context context){
        Log.d(TAG, "Costruttore RouteAdapter Chiamato");
        this.context = context;

        if(this.routeDataSet == null){
            this.routeDataSet = RouteManager.getInstance(context).getRouteList();
        }
    }

    public static RouteListAdapter getInstance(Context context) {
        if(instance == null){
            instance = new RouteListAdapter(context);
        }
        return instance;
    }

    public void addElementToList(Route route){
        Log.d(TAG, "Aggiunto un elemento alla lista: " + route.toString());
        RouteManager.getInstance(context).addRoute(route);
        this.routeDataSet.add(route);
    }

    public void removeElementFromList(Route route){
        Log.d(TAG, "Rimosso un elemento dalla lista: " + route.toString());
        RouteManager.getInstance(context).removeRoute(route);
        Log.d(TAG, String.valueOf(this.routeDataSet.remove(route)));
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "Chiamato onCreateViewHolder");
        this.context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.route_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "Chiamato onBindViewHolder");
        sortListByUserPreference(context);

        Route route = routeDataSet.get(position);
        String name = route.getName();
        String meanOfTransport = route.getMeansOfTransport();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        String date = formatter.format(route.getStartDate());

        holder.setText(name, meanOfTransport, date);
    }

    @Override
    public int getItemCount() {
        return routeDataSet.size();
    }

    //TODO: aggiungere i casi per le altre preferenze di ordinamento
    private void sortListByUserPreference(Context context){
        Log.d(TAG, "Riordinata la lista");
        switch (AppPreferencesManager.getSortingPreference(context)){
            case AppPreferencesManager.SORT_BY_MOST_RECENT:
                sortListByMostRecent(); break;
            case AppPreferencesManager.SORT_BY_LEAST_RECENT:
                sortListByLeastRecent(); break;
            case AppPreferencesManager.SORT_BY_ROUTE_LENGTH:
                sortListByRouteLength(); break;
            case AppPreferencesManager.SORT_BY_AVERAGE_SPEED:
                sortListByAverageSpeed(); break;
            case AppPreferencesManager.SORT_BY_NAME:
                sortListByName(); break;
            case AppPreferencesManager.SORT_BY_DOUBLE:
                sortListByLatitude(); break;
            default:
                break;
        }
    }

    //TODO: eliminare i due metodi qui sotto, utilizzati per il testing, e aggiungere dei Comparator alla classe Route
    private void sortListByName(){
        Log.d(TAG,"Ordinata la lista per nome");
        if (routeDataSet.size() > 0){
            Collections.sort(this.routeDataSet, new Comparator<Route>() {
                @Override
                public int compare(Route o1, Route o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
        }
    }

    private void sortListByLatitude(){
        Log.d(TAG, "Ordinata la lista per latitudine");
        if (routeDataSet.size() > 0){
            Collections.sort(this.routeDataSet, new Comparator<Route>() {
                @Override
                public int compare(Route o1, Route o2) {
                    if (o1.getStartLatitude() >= o2.getStartLatitude()) return 0;
                    return -1;
                }
            });
        }
    }

    private void sortListByMostRecent(){
        Log.d(TAG, "Ordinata la lista dagli elementi più recenti");
        if (routeDataSet.size() > 0){
            Collections.sort(this.routeDataSet, Route.SORT_BY_MOST_RECENT);
        }
    }

    private void sortListByLeastRecent(){
        Log.d(TAG, "Ordinata la lista dagli elementi meno recenti");
        if (routeDataSet.size() > 0){
            Collections.sort(this.routeDataSet, Route.SORT_BY_LEAST_RECENT);
        }
    }

    private void sortListByRouteLength(){
        Log.d(TAG, "Ordinata la lista per lunghezza Percorso");
        if (routeDataSet.size() > 0){
            Collections.sort(this.routeDataSet, Route.SORT_BY_ROUTE_LENGTH);
        }
    }

    private void sortListByAverageSpeed(){
        Log.d(TAG, "Ordinata la lista per velocità media");
        if (routeDataSet.size() > 0){
            Collections.sort(this.routeDataSet, Route.SORT_BY_AVERAGE_SPEED);
        }
    }

    private void sortListByDuration(){
        Log.d(TAG, "Ordinata la lista per durata complessiva");
        //TODO: aggiungere riordinamento
    }
}
