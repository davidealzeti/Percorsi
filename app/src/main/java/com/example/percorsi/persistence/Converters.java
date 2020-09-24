package com.example.percorsi.persistence;



import android.location.Location;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<Location> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Location>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<Location> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    /*
    @TypeConverter
    public static Location fromLocation(String locationString){
        try {
            return new Gson().fromJson(locationString, Location.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    @TypeConverter
    public static String toLocation(Location loc){
        try {
            return new Gson().toJson(loc);
        } catch (Exception e) {
            return null;
        }
    }
     */
}
