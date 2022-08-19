package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid

class DetailViewModel(asteroid: Asteroid, application: Application): AndroidViewModel(application) {
    //Selected Asteroid editable variable to be used internally in ViewModel
    private val _selectedAsteroid = MutableLiveData<Asteroid>()

    //Selected Asteroid non-editable variable to be used externally
    val selectedAsteroid: LiveData<Asteroid>
    get() = _selectedAsteroid

    //Initialize _selectedAsteroid MutableLiveData
    init {
        _selectedAsteroid.value=asteroid
    }


}