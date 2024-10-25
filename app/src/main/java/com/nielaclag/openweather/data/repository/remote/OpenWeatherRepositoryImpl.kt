package com.nielaclag.openweather.data.repository.remote

import com.nielaclag.openweather.data.remote.api.OpenWeatherApi
import com.nielaclag.openweather.data.remote.dto.LocationInfoDto
import com.nielaclag.openweather.data.remote.dto.WeatherDto
import com.nielaclag.openweather.domain.repository.remote.OpenWeatherRepository
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.http.Query

/**
 * Created by Niel on 10/21/2024.
 */
class OpenWeatherRepositoryImpl(
    private val openWeatherApi: OpenWeatherApi,
    private val moshi: Moshi
) : OpenWeatherRepository {

    override suspend fun getLocationsByName(
        appId: String,
        cityName: String,
        stateCode: String,
        countryCode: String,
        limit: Int
    ): Response<Array<LocationInfoDto>> {
        return openWeatherApi.getLocationsByName(
            appId = appId,
            query = "$cityName,$stateCode,$countryCode",
            limit = limit
        )
    }

    override suspend fun getLocationsByCoordinates(
        appId: String,
        latitude: Double,
        longitude: Double,
        limit: Int
    ): Response<Array<LocationInfoDto>> {
        return openWeatherApi.getLocationsByCoordinates(
            appId = appId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )
    }

    override suspend fun getCurrentWeather(
        appId: String,
        latitude: Double,
        longitude: Double,
        units: String?
    ): Response<WeatherDto> {
        return openWeatherApi.getCurrentWeather(
            appId = appId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )
    }

}