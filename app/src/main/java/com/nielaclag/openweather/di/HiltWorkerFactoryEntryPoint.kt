package com.nielaclag.openweather.di

import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.InstallIn
import dagger.hilt.android.EarlyEntryPoint
import dagger.hilt.components.SingletonComponent

/**
 * Created by Niel on 10/21/2024.
 */
@EarlyEntryPoint
@InstallIn(SingletonComponent::class)
interface HiltWorkerFactoryEntryPoint {

    fun workerFactory(): HiltWorkerFactory

}