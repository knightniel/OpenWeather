package com.nielaclag.openweather.domain.usecase.dao.locationinfodao

import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.repository.dao.LocationInfoDaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class GetLocationInfoFlow @Inject constructor(
    private val repository: LocationInfoDaoRepository
) {
    operator fun invoke(): Flow<LocationInfo?> {
        return repository.getDataFlow().map { it?.toDomain() }
    }
}