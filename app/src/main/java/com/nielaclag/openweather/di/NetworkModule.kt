package com.nielaclag.openweather.di

import com.nielaclag.openweather.common.constants.Constants
import com.nielaclag.openweather.common.constants.Constants.LOGGING_ENABLED
import com.nielaclag.openweather.common.constants.UtilConstants.DEFAULT_REQUEST_TIMEOUT
import com.nielaclag.openweather.common.helper.log
import com.nielaclag.openweather.common.interceptor.ConnectivityInterceptor
import com.nielaclag.openweather.common.interceptor.ContentTypeJsonInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by Niel on 10/21/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    HttpLoggingInterceptor
//    HttpLoggingInterceptor
//    HttpLoggingInterceptor

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            log(message = message, tag = "OkHttp3")
        }.apply {
            level = if (LOGGING_ENABLED) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @OpenWeather
    @Provides
    fun provideOpenWeatherOkHttpClient(
        contentTypeJsonInterceptor: ContentTypeJsonInterceptor,
        connectivityInterceptor: ConnectivityInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(contentTypeJsonInterceptor)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

//    RETROFIT
//    RETROFIT
//    RETROFIT

    @OpenWeather
    @Provides
    fun provideOpenWeatherRetrofit(
        @OpenWeather okHttpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(Constants.OPEN_WEATHER_API_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

}