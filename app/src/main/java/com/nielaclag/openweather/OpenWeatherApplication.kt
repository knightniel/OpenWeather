package com.nielaclag.openweather

import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.nielaclag.openweather.common.base.BaseApplication
import com.nielaclag.openweather.common.constants.Constants.LOGGING_ENABLED
import com.nielaclag.openweather.di.HiltWorkerFactoryEntryPoint
import dagger.hilt.android.EarlyEntryPoints
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
@HiltAndroidApp
class OpenWeatherApplication : BaseApplication(), Configuration.Provider, ImageLoaderFactory {

    @Inject
    lateinit var imageLoader: ImageLoader

    override val workManagerConfiguration: Configuration = Configuration.Builder()
        .setWorkerFactory(EarlyEntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory())
        .setMinimumLoggingLevel(if (LOGGING_ENABLED) android.util.Log.DEBUG else android.util.Log.ERROR)
        .build()

    override fun newImageLoader() = imageLoader

}
