package com.nielaclag.openweather.data.repository.dao

import com.nielaclag.openweather.data.database.dao.LocationInfoDao
import com.nielaclag.openweather.data.model.room.entity.LocationInfoEntity
import com.nielaclag.openweather.domain.repository.dao.LocationInfoDaoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
class LocationInfoDaoRepositoryImpl(
    private val locationInfoDao: LocationInfoDao
) : LocationInfoDaoRepository {

    override fun getDataFlow(): Flow<LocationInfoEntity?> {
        return locationInfoDao.getDataFlow()
    }

    override suspend fun getData(): LocationInfoEntity? {
        return locationInfoDao.getData()
    }

    override suspend fun insertData(locationInfoEntity: LocationInfoEntity): Long {
        return locationInfoDao.insertData(locationInfoEntity)
    }

    override suspend fun deleteAllData() {
        locationInfoDao.deleteAllData()
    }

    override suspend fun updateData(locationInfoEntity: LocationInfoEntity) {
        locationInfoDao.updateData(locationInfoEntity)
    }

    override suspend fun setNewData(locationInfoEntity: LocationInfoEntity): Long {
        return locationInfoDao.setNewData(locationInfoEntity)
    }

}