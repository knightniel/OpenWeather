package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.entity.LocalUserEntity
import com.nielaclag.openweather.domain.model.LocalUser

/**
 * Created by Niel on 10/21/2024.
 */
fun LocalUserEntity.toDomain(): LocalUser {
    return LocalUser(
        id = id,
        name = name,
        email = email,
        image = image,
        authenticationType = authenticationType.toDomain()
    )
}

fun LocalUser.toEntity(): LocalUserEntity {
    return LocalUserEntity(
        id = id,
        name = name,
        email = email,
        image = image,
        authenticationType = authenticationType.toEntity()
    )
}