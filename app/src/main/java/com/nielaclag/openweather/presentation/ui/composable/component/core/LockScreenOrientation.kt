package com.nielaclag.openweather.presentation.ui.composable.component.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.nielaclag.openweather.common.helper.findActivity

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun LockScreenOrientation(orientation: Int?, autoResetToOriginalOrientation: Boolean = true) {
    val context = LocalContext.current
    DisposableEffect(orientation, autoResetToOriginalOrientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        orientation?.let {
            activity.requestedOrientation = it
        }
        onDispose {
            // restore original orientation when view disappears
            if (autoResetToOriginalOrientation) {
                activity.requestedOrientation = originalOrientation
            }
        }
    }
}