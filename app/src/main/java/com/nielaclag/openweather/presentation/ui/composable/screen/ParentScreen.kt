package com.nielaclag.openweather.presentation.ui.composable.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.presentation.model.page.BasePage
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.ui.composable.component.dialog.LoadingDialog
import com.nielaclag.openweather.presentation.ui.composable.component.util.rememberGoogleAuthRequestState
import com.nielaclag.openweather.presentation.ui.composable.screen.basescreen.LocationSelectionScreen
import com.nielaclag.openweather.presentation.ui.composable.screen.basescreen.MainScreen
import com.nielaclag.openweather.presentation.ui.composable.screen.basescreen.SignInAndUpScreen
import com.nielaclag.openweather.presentation.ui.composable.screen.basescreen.UserInfo
import com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen.MainScreenTopBar
import com.nielaclag.openweather.presentation.viewmodel.DatabaseViewModel
import com.nielaclag.openweather.presentation.viewmodel.ParentScreenViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by Niel on 10/21/2024.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ParentScreen(
    databaseViewModel: DatabaseViewModel = hiltViewModel(),
    parentScreenViewModel: ParentScreenViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var userFetched by remember {
        mutableStateOf(false)
    }
    val localUser = databaseViewModel.localUser
    val locationInfo = databaseViewModel.locationInfo
    val signOutSharedFlow = parentScreenViewModel.signOut
    val signOut by parentScreenViewModel.signOut.collectAsState(initial = null)

    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current

    var showUserOptions by remember { mutableStateOf(false) }

    val googleAuthRequestState = rememberGoogleAuthRequestState()

    LaunchedEffect(Unit) {
        launch {
            localUser.collect { localUser ->
                if (localUser == null && userFetched) {
                    navController.navigate(BasePage.UserAuthentication) {
                        popUpTo<BasePage.Main> {
                            inclusive = true
                        }
                    }
//                } else if (localUser != null && userFetched) {
//                    if (locationInfo.value == null) {
//                        navController.navigate(BasePage.LocationSelection)
//                    }
                }
                userFetched = true
            }
        }
        launch {
            signOutSharedFlow.collectLatest { result ->
                if (result != null) {
                    if (result is Resource.Error) {
                        scope.launch {
                            snackbarHostState.showSnackbar(result.message ?: "Something went wrong.")
                        }
                    }
                }
            }
        }
    }

    if (signOut is Resource.Loading) {
        LoadingDialog(
            message = "Signing out. Please wait...",
            onDismissRequest = {
            }
        )
    } else if (showUserOptions) {
        UserInfo(
            onSignOut = {
                scope.launch {
                    showUserOptions = false
                    googleAuthRequestState.signOut()
                    parentScreenViewModel.signOut()
                }
            },
            onDismissRequest = {
                showUserOptions = false
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(horizontal = AppDimension.screenPadding)
                    .imePadding()
                    .navigationBarsPadding()
            )
        },
        containerColor = colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = remember {
                parentScreenViewModel.getScreen()
            },
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
            composable<BasePage.UserAuthentication> {
                SignInAndUpScreen(
                    googleAuthRequest = googleAuthRequestState,
                    onSignIn = {
                        navController.navigate(BasePage.Main) {
                            launchSingleTop = true
                            popUpTo<BasePage.UserAuthentication> {
                                inclusive = true
                                saveState = false
                            }
                        }
                        navController.navigate(BasePage.LocationSelection) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<BasePage.Main> {
                MainScreen(
                    onSelectLocation = {
                        navController.navigate(BasePage.LocationSelection) {
                            launchSingleTop = true
                        }
                    },
                    showUserOptions = {
                        showUserOptions = true
                    }
                )
            }
            composable<BasePage.LocationSelection> {
                LocationSelectionScreen(
                    onLocationSelected = { locationInfo ->
                        parentScreenViewModel.setUserLocation(locationInfo)
                        navController.popBackStack<BasePage.LocationSelection>(
                            inclusive = true
                        )
                    },
                    showUserOptions = {
                        showUserOptions = true
                    }
                )
            }
        }
    }
}