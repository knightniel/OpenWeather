package com.nielaclag.openweather.domain.usecase.dao.locationinfodao

import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.repository.dao.LocationInfoDaoRepository
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class GetLocationInfo @Inject constructor(
    private val repository: LocationInfoDaoRepository
) {
    suspend operator fun invoke(): LocationInfo? {
        return repository.getData()?.toDomain()
    }
}