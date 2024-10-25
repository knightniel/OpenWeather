package com.nielaclag.openweather.domain.repository.dao

import androidx.paging.PagingSource
import com.nielaclag.openweather.data.model.room.entity.WeatherEntity
import com.nielaclag.openweather.data.model.room.supportmodel.CoordinatePersistence
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
interface WeatherDaoRepository {

    fun getLatestDataFlow(): Flow<WeatherEntity?>
    suspend fun getLatestData(): WeatherEntity?
    fun getDataListPaged(): PagingSource<Int, WeatherEntity>
    fun getCityDataListPaged(coordinate: CoordinatePersistence): PagingSource<Int, WeatherEntity>
    suspend fun insertData(weatherEntity: WeatherEntity): Long
    suspend fun updateData(weatherEntity: WeatherEntity)
    suspend fun deleteAllData()
    suspend fun deleteData(weatherEntity: WeatherEntity)
    suspend fun setNewData(weatherEntity: WeatherEntity): Long

}