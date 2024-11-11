package com.nielaclag.openweather.domain.model.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherError(
    @Json(name = "code")
    val code: Int,
    @Json(name = "message")
    val message: String?
)