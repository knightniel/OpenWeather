package com.nielaclag.openweather.common.helper

import androidx.annotation.IdRes
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import com.nielaclag.openweather.common.constants.Constants
import kotlinx.serialization.Serializable

/**
 * Created by Niel on 10/21/2024.
 */

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

fun String.toColor(): Color? {
    return try {
        Color(
            if (this.startsWith("#")) {
                android.graphics.Color.parseColor(
                    "#${ removePrefix("#") }"
                )
            } else {
                android.graphics.Color.parseColor(this)
            }
        )
    } catch (e: Exception) {
        null
    }
}

fun buildClickableAnnotatedString(
    vararg annotatedStrings: Pair<AnnotatedString, (() -> Unit)?>
): AnnotatedString {
    return buildAnnotatedString {
        annotatedStrings.forEach { pair ->
            if (pair.second == null) {
                append(pair.first)
            } else {
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = pair.first.text,
                        linkInteractionListener = {
                            pair.second?.invoke()
                        }
                    )
                ) {
                    append(pair.first)
                }
            }
        }
    }
}

fun NavHostController.getBackStackEntryOrNull(@IdRes destinationId: Int): NavBackStackEntry? {
    return try {
        getBackStackEntry(destinationId)
    } catch (e: Exception) {
        null
    }
}

fun NavHostController.getBackStackEntryOrNull(route: String): NavBackStackEntry? {
    return try {
        getBackStackEntry(route)
    } catch (e: Exception) {
        null
    }
}

fun NavHostController.getBackStackEntryOrNull(route: Any): NavBackStackEntry? {
    return try {
        getBackStackEntry(route)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T: Any> NavHostController.getBackStackEntryOrNull(): NavBackStackEntry? {
    return try {
        getBackStackEntry<T>()
    } catch (e: Exception) {
        null
    }
}

fun NavDestination.isDestinationInstanceOf(classType: @Serializable Any): Boolean {
    return try {
        hasRoute(classType::class)
    } catch (_: Exception) {
        false
    }
}

fun NavBackStackEntry.isDestinationInstanceOf(classType: @Serializable Any): Boolean {
    return destination.isDestinationInstanceOf(classType)
}

fun Modifier.addBorder(color: Color = Color.Red) = this.then(
    if (Constants.LOGGING_ENABLED) {
        Modifier.border(Dp.Hairline, color)
    } else {
        Modifier
    }
)