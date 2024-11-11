package com.nielaclag.openweather.di

import com.nielaclag.openweather.common.constants.Constants
import com.nielaclag.openweather.di.qualifier.OpenWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Singleton

/**
 * Created by Niel on 11/3/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiUrlModule {

    @Provides
    @Singleton
    @OpenWeather
    fun provideOpenWeatherApiUrl(): HttpUrl {
        return Constants.OPEN_WEATHER_API_URL.toHttpUrl()
    }

}