package com.nielaclag.openweather.startup

import android.content.Context
import androidx.startup.Initializer
import com.nielaclag.openweather.common.constants.Constants
import timber.log.Timber

/**
 * Created by Niel on 10/21/2024.
 */
class TimberInitializer: Initializer<Unit> {

    private val isDebug: Boolean = Constants.LOGGING_ENABLED

    override fun create(context: Context) {
        if (isDebug) {
            Timber.plant(
                Timber.DebugTree()
            )
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}