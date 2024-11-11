package com.nielaclag.openweather.di

import com.nielaclag.openweather.di.qualifier.OpenWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Niel on 10/21/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @OpenWeather
    fun provideOpenWeatherRetrofit(
        @OpenWeather openWeatherApiUrl: HttpUrl,
        @OpenWeather okHttpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .baseUrl(openWeatherApiUrl)
            .build()
    }

}