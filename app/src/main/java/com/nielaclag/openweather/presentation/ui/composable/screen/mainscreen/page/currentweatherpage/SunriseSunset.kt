package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.nielaclag.openweather.presentation.model.type.SunsetSunrise
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.theme.Shapes

/**
 * Created by Niel on 10/22/2024.
 */
@Composable
fun SunriseSunset(
    sunriseTime: Long,
    sunsetTime: Long
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(colorScheme.surfaceVariant)
            .padding(AppDimension.itemSpaceMedium)
    ) {
//        Spacer(modifier = Modifier.height(AppDimension.itemSpaceSmall))
        SunsetSunriseInfo(
            sunsetSunrise = SunsetSunrise.SUNRISE,
            time = sunriseTime
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(AppDimension.itemSpaceSmall))
        SunsetSunriseInfo(
            sunsetSunrise = SunsetSunrise.SUNSET,
            time = sunsetTime
        )
    }
}