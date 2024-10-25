package com.nielaclag.openweather.domain.model.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class LocationInfo(
    @Json(name = "name")
    val name : String,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "country")
    val country: String,
    @Json(name = "state")
    val state: String?
)