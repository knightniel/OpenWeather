package com.nielaclag.openweather.presentation.ui.composable.screen.basescreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nielaclag.openweather.common.helper.fadingEdge
import com.nielaclag.openweather.presentation.model.page.MainPage
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.CurrentWeatherPage
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.MainScreenTopBar
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.WeatherHistoryPage
import com.nielaclag.openweather.presentation.viewmodel.DatabaseViewModel
import com.nielaclag.openweather.presentation.viewmodel.MainScreenViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun MainScreen(
    databaseViewModel: DatabaseViewModel = hiltViewModel(),
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    onSelectLocation: () -> Unit,
    showUserOptions: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val locationInfoFlow = databaseViewModel.locationInfo
    val localUser by databaseViewModel.localUser.collectAsState()
    val locationInfoState by databaseViewModel.locationInfo.collectAsState()

    val currentLocation by remember(locationInfoState) {
        derivedStateOf {
            locationInfoState?.let { locationInfo ->
                "${ locationInfo.name }, ${ locationInfo.state?.let { "$it - " } ?: "" }${ Locale("", locationInfo.country).displayName }"
            } ?: ""
        }
    }

    LaunchedEffect(Unit) {
        launch {
            locationInfoFlow.collectLatest { location ->
                if (location == null && localUser != null) {
                    onSelectLocation()
                }
            }
        }
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

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
//        containerColor = colorScheme.primaryContainer,
        topBar = {
            MainScreenTopBar(
                navController = navController,
                localUser = localUser,
                location = currentLocation,
                onLocationChangeRequest = onSelectLocation,
                showUserOptions = showUserOptions
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        val edges by remember(screenHeight, paddingValues) {
            derivedStateOf {
                Pair(
                    paddingValues.calculateTopPadding() / screenHeight,
                    (paddingValues.calculateTopPadding() + AppDimension.screenPadding) / screenHeight
                )
            }
        }
        NavHost(
            navController = navController,
            startDestination = MainPage.CurrentWeather,
            modifier = Modifier
                .fillMaxSize()
                .fadingEdge(
                    Brush.verticalGradient(
                        edges.first to Color.Transparent,
                        edges.second to Color.White,
                    )
                )
        ) {
            composable<MainPage.CurrentWeather> {
                CurrentWeatherPage(
                    paddingValues = paddingValues,
                    mainScreenViewModel = mainScreenViewModel
                )
            }
            composable<MainPage.WeatherHistory> {
                WeatherHistoryPage(
                    paddingValues = paddingValues,
                    mainScreenViewModel = mainScreenViewModel
                )
            }
        }
    }

}