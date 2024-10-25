package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import com.nielaclag.openweather.R
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.ui.composable.component.core.ListMessage
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.weatherhistorypage.CityWeatherRecord
import com.nielaclag.openweather.presentation.viewmodel.MainScreenViewModel

/**
 * Created by Niel on 10/23/2024.
 */
@Composable
fun WeatherHistoryPage(
    paddingValues: PaddingValues,
    mainScreenViewModel: MainScreenViewModel
) {
    val columnState = rememberLazyListState()
    val locationInfo by mainScreenViewModel.locationInfo.collectAsState()
    val cityWeatherRecords = mainScreenViewModel.cityWeatherRecords.value.collectAsLazyPagingItems()

    LazyColumn(
        state = columnState,
        verticalArrangement = if (cityWeatherRecords.itemCount == 0) Arrangement.Center else Arrangement.Top,
        contentPadding = PaddingValues(
            start = AppDimension.screenPadding,
            end = AppDimension.screenPadding,
            top = paddingValues.calculateTopPadding() + AppDimension.screenPadding,
            bottom = paddingValues.calculateBottomPadding() + AppDimension.screenPadding
        ),
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (cityWeatherRecords.itemCount == 0) {
            if (locationInfo == null) {
                item {
                    ListMessage(
                        message = "Please select your city location first.",
                        iconPainter = rememberAsyncImagePainter(model = R.drawable.ic_map_search)
                    )
                }
            } else {
                item {
                    ListMessage(
                        message = "Nothing to show here.",
                        iconPainter = rememberAsyncImagePainter(model = R.drawable.ic_map_search)
                    )
                }
            }
        } else {
            items(
                count = cityWeatherRecords.itemCount,
                key = cityWeatherRecords.itemKey { it.weather.id }
            ) { index ->
                val weatherRecord = cityWeatherRecords[index]
                if (weatherRecord != null) {
                    CityWeatherRecord(
                        weatherRecord = weatherRecord
                    )
                    if (index != cityWeatherRecords.itemCount - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}