package com.nielaclag.openweather.data.mapper

import com.nielaclag.openweather.data.model.room.supportmodel.AuthenticationTypePersistence
import com.nielaclag.openweather.domain.model.type.AuthenticationType

/**
 * Created by Niel on 10/24/2024.
 */

fun AuthenticationTypePersistence.toDomain(): AuthenticationType {
    return when(this) {
        AuthenticationTypePersistence.GOOGLE -> AuthenticationType.GOOGLE
        AuthenticationTypePersistence.EMAIL -> AuthenticationType.EMAIL
        AuthenticationTypePersistence.GUEST -> AuthenticationType.GUEST
        else -> AuthenticationType.OTHER
    }
}

fun AuthenticationType.toEntity(): AuthenticationTypePersistence {
    return when(this) {
        AuthenticationType.GOOGLE -> AuthenticationTypePersistence.GOOGLE
        AuthenticationType.EMAIL -> AuthenticationTypePersistence.EMAIL
        AuthenticationType.GUEST -> AuthenticationTypePersistence.GUEST
        else -> AuthenticationTypePersistence.OTHER
    }
}