package com.nielaclag.openweather.di

import android.content.Context
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
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
object AppModule {

    @Singleton
    @Provides
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .respectCacheHeaders(false)
            .memoryCache {
                // Configure memory cache
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25) // Use 25% of available memory
                    .build()
            }
            .components {
                add(SvgDecoder.Factory())
                add(GifDecoder.Factory())
                add(VideoFrameDecoder.Factory())
            }
            .diskCache {
                // Configure disk cache
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
//                    .maxSizePercent(0.02)
                    .maxSizeBytes(200 * 1024 * 1024) // 50MB
                    .build()
            }
            .build()
    }

}