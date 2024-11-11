package com.nielaclag.openweather.di

import com.nielaclag.openweather.data.model.moshiadapter.JsonObjectAdapter
import com.nielaclag.openweather.di.qualifier.StringConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

/**
 * Created by Niel on 10/21/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .add(JsonObjectAdapter())
            .build()
    }

    @Provides
    @Singleton
    fun provideJsonConverter(moshi: Moshi): Converter.Factory {
        return MoshiConverterFactory
            .create(moshi)
            .asLenient()
            .withNullSerialization()
    }

    @Provides
    @Singleton
    @StringConverter
    fun provideStringConverter(): Converter.Factory {
        val mediaType = "text/plain".toMediaTypeOrNull()
        return object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, *>? {
                return if (String::class.java == type) {
                    Converter { value -> value.string() }
                } else null
            }

            override fun requestBodyConverter(
                type: Type,
                parameterAnnotations: Array<out Annotation>,
                methodAnnotations: Array<out Annotation>,
                retrofit: Retrofit
            ): Converter<*, RequestBody>? {
                return if (String::class.java == type) {
                    Converter<String?, RequestBody> { value: String? -> value?.toRequestBody(mediaType) }
                } else null
            }
        }
    }

}