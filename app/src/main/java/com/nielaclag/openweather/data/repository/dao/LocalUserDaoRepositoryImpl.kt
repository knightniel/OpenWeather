package com.nielaclag.openweather.data.repository.dao

import com.nielaclag.openweather.data.database.dao.LocalUserDao
import com.nielaclag.openweather.data.model.room.entity.LocalUserEntity
import com.nielaclag.openweather.domain.repository.dao.LocalUserDaoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
class LocalUserDaoRepositoryImpl(
    private val localUserDao: LocalUserDao
) : LocalUserDaoRepository {

    override fun getDataFlow(): Flow<LocalUserEntity?> {
        return localUserDao.getDataFlow()
    }

    override suspend fun getData(): LocalUserEntity? {
        return localUserDao.getData()
    }

    override suspend fun insertData(localUserEntity: LocalUserEntity): Long {
        return localUserDao.insertData(localUserEntity)
    }

    override suspend fun deleteAllData() {
        localUserDao.deleteAllData()
    }

    override suspend fun updateData(localUserEntity: LocalUserEntity) {
        localUserDao.updateData(localUserEntity)
    }

    override suspend fun setNewData(localUserEntity: LocalUserEntity): Long {
        return localUserDao.setNewData(localUserEntity)
    }

}