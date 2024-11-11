package com.nielaclag.openweather.di

import android.content.Context
import com.nielaclag.openweather.data.database.AppDatabase
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Niel on 10/21/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context, moshi: Moshi): AppDatabase {
        return AppDatabase.getInstance(context = context, moshi = moshi)
    }

}