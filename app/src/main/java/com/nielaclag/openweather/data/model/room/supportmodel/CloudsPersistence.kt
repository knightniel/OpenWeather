package com.nielaclag.openweather.data.model.room.supportmodel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class CloudsPersistence(
    @Json(name = "all")
    val all: Int
)