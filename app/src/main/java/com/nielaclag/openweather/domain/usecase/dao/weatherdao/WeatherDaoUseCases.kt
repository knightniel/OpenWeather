package com.nielaclag.openweather.domain.usecase.dao.weatherdao

import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
data class WeatherDaoUseCases @Inject constructor(
    val getLatestWeatherFlow: GetLatestWeatherFlow,
    val getLatestWeather: GetLatestWeather,
    val getWeatherRecordsPaged: GetWeatherRecordsPaged,
    val getWeatherRecordsByCityPaged: GetWeatherRecordsByCityPaged,
    val insertWeather: InsertWeather,
    val updateWeather: UpdateWeather,
    val deleteWeather: DeleteWeather,
    val deleteAllWeather: DeleteAllWeather,
    val setNewWeather: SetNewWeather
)