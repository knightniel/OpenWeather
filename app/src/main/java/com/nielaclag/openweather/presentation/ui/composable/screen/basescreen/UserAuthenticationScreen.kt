package com.nielaclag.openweather.presentation.ui.composable.screen.basescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nielaclag.openweather.common.helper.isDestinationInstanceOf
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.presentation.model.page.SignInAndUpPage
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.ui.composable.component.util.GoogleAuthRequest
import com.nielaclag.openweather.presentation.ui.composable.screen.userauthenticationscreen.UserAuthenticationScreenTopBar
import com.nielaclag.openweather.presentation.ui.composable.screen.userauthenticationscreen.page.SignInPage
import com.nielaclag.openweather.presentation.ui.composable.screen.userauthenticationscreen.page.SignUpPage
import com.nielaclag.openweather.presentation.viewmodel.UserAuthenticationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by Niel on 10/24/2024.
 */
@Composable
fun SignInAndUpScreen(
    googleAuthRequest: GoogleAuthRequest,
    userAuthenticationViewModel: UserAuthenticationViewModel = hiltViewModel(),
    onSignIn: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val colorScheme = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    val signInSharedFlow = userAuthenticationViewModel.signIn
    var isLoading by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val backgroundGradient = remember {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    colors = listOf(
                        colorScheme.secondaryContainer,
                        colorScheme.tertiaryContainer,
                    ),
                    from = Offset(size.width * .3f, 0f),
                    to = Offset(size.width * .6f, size.height),
                    tileMode = TileMode.Clamp
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        launch {
            userAuthenticationViewModel.resetSignIn()
            googleAuthRequest.signOut()
        }
        launch {
            signInSharedFlow.collectLatest {
                if (it != null) {
                    when (it) {
                        is Resource.Loading -> {
                            isLoading = true
                        }
                        is Resource.Success -> {
                            userAuthenticationViewModel.resetSignIn()
                            onSignIn()
                        }
                        is Resource.Error -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(it.message ?: "Something went wrong.")
                            }
                            isLoading = false
                            googleAuthRequest.signOut()
                        }
                    }
                } else {
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(horizontal = AppDimension.screenPadding)
                    .imePadding()
            )
        },
        topBar = {
            UserAuthenticationScreenTopBar(
                withBackNavigation = navBackStackEntry?.isDestinationInstanceOf(SignInAndUpPage.SignIn) == false
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        val contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + AppDimension.screenPadding,
            bottom = AppDimension.screenPadding
        )
        NavHost(
            navController = navController,
            startDestination = SignInAndUpPage.SignIn,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
        ) {
            composable<SignInAndUpPage.SignIn> {
                SignInPage(
                    googleAuthRequest = googleAuthRequest,
                    contentPadding = contentPadding,
                    userAuthenticationViewModel = userAuthenticationViewModel,
                    onSignUp = {
                        navController.navigate(SignInAndUpPage.SignUp) {
                            launchSingleTop = true
                        }
                    },
                    onMessage = { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                )
            }
            composable<SignInAndUpPage.SignUp> {
                SignUpPage(
                    googleAuthRequest = googleAuthRequest,
                    contentPadding = contentPadding,
                    userAuthenticationViewModel = userAuthenticationViewModel,
                    onMessage = { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                )
            }
        }
    }
}