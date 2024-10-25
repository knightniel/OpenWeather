package com.nielaclag.openweather.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * Created by Niel on 10/21/2024.
 */
val Shapes = androidx.compose.material3.Shapes(
    extraSmall = RoundedCornerShape(AppDimension.cornerRadiusExtraSmall),
    small = RoundedCornerShape(AppDimension.cornerRadiusSmall),
    medium = RoundedCornerShape(AppDimension.cornerRadiusMedium),
    large = RoundedCornerShape(AppDimension.cornerRadiusLarge),
    extraLarge = RoundedCornerShape(AppDimension.cornerRadiusExtraLarge)
)