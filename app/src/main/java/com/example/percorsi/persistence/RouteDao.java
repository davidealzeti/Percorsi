package com.example.percorsi.persistence;

import android.location.Location;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.percorsi.model.Route;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RouteDao {

    @Insert
    void insertRoute(Route route);

    @Query("SELECT * FROM routes ORDER BY name DESC")
    List<Route> getAll();

    @Query("SELECT * FROM routes WHERE name = (:routeName)")
    Route getRouteWithName(String routeName);

    @Delete
    void delete(Route route);

    @Query("DELETE FROM routes")
    void deleteAll();

    @Update
    void updateRoutes(Route... routes);

    @Query("UPDATE routes SET done = (:done) WHERE name = (:routeName)")
    void setDone(String routeName, double done);

    @Query("UPDATE routes SET startLat = (:lat), startLon = (:lon) WHERE name = (:name)")
    void setStartLatitudeAndLongitude(String name, double lat, double lon);

    @Query("UPDATE routes SET averageSpeed = (:speed), averageAccuracy = (:accuracy) WHERE name = (:name)")
    void setAverageSpeedAndAccuracy(String name, double speed, double accuracy);

    /*
    @Query("SELECT locationsArray FROM routes WHERE name = (:name)")
    List<Location> getLocationList(String name);

    @Query("UPDATE routes SET locationsArray = (:list) WHERE name = (:name)")
    void setLocationList(String name, List<Location> list);
    */

}
