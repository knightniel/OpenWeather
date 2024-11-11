package com.nielaclag.openweather.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/24/2024.
 */
@JsonClass(generateAdapter = true)
data class UserAuthentication(
    @Json(name = "name")
    val name: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "image")
    val image: String?
)