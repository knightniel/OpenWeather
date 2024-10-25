package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.supportmodel.CloudsPersistence
import com.nielaclag.openweather.data.remote.dto.CloudsDto
import com.nielaclag.openweather.domain.model.weather.Clouds

/**
 * Created by Niel on 10/21/2024.
 */
fun Clouds.toDto(): CloudsDto {
    return CloudsDto(
        all = all
    )
}

fun CloudsDto.toDomain(): Clouds {
    return Clouds(
        all = all
    )
}

fun Clouds.toEntity(): CloudsPersistence {
    return CloudsPersistence(
        all = all
    )
}

fun CloudsPersistence.toDomain(): Clouds {
    return Clouds(
        all = all
    )
}