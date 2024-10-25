package com.nielaclag.openweather.domain.usecase.util

import com.nielaclag.openweather.common.helper.toLocalZonedDateTime
import com.nielaclag.openweather.domain.model.type.TimePeriod
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.TimeZone
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class GetTimePeriod @Inject constructor() {
    operator fun invoke(
        timeMillis: Long,
        zoneOffsetInMillis: Int = TimeZone.getDefault().rawOffset
    ): TimePeriod {
        val zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(zoneOffsetInMillis / 1000))
        val zonedDateTime = Instant.ofEpochMilli(timeMillis).atZone(zoneId)

        val localZonedDateTime = zonedDateTime.toLocalZonedDateTime()

        val hourOfDay = localZonedDateTime.hour.toFloat()
        val minute = localZonedDateTime.minute.toFloat()
        val timeOfDay = hourOfDay + (minute / 60f)

        return when (timeOfDay) {
            // 5:00 AM - 11:59 PM
            in 5f ..(11f + (59f / 60f)) -> TimePeriod.Morning
            // 12:00 PM
            12f -> TimePeriod.Noon
            // 12:01 PM - 5:59 PM
            in (12f + (1f / 60f))..(12F + 5f + (59f / 60f)) -> TimePeriod.Afternoon
            // 6:00 PM - 11:59 AM
            in (12f + 6f)..(12f + 11f + (59f / 60f)) -> TimePeriod.Evening
            // 12:00 AM
            0f -> TimePeriod.Midnight
            // 12:01 AM - 4:59 AM
            else -> TimePeriod.EarlyMorning
        }
    }
}