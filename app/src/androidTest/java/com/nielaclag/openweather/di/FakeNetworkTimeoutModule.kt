package com.nielaclag.openweather.di

import com.nielaclag.openweather.common.networkutil.config.NetworkTimeout
import com.nielaclag.openweather.di.qualifier.OpenWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Niel on 11/5/2024.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkTimeoutModule::class]
)
object FakeNetworkTimeoutModule {

    @Provides
    @Singleton
    @OpenWeather
    fun provideOpenWeatherNetworkTimeout(): NetworkTimeout {
        return NetworkTimeout(
            readTimeout = NetworkTimeout.Timeout(
                5,
                TimeUnit.SECONDS
            ),
            connectTimeout = NetworkTimeout.Timeout(
                5,
                TimeUnit.SECONDS
            ),
            writeTimeout = NetworkTimeout.Timeout(
                5,
                TimeUnit.SECONDS
            )
        )
    }

}
