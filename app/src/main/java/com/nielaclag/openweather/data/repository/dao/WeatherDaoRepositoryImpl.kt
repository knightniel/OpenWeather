package com.nielaclag.openweather.data.repository.dao

import androidx.paging.PagingSource
import com.nielaclag.openweather.data.database.dao.WeatherDao
import com.nielaclag.openweather.data.model.room.entity.WeatherEntity
import com.nielaclag.openweather.data.model.room.supportmodel.CoordinatePersistence
import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
class WeatherDaoRepositoryImpl(
    private val weatherDao: WeatherDao
) : WeatherDaoRepository {

    override fun getLatestDataFlow(): Flow<WeatherEntity?> {
        return weatherDao.getLatestDataFlow()
    }

    override suspend fun getLatestData(): WeatherEntity? {
        return weatherDao.getLatestData()
    }

    override fun getDataListPaged(): PagingSource<Int, WeatherEntity> {
        return weatherDao.getDataListPaged()
    }

    override fun getCityDataListPaged(
        coordinate: CoordinatePersistence
    ): PagingSource<Int, WeatherEntity> {
        return weatherDao.getCityDataListPaged(
            coordinate = coordinate
        )
    }

    override suspend fun insertData(weatherEntity: WeatherEntity): Long {
        return weatherDao.insertData(weatherEntity)
    }

    override suspend fun deleteAllData() {
        weatherDao.deleteAllData()
    }

    override suspend fun deleteData(weatherEntity: WeatherEntity) {
        weatherDao.deleteData(weatherEntity)
    }

    override suspend fun updateData(weatherEntity: WeatherEntity) {
        weatherDao.updateData(weatherEntity)
    }

    override suspend fun setNewData(weatherEntity: WeatherEntity): Long {
        return weatherDao.setNewData(weatherEntity)
    }

}