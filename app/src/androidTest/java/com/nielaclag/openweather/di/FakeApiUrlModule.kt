package com.nielaclag.openweather.di

import com.nielaclag.openweather.di.qualifier.OpenWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton


/**
 * Created by Niel on 11/4/2024.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiUrlModule::class]
)
class FakeApiUrlModule {

    @Provides
    @Singleton
    @OpenWeather
    fun provideOpenWeatherApiUrl(mockWebServer: MockWebServer): HttpUrl {
        return mockWebServer.url("/")
    }

}
