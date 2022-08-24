package com.udacity.asteroidradar

import com.squareup.moshi.Json

data class PictureOfDay(
    val media_type: String,
    val title: String,
    val url: String)