package com.nielaclag.openweather.data.remote.api

import com.nielaclag.openweather.data.remote.dto.LocationInfoDto
import com.nielaclag.openweather.data.remote.dto.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Niel on 10/21/2024.
 */
interface OpenWeatherApi {

    @GET("geo/1.0/direct")
    suspend fun getLocationsByName(
        @Query("appid") appId: String,
        @Query("q") query: String,
        @Query("limit") limit: Int,
    ): Response<Array<LocationInfoDto>>

    @GET("geo/1.0/reverse")
    suspend fun getLocationsByCoordinates(
        @Query("appid") appId: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int,
    ): Response<Array<LocationInfoDto>>

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("appid") appId: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String?
    ): Response<WeatherDto>

}