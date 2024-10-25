package com.nielaclag.openweather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nielaclag.openweather.data.model.room.entity.LocationInfoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
@Dao
interface LocationInfoDao {

    @Query("SELECT * FROM location_info_tbl LIMIT 1")
    fun getDataFlow(): Flow<LocationInfoEntity?>

    @Query("SELECT * FROM location_info_tbl LIMIT 1")
    suspend fun getData(): LocationInfoEntity?

    @Update
    suspend fun updateData(locationInfoEntity: LocationInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(locationInfoEntity: LocationInfoEntity): Long

    @Query("DELETE FROM location_info_tbl")
    suspend fun deleteAllData()

    @Transaction
    suspend fun setNewData(
        locationInfoEntity: LocationInfoEntity
    ): Long {
        deleteAllData()
        return insertData(locationInfoEntity)
    }

}