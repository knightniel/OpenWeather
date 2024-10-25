package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.supportmodel.SysPersistence
import com.nielaclag.openweather.data.remote.dto.SysDto
import com.nielaclag.openweather.domain.model.weather.Sys

/**
 * Created by Niel on 10/21/2024.
 */
fun Sys.toDto(): SysDto {
    return SysDto(
        type = type,
        id = id,
        message = message,
        country = country,
        sunrise = sunrise,
        sunset = sunset
    )
}

fun SysDto.toDomain(): Sys {
    return Sys(
        type = type,
        id = id,
        message = message,
        country = country,
        sunrise = sunrise,
        sunset = sunset
    )
}

fun Sys.toEntity(): SysPersistence {
    return SysPersistence(
        type = type,
        id = id,
        message = message,
        country = country,
        sunrise = sunrise,
        sunset = sunset
    )
}

fun SysPersistence.toDomain(): Sys {
    return Sys(
        type = type,
        id = id,
        message = message,
        country = country,
        sunrise = sunrise,
        sunset = sunset
    )
}