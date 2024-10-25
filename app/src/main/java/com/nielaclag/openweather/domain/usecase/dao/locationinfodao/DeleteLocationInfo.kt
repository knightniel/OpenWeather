package com.nielaclag.openweather.domain.usecase.dao.locationinfodao

import com.nielaclag.openweather.domain.repository.dao.LocationInfoDaoRepository
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class DeleteLocationInfo @Inject constructor(
    private val repository: LocationInfoDaoRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllData()
    }
}