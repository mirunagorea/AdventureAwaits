package com.example.adventureawaits;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {

    @Insert
    void insertTrip(TripEntity tripEntity);

    @Update
    void updateTrip(TripEntity tripEntity);

    @Query("SELECT * FROM trips")
    List<TripEntity> allTrips();

    @Query("SELECT * FROM trips WHERE name=:name")
    TripEntity findTripByName(String name);

    @Query("SELECT * FROM trips WHERE id=:id")
    TripEntity findTripById(int id);

}
