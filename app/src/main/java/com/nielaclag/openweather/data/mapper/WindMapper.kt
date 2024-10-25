package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.supportmodel.WindPersistence
import com.nielaclag.openweather.data.remote.dto.WindDto
import com.nielaclag.openweather.domain.model.weather.Wind

/**
 * Created by Niel on 10/21/2024.
 */
fun Wind.toDto(): WindDto {
    return WindDto(
        speed = speed,
        degrees = degrees,
        gust = gust
    )
}

fun WindDto.toDomain(): Wind {
    return Wind(
        speed = speed,
        degrees = degrees,
        gust = gust
    )
}

fun Wind.toEntity(): WindPersistence {
    return WindPersistence(
        speed = speed,
        degrees = degrees,
        gust = gust
    )
}

fun WindPersistence.toDomain(): Wind {
    return Wind(
        speed = speed,
        degrees = degrees,
        gust = gust
    )
}