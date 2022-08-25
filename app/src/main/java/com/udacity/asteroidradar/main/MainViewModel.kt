package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log


import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT

import com.udacity.asteroidradar.DataRepository
import com.udacity.asteroidradar.Database.getDatabase
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import kotlin.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var filter= MutableLiveData(Filter.ALL)

    private val database= getDatabase(application)
    private val repository= DataRepository(database)
    private var sharedPreferences: SharedPreferences =application.getSharedPreferences("PodCache", Context.MODE_PRIVATE)
    private var pictureOfDayCache: SharedPreferences.Editor =sharedPreferences.edit()

    private val current: LocalDateTime =LocalDateTime.now()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(API_QUERY_DATE_FORMAT)
    private val startDate: String =current.format(formatter)


    private val _navigateToSelectedAsteroid =MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
    get() = _navigateToSelectedAsteroid



   init {
                refreshData()
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsCompleted(){
        _navigateToSelectedAsteroid.value=null
    }

    private fun refreshData(){
        Log.i("start Date ",startDate)
        viewModelScope.launch {
            try {
                repository.refreshAsteroids(startDate)

            }
            catch (e: Exception){
                Log.i("err","No internet")
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
    val asteroidsToday=repository.asteroidsToday
    val pictureOfDay=repository.pictureOfDay

    private fun cachePictureOfDay(pictureOfDay: PictureOfDay){
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