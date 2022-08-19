package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.DataRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val repository= DataRepository()
    // Internally, we use a MutableLiveData, because we will be updating the List of Asteroids
    // with new values
    private var _asteroids = MutableLiveData<ArrayList<Asteroid>>()

    // The external LiveData interface to the asteroid is immutable, so only this class can modify
    val asteroids: LiveData<ArrayList<Asteroid>>
        get() = _asteroids

    private val _navigateToSelectedAsteroid =MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid:LiveData<Asteroid>
    get() = _navigateToSelectedAsteroid



   init {
        getAsteroids()
       Log.i("test", _asteroids.value?.get(0)?.codename.toString())
    }
//init {
//    _asteroids.value= listOf(
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

    private fun getAsteroids(){
        viewModelScope.launch {
            try {
                _asteroids=repository.asteroids
               repository.refreshAsteroids()
//                _asteroids=repository.asteroids
            }
            catch (e: Exception){
                _asteroids.value= arrayListOf(
                    Asteroid(1,"ERROR",
                        "00/00/0000",
                        0.0,
                        0.0,
                        0.0,
                        0.0,
                        true))
            }
        }
    }

}