package com.nielaclag.openweather.di.qualifier

import javax.inject.Qualifier

/**
 * Created by Niel on 11/5/2024.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HttpLogging

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Connectivity

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ContentTypeJson