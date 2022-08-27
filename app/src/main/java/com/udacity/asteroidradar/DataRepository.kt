package com.udacity.asteroidradar

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Database.AsteroidDatabase
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DataRepository(private val database: AsteroidDatabase) {
    private val current: LocalDateTime = LocalDateTime.now()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)
    private val startDate: String =current.format(formatter)

    val asteroids : LiveData<List<Asteroid>> = database.asteroidDao.getAsteroids()
    val asteroidsToday : LiveData<List<Asteroid>> = database.asteroidDao.getTodayAsteroids(startDate)

    val pictureOfDay= MutableLiveData<PictureOfDay>()


    suspend fun refreshAsteroids(startDate:String){

        withContext(Dispatchers.IO)
        {
           val asteroids = parseAsteroidsJsonResult(
                JSONObject(
                    Network
                        .asteroids
                        .getData(startDate,API_KEY)
                )
            )
            Log.i("Called arr size ",asteroids.size.toString())

         database.asteroidDao.delete()
        database.asteroidDao.insertAsteroids(asteroids)
        }

    }

    suspend fun refreshPictureOfDay(){
        val pictureOfDay= withContext(Dispatchers.IO){
            val pictureOfDay= Network.asteroids.getPictureOfDay(API_KEY)
            return@withContext pictureOfDay
        }
        pictureOfDay.let {
            this.pictureOfDay.value=it
        }
    }
    fun getCachedPictureOfDay(url:String,title:String){
        pictureOfDay.value= PictureOfDay("image",title,url)
    }
}