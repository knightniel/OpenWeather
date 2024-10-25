package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.entity.WeatherEntity
import com.nielaclag.openweather.data.remote.dto.WeatherDto
import com.nielaclag.openweather.domain.model.weather.Weather

/**
 * Created by Niel on 10/21/2024.
 */
fun Weather.toDto(): WeatherDto {
    return WeatherDto(
        coordinate = coordinate.toDto(),
        weatherInfo = weatherInfo.map { it.toDto() },
        base = base,
        temperature = temperature.toDto(),
        visibility = visibility,
        wind = wind?.toDto(),
        clouds = clouds?.toDto(),
        rain = rain?.toDto(),
        snow = snow?.toDto(),
        dt = dt,
        sys = sys.toDto(),
        timezone = timezone,
        cityId = cityId,
        cityName = cityName,
        cod = cod
    )
}

fun WeatherDto.toDomain(): Weather {
    return Weather(
        id = 0,
        coordinate = coordinate.toDomain(),
        weatherInfo = weatherInfo.map { it.toDomain() },
        base = base,
        temperature = temperature.toDomain(),
        visibility = visibility,
        wind = wind?.toDomain(),
        clouds = clouds?.toDomain(),
        rain = rain?.toDomain(),
        snow = snow?.toDomain(),
        dt = dt,
        sys = sys.toDomain(),
        timezone = timezone,
        cityId = cityId,
        cityName = cityName,
        cod = cod
    )
}

fun Weather.toEntity(): WeatherEntity {
    return WeatherEntity(
        id = id,
        coordinate = coordinate.toEntity(),
        weatherInfo = weatherInfo.map { it.toEntity() },
        base = base,
        temperature = temperature.toEntity(),
        visibility = visibility,
        wind = wind?.toEntity(),
        clouds = clouds?.toEntity(),
        rain = rain?.toEntity(),
        snow = snow?.toEntity(),
        dt = dt,
        sys = sys.toEntity(),
        timezone = timezone,
        cityId = cityId,
        cityName = cityName,
        cod = cod
    )
}

fun WeatherEntity.toDomain(): Weather {
    return Weather(
        id = id,
        coordinate = coordinate.toDomain(),
        weatherInfo = weatherInfo.map { it.toDomain() },
        base = base,
        temperature = temperature.toDomain(),
        visibility = visibility,
        wind = wind?.toDomain(),
        clouds = clouds?.toDomain(),
        rain = rain?.toDomain(),
        snow = snow?.toDomain(),
        dt = dt,
        sys = sys.toDomain(),
        timezone = timezone,
        cityId = cityId,
        cityName = cityName,
        cod = cod
    )
}