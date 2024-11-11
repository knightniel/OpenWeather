package com.nielaclag.openweather.di

import com.nielaclag.openweather.common.constants.UtilConstants.DEFAULT_REQUEST_TIMEOUT
import com.nielaclag.openweather.common.networkutil.config.NetworkTimeout
import com.nielaclag.openweather.di.qualifier.OpenWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Niel on 11/5/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkTimeoutModule {

    @Provides
    @Singleton
    @OpenWeather
    fun provideOpenWeatherNetworkTimeout(): NetworkTimeout {
        return NetworkTimeout(
            readTimeout = NetworkTimeout.Timeout(
                DEFAULT_REQUEST_TIMEOUT,
                TimeUnit.SECONDS
            ),
            connectTimeout = NetworkTimeout.Timeout(
                DEFAULT_REQUEST_TIMEOUT,
                TimeUnit.SECONDS
            ),
            writeTimeout = NetworkTimeout.Timeout(
                DEFAULT_REQUEST_TIMEOUT,
                TimeUnit.SECONDS
            )
        )
    }

}