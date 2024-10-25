package com.nielaclag.openweather.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nielaclag.openweather.data.model.room.entity.LocalUserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Niel on 10/21/2024.
 */
@Dao
interface LocalUserDao {

    @Query("SELECT * FROM local_user_tbl LIMIT 1")
    fun getDataFlow(): Flow<LocalUserEntity?>

    @Query("SELECT * FROM local_user_tbl LIMIT 1")
    suspend fun getData(): LocalUserEntity?

    @Update
    suspend fun updateData(localUserEntity: LocalUserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(localUserEntity: LocalUserEntity): Long

    @Query("DELETE FROM local_user_tbl")
    suspend fun deleteAllData()

    @Transaction
    suspend fun setNewData(
        localUserEntity: LocalUserEntity
    ): Long {
        deleteAllData()
        return insertData(localUserEntity)
    }

}