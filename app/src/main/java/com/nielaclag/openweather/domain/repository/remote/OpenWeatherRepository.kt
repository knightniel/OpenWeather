package com.nielaclag.openweather.domain.repository.remote

import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.data.remote.dto.LocationInfoDto
import com.nielaclag.openweather.data.remote.dto.OpenWeatherErrorDto
import com.nielaclag.openweather.data.remote.dto.WeatherDto

/**
 * Created by Niel on 10/21/2024.
 */
interface OpenWeatherRepository {

    suspend fun getLocationsByName(
        appId: String,
        query: String,
        limit: Int
    ): DataResponse<Array<LocationInfoDto>, OpenWeatherErrorDto>

    suspend fun getLocationsByCoordinate(
        appId: String,
        latitude: Double,
        longitude: Double,
        limit: Int
    ): DataResponse<Array<LocationInfoDto>, OpenWeatherErrorDto>

    suspend fun getCurrentWeather(
        appId: String,
        latitude: Double,
        longitude: Double,
        units: String?
    ): DataResponse<WeatherDto, OpenWeatherErrorDto>

}