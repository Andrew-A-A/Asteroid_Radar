package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid

import com.udacity.asteroidradar.DataRepository
import com.udacity.asteroidradar.Database.getDatabase
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.launch

import kotlin.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val database= getDatabase(application)
     val repository= DataRepository(database)
    var sharedPreferences=application.getSharedPreferences("PodCache", Context.MODE_PRIVATE)
        var pictureOfDayCache=sharedPreferences.edit()
       // lateinit var today :String
       // lateinit var next77Days : String

    // Internally, we use a MutableLiveData, because we will be updating the List of Asteroids
    // with new values
   // private var _asteroids = MutableLiveData<ArrayList<Asteroid>>()

    // The external LiveData interface to the asteroid is immutable, so only this class can modify
//    val asteroids: LiveData<ArrayList<Asteroid>>
//        get() = _asteroids

    private val _navigateToSelectedAsteroid =MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
    get() = _navigateToSelectedAsteroid



   init {
                refreshData()
    }
//init {
//    _asteroids.value= arrayListOf(
//        Asteroid(1,"Test","10/01/2020",123.2323,1313.21313,12313.123123,131.12313,true)
//        , Asteroid(2,"Test2","10/12/2020",123.2323,1313.21313,12313.123123,131.12313,false),
//        Asteroid(2,"Test3","01/01/2022",123.2323,1313.21313,12313.123123,131.12313,true)
//    )
//
//}

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsCompleted(){
        _navigateToSelectedAsteroid.value=null
    }

    private fun refreshData(){
       // getTodayAndNext7()
        viewModelScope.launch {
            try {
                repository.refreshAsteroids()
            //   repository.refreshAsteroids(today,next77Days)

            }
            catch (e: Exception){
                e.printStackTrace()
            }

            try {
                repository.refreshPictureOfDay()
                repository.pictureOfDay.value?.let { cachePictureOfDay(it) }
            }
            catch (e:Exception){
                sharedPreferences.getString("URL","")?.let {
                    repository.getCachedPictureOfDay(it,
                        sharedPreferences.getString("Description","")!!
                    ) }
            }
        }
    }
    val asteroids=repository.asteroids
    val pictureOfDay=repository.pictureOfDay

    fun cachePictureOfDay(pictureOfDay: PictureOfDay){
        pictureOfDayCache.putString("URL",pictureOfDay.url)
        pictureOfDayCache.putString("Description",pictureOfDay.title)
        pictureOfDayCache.apply()
    }

//    fun getTodayAndNext7(){
//        val calendar = java.util.Calendar.getInstance()
//
//        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
//        today = dateFormat.format(calendar)
//        calendar.add(java.util.Calendar.DAY_OF_YEAR, 7)
//        val lastTime = calendar.time
//        next77Days = dateFormat.format(calendar)}
//
//
}