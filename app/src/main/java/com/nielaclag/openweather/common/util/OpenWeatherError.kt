package com.nielaclag.openweather.common.util

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherError(
    @Json(name = "cod")
    val cod: Int?,
    @Json(name = "message")
    val message: String?
)