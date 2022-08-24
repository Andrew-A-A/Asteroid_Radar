package com.udacity.asteroidradar.Database

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [Asteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase(){
    abstract val asteroidDao: AsteroidDao
}
//Instance variable used to make sure that the database will be initialized only once
private lateinit var INSTANCE :AsteroidDatabase


//function that initialize database in case it is not



@OptIn(InternalCoroutinesApi::class)
fun getDatabase(context: Context): AsteroidDatabase{
    synchronized(AsteroidDatabase::class.java){
        if (!::INSTANCE.isInitialized){
            INSTANCE=Room.databaseBuilder(context.applicationContext,AsteroidDatabase::class.java,
            "Database").build()
        }
    }
    return INSTANCE
}