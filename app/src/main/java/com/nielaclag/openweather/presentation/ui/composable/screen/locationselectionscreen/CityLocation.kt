package com.nielaclag.openweather.presentation.ui.composable.screen.locationselectionscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.theme.Shapes
import java.util.Locale

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun CityLocation(
    locationInfo: LocationInfo,
    onClick: () -> Unit
) {
    val country by remember(locationInfo) {
        derivedStateOf {
            Locale("", locationInfo.country).displayName
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppDimension.screenPadding - AppDimension.clickablePaddingMedium,
                vertical = AppDimension.itemSpaceSmall - AppDimension.clickablePaddingSmall
            )
            .clip(Shapes.small)
            .clickable {
                onClick()
            }
            .padding(
                horizontal = AppDimension.clickablePaddingMedium,
                vertical = AppDimension.clickablePaddingSmall
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = locationInfo.name,
                fontSize = 12.sp,
                color = colorScheme.onBackground
            )
            Text(
                text = "${ locationInfo.state?.let { "$it - " } ?: "" }${ country }",
                fontSize = 10.sp,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}