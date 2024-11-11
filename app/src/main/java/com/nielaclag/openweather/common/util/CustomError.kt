package com.nielaclag.openweather.common.util

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 11/8/2024.
 */
@JsonClass(generateAdapter = true)
data class CustomError(
    @Json(name = "message")
    val message: String?,
    @Json(name = "code")
    val code: Int? = null
)