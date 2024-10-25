package com.nielaclag.openweather.domain.usecase.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.TimeZone
import javax.inject.Inject

/**
 * Created by Niel on 10/22/2024.
 */
class GetZonedDateTime @Inject constructor() {
    operator fun invoke(
        timeMillis: Long,
        zoneOffsetInMillis: Int = TimeZone.getDefault().rawOffset,
//        onResult: (zonedDateTime: ZonedDateTime, localZonedDateTime: ZonedDateTime) -> Unit = { _, _ -> }
    ): ZonedDateTime {
        val zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(zoneOffsetInMillis / 1000))
        val zonedDateTime = Instant.ofEpochMilli(timeMillis).atZone(zoneId)

        // UTC TIME, COULD JUST BE DATE(timeMillis)
//         val utcTime = zonedDateTime.toInstant(ZoneOffset.ofTotalSeconds(0))

        // ZONE TIME
//         val zoneTime = zonedDateTime.toInstant()

//        onResult(zonedDateTime, zonedDateTime)
        return zonedDateTime
    }
}