package com.nielaclag.openweather.presentation.model.page

import kotlinx.serialization.Serializable

/**
 * Created by Niel on 10/21/2024.
 */
@Serializable
open class MainPage {

    @Serializable
    data object CurrentWeather : MainPage()

    @Serializable
    data object WeatherHistory : MainPage()

}