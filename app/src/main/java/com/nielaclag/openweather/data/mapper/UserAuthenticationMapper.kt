package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.remote.dto.UserAuthenticationDto
import com.nielaclag.openweather.domain.model.UserAuthentication

/**
 * Created by Niel on 10/24/2024.
 */
fun UserAuthentication.toDto(): UserAuthenticationDto {
    return UserAuthenticationDto(
        id = id,
        email = email,
        name = name,
        image = image
    )
}

fun UserAuthenticationDto.toDomain(): UserAuthentication {
    return UserAuthentication(
        id = id,
        email = email,
        name = name,
        image = image
    )
}