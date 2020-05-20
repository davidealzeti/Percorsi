package com.example.percorsi.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.percorsi.R;
import com.example.percorsi.model.Route;

import java.util.ArrayList;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {
    private static final String TAG = "AdapterListaPercorsi";

    private ArrayList<Route> routeDataSet;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View v = null;

        public ViewHolder(View v){
            super(v);
            Log.d(TAG, "Chiamato il costruttore ViewHolder del RouteAdapter");

            this.v = v;

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Cliccato un elemento della lista di Percorsi alla posizione: " + getAdapterPosition());
                    //TODO: gestire il click di un elemento della lista
                }
            });
        }

        public void setText(String name, String startLat, String startLon){
            Log.d(TAG, "Aggiunto testo a un elemento della lista");
            TextView routeItemNameTextView = v.findViewById(R.id.route_item_name_text_view);
            TextView routeItemStartLatTextView = v.findViewById(R.id.route_item_start_lat_text_view);
            TextView routeItemStartLonTextView = v.findViewById(R.id.route_item_start_lon_text_view);

            routeItemNameTextView.setText(name);
            routeItemStartLatTextView.setText(startLat);
            routeItemStartLonTextView.setText(startLon);
        }
    }

    public RouteAdapter(ArrayList<Route> routeDataSet){
        Log.d(TAG, "Costruttore RouteAdapter chiamato");
        this.routeDataSet = routeDataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "Chiamato onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "Chiamato onBindViewHolder");
        Route route = routeDataSet.get(position);
        String name = route.getName();
        String startLat = String.valueOf(route.getStartLatitude());
        String startLon = String.valueOf(route.getStartLongitude());

        holder.setText(name, startLat, startLon);
    }

    @Override
    public int getItemCount() {
        return routeDataSet.size();
    }


}
