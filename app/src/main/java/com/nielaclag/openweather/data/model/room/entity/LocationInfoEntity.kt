package com.nielaclag.openweather.data.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Niel on 10/21/2024.
 */
@Entity(tableName = "location_info_tbl")
data class LocationInfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name : String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "state")
    val state: String?
)