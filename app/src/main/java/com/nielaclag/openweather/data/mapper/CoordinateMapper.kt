package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.supportmodel.CoordinatePersistence
import com.nielaclag.openweather.data.remote.dto.CoordinateDto
import com.nielaclag.openweather.domain.model.weather.Coordinate

/**
 * Created by Niel on 10/21/2024.
 */
fun Coordinate.toDto(): CoordinateDto {
    return CoordinateDto(
        latitude = latitude,
        longitude = longitude
    )
}

fun CoordinateDto.toDomain(): Coordinate {
    return Coordinate(
        latitude = latitude,
        longitude = longitude
    )
}

fun Coordinate.toEntity(): CoordinatePersistence {
    return CoordinatePersistence(
        latitude = latitude,
        longitude = longitude
    )
}

fun CoordinatePersistence.toDomain(): Coordinate {
    return Coordinate(
        latitude = latitude,
        longitude = longitude
    )
}