package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.supportmodel.TemperaturePersistence
import com.nielaclag.openweather.data.remote.dto.TemperatureDto
import com.nielaclag.openweather.domain.model.weather.Temperature

/**
 * Created by Niel on 10/21/2024.
 */
fun Temperature.toDto(): TemperatureDto {
    return TemperatureDto(
        temp = temp,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        pressure = pressure,
        humidity = humidity,
        seaLevel = seaLevel,
        groundLevel = groundLevel
    )
}

fun TemperatureDto.toDomain(): Temperature {
    return Temperature(
        temp = temp,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        pressure = pressure,
        humidity = humidity,
        seaLevel = seaLevel,
        groundLevel = groundLevel
    )
}

fun Temperature.toEntity(): TemperaturePersistence {
    return TemperaturePersistence(
        temp = temp,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        pressure = pressure,
        humidity = humidity,
        seaLevel = seaLevel,
        groundLevel = groundLevel
    )
}

fun TemperaturePersistence.toDomain(): Temperature {
    return Temperature(
        temp = temp,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        pressure = pressure,
        humidity = humidity,
        seaLevel = seaLevel,
        groundLevel = groundLevel
    )
}