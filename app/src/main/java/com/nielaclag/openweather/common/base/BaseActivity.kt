package com.nielaclag.openweather.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen

/**
 * Created by Niel on 10/21/2024.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installAppSplashScreen(savedInstanceState)
        onCreateActivity(savedInstanceState, splashScreen)
    }

    protected abstract fun onCreateActivity(savedInstanceState: Bundle?, splashScreen: SplashScreen?)

    open fun installAppSplashScreen(savedInstanceState: Bundle?): SplashScreen? {
        return null
    }

}