package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.page.currentweatherpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.theme.Shapes
import java.util.Locale

/**
 * Created by Niel on 10/23/2024.
 */
@Composable
fun LocationInfo(
    weather: Weather,
    timeZone: String?
) {
    val country by remember(weather) {
        derivedStateOf {
            Locale("", weather.sys.country).displayName
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(colorScheme.surfaceVariant)
            .padding(AppDimension.itemSpaceMedium)
    ) {
        Text(
            text = weather.cityName,
            color = colorScheme.onPrimaryContainer,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
        Text(
            text = country,
            color = colorScheme.onPrimaryContainer,
            fontSize = 12.sp,
            lineHeight = 12.sp.times(1.3f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal
        )

//        Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
        timeZone?.let {
            Text(
                text = timeZone,
                color = colorScheme.onPrimaryContainer,
                fontSize = 20.sp,
                lineHeight = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
        Text(
            text = "Lat: ${ weather.coordinate.latitude }",
            color = colorScheme.onPrimaryContainer,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = "Lon: ${ weather.coordinate.longitude }",
            color = colorScheme.onPrimaryContainer,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal
        )
    }
}