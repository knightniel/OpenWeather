package com.nielaclag.openweather.di

import android.content.Context
import com.nielaclag.openweather.common.constants.Constants.LOGGING_ENABLED
import com.nielaclag.openweather.common.helper.log
import com.nielaclag.openweather.common.networkutil.interceptor.ConnectivityInterceptor
import com.nielaclag.openweather.common.networkutil.interceptor.ContentTypeJsonInterceptor
import com.nielaclag.openweather.di.qualifier.Connectivity
import com.nielaclag.openweather.di.qualifier.ContentTypeJson
import com.nielaclag.openweather.di.qualifier.HttpLogging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * Created by Niel on 11/5/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Provides
    @Singleton
    @HttpLogging
    fun provideHttpLoggingInterceptor(): Interceptor {
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

    @Provides
    @Singleton
    @Connectivity
    fun provideConnectivityInterceptor(@ApplicationContext context: Context): Interceptor {
        return ConnectivityInterceptor(context)
    }

    @Provides
    @Singleton
    @ContentTypeJson
    fun provideContentTypeJsonInterceptor(): Interceptor {
        return ContentTypeJsonInterceptor()
    }

}