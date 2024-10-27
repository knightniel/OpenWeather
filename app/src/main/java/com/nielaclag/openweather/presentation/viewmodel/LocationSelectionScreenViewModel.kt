package com.nielaclag.openweather.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.nielaclag.openweather.common.helper.log
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.usecase.remote.openweather.OpenWeatherUseCases
import com.nielaclag.openweather.domain.viewmodel.DatabaseViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
@HiltViewModel
class LocationSelectionScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val openWeatherUseCases: OpenWeatherUseCases,
    private val databaseViewModelScope: DatabaseViewModelScope
) : ViewModel() {

    private val searchLocationLimit = 30

    val locationInfo = databaseViewModelScope.locationInfo
    val localUser = databaseViewModelScope.localUser

    private var _searchLocationJob: Job? = null
    private val _searchLocation = MutableStateFlow<Resource<Array<LocationInfo>>?>(null)
    val searchLocation = _searchLocation.asStateFlow()

    private val _cityLocations = mutableStateListOf<LocationInfo>()
    val cityLocations: SnapshotStateList<LocationInfo> = _cityLocations

    private val _currentLocation = mutableStateOf<Location?>(null)
    val currentLocation: State<Location?> = _currentLocation

    private val _gettingDeviceLocation = mutableStateOf(false)
    val gettingDeviceLocation: State<Boolean> = _gettingDeviceLocation

    init {
        locationInfo.value?.let { location ->
            searchLocation(location.name)
        }
    }

    fun searchLocation(location: String) {
        viewModelScope.launch {
            _searchLocationJob?.cancel()
            if (location.isBlank()) {
                _cityLocations.clear()
                _searchLocation.emit(Resource.Success(arrayOf()))
            } else {
                _searchLocationJob = openWeatherUseCases
                    .getLocationsByName(
                        cityName = location,
                        stateCode = "",
                        countryCode = "",
                        limit = searchLocationLimit
                    ).onEach { result ->
                        when (result) {
                            is Resource.Loading -> {
                            }
                            is Resource.Error -> {
                                _cityLocations.clear()
                            }
                            is Resource.Success -> {
                                _cityLocations.clear()
                                _cityLocations.addAll(result.data ?: arrayOf())
                            }
                        }
                        _searchLocation.emit(result)
                    }.launchIn(this)
            }
        }
    }

    fun searchLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _currentLocation.value = null
            _gettingDeviceLocation.value = false
            _searchLocationJob?.cancel()
            _searchLocationJob = openWeatherUseCases
                .getLocationsByCoordinate(
                    latitude = latitude,
                    longitude = longitude,
                    limit = searchLocationLimit
                ).onEach { result ->
                    when (result) {
                        is Resource.Loading -> {
                        }
                        is Resource.Error -> {
                            _cityLocations.clear()
                        }
                        is Resource.Success -> {
                            closeLocationListener()
                            _cityLocations.clear()
                            _cityLocations.addAll(result.data ?: arrayOf())
                        }
                    }
                    _searchLocation.emit(result)
                }.launchIn(this)
        }
    }

    private fun readLocation(location: Location) {
        viewModelScope.launch {
            searchLocation(
                latitude = location.latitude,
                longitude = location.longitude
            )
        }
    }

    private fun closeLocationListener() {
        fusedLocationClient.removeLocationUpdates(locationListener)
    }

    override fun onCleared() {
        super.onCleared()
        closeLocationListener()
    }

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private val locationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500).build()
    }
    private val locationListener by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.locations.maxByOrNull { it.accuracy } ?: locationResult.lastLocation
                if (location != null) {
                    val currentLocation = _currentLocation.value
                    log(
                        tag = "Location",
                        message = "onLocationChanged:\n" +
                                "oldLocation: lat: ${ currentLocation?.latitude }, lon: ${ currentLocation?.longitude }, accuracy: ${ currentLocation?.accuracy }\n" +
                                "newLocation: lat: ${ location.latitude }, lon: ${ location.longitude }, accuracy: ${ location.accuracy }"
                    )
                    if (
                        currentLocation == null ||
                        location.accuracy > currentLocation.accuracy
                    ) {
                        log(
                            tag = "Location",
                            message = "Location Updated: lat: ${ location.latitude }, lon: ${ location.longitude }, accuracy: ${ location.accuracy }"
                        )
                        _currentLocation.value = location
                        _gettingDeviceLocation.value = false
                        readLocation(location)
                        closeLocationListener()
                    }
                }
            }
        }
    }

    fun useDeviceLocation() {
        viewModelScope.launch {
            val withPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val withGpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val withNetworkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            when {
                withPermission && (withGpsProvider || withNetworkProvider) -> {
                    _currentLocation.value = null
                    _gettingDeviceLocation.value = true
                    _searchLocation.emit(Resource.Loading())

                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationListener,
                        Looper.getMainLooper()
                    )
                }
                withPermission -> {
                    _gettingDeviceLocation.value = false
                    _searchLocation.emit(Resource.Error("Please turn on device location."))
                }
                else -> {
                    _gettingDeviceLocation.value = false
                    _searchLocation.emit(Resource.Error("Location permission is required."))
                }
            }
        }
    }

}