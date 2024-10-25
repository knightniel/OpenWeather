package com.nielaclag.openweather.common.constants

import com.nielaclag.openweather.BuildConfig

/**
 * Created by Niel on 10/21/2024.
 */
object Constants {

    val BASE_URL by lazy {
        "https://base_url_not_defined/"
    }

    val OPEN_WEATHER_API_URL by lazy {
        "https://api.openweathermap.org/"
    }

    val OPEN_WEATHER_URL by lazy {
        "https://openweathermap.org/"
    }

    val LOGGING_ENABLED by lazy {
        when (BuildConfig.BUILD_VARIANT) {
            "release" -> true
            "development" -> true
            "debug" -> true
            else -> false
        }
    }

    val OPEN_WEATHER_API_KEY by lazy {
        BuildConfig.OPEN_WEATHER_API_KEY
    }

    val GOOGLE_OAUTH_SERVER_CLIENT_ID by lazy {
        BuildConfig.GOOGLE_OAUTH_SERVER_CLIENT_ID
    }

}