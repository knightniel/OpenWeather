package com.nielaclag.openweather.data.repository.dao

import androidx.paging.PagingSource
import com.nielaclag.openweather.data.database.dao.CurrentWeatherDao
import com.nielaclag.openweather.data.model.room.entity.WeatherEntity
import com.nielaclag.openweather.data.model.room.supportmodel.CoordinatePersistence
import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
class WeatherDaoRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao
) : WeatherDaoRepository {

    override fun getLatestDataFlow(): Flow<WeatherEntity?> {
        return currentWeatherDao.getLatestDataFlow()
    }

    override suspend fun getLatestData(): WeatherEntity? {
        return currentWeatherDao.getLatestData()
    }

    override fun getDataListPaged(): PagingSource<Int, WeatherEntity> {
        return currentWeatherDao.getDataListPaged()
    }

    override fun getCityDataListPaged(
        coordinate: CoordinatePersistence
    ): PagingSource<Int, WeatherEntity> {
        return currentWeatherDao.getCityDataListPaged(
            coordinate = coordinate
        )
    }

    override suspend fun insertData(weatherEntity: WeatherEntity): Long {
        return currentWeatherDao.insertData(weatherEntity)
    }

    override suspend fun deleteAllData() {
        currentWeatherDao.deleteAllData()
    }

    override suspend fun deleteData(weatherEntity: WeatherEntity) {
        currentWeatherDao.deleteData(weatherEntity)
    }

    override suspend fun updateData(weatherEntity: WeatherEntity) {
        currentWeatherDao.updateData(weatherEntity)
    }

    override suspend fun setNewData(weatherEntity: WeatherEntity): Long {
        return currentWeatherDao.setNewData(weatherEntity)
    }

}