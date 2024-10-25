package com.nielaclag.openweather.presentation.ui.composable.component.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.nielaclag.openweather.R
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.theme.Shapes

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun ListError(
    modifier: Modifier = Modifier,
    iconPainter: Painter = rememberAsyncImagePainter(R.drawable.ic_error),
    errorMessage: String? = null,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = AppDimension.screenPadding,
        vertical = AppDimension.itemSpaceMedium
    ),
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(colorScheme.errorContainer)
            .clickable(onClick != null) {
                onClick?.invoke()
            }
            .padding(contentPadding)
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = "Error",
            tint = colorScheme.onErrorContainer,
            modifier = Modifier
                .size(50.dp)
        )
        Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
        Text(
            text = errorMessage ?: "Something went wrong.",
            color = colorScheme.onErrorContainer,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}