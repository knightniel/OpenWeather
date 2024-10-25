package com.nielaclag.openweather.data.model.room.supportmodel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class TemperaturePersistence(
    @Json(name = "temp")
    val temp: Float,
    @Json(name = "feelsLike")
    val feelsLike: Float,
    @Json(name = "tempMin")
    val tempMin: Float,
    @Json(name = "tempMax")
    val tempMax: Float,
    @Json(name = "pressure")
    val pressure: Int,
    @Json(name = "humidity")
    val humidity: Int,
    @Json(name = "seaLevel")
    val seaLevel: Int,
    @Json(name = "groundLevel")
    val groundLevel: Int
)