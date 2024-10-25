package com.nielaclag.openweather.domain.usecase.dao.weatherdao

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.nielaclag.openweather.common.helper.haversine
import com.nielaclag.openweather.common.helper.log
import com.nielaclag.openweather.common.helper.toLocalZonedDateTime
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import com.nielaclag.openweather.domain.usecase.util.UtilUseCases
import com.nielaclag.openweather.presentation.model.core.WeatherRecord
import com.nielaclag.openweather.presentation.model.type.SunsetSunrise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

/**
 * Created by Niel on 10/23/2024.
 */
class GetWeatherRecordsByCityPaged @Inject constructor(
    private val repository: WeatherDaoRepository,
    private val utilUseCases: UtilUseCases,
) {
    operator fun invoke(
        locationInfo: LocationInfo?
    ): Flow<PagingData<WeatherRecord>> {
        log(
            tag = "cityWeatherRecords",
            message = "GetWeatherRecordsByCityPaged: ${ locationInfo?.name } lat: ${ locationInfo?.latitude }, lon: ${ locationInfo?.longitude }"
        )
        return if (locationInfo != null) {
            Pager(
                config = PagingConfig(
                    pageSize = 5,
                    prefetchDistance = 5
                )
            ) {
//                repository.getCityDataListPaged(
//                    coordinate = coordinate.toEntity()
//                )
                repository.getDataListPaged()
            }
                .flow
                .map { pagingData ->
                    pagingData
                        .filter { weather ->
                            val distance = haversine(
                                lat1 = locationInfo.latitude,
                                lon1 = locationInfo.longitude,
                                lat2 = weather.coordinate.latitude,
                                lon2 = weather.coordinate.longitude
                            )
                            val valid = distance <= 20 || (weather.cityName == locationInfo.name && weather.sys.country == locationInfo.country)

                            log(
                                tag = "cityWeatherRecords",
                                message = "checkEqual\n" +
                                        "lookingFor: ${ locationInfo.latitude }, lon: ${ locationInfo.longitude }\n" +
                                        "entity: ${ weather.coordinate.latitude }, lon: ${ weather.coordinate.longitude }\n" +
                                        "distance: $distance\n" +
                                        "locationInfo: ${ locationInfo.name }, ${ locationInfo.country }\n" +
                                        "weather: ${ weather.cityName }, ${ weather.sys.country }\n" +
                                        "valid: $valid"
                            )
                            valid
                        }
                        .map { weatherEntity ->
                            val zonedDateTime = utilUseCases.getZonedDateTime(
                                timeMillis = weatherEntity.dt * 1000,
                                zoneOffsetInMillis = weatherEntity.timezone * 1000
                            )
                            val sunriseZonedDateTime = utilUseCases
                                .getZonedDateTime(
                                    timeMillis = weatherEntity.sys.sunrise * 1000,
                                    zoneOffsetInMillis = weatherEntity.timezone * 1000
                                )
                            val sunsetZonedDateTime = utilUseCases
                                .getZonedDateTime(
                                    timeMillis = weatherEntity.sys.sunset * 1000,
                                    zoneOffsetInMillis = weatherEntity.timezone * 1000
                                )

                            val date = Date.from(zonedDateTime.toLocalZonedDateTime().toInstant())
                            val sunriseDate = Date.from(sunriseZonedDateTime.toLocalZonedDateTime().toInstant())
                            val sunsetDate = Date.from(sunsetZonedDateTime.toLocalZonedDateTime().toInstant())

                            val currentTimeFloat =  zonedDateTime.hour.toFloat() + (zonedDateTime.minute.toFloat() / 60f)
                            val sunriseTimeFloat =  sunriseZonedDateTime.hour.toFloat() + (sunriseZonedDateTime.minute.toFloat() / 60f)
                            val sunsetTimeFloat =  sunsetZonedDateTime.hour.toFloat() + (sunsetZonedDateTime.minute.toFloat() / 60f)

                            val isSunrise = if (sunriseTimeFloat < sunsetTimeFloat) {
                                currentTimeFloat in sunriseTimeFloat..sunsetTimeFloat
                            } else {
                                currentTimeFloat !in sunsetTimeFloat..sunriseTimeFloat
                            }

                            WeatherRecord(
                                weather = weatherEntity.toDomain(),
                                dateTime = date.time,
                                sunrise = sunriseDate.time,
                                sunset = sunsetDate.time,
                                sunsetSunrise = if (isSunrise) SunsetSunrise.SUNRISE else SunsetSunrise.SUNSET
                            )
                        }
                }
        } else {
            emptyFlow()
        }
    }
}