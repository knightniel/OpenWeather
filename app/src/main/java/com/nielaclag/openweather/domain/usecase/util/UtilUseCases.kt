package com.nielaclag.openweather.domain.usecase.util

import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
data class UtilUseCases @Inject constructor(
    val getTimePeriod: GetTimePeriod,
    val getZonedDateTime: GetZonedDateTime
)