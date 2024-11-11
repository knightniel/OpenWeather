package com.nielaclag.openweather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

/**
 * Created by Niel on 11/5/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object MockWebServerModule {

    @Provides
    @Singleton
    fun provideMockWebServer(): MockWebServer {
        return MockWebServer()
    }

}