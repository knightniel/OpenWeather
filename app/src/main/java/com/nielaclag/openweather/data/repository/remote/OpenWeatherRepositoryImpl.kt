package com.nielaclag.openweather.data.repository.remote

import com.nielaclag.openweather.common.helper.toJsonObject
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.data.remote.api.OpenWeatherApi
import com.nielaclag.openweather.data.remote.dto.LocationInfoDto
import com.nielaclag.openweather.data.remote.dto.OpenWeatherErrorDto
import com.nielaclag.openweather.data.remote.dto.WeatherDto
import com.nielaclag.openweather.domain.repository.remote.OpenWeatherRepository
import com.squareup.moshi.Moshi

/**
 * Created by Niel on 10/21/2024.
 */
class OpenWeatherRepositoryImpl(
    private val openWeatherApi: OpenWeatherApi,
    private val moshi: Moshi
) : OpenWeatherRepository {

    override suspend fun getLocationsByName(
        appId: String,
        query: String,
        limit: Int
    ): DataResponse<Array<LocationInfoDto>, OpenWeatherErrorDto> {
        val response = openWeatherApi.getLocationsByName(
            appId = appId,
            query = query,
            limit = limit
        )
        return if (response.isSuccessful) {
            DataResponse.Success(
                data = response.body(),
                statusCode = response.code(),
                message = response.message()
            )
        } else {
            val responseError = response.errorBody()?.toJsonObject<OpenWeatherErrorDto>(moshi)
            DataResponse.Error(
                data = response.body(),
                statusCode = response.code(),
                error = responseError,
                message = responseError?.message
            )
        }
    }

    override suspend fun getLocationsByCoordinate(
        appId: String,
        latitude: Double,
        longitude: Double,
        limit: Int
    ): DataResponse<Array<LocationInfoDto>, OpenWeatherErrorDto> {
        val response = openWeatherApi.getLocationsByCoordinate(
            appId = appId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )
        return if (response.isSuccessful) {
            DataResponse.Success(
                data = response.body(),
                statusCode = response.code(),
                message = response.message()
            )
        } else {
            val responseError = response.errorBody()?.toJsonObject<OpenWeatherErrorDto>(moshi)
            DataResponse.Error(
                data = response.body(),
                statusCode = response.code(),
                error = responseError,
                message = responseError?.message
            )
        }
    }

    override suspend fun getCurrentWeather(
        appId: String,
        latitude: Double,
        longitude: Double,
        units: String?
    ): DataResponse<WeatherDto, OpenWeatherErrorDto> {
        val response = openWeatherApi.getCurrentWeather(
            appId = appId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )
        return if (response.isSuccessful) {
            DataResponse.Success(
                data = response.body(),
                statusCode = response.code(),
                message = response.message()
            )
        } else {
            val responseError = response.errorBody()?.toJsonObject<OpenWeatherErrorDto>(moshi)
            DataResponse.Error(
                data = response.body(),
                statusCode = response.code(),
                error = responseError,
                message = responseError?.message
            )
        }
    }

}