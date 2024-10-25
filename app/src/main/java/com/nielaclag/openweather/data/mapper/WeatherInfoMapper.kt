package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.supportmodel.WeatherInfoPersistence
import com.nielaclag.openweather.data.remote.dto.WeatherInfoDto
import com.nielaclag.openweather.domain.model.weather.WeatherInfo

/**
 * Created by Niel on 10/21/2024.
 */
fun WeatherInfo.toDto(): WeatherInfoDto {
    return WeatherInfoDto(
        id = id,
        main = main,
        description = description,
        icon = icon
    )
}

fun WeatherInfoDto.toDomain(): WeatherInfo {
    return WeatherInfo(
        id = id,
        main = main,
        description = description,
        icon = icon
    )
}

fun WeatherInfo.toEntity(): WeatherInfoPersistence {
    return WeatherInfoPersistence(
        id = id,
        main = main,
        description = description,
        icon = icon
    )
}

fun WeatherInfoPersistence.toDomain(): WeatherInfo {
    return WeatherInfo(
        id = id,
        main = main,
        description = description,
        icon = icon
    )
}