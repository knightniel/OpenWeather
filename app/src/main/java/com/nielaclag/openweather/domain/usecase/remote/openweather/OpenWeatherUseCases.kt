package com.nielaclag.openweather.domain.usecase.remote.openweather

import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
data class OpenWeatherUseCases @Inject constructor(
    val getLocationsByName: GetLocationsByName,
    val getLocationsByCoordinate: GetLocationsByCoordinate,
    val getCurrentWeather: GetCurrentWeather
)