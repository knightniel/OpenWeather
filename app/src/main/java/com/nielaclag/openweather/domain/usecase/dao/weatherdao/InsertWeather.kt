package com.nielaclag.openweather.domain.usecase.dao.weatherdao

import com.nielaclag.openweather.data.mapper.toEntity
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class InsertWeather @Inject constructor(
    private val repository: WeatherDaoRepository
) {
    suspend operator fun invoke(weather: Weather): Long {
        return repository.insertData(weather.toEntity())
    }
}