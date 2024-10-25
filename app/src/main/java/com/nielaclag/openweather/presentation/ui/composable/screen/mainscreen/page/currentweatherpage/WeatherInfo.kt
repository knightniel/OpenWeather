package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
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
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.presentation.model.type.SunsetSunrise
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.theme.Shapes

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun WeatherInfo(
    weather: Weather,
    sunsetSunrise: SunsetSunrise,
    dateTime: String
) {
    val weatherIcon by remember(weather, sunsetSunrise) {
        derivedStateOf {
            weather.getWeatherIcon(sunsetSunrise)
        }
    }
    val temperature by remember(weather) {
        derivedStateOf {
            "${ weather.temperature.temp.toFloat2() }°C"
        }
    }
    val temperatureFeelsLike by remember(weather) {
        derivedStateOf {
            "Feels like ${ weather.temperature.feelsLike.toInt() }°C"
        }
    }
    val temperatureHighLow by remember(weather) {
        derivedStateOf {
            "High: ${ weather.temperature.tempMax.toInt() }° • Low: ${ weather.temperature.tempMin.toInt() }°"
        }
    }
    val weatherDetails by remember(weather) {
        derivedStateOf {
            weather.weatherInfo.firstOrNull()?.description?.toProperCase() ?: ""
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clip(Shapes.medium)
            .background(colorScheme.surfaceVariant)
            .padding(AppDimension.itemSpaceMedium)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(1.3f)
                .fillMaxHeight()
        ) {
            Text(
                text = dateTime,
                color = colorScheme.onPrimaryContainer,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
            Column {
                Text(
                    text = temperatureFeelsLike,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = temperature,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = temperatureHighLow,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.width(AppDimension.itemSpaceMedium))
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .weight(1f)
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(weatherIcon)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            Box(
                contentAlignment = Alignment.BottomCenter,
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(150.dp)
                )
                Text(
                    text = weatherDetails,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.width(AppDimension.itemSpaceMedium))
    }
}