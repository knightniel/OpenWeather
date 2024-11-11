package com.nielaclag.openweather.common.util

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = false)
sealed class Resource<out T>(
    @Json(name = "code")
    val code: Int? = null,
    @Json(name = "data")
    val data: T? = null,
    @Json(name = "loadingProgress")
    val loadingProgress: Float? = null,
    @Json(name = "message")
    val message: String? = null,
    @Json(name = "extra")
    val extra: Any? = null
) {

    class Loading<T: Any>(progress: Float? = 0f, extra: Any? = null) : Resource<T>(loadingProgress = progress, extra = extra)
    class Success<T: Any>(data: T?, message: String? = null, code: Int? = null, extra: Any? = null) : Resource<T>(code = code, data = data, message = message, extra = extra)
    class Error<T: Any>(message: String? = null, data: T? = null, code: Int? = null, extra: Any? = null) : Resource<T>(code = code, message = message, data = data, extra = extra)

}