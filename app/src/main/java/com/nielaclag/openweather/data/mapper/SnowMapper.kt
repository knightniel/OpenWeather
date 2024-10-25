package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.supportmodel.SnowPersistence
import com.nielaclag.openweather.data.remote.dto.SnowDto
import com.nielaclag.openweather.domain.model.weather.Snow

/**
 * Created by Niel on 10/21/2024.
 */
fun Snow.toDto(): SnowDto {
    return SnowDto(
        oneHour = oneHour
    )
}

fun SnowDto.toDomain(): Snow {
    return Snow(
        oneHour = oneHour
    )
}

fun Snow.toEntity(): SnowPersistence {
    return SnowPersistence(
        oneHour = oneHour
    )
}

fun SnowPersistence.toDomain(): Snow {
    return Snow(
        oneHour = oneHour
    )
}