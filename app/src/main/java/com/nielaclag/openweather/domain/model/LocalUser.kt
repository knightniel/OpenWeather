package com.nielaclag.openweather.domain.model

import com.nielaclag.openweather.domain.model.type.AuthenticationType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class LocalUser(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "authenticationType")
    val authenticationType: AuthenticationType
)