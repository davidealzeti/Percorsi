package com.example.percorsi.persistence;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.percorsi.adapter.RouteListAdapter;
import com.example.percorsi.model.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton utilizzato per la gestione degli elementi di tipo Route, cioè
 * i Percorsi effettuati dall'utente.
 */
public class RouteManager {
    private static final String TAG = "ListaPercorsi";

    private static RouteManager instance = null;
    private List<Route> routeList = null;
    private Context context = null;
    private AppDatabase db = null;
    private RouteDao routeDao = null;

    private RouteManager(Context context){
        Log.d(TAG, "Costruttore RouteManager chiamato");
        if (routeList == null){
            this.context = context;

            this.db = Room.databaseBuilder(context, AppDatabase.class, "routes-database").allowMainThreadQueries().build();
            this.routeDao = this.db.routeDao();
            routeList = new ArrayList<>();
        }
    }

    public static RouteManager getInstance(Context context){
        Log.d(TAG, "Recuperata l'istanza della classe RouteManager");
        if (instance == null){
            instance = new RouteManager(context);
        }
        return instance;
    }

    public void updateRoutes(Route... routes){
        Log.d(TAG, "Aggiornamento dei percorsi nel DB");
        this.routeDao.updateRoutes(routes);
    }

    public void addRoute(Route route){
        Log.d(TAG, "Aggiunto un Percorso alla lista -> " + route.toString());
        this.routeDao.insertRoute(route);
    }

    public void removeRoute(Route route){
        Log.d(TAG, "Rimosso un Percorso dalla lista -> " + route.toString());
        this.routeDao.delete(route);
    }

    public Route getRouteWithName(String name){
        return this.routeDao.getRouteWithName(name);
    }

    public List<Route> getRouteList(){
        Log.d(TAG, "Ritornata la lista completa dei Percorsi");
        return this.routeDao.getAll();
    }

    public void deleteAllRoutes(){
        Log.d(TAG, "Rimossi tutti i Percorsi nel DB");
        this.routeDao.deleteAll();
    }
}
