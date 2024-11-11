package com.nielaclag.openweather.presentation.ui.composable.screen.userauthenticationscreen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import com.nielaclag.openweather.R
import com.nielaclag.openweather.presentation.theme.AppDimension

/**
 * Created by Niel on 10/24/2024.
 */
@Composable
fun UserAuthenticationScreenTopBar(
    withBackNavigation: Boolean
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = WindowInsets
                    .systemBars
                    .asPaddingValues()
                    .calculateTopPadding() + AppDimension.screenPadding
            )
    ) {
        AnimatedVisibility(
            visible = withBackNavigation,
            label = "With back navigation",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppDimension.screenPadding - AppDimension.clickablePaddingMedium,
                    end = AppDimension.screenPadding
                )
                .height(TextFieldDefaults.MinHeight)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                IconButton(
                    onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                ) {
                    Icon(
                        painter = rememberAsyncImagePainter(R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = colorScheme.tertiary
                    )
                }
            }
        }
    }
}