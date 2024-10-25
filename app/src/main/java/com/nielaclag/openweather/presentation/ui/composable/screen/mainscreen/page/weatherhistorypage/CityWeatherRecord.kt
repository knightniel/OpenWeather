package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.weatherhistorypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nielaclag.openweather.common.helper.toFloat2
import com.nielaclag.openweather.common.helper.toProperCase
import com.nielaclag.openweather.presentation.model.core.WeatherRecord
import com.nielaclag.openweather.presentation.theme.AppDimension
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Niel on 10/23/2024.
 */
@Composable
fun CityWeatherRecord(
    weatherRecord: WeatherRecord
) {
    val time by remember(weatherRecord) {
        derivedStateOf {
            SimpleDateFormat("EEE h:mm a", Locale.getDefault()).format(
                Date(weatherRecord.dateTime)
            )
        }
    }
    val temperature by remember(weatherRecord) {
        derivedStateOf {
            "${ weatherRecord.weather.temperature.temp.toFloat2() }°C"
        }
    }
    val weatherIcon by remember(weatherRecord) {
        derivedStateOf {
            weatherRecord.weather.getWeatherIcon(weatherRecord.sunsetSunrise)
        }
    }
    val weatherDetails by remember(weatherRecord) {
        derivedStateOf {
            weatherRecord.weather.weatherInfo.firstOrNull()?.description?.toProperCase() ?: ""
        }
    }
    val temperatureFeelsLike by remember(weatherRecord) {
        derivedStateOf {
            "Feels like ${ weatherRecord.weather.temperature.feelsLike.toInt() }°C"
        }
    }
    val temperatureHighLow by remember(weatherRecord) {
        derivedStateOf {
            "High: ${ weatherRecord.weather.temperature.tempMax.toInt() }° • Low: ${ weatherRecord.weather.temperature.tempMin.toInt() }°"
        }
    }


    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(weatherIcon)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(
                vertical = AppDimension.clickablePaddingSmall
            )
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                text = weatherRecord.weather.cityName,
                fontSize = 12.sp,
                lineHeight = 12.sp.times(1.3f),
                color = colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
            Column {
                Text(
                    text = temperature,
                    fontSize = 16.sp,
                    lineHeight = 16.sp.times(1.3f),
                    color = colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = temperatureHighLow,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    lineHeight = 12.sp.times(1.3f),
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.width(AppDimension.itemSpaceMedium))
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(50.dp)
        )
        Spacer(modifier = Modifier.width(AppDimension.itemSpaceMedium))

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                text = time,
                fontSize = 12.sp,
                lineHeight = 12.sp.times(1.3f),
                color = colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = weatherDetails,
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 12.sp.times(1.3f),
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = temperatureFeelsLike,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    lineHeight = 12.sp.times(1.3f),
                    fontWeight = FontWeight.Normal
                )
            }
        }

    }
}