package com.nielaclag.openweather.domain.usecase.dao.locationinfodao

import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
data class LocationInfoDaoUseCases @Inject constructor(
    val getLocationInfoFlow: GetLocationInfoFlow,
    val getLocationInfo: GetLocationInfo,
    val insertLocationInfo: InsertLocationInfo,
    val updateLocationInfo: UpdateLocationInfo,
    val deleteLocationInfo: DeleteLocationInfo,
    val setNewLocationInfo: SetNewLocationInfo
)