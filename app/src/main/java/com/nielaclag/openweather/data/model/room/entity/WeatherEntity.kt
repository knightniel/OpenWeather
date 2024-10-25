package com.nielaclag.openweather.data.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nielaclag.openweather.data.model.room.supportmodel.CloudsPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.CoordinatePersistence
import com.nielaclag.openweather.data.model.room.supportmodel.RainPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.SnowPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.SysPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.TemperaturePersistence
import com.nielaclag.openweather.data.model.room.supportmodel.WeatherInfoPersistence
import com.nielaclag.openweather.data.model.room.supportmodel.WindPersistence

/**
 * Created by Niel on 10/21/2024.
 */
@Entity(tableName = "weather_tbl")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "dt")
    val dt: Long,
    @ColumnInfo(name = "coordinate")
    val coordinate: CoordinatePersistence,
    @ColumnInfo(name = "weatherInfo")
    val weatherInfo: List<WeatherInfoPersistence>,
    @ColumnInfo(name = "base")
    val base: String,
    @ColumnInfo(name = "temperature")
    val temperature: TemperaturePersistence,
    @ColumnInfo(name = "visibility")
    val visibility: Int,
    @ColumnInfo(name = "wind")
    val wind: WindPersistence?,
    @ColumnInfo(name = "clouds")
    val clouds: CloudsPersistence?,
    @ColumnInfo(name = "rain")
    val rain: RainPersistence?,
    @ColumnInfo(name = "snow")
    val snow: SnowPersistence?,
    @ColumnInfo(name = "sys")
    val sys: SysPersistence,
    @ColumnInfo(name = "timezone")
    val timezone: Int,
    @ColumnInfo(name = "cityId")
    val cityId: Long,
    @ColumnInfo(name = "name")
    val cityName: String,
    @ColumnInfo(name = "cod")
    val cod: Int
)