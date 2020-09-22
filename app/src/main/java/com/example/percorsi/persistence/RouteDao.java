package com.example.percorsi.persistence;

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

}
