package com.nielaclag.openweather.domain.model.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed")
    val speed: Float,
    @Json(name = "degrees")
    val degrees: Int,
    @Json(name = "gust")
    val gust: Float?
)