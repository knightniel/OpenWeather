package com.nielaclag.openweather.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nielaclag.openweather.data.database.converter.LocalUserConverter
import com.nielaclag.openweather.data.database.converter.WeatherConverter
import com.nielaclag.openweather.data.database.dao.CurrentWeatherDao
import com.nielaclag.openweather.data.database.dao.LocalUserDao
import com.nielaclag.openweather.data.database.dao.LocationInfoDao
import com.nielaclag.openweather.data.model.room.entity.LocalUserEntity
import com.nielaclag.openweather.data.model.room.entity.LocationInfoEntity
import com.nielaclag.openweather.data.model.room.entity.WeatherEntity
import com.nielaclag.openweather.domain.worker.SeedDatabaseWorker
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Created by Niel on 10/21/2024.
 */
@Database(
    entities = [
        LocalUserEntity::class,
        LocationInfoEntity::class,
        WeatherEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalUserConverter::class,
    WeatherConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract val localUserDao: LocalUserDao
    abstract val locationInfoDao: LocationInfoDao
    abstract val currentWeatherDao: CurrentWeatherDao

    companion object {

        @Volatile private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(@ApplicationContext context: Context, moshi: Moshi): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(
                    context = context,
                    moshi = moshi
                ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, moshi: Moshi): AppDatabase {
            val roomCallback = object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    SeedDatabaseWorker.seedDatabase(context = context)
                }
            }

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "niel_open_weather_app_database"
            )
                .addTypeConverter(LocalUserConverter(moshi = moshi))
                .addTypeConverter(WeatherConverter(moshi = moshi))
                .fallbackToDestructiveMigration()
                .addCallback(roomCallback)
                .build()
        }

        fun destroyInstance() {
            instance = null
        }

    }

}