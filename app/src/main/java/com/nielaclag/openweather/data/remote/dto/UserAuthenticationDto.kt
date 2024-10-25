package com.nielaclag.openweather.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/24/2024.
 */
@JsonClass(generateAdapter = true)
data class UserAuthenticationDto(
    @Json(name = "id")
    val id: String,
    @Json(name = "email")
    val email: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "image")
    val image: String?
)