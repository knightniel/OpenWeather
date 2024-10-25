package com.nielaclag.openweather.domain.repository.dao

import com.nielaclag.openweather.data.model.room.entity.LocationInfoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
interface LocationInfoDaoRepository {

    fun getDataFlow(): Flow<LocationInfoEntity?>
    suspend fun getData(): LocationInfoEntity?
    suspend fun insertData(locationInfoEntity: LocationInfoEntity): Long
    suspend fun updateData(locationInfoEntity: LocationInfoEntity)
    suspend fun deleteAllData()
    suspend fun setNewData(locationInfoEntity: LocationInfoEntity): Long

}