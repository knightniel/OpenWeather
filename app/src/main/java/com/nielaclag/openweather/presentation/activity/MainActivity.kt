package com.nielaclag.openweather.presentation.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nielaclag.openweather.common.base.BaseActivity
import com.nielaclag.openweather.presentation.theme.OpenWeatherTheme
import com.nielaclag.openweather.presentation.ui.composable.component.core.LockScreenOrientation
import com.nielaclag.openweather.presentation.ui.composable.screen.ParentScreen
import com.nielaclag.openweather.presentation.viewmodel.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val databaseViewModel: DatabaseViewModel by viewModels()

    override fun onCreateActivity(savedInstanceState: Bundle?, splashScreen: SplashScreen?) {
        enableEdgeToEdge()
        setContent {
            OpenWeatherTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                LockScreenOrientation(
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                )
                ParentScreen()
            }
        }
    }

    override fun installAppSplashScreen(savedInstanceState: Bundle?): SplashScreen {
        return installSplashScreen()
            .apply {
                setKeepOnScreenCondition {
                    databaseViewModel.showSplash.value
                }
            }
    }

}