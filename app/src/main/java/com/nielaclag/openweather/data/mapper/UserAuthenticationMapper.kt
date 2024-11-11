package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.remote.dto.UserAuthenticationDto
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.model.UserAuthentication
import com.nielaclag.openweather.domain.model.type.AuthenticationType

/**
 * Created by Niel on 10/24/2024.
 */
fun UserAuthentication.toDto(): UserAuthenticationDto {
    return UserAuthenticationDto(
        email = email,
        name = name,
        image = image
    )
}

fun UserAuthenticationDto.toDomain(): UserAuthentication {
    return UserAuthentication(
        email = email,
        name = name,
        image = image
    )
}

fun UserAuthentication.toLocalUser(authenticationType: AuthenticationType): LocalUser {
    return LocalUser(
        id = 0,
        email = email,
        name = name,
        image = image,
        authenticationType = authenticationType
    )
}