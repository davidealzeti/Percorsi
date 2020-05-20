package com.example.percorsi.model;

import androidx.annotation.NonNull;

/**
 * Modello che rappresenta i Percorsi effettuati dall'utente.
 */
public class Route {
    private static final String TAG = "Percorso";

    private String name;
    private double startLatitude, startLongitude;

    public Route(String name, double startLatitude, double startLongitude){
        this.name = name;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    @NonNull
    @Override
    public String toString() {
        return "Nome Percorso: " + this.getName() +  "\nStart Lat: "  + this.getStartLatitude() + " Start Lon: " + this.getStartLongitude();
    }
}
