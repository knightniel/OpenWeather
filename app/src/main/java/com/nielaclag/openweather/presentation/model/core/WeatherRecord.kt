package com.nielaclag.openweather.presentation.model.core

import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.presentation.model.type.SunsetSunrise
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/23/2024.
 */
@JsonClass(generateAdapter = true)
data class WeatherRecord(
    @Json(name = "weather")
    val weather: Weather,
    @Json(name = "dateTime")
    val dateTime: Long,
    @Json(name = "sunrise")
    val sunrise: Long,
    @Json(name = "sunset")
    val sunset: Long,
    @Json(name = "sunsetSunrise")
    val sunsetSunrise: SunsetSunrise
)