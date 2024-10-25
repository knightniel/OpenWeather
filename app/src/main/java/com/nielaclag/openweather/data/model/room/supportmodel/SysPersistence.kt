package com.nielaclag.openweather.data.model.room.supportmodel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class SysPersistence(
    @Json(name = "type")
    val type: Int?,
    @Json(name = "id")
    val id: Long?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "country")
    val country: String,
    @Json(name = "sunrise")
    val sunrise: Long,
    @Json(name = "sunset")
    val sunset: Long
)