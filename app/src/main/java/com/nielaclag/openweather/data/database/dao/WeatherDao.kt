package com.nielaclag.openweather.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nielaclag.openweather.data.model.room.entity.WeatherEntity
import com.nielaclag.openweather.data.model.room.supportmodel.CoordinatePersistence
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_tbl ORDER BY id DESC LIMIT 1")
    fun getLatestDataFlow(): Flow<WeatherEntity?>

    @Query("SELECT * FROM weather_tbl ORDER BY id DESC LIMIT 1")
    suspend fun getLatestData(): WeatherEntity?

    @Query("SELECT * FROM weather_tbl ORDER BY id DESC ")
    fun getDataListPaged(): PagingSource<Int, WeatherEntity>

    @Query("SELECT * FROM weather_tbl WHERE coordinate = :coordinate ORDER BY id DESC")
    fun getCityDataListPaged(
        coordinate: CoordinatePersistence
    ): PagingSource<Int, WeatherEntity>

    @Delete
    suspend fun deleteData(weatherEntity: WeatherEntity)

    @Update
    suspend fun updateData(weatherEntity: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(weatherEntity: WeatherEntity): Long

    @Query("DELETE FROM weather_tbl")
    suspend fun deleteAllData()

    @Transaction
    suspend fun setNewData(
        weatherEntity: WeatherEntity
    ): Long {
        deleteAllData()
        return insertData(weatherEntity)
    }

}