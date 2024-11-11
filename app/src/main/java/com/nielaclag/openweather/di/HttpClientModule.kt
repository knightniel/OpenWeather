package com.nielaclag.openweather.di

import com.nielaclag.openweather.common.networkutil.config.NetworkTimeout
import com.nielaclag.openweather.common.networkutil.interceptor.ConnectivityInterceptor
import com.nielaclag.openweather.common.networkutil.interceptor.ContentTypeJsonInterceptor
import com.nielaclag.openweather.di.qualifier.Connectivity
import com.nielaclag.openweather.di.qualifier.ContentTypeJson
import com.nielaclag.openweather.di.qualifier.HttpLogging
import com.nielaclag.openweather.di.qualifier.OpenWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * Created by Niel on 11/5/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    @Singleton
    @OpenWeather
    fun provideOpenWeatherOkHttpClient(
        @ContentTypeJson contentTypeJsonInterceptor: Interceptor,
        @Connectivity connectivityInterceptor: Interceptor,
        @HttpLogging httpLoggingInterceptor: Interceptor,
        @OpenWeather networkTimeout: NetworkTimeout
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(contentTypeJsonInterceptor)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(networkTimeout.readTimeout.duration, networkTimeout.readTimeout.unit)
            .connectTimeout(networkTimeout.connectTimeout.duration, networkTimeout.connectTimeout.unit)
            .connectTimeout(networkTimeout.writeTimeout.duration, networkTimeout.writeTimeout.unit)
            .retryOnConnectionFailure(true)
            .build()
    }

}