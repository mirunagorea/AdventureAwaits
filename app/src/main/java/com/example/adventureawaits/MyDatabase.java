package com.example.adventureawaits;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TripEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MyDatabase extends RoomDatabase {
    private static final String dbName = "trips";
    private static MyDatabase tripDatabase;

    public static synchronized MyDatabase getTripDatabase(Context context){
        if(tripDatabase == null){
            tripDatabase = Room.databaseBuilder(context, MyDatabase.class, dbName).fallbackToDestructiveMigration().build();
        }
        return tripDatabase;
    }

    public abstract TripDao tripDao();
}
