package com.nielaclag.openweather.presentation.ui.composable.screen.basescreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.nielaclag.openweather.R
import com.nielaclag.openweather.common.helper.fadingEdge
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.ui.composable.component.core.ListError
import com.nielaclag.openweather.presentation.ui.composable.component.core.ListLoader
import com.nielaclag.openweather.presentation.ui.composable.component.core.ListMessage
import com.nielaclag.openweather.presentation.ui.composable.component.core.rememberPermissionRequestState
import com.nielaclag.openweather.presentation.ui.composable.screen.locationselectionscreen.CityLocation
import com.nielaclag.openweather.presentation.ui.composable.screen.locationselectionscreen.LocationSelectionScreenTopBar
import com.nielaclag.openweather.presentation.viewmodel.LocationSelectionScreenViewModel
import kotlinx.coroutines.launch

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun LocationSelectionScreen(
    locationSelectionScreenViewModel: LocationSelectionScreenViewModel = hiltViewModel(),
    onLocationSelected: (locationInfo: LocationInfo) -> Unit,
    showUserOptions: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current

    val localUser by locationSelectionScreenViewModel.localUser.collectAsState()
    val locationInfo by locationSelectionScreenViewModel.locationInfo.collectAsState()

    val initialLocation = remember {
        locationInfo?.name
    }
    var location by remember {
        mutableStateOf(initialLocation ?: "")
    }

    val gettingDeviceLocation by locationSelectionScreenViewModel.gettingDeviceLocation
    val cityLocationsResponse by locationSelectionScreenViewModel.searchLocation.collectAsState()
    val cityLocations = locationSelectionScreenViewModel.cityLocations

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // Location settings were enabled
//            // You can now proceed with location-related tasks
//            locationSelectionScreenViewModel.useDeviceLocation()
//        } else {
//            // Location settings were not enabled
//            // Handle this case, e.g., show an error message
//            locationSelectionScreenViewModel.useDeviceLocation()
//        }
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val withGpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val withNetworkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (withGpsProvider || withNetworkProvider) {
            locationSelectionScreenViewModel.useDeviceLocation()
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Please turn on device location.")
            }
        }
    }

    val permissionStateFineLocation = rememberPermissionRequestState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        rationaleTitle = "Location Permission",
        permanentlyDeclinedTitle = "Location Permission",
        rationaleMessage = "We need access to your location.\nPlease grant the permission.",
        onPermissionResult = { granted ->
            if (granted) {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val withGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (!withGps && !hasNetwork) {
                    locationSettingsLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                } else {
                    locationSelectionScreenViewModel.useDeviceLocation()
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar("Location permission is required.")
                }
            }
        }
    )

    BackHandler(initialLocation == null) {
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(horizontal = AppDimension.screenPadding)
                    .imePadding()
                    .navigationBarsPadding()
            )
        },
        containerColor = colorScheme.background,
        topBar = {
            LocationSelectionScreenTopBar(
                initialLocation = initialLocation,
                localUser = localUser,
                gettingDeviceLocation = gettingDeviceLocation,
                onLocationTextChanged = { newLocation ->
                    location = newLocation
                    locationSelectionScreenViewModel.searchLocationByName(newLocation)
                },
                onUseCurrentLocation = {
                    permissionStateFineLocation.launchPermissionRequest()
                },
                showUserOptions = showUserOptions
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
    ) { paddingValues ->
        val edges by remember(screenHeight, paddingValues) {
            derivedStateOf {
                Pair(
                    paddingValues.calculateTopPadding() / screenHeight,
                    (paddingValues.calculateTopPadding() + AppDimension.screenPadding) / screenHeight
                )
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + AppDimension.screenPadding,
                bottom = paddingValues.calculateBottomPadding() + AppDimension.screenPadding
            ),
            verticalArrangement = if (cityLocations.isEmpty()) Arrangement.Center else Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .fadingEdge(
                    Brush.verticalGradient(
                        edges.first to Color.Transparent,
                        edges.second to Color.White,
                    )
                )
                .imePadding()
        ) {
            if (!gettingDeviceLocation) {
                when (cityLocationsResponse) {
                    is Resource.Loading -> {
                        item {
                            ListLoader(
                                modifier = Modifier
                                    .padding(
                                        horizontal = AppDimension.screenPadding
                                    )
                            )
                        }
                        if (cityLocations.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(AppDimension.screenPadding))
                            }
                        }
                    }
                    is Resource.Error -> {
                        item {
                            ListError(
                                errorMessage = (cityLocationsResponse?.message ?: "Something went wrong.") + "\nClick to retry.",
                                onClick = {
                                    locationSelectionScreenViewModel.searchLocationByName(location)
                                },
                                modifier = Modifier
                                    .padding(
                                        horizontal = AppDimension.screenPadding
                                    )
                            )
                        }
                        if (cityLocations.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(AppDimension.screenPadding))
                            }
                        }
                    }
                    else -> {
                        if (cityLocations.isEmpty()) {
                            if (location.isBlank()) {
                                item {
                                    ListMessage(
                                        message = "Search your city location.",
                                        iconPainter = rememberAsyncImagePainter(model = R.drawable.ic_map_search),
                                        modifier = Modifier
                                            .padding(
                                                horizontal = AppDimension.screenPadding
                                            )
                                    )
                                }
                            } else {
                                item {
                                    ListMessage(
                                        message = "Nothing found. Please try again.",
                                        iconPainter = rememberAsyncImagePainter(model = R.drawable.ic_map_search),
                                        modifier = Modifier
                                            .padding(
                                                horizontal = AppDimension.screenPadding
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            itemsIndexed(
                items = cityLocations
            ) { index, locationInfo ->
                CityLocation(
                    locationInfo = locationInfo,
                    onClick = {
                        onLocationSelected(locationInfo)
                    }
                )
                if (cityLocations.lastIndex != index) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(
                                horizontal = AppDimension.screenPadding
                            )
                    )
                }
            }

        }
    }
}