package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import com.nielaclag.openweather.R
import com.nielaclag.openweather.presentation.model.type.SunsetSunrise
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Niel on 10/22/2024.
 */
@Composable
fun SunsetSunriseInfo(
    sunsetSunrise: SunsetSunrise,
    time: Long
) {
    val timeString by remember(time) {
        derivedStateOf {
            SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(time))
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(if (sunsetSunrise == SunsetSunrise.SUNSET) R.raw.sunset else R.raw.sunrise)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = if (sunsetSunrise == SunsetSunrise.SUNSET) "Sunset" else "Sunrise",
                color = colorScheme.onPrimaryContainer,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = timeString,
                color = colorScheme.onPrimaryContainer,
                fontSize = 18.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(50.dp)
        )
    }
}