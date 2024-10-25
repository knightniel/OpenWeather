package com.nielaclag.openweather.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Created by Niel on 10/21/2024.
 */
private val LightColorScheme = lightColorScheme(
    primary = AppColor.primaryLight,
    onPrimary = AppColor.onPrimaryLight,
    primaryContainer = AppColor.primaryContainerLight,
    onPrimaryContainer = AppColor.onPrimaryContainerLight,
    secondary = AppColor.secondaryLight,
    onSecondary = AppColor.onSecondaryLight,
    secondaryContainer = AppColor.secondaryContainerLight,
    onSecondaryContainer = AppColor.onSecondaryContainerLight,
    tertiary = AppColor.tertiaryLight,
    onTertiary = AppColor.onTertiaryLight,
    tertiaryContainer = AppColor.tertiaryContainerLight,
    onTertiaryContainer = AppColor.onTertiaryContainerLight,
    error = AppColor.errorLight,
    onError = AppColor.onErrorLight,
    errorContainer = AppColor.errorContainerLight,
    onErrorContainer = AppColor.onErrorContainerLight,
    background = AppColor.backgroundLight,
    onBackground = AppColor.onBackgroundLight,
    surface = AppColor.surfaceLight,
    onSurface = AppColor.onSurfaceLight,
    surfaceVariant = AppColor.surfaceVariantLight,
    onSurfaceVariant = AppColor.onSurfaceVariantLight,
    outline = AppColor.outlineLight,
    outlineVariant = AppColor.outlineVariantLight,
    scrim = AppColor.scrimLight,
    inverseSurface = AppColor.inverseSurfaceLight,
    inverseOnSurface = AppColor.inverseOnSurfaceLight,
    inversePrimary = AppColor.inversePrimaryLight,
    surfaceDim = AppColor.surfaceDimLight,
    surfaceBright = AppColor.surfaceBrightLight,
    surfaceContainerLowest = AppColor.surfaceContainerLowestLight,
    surfaceContainerLow = AppColor.surfaceContainerLowLight,
    surfaceContainer = AppColor.surfaceContainerLight,
    surfaceContainerHigh = AppColor.surfaceContainerHighLight,
    surfaceContainerHighest = AppColor.surfaceContainerHighestLight
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColor.primaryDark,
    onPrimary = AppColor.onPrimaryDark,
    primaryContainer = AppColor.primaryContainerDark,
    onPrimaryContainer = AppColor.onPrimaryContainerDark,
    secondary = AppColor.secondaryDark,
    onSecondary = AppColor.onSecondaryDark,
    secondaryContainer = AppColor.secondaryContainerDark,
    onSecondaryContainer = AppColor.onSecondaryContainerDark,
    tertiary = AppColor.tertiaryDark,
    onTertiary = AppColor.onTertiaryDark,
    tertiaryContainer = AppColor.tertiaryContainerDark,
    onTertiaryContainer = AppColor.onTertiaryContainerDark,
    error = AppColor.errorDark,
    onError = AppColor.onErrorDark,
    errorContainer = AppColor.errorContainerDark,
    onErrorContainer = AppColor.onErrorContainerDark,
    background = AppColor.backgroundDark,
    onBackground = AppColor.onBackgroundDark,
    surface = AppColor.surfaceDark,
    onSurface = AppColor.onSurfaceDark,
    surfaceVariant = AppColor.surfaceVariantDark,
    onSurfaceVariant = AppColor.onSurfaceVariantDark,
    outline = AppColor.outlineDark,
    outlineVariant = AppColor.outlineVariantDark,
    scrim = AppColor.scrimDark,
    inverseSurface = AppColor.inverseSurfaceDark,
    inverseOnSurface = AppColor.inverseOnSurfaceDark,
    inversePrimary = AppColor.inversePrimaryDark,
    surfaceDim = AppColor.surfaceDimDark,
    surfaceBright = AppColor.surfaceBrightDark,
    surfaceContainerLowest = AppColor.surfaceContainerLowestDark,
    surfaceContainerLow = AppColor.surfaceContainerLowDark,
    surfaceContainer = AppColor.surfaceContainerDark,
    surfaceContainerHigh = AppColor.surfaceContainerHighDark,
    surfaceContainerHighest = AppColor.surfaceContainerHighestDark
)

@Composable
private fun colorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true
): ColorScheme {
    return when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}

@Composable
fun OpenWeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme(
            darkTheme = darkTheme,
            dynamicColor = dynamicColor
        ),
        typography = AppTypography,
        content = content
    )
}