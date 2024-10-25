package com.nielaclag.openweather.domain.usecase.dao.weatherdao

import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class GetLatestWeatherFlow @Inject constructor(
    private val repository: WeatherDaoRepository
) {
    operator fun invoke(): Flow<Weather?> {
        return repository.getLatestDataFlow().map { it?.toDomain() }
    }
}