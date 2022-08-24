package com.udacity.asteroidradar

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Database.AsteroidDatabase
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DataRepository(private val database: AsteroidDatabase) {
    val asteroids : LiveData<List<Asteroid>> = database.asteroidDao.getAsteroids()



    suspend fun refreshAsteroids(){

        withContext(Dispatchers.IO)
        {
           val asteroids = parseAsteroidsJsonResult(
                JSONObject(
                    Network
                        .asteroids
                        .getData(Constants.API_KEY)
                )
            )
            Log.i("Called arr size ",asteroids.size.toString())


//        asteroids.value= arrayListOf(
//            Asteroid(1,"Test","10/01/2020",123.2323,1313.21313,12313.123123,131.12313,true)
//            , Asteroid(2,"Test2","10/12/2020",123.2323,1313.21313,12313.123123,131.12313,false),
//            Asteroid(2,"Test3","01/01/2022",123.2323,1313.21313,12313.123123,131.12313,true)
         database.asteroidDao.delete()
        database.asteroidDao.insertAsteroids(asteroids)
        }

    }
}