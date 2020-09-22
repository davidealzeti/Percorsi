package com.example.percorsi.model;

import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Modello che rappresenta i Percorsi effettuati dall'utente.
 */
@Entity(tableName = "routes")
public class Route implements Parcelable {
    private static final String TAG = "Percorso";

    private static final double UNSET = 0;
    private static final double DONE = 73;

    public static final String ON_FOOT = "A piedi";
    public static final String BY_CAR = "In macchina";
    public static final String BY_PUBLIC_TRANSPORT = "Mezzi pubblici";

    @PrimaryKey
    @NonNull
    private String name;
    private String meansOfTransport;
    @ColumnInfo(name = "startLat")
    private double startLatitude;
    @ColumnInfo(name = "startLon")
    private double startLongitude;
    @Ignore
    private double stopLatitude = UNSET, stopLongitude = UNSET;
    private Date startDate;
    private Date stopDate;
    @Ignore
    private double routeLength = UNSET;
    private double averageSpeed;
    private double averageAccuracy;
    private double done = UNSET;

    private ArrayList<Location> locationsArray;
    @Ignore
    private Polyline routePolyline;


    public static final Comparator<Route> SORT_BY_MOST_RECENT = new Comparator<Route>() {
        @Override
        public int compare(Route o1, Route o2) {
            if(o1.getStartDate().before(o2.getStartDate())) return 0;
            return -1;
        }
    };

    public static final Comparator<Route> SORT_BY_LEAST_RECENT = new Comparator<Route>() {
        @Override
        public int compare(Route o1, Route o2) {
            if(o1.getStartDate().before(o2.getStartDate())) return -1;
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


    public Route(String name, String meansOfTransport){
        Log.d(TAG, "Chiamato costruttore della classe Route");
        this.name = name;
        this.meansOfTransport = meansOfTransport;
        this.startDate = new Date();
        this.startLatitude = 0;
        this.startLongitude = 0;
        this.locationsArray = new ArrayList<>();
        this.averageAccuracy = 0;
        this.averageSpeed = 0;
    }

    private Route(Parcel in) {
        name = in.readString();
        meansOfTransport = in.readString();
        startDate = new Date(in.readLong());
        stopDate = new Date(in.readLong());
        startLatitude = in.readDouble();
        startLongitude = in.readDouble();
        stopLatitude = in.readDouble();
        stopLongitude = in.readDouble();
        routeLength = in.readDouble();
        averageSpeed = in.readDouble();
        averageAccuracy = in.readDouble();
        locationsArray = in.readArrayList(null);
        done = in.readDouble();
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

    public Date getStartDate() {
        return startDate;
    }

    public String getFormattedDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy kk:mm", Locale.getDefault());

        return formatter.format(this.getStartDate());
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setAverageAccuracy(double averageAccuracy) {
        this.averageAccuracy = averageAccuracy;
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

    public ArrayList<Location> getLocationsArray() {
        return locationsArray;
    }

    public void setLocationsArray(ArrayList<Location> locationsArray) {
        this.locationsArray = locationsArray;
    }

    public double getAverageSpeed() {
        double tot = 0;
        if (locationsArray.size() != 0) {
            for (Location loc : locationsArray){
                tot += loc.getSpeed();
            }
            averageSpeed = tot / locationsArray.size();
            averageSpeed = averageSpeed * (18.0/5.0);
            return averageSpeed;
        }
        return 0;
    }

    public double getAverageAccuracy() {
        double tot = 0;
        if (locationsArray.size() != 0) {
            for (Location loc : locationsArray){
                tot += loc.getAccuracy();
            }
            averageAccuracy = tot / locationsArray.size();
            return averageAccuracy;
        }
        return 0;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public void addLocationToLocationsArray(Location location){
        locationsArray.add(location);
    }

    public Polyline getRoutePolyline() {
        return routePolyline;
    }

    public void setRoutePolyline(Polyline routePolyline) {
        this.routePolyline = routePolyline;
    }

    public double getDone() {
        return done;
    }

    public void setDone(double done) {
        this.done = done;
    }

    public void addPolylineToGoogleMap(GoogleMap gMap){
        PolylineOptions options = new PolylineOptions();
        for (Location loc : locationsArray){
            options.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
        }
        options.color(Color.BLUE);
        options.width(12);
        routePolyline = gMap.addPolyline(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return name.equals(route.name) &&
                Objects.equals(meansOfTransport, route.meansOfTransport) &&
                Objects.equals(startDate, route.startDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(name, meansOfTransport, startDate);
    }

    @NonNull
    @Override
    public String toString() {
        return "Nome Percorso: " + this.getName() + " Mezzo di trasporto: " + this.getMeansOfTransport()  +
                "\nStart Lat: "  + this.getStartLatitude() + " Start Lon: " + this.getStartLongitude() +
                "\nDataInizio: " + this.getStartDate() + " DataFine: " + this.getStopDate() +
                "\nVelMedia: " + this.getAverageSpeed() + " AccuratezzaMedia: " + this.getAverageAccuracy() +
                "\nFinito: " + this.getDone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(meansOfTransport);
        dest.writeLong(startDate.getTime());
        dest.writeDouble(startLatitude);
        dest.writeDouble(startLongitude);
        dest.writeDouble(stopLatitude);
        dest.writeDouble(stopLongitude);
        dest.writeDouble(routeLength);
        dest.writeDouble(averageSpeed);
        dest.writeDouble(averageAccuracy);
        dest.writeDouble(done);
    }
}
