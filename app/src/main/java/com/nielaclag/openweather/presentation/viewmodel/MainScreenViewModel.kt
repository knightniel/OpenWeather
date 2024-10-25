package com.nielaclag.openweather.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nielaclag.openweather.common.helper.dateDiffInMilliSeconds
import com.nielaclag.openweather.common.helper.log
import com.nielaclag.openweather.common.helper.toLocalZonedDateTime
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.model.type.TimePeriod
import com.nielaclag.openweather.domain.usecase.dao.weatherdao.WeatherDaoUseCases
import com.nielaclag.openweather.domain.usecase.remote.openweather.OpenWeatherUseCases
import com.nielaclag.openweather.domain.usecase.util.UtilUseCases
import com.nielaclag.openweather.domain.viewmodel.DatabaseViewModelScope
import com.nielaclag.openweather.presentation.model.core.WeatherRecord
import com.nielaclag.openweather.presentation.model.type.SunsetSunrise
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    databaseViewModelScope: DatabaseViewModelScope,
    private val weatherDaoUseCases: WeatherDaoUseCases,
    private val openWeatherUseCases: OpenWeatherUseCases,
    private val utilUseCases: UtilUseCases,
    val moshi: Moshi
) : ViewModel() {

    val localUser = databaseViewModelScope.localUser
    val locationInfo = databaseViewModelScope.locationInfo
    val latestWeather = databaseViewModelScope.latestWeather

    private val currentWeatherCheckTrigger = MutableStateFlow(0L)

    private var _getCurrentWeatherJob: Job? = null
    private val _getWeatherResponse = MutableStateFlow<Resource<Weather>?>(null)
    val getWeatherResponse = _getWeatherResponse.asStateFlow()

    private val _latestWeatherTimePeriod = mutableStateOf(TimePeriod.Morning)
    val latestWeatherTimePeriod: State<TimePeriod> = _latestWeatherTimePeriod

    private val _sunsetSunrise = mutableStateOf(SunsetSunrise.SUNRISE)
    val sunsetSunrise: State<SunsetSunrise> = _sunsetSunrise

    private val _latestWeatherDate = mutableStateOf("")
    val latestWeatherDate: State<String> = _latestWeatherDate

    private val _timeZone = mutableStateOf<String?>(null)
    val timeZone: State<String?> = _timeZone

    private val _sunriseTime = mutableLongStateOf(0L)
    val sunriseTime: State<Long> = _sunriseTime

    private val _sunsetTime = mutableLongStateOf(0L)
    val sunsetTime: State<Long> = _sunsetTime

    private val _cityWeatherRecords = mutableStateOf<Flow<PagingData<WeatherRecord>>>(
        flowOf(PagingData.empty())
    )
    val cityWeatherRecords: State<Flow<PagingData<WeatherRecord>>> = _cityWeatherRecords

    init {
        checkLatestWeatherTimePeriod()
        initCurrentWeatherChecker()
        initCityWeatherRecordChecker()
    }

    private fun checkLatestWeatherTimePeriod() {
        viewModelScope.launch {
            latestWeather.collectLatest { weather ->
                log("checkLatestWeatherTimePeriod: [${ weather?.timezone } - ${ weather?.sys?.country }]}")
                if (weather != null) {
                    utilUseCases.getZonedDateTime(
                        timeMillis = weather.dt * 1000,
                        zoneOffsetInMillis = weather.timezone * 1000
                    ).let { zonedDateTime ->
                        val date = Date.from(zonedDateTime.toLocalZonedDateTime().toInstant())
                        _timeZone.value = zonedDateTime.zone.id

                        val dateDiff = date.dateDiffInMilliSeconds(
                            dateTo = Date(),
                            computeBeforeAndAfter = false
                        ) ?: 0L
                        val sunriseZonedDateTime = utilUseCases
                            .getZonedDateTime(
                                timeMillis = weather.sys.sunrise * 1000,
                                zoneOffsetInMillis = weather.timezone * 1000
                            )

                        val sunsetZonedDateTime = utilUseCases
                            .getZonedDateTime(
                                timeMillis = weather.sys.sunset * 1000,
                                zoneOffsetInMillis = weather.timezone * 1000
                            )

                        _sunriseTime.longValue = sunriseZonedDateTime.toLocalZonedDateTime().toInstant().toEpochMilli()
                        _sunsetTime.longValue = sunsetZonedDateTime.toLocalZonedDateTime().toInstant().toEpochMilli()

                        while (true) {
                            val workingDate = Date(System.currentTimeMillis() - dateDiff)

                            val currentTimeFloat =  zonedDateTime.hour.toFloat() + (zonedDateTime.minute.toFloat() / 60f)
                            val sunriseTimeFloat =  sunriseZonedDateTime.hour.toFloat() + (sunriseZonedDateTime.minute.toFloat() / 60f)
                            val sunsetTimeFloat =  sunsetZonedDateTime.hour.toFloat() + (sunsetZonedDateTime.minute.toFloat() / 60f)

                            if (sunriseTimeFloat < sunsetTimeFloat) {
                                currentTimeFloat in sunriseTimeFloat..sunsetTimeFloat
                            } else {
                                currentTimeFloat !in sunsetTimeFloat..sunriseTimeFloat
                            }.let { isDay ->
                                _sunsetSunrise.value = if (isDay) SunsetSunrise.SUNRISE else SunsetSunrise.SUNSET
                            }
                            _latestWeatherTimePeriod.value = utilUseCases.getTimePeriod(
                                timeMillis = workingDate.time,
                                zoneOffsetInMillis = 0
                            )

                            _latestWeatherDate.value = SimpleDateFormat("EEE h:mm a", Locale.getDefault()).format(workingDate)

                            delay(1000L * 60L)
                        }
                    }
                }
            }
        }
    }

    private fun initCurrentWeatherChecker() {
        log("checkCurrentWeather init")
        viewModelScope.launch {
            combine(
                locationInfo,
                currentWeatherCheckTrigger
            ) { locationInfo, _ ->
                locationInfo
            }.collectLatest { locationInfo ->
                log("checkCurrentWeather: ${locationInfo?.name}")
                checkCurrentWeather(locationInfo)
            }
        }
    }

    private fun initCityWeatherRecordChecker() {
        viewModelScope.launch {
            locationInfo.collectLatest { locationInfo ->
                log(
                    tag = "cityWeatherRecords",
                    message = "location changed: ${ locationInfo?.name }, lat: ${ locationInfo?.latitude }, lon: ${ locationInfo?.longitude }"
                )
                _cityWeatherRecords.value = getCityWeatherRecords(
                    locationInfo = locationInfo
                )
            }
        }
    }

    fun refreshCurrentWeatherStatus() {
        viewModelScope.launch {
            currentWeatherCheckTrigger.emit(System.currentTimeMillis())
        }
    }

    private fun checkCurrentWeather(locationInfo: LocationInfo?) {
        _getCurrentWeatherJob?.cancel()
        viewModelScope.launch {
            if (locationInfo != null) {
                _getCurrentWeatherJob = openWeatherUseCases
                    .getCurrentWeather(
                        latitude = locationInfo.latitude,
                        longitude = locationInfo.longitude,
                        units = "metric"
                    ).onEach { result ->
                        when (result) {
                            is Resource.Loading -> {
                            }
                            is Resource.Error -> {
                            }
                            is Resource.Success -> {
                                result.data?.let { currentWeather ->
                                    weatherDaoUseCases.insertWeather(currentWeather)
                                }
                            }
                        }
                        _getWeatherResponse.emit(result)
                    }.launchIn(this)
            } else {
                _getWeatherResponse.emit(null)
            }
        }
    }

    private fun CoroutineScope.getCityWeatherRecords(locationInfo: LocationInfo?): Flow<PagingData<WeatherRecord>> {
        return weatherDaoUseCases
            .getWeatherRecordsByCityPaged(
                locationInfo = locationInfo
            ).cachedIn(this)
    }

}