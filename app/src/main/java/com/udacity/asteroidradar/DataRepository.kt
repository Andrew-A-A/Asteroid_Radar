package com.udacity.asteroidradar

import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DataRepository {
    val asteroids =MutableLiveData<ArrayList<Asteroid>>()
    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO)
        {
            val asteroids = parseAsteroidsJsonResult(
                JSONObject(
                    Network
                        .asteroids
                        .getData("2015-09-07", "2015-09-08", Constants.API_KEY)
                )
            )
        }
    }
}