package com.example.percorsi.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Modello che rappresenta i Percorsi effettuati dall'utente.
 */
public class Route implements Parcelable {
    private static final String TAG = "Percorso";

    public static final String ON_FOOT = "A piedi";
    public static final String BY_CAR = "In macchina";
    public static final String BY_PUBLIC_TRANSPORT = "Mezzi pubblici";

    private String name;
    private String meansOfTransport;
    private double startLatitude, startLongitude;

    public Route(String name, String meansOfTransport, double startLatitude, double startLongitude){
        Log.d(TAG, "Chiamato costruttore della classe Route");
        this.name = name;
        this.meansOfTransport = meansOfTransport;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
    }

    private Route(Parcel in) {
        name = in.readString();
        meansOfTransport = in.readString();
        startLatitude = in.readDouble();
        startLongitude = in.readDouble();
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

    public String getMeansOfTransport() {
        return meansOfTransport;
    }

    public void setMeansOfTransport(String meansOfTransport) {
        this.meansOfTransport = meansOfTransport;
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "Nome Percorso: " + this.getName() + " Mezzo di trasporto: " + this.getMeansOfTransport()  +
                "\nStart Lat: "  + this.getStartLatitude() + " Start Lon: " + this.getStartLongitude();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(meansOfTransport);
        dest.writeDouble(startLatitude);
        dest.writeDouble(startLongitude);
    }
}
