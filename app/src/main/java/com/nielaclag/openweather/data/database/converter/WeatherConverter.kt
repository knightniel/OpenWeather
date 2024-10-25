package com.nielaclag.openweather.data.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.nielaclag.openweather.common.helper.fromJson
import com.nielaclag.openweather.common.helper.toJson
import com.nielaclag.openweather.data.model.room.supportmodel.CloudsPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.CoordinatePersistence
import com.nielaclag.openweather.data.model.room.supportmodel.RainPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.SnowPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.SysPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.TemperaturePersistence
import com.nielaclag.openweather.data.model.room.supportmodel.WeatherInfoPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.WindPersistence
import com.squareup.moshi.Moshi

/**
 * Created by Niel on 10/21/2024.
 */
@ProvidedTypeConverter
class WeatherConverter(private val moshi: Moshi) {

    @TypeConverter
    fun fromCoordinatePersistence(value: CoordinatePersistence?): String? {
        if (value == null) {
            return null
        }
        return moshi.toJson(value)
    }

    @TypeConverter
    fun toCoordinatePersistence(value: String?): CoordinatePersistence? {
        if (value == null) {
            return null
        }
        return moshi.fromJson(value)
    }

    @TypeConverter
    fun fromWeatherInfoPersistenceList(value: List<WeatherInfoPersistence>?): String? {
        if (value == null) {
            return null
        }
        return moshi.toJson(value)
    }

    @TypeConverter
    fun toWeatherInfoPersistenceList(value: String?): List<WeatherInfoPersistence>? {
        if (value == null) {
            return null
        }
        return moshi.fromJson(value)
    }

    @TypeConverter
    fun fromTemperaturePersistence(value: TemperaturePersistence?): String? {
        if (value == null) {
            return null
        }
        return moshi.toJson(value)
    }

    @TypeConverter
    fun toTemperaturePersistence(value: String?): TemperaturePersistence? {
        if (value == null) {
            return null
        }
        return moshi.fromJson(value)
    }

    @TypeConverter
    fun fromWindPersistence(value: WindPersistence?): String? {
        if (value == null) {
            return null
        }
        return moshi.toJson(value)
    }

    @TypeConverter
    fun toWindPersistence(value: String?): WindPersistence? {
        if (value == null) {
            return null
        }
        return moshi.fromJson(value)
    }


    @TypeConverter
    fun fromCloudsPersistence(value: CloudsPersistence?): String? {
        if (value == null) {
            return null
        }
        return moshi.toJson(value)
    }

    @TypeConverter
    fun toCloudsPersistence(value: String?): CloudsPersistence? {
        if (value == null) {
            return null
        }
        return moshi.fromJson(value)
    }

    @TypeConverter
    fun fromRainPersistence(value: RainPersistence?): String? {
        if (value == null) {
            return null
        }
        return moshi.toJson(value)
    }

    @TypeConverter
    fun toRainPersistence(value: String?): RainPersistence? {
        if (value == null) {
            return null
        }
        return moshi.fromJson(value)
    }

    @TypeConverter
    fun fromSnowPersistence(value: SnowPersistence?): String? {
        if (value == null) {
            return null
        }
        return moshi.toJson(value)
    }

    @TypeConverter
    fun toSnowPersistence(value: String?): SnowPersistence? {
        if (value == null) {
            return null
        }
        return moshi.fromJson(value)
    }

    @TypeConverter
    fun fromSysPersistence(value: SysPersistence?): String? {
        if (value == null) {
            return null
        }
        return moshi.toJson(value)
    }

    @TypeConverter
    fun toSysPersistence(value: String?): SysPersistence? {
        if (value == null) {
            return null
        }
        return moshi.fromJson(value)
    }

}