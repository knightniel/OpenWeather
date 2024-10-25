package com.nielaclag.openweather.presentation.ui.composable.component.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Created by Niel on 10/21/2024.
 */
interface MultipleEventsCutter {
    operator fun invoke(event: () -> Unit)

    companion object {
        fun get(timeOut: Long = 300L): MultipleEventsCutter = MultipleEventsCutterImpl(timeOut = timeOut)
    }
}

private class MultipleEventsCutterImpl(val timeOut: Long) : MultipleEventsCutter {

    private var lastEventTimeMs: Long = 0

    override operator fun invoke(event: () -> Unit) {
        if (System.currentTimeMillis() - lastEventTimeMs >= timeOut) {
            event.invoke()
            lastEventTimeMs = System.currentTimeMillis()
        }
    }

}

@Composable
fun rememberMultipleEventsCutter(timeOut: Long = 300L): MultipleEventsCutter {
    return remember(timeOut) { MultipleEventsCutter.get(timeOut = timeOut) }
}