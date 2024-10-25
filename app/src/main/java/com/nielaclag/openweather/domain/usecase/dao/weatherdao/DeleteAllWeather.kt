package com.nielaclag.openweather.domain.usecase.dao.weatherdao

import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class DeleteAllWeather @Inject constructor(
    private val repository: WeatherDaoRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllData()
    }
}