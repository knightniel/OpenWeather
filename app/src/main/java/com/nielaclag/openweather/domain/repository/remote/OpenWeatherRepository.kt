package com.nielaclag.openweather.domain.repository.remote

import com.nielaclag.openweather.data.remote.dto.LocationInfoDto
import com.nielaclag.openweather.data.remote.dto.WeatherDto
import retrofit2.Response

/**
 * Created by Niel on 10/21/2024.
 */
interface OpenWeatherRepository {

    suspend fun getLocationsByName(
        appId: String,
        cityName: String,
        stateCode: String,
        countryCode: String,
        limit: Int
    ): Response<Array<LocationInfoDto>>

    suspend fun getLocationsByCoordinates(
        appId: String,
        latitude: Double,
        longitude: Double,
        limit: Int
    ): Response<Array<LocationInfoDto>>

    suspend fun getCurrentWeather(
        appId: String,
        latitude: Double,
        longitude: Double,
        units: String?
    ): Response<WeatherDto>

}