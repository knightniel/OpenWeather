package com.nielaclag.openweather.di

import com.nielaclag.openweather.data.database.AppDatabase
import com.nielaclag.openweather.data.remote.api.OpenWeatherApi
import com.nielaclag.openweather.data.repository.dao.LocalUserDaoRepositoryImpl
import com.nielaclag.openweather.data.repository.dao.LocationInfoDaoRepositoryImpl
import com.nielaclag.openweather.data.repository.dao.WeatherDaoRepositoryImpl
import com.nielaclag.openweather.data.repository.remote.AuthenticationRepositoryImpl
import com.nielaclag.openweather.data.repository.remote.OpenWeatherRepositoryImpl
import com.nielaclag.openweather.domain.repository.dao.LocalUserDaoRepository
import com.nielaclag.openweather.domain.repository.dao.LocationInfoDaoRepository
import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import com.nielaclag.openweather.domain.repository.remote.AuthenticationRepository
import com.nielaclag.openweather.domain.repository.remote.OpenWeatherRepository
import com.nielaclag.openweather.domain.util.FirebaseAuthHandler
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

    @Provides
    @Singleton
    fun provideLocalUserDaoRepository(database: AppDatabase): LocalUserDaoRepository {
        return LocalUserDaoRepositoryImpl(database.localUserDao)
    }

    @Provides
    @Singleton
    fun provideLocationInfoDaoRepository(database: AppDatabase): LocationInfoDaoRepository {
        return LocationInfoDaoRepositoryImpl(database.locationInfoDao)
    }

    @Provides
    @Singleton
    fun provideCurrentWeatherDaoRepository(database: AppDatabase): WeatherDaoRepository {
        return WeatherDaoRepositoryImpl(database.weatherDao)
    }

    /// REMOTE
    /// REMOTE
    /// REMOTE

    @Provides
    @Singleton
    fun provideOpenWeatherRepository(openWeatherApi: OpenWeatherApi, moshi: Moshi): OpenWeatherRepository {
        return OpenWeatherRepositoryImpl(openWeatherApi, moshi)
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(firebaseAuthHandler: FirebaseAuthHandler, moshi: Moshi): AuthenticationRepository {
        return AuthenticationRepositoryImpl(firebaseAuthHandler, moshi)
    }


}