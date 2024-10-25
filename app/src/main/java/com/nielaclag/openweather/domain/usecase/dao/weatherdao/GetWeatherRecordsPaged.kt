package com.nielaclag.openweather.domain.usecase.dao.weatherdao

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class GetWeatherRecordsPaged @Inject constructor(
    private val repository: WeatherDaoRepository
) {
    operator fun invoke(): Flow<PagingData<Weather>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 40
            )
        ) {
            repository.getDataListPaged()
        }
            .flow
            .map { pagingData ->
                pagingData.map { weatherEntity ->
                    weatherEntity.toDomain()
                }
            }
    }
}