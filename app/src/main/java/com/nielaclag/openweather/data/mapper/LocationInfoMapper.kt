package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.entity.LocationInfoEntity
import com.nielaclag.openweather.data.remote.dto.LocationInfoDto
import com.nielaclag.openweather.domain.model.weather.LocationInfo

/**
 * Created by Niel on 10/21/2024.
 */
fun LocationInfo.toDto(): LocationInfoDto {
    return LocationInfoDto(
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        state = state
    )
}

fun LocationInfoDto.toDomain(): LocationInfo {
    return LocationInfo(
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        state = state
    )
}

fun LocationInfo.toEntity(): LocationInfoEntity {
    return LocationInfoEntity(
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        state = state
    )
}

fun LocationInfoEntity.toDomain(): LocationInfo {
    return LocationInfo(
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        state = state
    )
}