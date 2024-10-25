package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nielaclag.openweather.R
import com.nielaclag.openweather.common.helper.toFloat2
import com.nielaclag.openweather.domain.model.weather.Wind
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.theme.Shapes

/**
 * Created by Niel on 10/23/2024.
 */
@Composable
fun WindInfo(
    wind: Wind
) {
    val windDirection by remember(wind) {
        derivedStateOf {
            // FLIPS THE DIRECTION, DEGREE VALUE IS FOR WHERE IT ORIGINATED FROM
            wind.degrees.toFloat() + 90f + 180f
        }
    }
    val windSpeed by remember(wind) {
        derivedStateOf {
            "${ wind.speed } m/s"
        }
    }
    val gustSpeed by remember(wind) {
        derivedStateOf {
            wind.gust?.let { speed ->
                "${ speed.toFloat2() } m/s"
            }
        }
    }
    val windDirectionAnim by animateFloatAsState(
        targetValue = windDirection,
        label = "Direction"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
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
            Column {
                Text(
                    text = "Direction",
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "${ wind.degrees }°N",
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            AsyncImage(
                model = R.drawable.ic_arrow_left_alt,
                contentDescription = "Wind direction",
                colorFilter = ColorFilter.tint(colorScheme.onPrimaryContainer),
                modifier = Modifier
                    .size(50.dp)
                    .rotate(windDirectionAnim)
            )
        }

        Spacer(modifier = Modifier.height(AppDimension.itemSpaceSmall))
        HorizontalDivider()

//        Spacer(modifier = Modifier.height(AppDimension.itemSpaceSmall))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(AppDimension.itemSpaceSmall))
                Text(
                    text = "Wind",
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = windSpeed,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal
                )
                gustSpeed?.let { speed ->
                    Spacer(modifier = Modifier.height(AppDimension.itemSpaceSmall))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(AppDimension.itemSpaceSmall))
                    Text(
                        text = "Gust",
                        color = colorScheme.onPrimaryContainer,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = speed,
                        color = colorScheme.onPrimaryContainer,
                        fontSize = 18.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(if (wind.speed >= 4.5f) R.raw.windsock else R.raw.windsock_weak)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(50.dp)
            )
        }

    }
}