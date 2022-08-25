package com.udacity.asteroidradar.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.udacity.asteroidradar.Asteroid

//Dao used to get data from database
@Dao
interface AsteroidDao {

    //Getter for asteroids from database
    @androidx.room.Query("select * from asteroid ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<Asteroid>>
    @androidx.room.Query("select * from asteroid WHERE closeApproachDate=:startDate ")
    fun getTodayAsteroids(startDate : String): LiveData<List<Asteroid>>
    //Function to insert data in Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(asteroids: ArrayList<Asteroid>)
    //Delete the Asteroids table
    @androidx.room.Query("DELETE FROM asteroid")
    fun delete()
}
