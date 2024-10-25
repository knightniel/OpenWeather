package com.nielaclag.openweather.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class LocationInfoDto(
    @Json(name = "name")
    val name: String,
    @Json(name = "lat")
    val latitude: Double,
    @Json(name = "lon")
    val longitude: Double,
    @Json(name = "country")
    val country: String,
    @Json(name = "state")
    val state: String?
)