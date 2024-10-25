package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.supportmodel.RainPersistence
import com.nielaclag.openweather.data.remote.dto.RainDto
import com.nielaclag.openweather.domain.model.weather.Rain

/**
 * Created by Niel on 10/21/2024.
 */
fun Rain.toDto(): RainDto {
    return RainDto(
        oneHour = oneHour
    )
}

fun RainDto.toDomain(): Rain {
    return Rain(
        oneHour = oneHour
    )
}

fun Rain.toEntity(): RainPersistence {
    return RainPersistence(
        oneHour = oneHour
    )
}

fun RainPersistence.toDomain(): Rain {
    return Rain(
        oneHour = oneHour
    )
}