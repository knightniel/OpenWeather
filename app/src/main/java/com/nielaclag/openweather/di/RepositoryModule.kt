package com.nielaclag.openweather.di

import com.nielaclag.openweather.data.database.AppDatabase
import com.nielaclag.openweather.data.remote.api.OpenWeatherApi
import com.nielaclag.openweather.data.repository.dao.LocalUserDaoRepositoryImpl
import com.nielaclag.openweather.data.repository.dao.LocationInfoDaoRepositoryImpl
import com.nielaclag.openweather.data.repository.dao.WeatherDaoRepositoryImpl
import com.nielaclag.openweather.data.repository.remote.OpenWeatherRepositoryImpl
import com.nielaclag.openweather.domain.repository.dao.LocalUserDaoRepository
import com.nielaclag.openweather.domain.repository.dao.LocationInfoDaoRepository
import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import com.nielaclag.openweather.domain.repository.remote.OpenWeatherRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Niel on 10/21/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /// ROOM DAO
    /// ROOM DAO
    /// ROOM DAO

    @Singleton
    @Provides
    fun provideLocalUserDaoRepository(database: AppDatabase): LocalUserDaoRepository {
        return LocalUserDaoRepositoryImpl(database.localUserDao)
    }

    @Singleton
    @Provides
    fun provideLocationInfoDaoRepository(database: AppDatabase): LocationInfoDaoRepository {
        return LocationInfoDaoRepositoryImpl(database.locationInfoDao)
    }

    @Singleton
    @Provides
    fun provideCurrentWeatherDaoRepository(database: AppDatabase): WeatherDaoRepository {
        return WeatherDaoRepositoryImpl(database.currentWeatherDao)
    }

    /// REMOTE
    /// REMOTE
    /// REMOTE

    @Singleton
    @Provides
    fun provideOpenWeatherRepository(openWeatherApi: OpenWeatherApi, moshi: Moshi): OpenWeatherRepository {
        return OpenWeatherRepositoryImpl(openWeatherApi, moshi)
    }


}