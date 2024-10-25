package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.nielaclag.openweather.R
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.ui.composable.component.core.ListError
import com.nielaclag.openweather.presentation.ui.composable.component.core.ListLoader
import com.nielaclag.openweather.presentation.ui.composable.component.core.ListMessage
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage.HumidityInfo
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage.WeatherInfo
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage.LocationInfo
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage.PressureInfo
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage.SunriseSunset
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage.WindInfo
import com.nielaclag.openweather.presentation.viewmodel.MainScreenViewModel

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun CurrentWeatherPage(
    paddingValues: PaddingValues,
    mainScreenViewModel: MainScreenViewModel
) {
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()

    val latestWeather by mainScreenViewModel.latestWeather.collectAsState()
    val getWeatherResponse by mainScreenViewModel.getWeatherResponse.collectAsState()
    val locationInfo by mainScreenViewModel.locationInfo.collectAsState()
    val timeZone by mainScreenViewModel.timeZone

    val latestWeatherTimePeriod by mainScreenViewModel.latestWeatherTimePeriod
    val sunsetSunrise by mainScreenViewModel.sunsetSunrise
    val latestWeatherDate by mainScreenViewModel.latestWeatherDate

    val sunriseTime by mainScreenViewModel.sunriseTime
    val sunsetTime by mainScreenViewModel.sunsetTime

    val itemVerticalPadding = AppDimension.itemSpaceMedium
    val itemHorizontalPadding = itemVerticalPadding / 2

    Crossfade(
        targetState = locationInfo != null,
        label = "With Location",
        modifier = Modifier
            .fillMaxSize()
    ) { withLocation ->
        if (withLocation) {
            LazyVerticalStaggeredGrid(
                state = lazyStaggeredGridState,
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = itemVerticalPadding,
                contentPadding = PaddingValues(
                    start = (AppDimension.screenPadding - itemHorizontalPadding).coerceAtLeast(0.dp),
                    end = (AppDimension.screenPadding - itemHorizontalPadding).coerceAtLeast(0.dp),
                    top = paddingValues.calculateTopPadding() + AppDimension.screenPadding,
                    bottom = paddingValues.calculateBottomPadding() + AppDimension.screenPadding
                ),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (getWeatherResponse is Resource.Loading) {
                    item(
                        key = "Loader",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        ListLoader(
                            modifier = Modifier
                                .animateItem()
                                .padding(
                                    horizontal = itemHorizontalPadding
                                )
                        )
                    }
                    if (latestWeather != null) {
                        item(
                            key = "LoaderSpacer",
                            span = StaggeredGridItemSpan.FullLine
                        ) {
                            Spacer(modifier = Modifier.height(AppDimension.screenPadding))
                        }
                    }
                } else if (getWeatherResponse is Resource.Error) {
                    item(
                        key = "Error",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        ListError(
                            errorMessage = getWeatherResponse?.message,
                            modifier = Modifier
                                .animateItem()
                                .padding(
                                    horizontal = itemHorizontalPadding
                                )
                        )
                    }
                    if (latestWeather != null) {
                        item(
                            key = "ErrorSpacer",
                            span = StaggeredGridItemSpan.FullLine
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .animateItem()
                                    .height(AppDimension.screenPadding)
                            )
                        }
                    }
                } else if (locationInfo == null) {
                    item(
                        key = "NoLocation",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        ListMessage(
                            message = "Please select your city location first.",
                            iconPainter = rememberAsyncImagePainter(model = R.drawable.ic_map_search)
                        )
                    }
                }

                latestWeather?.let { weather ->
                    item(
                        key = "WeatherInfo",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        Box(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .padding(
                                    horizontal = itemHorizontalPadding
                                )
                        ) {
                            WeatherInfo(
                                weather = weather,
                                sunsetSunrise = sunsetSunrise,
                                dateTime = latestWeatherDate
                            )
                        }
                    }
                    item(
                        key = "SunriseSunset",
                        span = StaggeredGridItemSpan.SingleLane
                    ) {
                        Box(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .padding(
                                    horizontal = itemHorizontalPadding
                                )
                        ) {
                            SunriseSunset(
                                sunriseTime = sunriseTime,
                                sunsetTime = sunsetTime
                            )
                        }
                    }
                    item(
                        key = "LocationInfo",
                        span = StaggeredGridItemSpan.SingleLane
                    ) {
                        Box(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .padding(
                                    horizontal = itemHorizontalPadding
                                )
                        ) {
                            LocationInfo(
                                weather = weather,
                                timeZone = timeZone
                            )
                        }
                    }
                    weather.wind?.let { wind ->
                        item(
                            key = "WindInfo",
                            span = StaggeredGridItemSpan.SingleLane
                        ) {
                            Box(
                                modifier = Modifier
                                    .animateItem()
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = itemHorizontalPadding
                                    )
                            ) {
                                WindInfo(
                                    wind = wind
                                )
                            }
                        }
                    }
                    item(
                        key = "PressureInfo",
                        span = StaggeredGridItemSpan.SingleLane
                    ) {
                        Box(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .padding(
                                    horizontal = itemHorizontalPadding
                                )
                        ) {
                            PressureInfo(
                                weather = weather
                            )
                        }
                    }
                    item(
                        key = "HumidityInfo",
                        span = StaggeredGridItemSpan.SingleLane
                    ) {
                        Box(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .padding(
                                    horizontal = itemHorizontalPadding
                                )
                        ) {
                            HumidityInfo(
                                weather = weather
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding() + AppDimension.screenPadding,
                        bottom = paddingValues.calculateBottomPadding() + AppDimension.screenPadding
                    )
            ) {
                ListMessage(
                    message = "Please select your city location first.",
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