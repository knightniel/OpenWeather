package com.nielaclag.openweather.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class WindDto(
    @Json(name = "speed")
    val speed: Float,
    @Json(name = "deg")
    val degrees: Int,
    @Json(name = "gust")
    val gust: Float?
)