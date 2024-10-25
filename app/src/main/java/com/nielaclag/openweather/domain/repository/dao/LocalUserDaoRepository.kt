package com.nielaclag.openweather.domain.repository.dao

import com.nielaclag.openweather.data.model.room.entity.LocalUserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
interface LocalUserDaoRepository {

    fun getDataFlow(): Flow<LocalUserEntity?>
    suspend fun getData(): LocalUserEntity?
    suspend fun insertData(localUserEntity: LocalUserEntity): Long
    suspend fun updateData(localUserEntity: LocalUserEntity)
    suspend fun deleteAllData()
    suspend fun setNewData(localUserEntity: LocalUserEntity): Long

}