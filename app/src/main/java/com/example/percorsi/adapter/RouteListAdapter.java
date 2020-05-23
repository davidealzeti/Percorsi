package com.example.percorsi.adapter;

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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Classe che gestisce la lista dei Percorsi.
 */
public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {
    private static final String TAG = "AdapterListaPercorsi";

    public static final String ROUTE_ITEM = "Percorso";

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
                    Log.d(TAG, "Cliccato un elemento della lista dei Percorsi alla posizione: " + getAdapterPosition());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ROUTE_ITEM, routeDataSet.get(getAdapterPosition()));

                    Intent routeClickedIntent = new Intent(new Intent(v.getContext(), RouteActivity.class));
                    routeClickedIntent.putExtras(bundle);

                    v.getContext().startActivity(routeClickedIntent);
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

    public RouteListAdapter(ArrayList<Route> routeDataSet){
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
        String startLat = new DecimalFormat("##.##").format(route.getStartLatitude());
        String startLon = new DecimalFormat("##.##").format(route.getStartLongitude());

        holder.setText(name, startLat, startLon);
    }

    @Override
    public int getItemCount() {
        return routeDataSet.size();
    }

}
