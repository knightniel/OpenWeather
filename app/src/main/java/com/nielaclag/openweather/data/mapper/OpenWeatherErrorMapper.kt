package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.domain.model.weather.OpenWeatherError
import com.nielaclag.openweather.data.remote.dto.OpenWeatherErrorDto

/**
 * Created by Niel on 11/10/2024.
 */

fun OpenWeatherError.toDto(): OpenWeatherErrorDto {
    return OpenWeatherErrorDto(
        code = code,
        message = message
    )
}

fun OpenWeatherErrorDto.toDomain(): OpenWeatherError {
    return OpenWeatherError(
        code = code,
        message = message
    )
}