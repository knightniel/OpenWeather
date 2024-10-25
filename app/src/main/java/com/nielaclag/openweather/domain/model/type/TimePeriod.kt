package com.nielaclag.openweather.domain.model.type

/**
 * Created by Niel on 10/21/2024.
 */
enum class TimePeriod {
// DAY
// 5:00 AM - 11:59 PM
    Morning,
// 12:00 PM
    Noon,
// 12:01 PM - 5:59 PM
    Afternoon,

// NIGHT
// 6:00 PM - 11:59 AM
    Evening,
// 12:00 AM
    Midnight,
// 12:01 AM - 4:59 AM
    EarlyMorning
}