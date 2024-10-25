package com.nielaclag.openweather.presentation.ui.composable.component.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.theme.Shapes

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun ListLoader(
    modifier: Modifier = Modifier,
    id: Long? = null,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = AppDimension.screenPadding,
        vertical = AppDimension.itemSpaceMedium
    ),
    onLoad: (id: Long) -> Unit = {}
) {
    SideEffect {
        id?.let { id ->
            onLoad(id)
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .padding(contentPadding)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(20.dp)
        )
    }
}