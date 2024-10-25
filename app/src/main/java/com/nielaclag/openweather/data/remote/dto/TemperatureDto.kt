package com.nielaclag.openweather.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class TemperatureDto(
    @Json(name = "temp")
    val temp: Float,
    @Json(name = "feels_like")
    val feelsLike: Float,
    @Json(name = "temp_min")
    val tempMin: Float,
    @Json(name = "temp_max")
    val tempMax: Float,
    @Json(name = "pressure")
    val pressure: Int,
    @Json(name = "humidity")
    val humidity: Int,
    @Json(name = "sea_level")
    val seaLevel: Int,
    @Json(name = "grnd_level")
    val groundLevel: Int
)