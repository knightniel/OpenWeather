package com.nielaclag.openweather.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class WeatherDto(
    @Json(name = "coord")
    val coordinate: CoordinateDto,
    @Json(name = "weather")
    val weatherInfo: List<WeatherInfoDto>,
    @Json(name = "base")
    val base: String,
    @Json(name = "main")
    val temperature: TemperatureDto,
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "wind")
    val wind: WindDto?,
    @Json(name = "clouds")
    val clouds: CloudsDto?,
    @Json(name = "rain")
    val rain: RainDto?,
    @Json(name = "snow")
    val snow: SnowDto?,
    @Json(name = "dt")
    val dt: Long,
    @Json(name = "sys")
    val sys: SysDto,
    @Json(name = "timezone")
    val timezone: Int,
    @Json(name = "id")
    val cityId: Long,
    @Json(name = "name")
    val cityName: String,
    @Json(name = "cod")
    val cod: Int
)