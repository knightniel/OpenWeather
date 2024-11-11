package com.nielaclag.openweather.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nielaclag.openweather.data.database.AppDatabase
import com.nielaclag.openweather.data.database.converter.LocalUserConverter
import com.nielaclag.openweather.data.database.converter.WeatherConverter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Created by Niel on 11/11/2024.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class FakeDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context, moshi: Moshi): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .addTypeConverter(LocalUserConverter(moshi = moshi))
            .addTypeConverter(WeatherConverter(moshi = moshi))
            .build()
    }

}