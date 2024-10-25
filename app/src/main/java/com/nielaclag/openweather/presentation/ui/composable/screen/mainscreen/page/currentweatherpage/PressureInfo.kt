package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.nielaclag.openweather.R
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.theme.Shapes

/**
 * Created by Niel on 10/23/2024.
 */
@Composable
fun PressureInfo(
    weather: Weather
) {
    val pressure by remember(weather) {
        derivedStateOf {
            "${ weather.temperature.pressure } hPa"
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(colorScheme.surfaceVariant)
            .padding(AppDimension.itemSpaceMedium)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Pressure",
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = pressure,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
            ) {
                val compositionWind by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.overcast)
                )
                val progressWind by animateLottieCompositionAsState(
                    composition = compositionWind,
                    iterations = LottieConstants.IterateForever
                )
                val compositionArrow by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.arrow_down)
                )
                val progressArrow by animateLottieCompositionAsState(
                    composition = compositionArrow,
                    iterations = LottieConstants.IterateForever
                )

                LottieAnimation(
                    composition = compositionWind,
                    progress = { progressWind },
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.BottomCenter)
                )
                LottieAnimation(
                    composition = compositionArrow,
                    progress = { (progressArrow + .1f).let { adjustedValue ->
                        if (adjustedValue > 1f) adjustedValue - 1f else adjustedValue }
                    },
                    modifier = Modifier
                        .fillMaxSize(.7f)
                        .align(Alignment.TopStart)
                )
                LottieAnimation(
                    composition = compositionArrow,
                    progress = { progressArrow },
                    modifier = Modifier
                        .fillMaxSize(.7f)
                        .align(Alignment.TopEnd)
                )
            }
        }

    }
}