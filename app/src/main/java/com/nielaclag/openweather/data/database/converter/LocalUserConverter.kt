package com.nielaclag.openweather.data.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.nielaclag.openweather.data.model.room.supportmodel.AuthenticationTypePersistence
import com.squareup.moshi.Moshi

/**
 * Created by Niel on 10/24/2024.
 */
@ProvidedTypeConverter
class LocalUserConverter(private val moshi: Moshi) {

    @TypeConverter
    fun fromAuthenticationTypePersistence(value: AuthenticationTypePersistence?): String? {
        if (value == null) {
            return null
        }
        return value.name
    }

    @TypeConverter
    fun toAuthenticationTypePersistence(value: String?): AuthenticationTypePersistence? {
        if (value == null) {
            return null
        }
        return try {
            AuthenticationTypePersistence.valueOf(value)
        } catch (_: Exception) {
            AuthenticationTypePersistence.OTHER
        }
    }
}