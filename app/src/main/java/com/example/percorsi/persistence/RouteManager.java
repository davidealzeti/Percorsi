package com.example.percorsi.persistence;

import android.util.Log;

import com.example.percorsi.model.Route;

import java.util.ArrayList;

/**
 * Singleton utilizzato per la gestione degli elementi di tipo Route, cioè
 * i Percorsi effettuati dall'utente.
 */
public class RouteManager {
    private static final String TAG = "ListaPercorsi";

    private static RouteManager instance = null;
    private ArrayList<Route> routeList = null;

    private RouteManager(){
        Log.d(TAG, "Costruttore RouteManager chiamato");
        if (routeList == null){
            routeList = new ArrayList<>();
        }
    }

    public static RouteManager getInstance(){
        Log.d(TAG, "Recuperata l'istanza della classe RouteManager");
        if (instance == null){
            instance = new RouteManager();
        }
        return instance;
    }

    public void addRoute(Route route){
        Log.d(TAG, "Aggiunto un Percorso alla lista -> " + route.toString());
        routeList.add(route);
    }

    public void removeRoute(int position){
        Log.d(TAG, "Rimosso un Percorso dalla lista alla posizione: " + position);
        routeList.remove(position);
    }

    public void removeRoute(Route route){
        Log.d(TAG, "Rimosso un Percorso dalla lista -> " + route.toString());
        routeList.remove(route);
    }

    public ArrayList<Route> getRouteList(){
        Log.d(TAG, "Ritornata la lista completa dei Percorsi");
        return routeList;
    }
}
