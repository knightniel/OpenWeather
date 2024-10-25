package com.nielaclag.openweather.domain.viewmodel

import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.usecase.dao.localuserdao.LocalUserDaoUseCases
import com.nielaclag.openweather.domain.usecase.dao.locationinfodao.LocationInfoDaoUseCases
import com.nielaclag.openweather.domain.usecase.dao.weatherdao.WeatherDaoUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Niel on 10/21/2024.
 */
@Singleton
class DatabaseViewModelScope @Inject constructor(
    private val localUserDaoUseCases: LocalUserDaoUseCases,
    private val locationInfoDaoUseCases: LocationInfoDaoUseCases,
    private val weatherDaoUseCases: WeatherDaoUseCases
) {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _showSplash = MutableStateFlow(true)
    val showSplash = _showSplash.asStateFlow()

    private val _localUser = MutableStateFlow<LocalUser?>(null)
    val localUser = _localUser.asStateFlow()

    private val _locationInfo = MutableStateFlow<LocationInfo?>(null)
    val locationInfo = _locationInfo.asStateFlow()

    private val _latestWeather = MutableStateFlow<Weather?>(null)
    val latestWeather = _latestWeather.asStateFlow()

    init {
        checkUser()
        checkLocationInfo()
        checkLatestWeather()
    }

    suspend fun signOut() {
        withContext(Dispatchers.IO) {
            localUserDaoUseCases.deleteLocalUser()
            locationInfoDaoUseCases.deleteLocationInfo()
            weatherDaoUseCases.deleteAllWeather()
        }
    }

    private fun checkUser() {
        runBlocking(Dispatchers.IO) {
            _localUser.emit(localUserDaoUseCases.getLocalUser())
        }
        uiScope.launch {
            delay(1000)
            _showSplash.emit(false)
            localUserDaoUseCases.getLocalUserFlow().collectLatest { user ->
                _localUser.emit(user)
            }
        }
    }

    private fun checkLocationInfo() {
        runBlocking(Dispatchers.IO) {
            _locationInfo.emit(locationInfoDaoUseCases.getLocationInfo())
        }
        uiScope.launch {
            locationInfoDaoUseCases.getLocationInfoFlow().collectLatest { locationInfo ->
                _locationInfo.emit(locationInfo)
            }
        }
    }

    private fun checkLatestWeather() {
        uiScope.launch {
            weatherDaoUseCases.getLatestWeatherFlow().collectLatest { currentWeather ->
                _latestWeather.emit(currentWeather)
            }
        }
    }

}