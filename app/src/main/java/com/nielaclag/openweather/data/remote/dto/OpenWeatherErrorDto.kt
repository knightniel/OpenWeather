package com.nielaclag.openweather.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 11/10/2024.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherErrorDto(
    @Json(name = "cod")
    val code: Int,
    @Json(name = "message")
    val message: String?
)