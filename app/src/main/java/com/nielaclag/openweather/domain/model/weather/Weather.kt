package com.nielaclag.openweather.domain.model.weather

import com.nielaclag.openweather.R
import com.nielaclag.openweather.domain.model.type.TimePeriod
import com.nielaclag.openweather.presentation.model.type.SunsetSunrise
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Niel on 10/21/2024.
 */
@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "id")
    val id: Long,
    @Json(name = "coordinate")
    val coordinate: Coordinate,
    @Json(name = "weatherInfo")
    val weatherInfo: List<WeatherInfo>,
    @Json(name = "base")
    val base: String,
    @Json(name = "temperature")
    val temperature: Temperature,
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "wind")
    val wind: Wind?,
    @Json(name = "clouds")
    val clouds: Clouds?,
    @Json(name = "rain")
    val rain: Rain?,
    @Json(name = "snow")
    val snow: Snow?,
    @Json(name = "dt")
    val dt: Long,
    @Json(name = "sys")
    val sys: Sys,
    @Json(name = "timezone")
    val timezone: Int,
    @Json(name = "cityId")
    val cityId: Long,
    @Json(name = "name")
    val cityName: String,
    @Json(name = "cod")
    val cod: Int
) {

    fun getWeatherIcon(timePeriod: TimePeriod = TimePeriod.Morning): Int {
        val withSun = timePeriod in listOf(TimePeriod.Morning, TimePeriod.Noon, TimePeriod.Afternoon)
        return getWeatherIcon(if (withSun) SunsetSunrise.SUNRISE else SunsetSunrise.SUNSET)
    }

    fun getWeatherIcon(sunsetSunrise: SunsetSunrise): Int {
        val isSunrise = sunsetSunrise == SunsetSunrise.SUNRISE
        return weatherInfo.firstOrNull()?.let { weatherInfo ->
            when (weatherInfo.id) {
                200 -> { // thunderstorm with light rain
                    if (isSunrise) {
                        R.raw.thunderstorms_day_rain
                    } else {
                        R.raw.thunderstorms_night_rain
                    }
                }
                201 -> { // thunderstorm with rain
                    if (isSunrise) {
                        R.raw.thunderstorms_day_rain
                    } else {
                        R.raw.thunderstorms_night_rain
                    }
                }
                202 -> { //	thunderstorm with heavy rain
                    if (isSunrise) {
                        R.raw.thunderstorms_day_extreme_rain
                    } else {
                        R.raw.thunderstorms_night_extreme_rain
                    }
                }
                210 -> { // light thunderstorm
                    if (isSunrise) {
                        R.raw.thunderstorms_day
                    } else {
                        R.raw.thunderstorms_night
                    }
                }
                211 -> { // thunderstorm
                    if (isSunrise) {
                        R.raw.thunderstorms_day
                    } else {
                        R.raw.thunderstorms_night
                    }
                }
                212 -> { // heavy thunderstorm
                    if (isSunrise) {
                        R.raw.thunderstorms_day_extreme
                    } else {
                        R.raw.thunderstorms_night_extreme
                    }
                }
                221 -> { // ragged thunderstorm
                    if (isSunrise) {
                        R.raw.thunderstorms_day_overcast
                    } else {
                        R.raw.thunderstorms_night_overcast
                    }
                }
                230 -> { // thunderstorm with light drizzle
                    if (isSunrise) {
                        R.raw.thunderstorms_day_overcast
                    } else {
                        R.raw.thunderstorms_night_overcast
                    }
                }
                231 -> { // thunderstorm with drizzle
                    if (isSunrise) {
                        R.raw.thunderstorms_day_extreme
                    } else {
                        R.raw.thunderstorms_night_extreme
                    }
                }
                232 -> { // thunderstorm with heavy drizzle
                    if (isSunrise) {
                        R.raw.thunderstorms_day_extreme
                    } else {
                        R.raw.thunderstorms_night_extreme
                    }
                }

                300 -> { // light intensity drizzle
                    if (isSunrise) {
                        R.raw.partly_cloudy_day_drizzle
                    } else {
                        R.raw.partly_cloudy_night_drizzle
                    }
                }
                301 -> { // drizzle
                    if (isSunrise) {
                        R.raw.overcast_day_drizzle
                    } else {
                        R.raw.overcast_night_drizzle
                    }
                }
                302 -> { // heavy intensity drizzle
                    if (isSunrise) {
                        R.raw.extreme_day_drizzle
                    } else {
                        R.raw.extreme_night_drizzle
                    }
                }
                310 -> { // light intensity drizzle rain
                    if (isSunrise) {
                        R.raw.partly_cloudy_day_drizzle
                    } else {
                        R.raw.partly_cloudy_night_drizzle
                    }
                }
                311 -> { // drizzle rain
                    if (isSunrise) {
                        R.raw.overcast_day_drizzle
                    } else {
                        R.raw.overcast_night_drizzle
                    }
                }
                312 -> { // heavy intensity drizzle rain
                    if (isSunrise) {
                        R.raw.extreme_day_drizzle
                    } else {
                        R.raw.extreme_night_drizzle
                    }
                }
                313 -> { // shower rain and drizzle
                    if (isSunrise) {
                        R.raw.partly_cloudy_day_drizzle
                    } else {
                        R.raw.partly_cloudy_night_drizzle
                    }
                }
                314 -> { // heavy shower rain and drizzle
                    if (isSunrise) {
                        R.raw.extreme_day_drizzle
                    } else {
                        R.raw.extreme_night_drizzle
                    }
                }
                321 -> { // shower drizzle
                    if (isSunrise) {
                        R.raw.extreme_day_drizzle
                    } else {
                        R.raw.extreme_night_drizzle
                    }
                }

                500 -> { // light rain
                    if (isSunrise) {
                        R.raw.partly_cloudy_day_rain
                    } else {
                        R.raw.partly_cloudy_night_rain
                    }
                }
                501 -> { // moderate rain
                    if (isSunrise) {
                        R.raw.overcast_day_rain
                    } else {
                        R.raw.overcast_night_rain
                    }
                }
                502 -> { // heavy intensity rain
                    if (isSunrise) {
                        R.raw.extreme_day_rain
                    } else {
                        R.raw.extreme_night_rain
                    }
                }
                503 -> { // very heavy rain
                    if (isSunrise) {
                        R.raw.extreme_day_rain
                    } else {
                        R.raw.extreme_night_rain
                    }
                }
                504 -> { // extreme rain
                    if (isSunrise) {
                        R.raw.extreme_day_rain
                    } else {
                        R.raw.extreme_night_rain
                    }
                }
                511 -> { // freezing rainhello
                    if (isSunrise) {
                        R.raw.overcast_day_rain
                    } else {
                        R.raw.overcast_night_rain
                    }
                }
                520 -> { // light intensity shower rain
                    if (isSunrise) {
                        R.raw.overcast_day_rain
                    } else {
                        R.raw.overcast_night_rain
                    }
                }
                521 -> { // shower rain
                    if (isSunrise) {
                        R.raw.overcast_day_rain
                    } else {
                        R.raw.overcast_night_rain
                    }
                }
                522 -> { // heavy intensity shower rain
                    if (isSunrise) {
                        R.raw.extreme_day_rain
                    } else {
                        R.raw.extreme_night_rain
                    }
                }
                531 -> { // ragged shower rain
                    if (isSunrise) {
                        R.raw.partly_cloudy_day_rain
                    } else {
                        R.raw.partly_cloudy_night_rain
                    }
                }

                600 -> { // light snow
                    if (isSunrise) {
                        R.raw.partly_cloudy_day_snow
                    } else {
                        R.raw.partly_cloudy_night_snow
                    }
                }
                601 -> { // snow
                    if (isSunrise) {
                        R.raw.overcast_day_snow
                    } else {
                        R.raw.overcast_night_snow
                    }
                }
                602 -> { // heavy snow
                    if (isSunrise) {
                        R.raw.extreme_day_snow
                    } else {
                        R.raw.extreme_night_snow
                    }
                }
                611 -> { // sleet
                    if (isSunrise) {
                        R.raw.overcast_day_sleet
                    } else {
                        R.raw.overcast_night_sleet
                    }
                }
                612 -> { // light shower sleet
                    if (isSunrise) {
                        R.raw.partly_cloudy_day_sleet
                    } else {
                        R.raw.partly_cloudy_night_sleet
                    }
                }
                613 -> { // shower sleet
                    if (isSunrise) {
                        R.raw.extreme_day_sleet
                    } else {
                        R.raw.extreme_night_sleet
                    }
                }
                615 -> { // light rain and snow
                    if (isSunrise) {
                        R.raw.partly_cloudy_day_sleet
                    } else {
                        R.raw.partly_cloudy_night_sleet
                    }
                }
                616 -> { // rain and snow
                    if (isSunrise) {
                        R.raw.extreme_day_sleet
                    } else {
                        R.raw.extreme_night_sleet
                    }
                }
                620 -> { // light shower snow
                    if (isSunrise) {
                        R.raw.partly_cloudy_day_snow
                    } else {
                        R.raw.partly_cloudy_night_snow
                    }
                }
                621 -> { // shower snow
                    if (isSunrise) {
                        R.raw.overcast_day_snow
                    } else {
                        R.raw.overcast_night_snow
                    }
                }
                622 -> { // heavy shower snow
                    if (isSunrise) {
                        R.raw.extreme_day_snow
                    } else {
                        R.raw.extreme_night_snow
                    }
                }

                701 -> { // mist
                    if (isSunrise) {
                        R.raw.mist
                    } else {
                        R.raw.mist
                    }
                }
                711 -> { // smoke
                    if (isSunrise) {
                        R.raw.overcast_day_smoke
                    } else {
                        R.raw.overcast_night_smoke
                    }
                }
                721 -> { // haze
                    if (isSunrise) {
                        R.raw.overcast_day_haze
                    } else {
                        R.raw.overcast_night_haze
                    }
                }
                731 -> { // sand/dust whirls
                    if (isSunrise) {
                        R.raw.dust_wind
                    } else {
                        R.raw.dust_wind
                    }
                }
                741 -> { // fog
                    if (isSunrise) {
                        R.raw.extreme_day_fog
                    } else {
                        R.raw.extreme_night_fog
                    }
                }
                751 -> { // sand
                    if (isSunrise) {
                        R.raw.dust_day
                    } else {
                        R.raw.dust_night
                    }
                }
                761 -> { // dust
                    if (isSunrise) {
                        R.raw.dust_day
                    } else {
                        R.raw.dust_night
                    }
                }
                762 -> { // volcanic ash
                    if (isSunrise) {
                        R.raw.extreme_day_smoke
                    } else {
                        R.raw.extreme_night_smoke
                    }
                }
                771 -> { // squalls
                    if (isSunrise) {
                        R.raw.wind
                    } else {
                        R.raw.wind
                    }
                }
                781 -> { // tornado
                    if (isSunrise) {
                        R.raw.tornado
                    } else {
                        R.raw.tornado
                    }
                }

                800 -> { // clear sky
                    if (isSunrise) {
                        R.raw.clear_day
                    } else {
                        R.raw.clear_night
                    }
                }

                801 -> { // few clouds: 11-25%
                    if (isSunrise) {
                        R.raw.partly_cloudy_day
                    } else {
                        R.raw.partly_cloudy_night
                    }
                }
                802 -> { // scattered clouds: 25-50%
                    if (isSunrise) {
                        R.raw.overcast_day
                    } else {
                        R.raw.overcast_night
                    }
                }
                803 -> { // broken clouds: 51-84%
                    if (isSunrise) {
                        R.raw.extreme_day
                    } else {
                        R.raw.extreme_night
                    }
                }
                804 -> { // overcast clouds: 85-100%
                    if (isSunrise) {
                        R.raw.extreme_day
                    } else {
                        R.raw.extreme_night
                    }
                }

                else -> null
            }
        } ?: if (isSunrise) {
            R.raw.clear_day
        } else {
            R.raw.clear_night
        }
    }

}