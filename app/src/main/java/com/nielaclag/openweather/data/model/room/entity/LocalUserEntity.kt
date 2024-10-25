package com.nielaclag.openweather.data.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nielaclag.openweather.data.model.room.supportmodel.AuthenticationTypePersistence
import com.nielaclag.openweather.domain.model.type.AuthenticationType

/**
 * Created by Niel on 10/21/2024.
 */
@Entity(tableName = "local_user_tbl")
data class LocalUserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "authenticationType")
    val authenticationType: AuthenticationTypePersistence
)