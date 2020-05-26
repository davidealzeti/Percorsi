package com.example.percorsi.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Modello che rappresenta i Percorsi effettuati dall'utente.
 */
public class Route implements Parcelable {
    private static final String TAG = "Percorso";

    private static final double UNSET = 0;

    public static final String ON_FOOT = "A piedi";
    public static final String BY_CAR = "In macchina";
    public static final String BY_PUBLIC_TRANSPORT = "Mezzi pubblici";

    private String name;
    private String meansOfTransport;
    private double startLatitude, startLongitude;
    private double stopLatitude = UNSET, stopLongitude = UNSET;
    private Date date;
    private double routeLength = UNSET;
    private double averageSpeed = UNSET;
    private double averageAccuracy = UNSET;

    //TODO: aggiungere Durata, Polyline e Foto scattata ai campi privati

    public static final Comparator<Route> SORT_BY_MOST_RECENT = new Comparator<Route>() {
        @Override
        public int compare(Route o1, Route o2) {
            if(o1.getDate().before(o2.getDate())) return 0;
            return -1;
        }
    };

    public static final Comparator<Route> SORT_BY_LEAST_RECENT = new Comparator<Route>() {
        @Override
        public int compare(Route o1, Route o2) {
            if(o1.getDate().before(o2.getDate())) return -1;
            return 0;
        }
    };

    public static final Comparator<Route> SORT_BY_ROUTE_LENGTH = new Comparator<Route>() {
        @Override
        public int compare(Route o1, Route o2) {
            if(o1.getRouteLength() >= o2.getRouteLength()) return 0;
            return -1;
        }
    };

    public static final Comparator<Route> SORT_BY_AVERAGE_SPEED = new Comparator<Route>() {
        @Override
        public int compare(Route o1, Route o2) {
            if(o1.getAverageSpeed() >= o2.getAverageSpeed()) return 0;
            return -1;
        }
    };


    public Route(String name, String meansOfTransport, double startLatitude, double startLongitude){
        Log.d(TAG, "Chiamato costruttore della classe Route");
        this.name = name;
        this.meansOfTransport = meansOfTransport;
        this.date = new Date();
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
    }

    private Route(Parcel in) {
        name = in.readString();
        meansOfTransport = in.readString();
        date = new Date(in.readLong());
        startLatitude = in.readDouble();
        startLongitude = in.readDouble();
        stopLatitude = in.readDouble();
        stopLongitude = in.readDouble();
        routeLength = in.readDouble();
        averageSpeed = in.readDouble();
        averageAccuracy = in.readDouble();
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

    public Date getDate() {
        return date;
    }

    public String getFormattedDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy kk:mm", Locale.getDefault());

        return formatter.format(this.getDate());
    }

    public double getStopLatitude() {
        return stopLatitude;
    }

    public double getStopLongitude() {
        return stopLongitude;
    }

    public double getRouteLength() {
        return routeLength;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public double getAverageAccuracy() {
        return averageAccuracy;
    }

    @NonNull
    @Override
    public String toString() {
        return "Nome Percorso: " + this.getName() + " Mezzo di trasporto: " + this.getMeansOfTransport()  +
                "\nStart Lat: "  + this.getStartLatitude() + " Start Lon: " + this.getStartLongitude() +
                "\nData: " + this.getDate();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(meansOfTransport);
        dest.writeLong(date.getTime());
        dest.writeDouble(startLatitude);
        dest.writeDouble(startLongitude);
        dest.writeDouble(stopLatitude);
        dest.writeDouble(stopLongitude);
        dest.writeDouble(routeLength);
        dest.writeDouble(averageSpeed);
        dest.writeDouble(averageAccuracy);
    }
}
