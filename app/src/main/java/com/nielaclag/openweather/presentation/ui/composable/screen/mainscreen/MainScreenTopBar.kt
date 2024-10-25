package com.nielaclag.openweather.presentation.ui.composable.screen.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.nielaclag.openweather.R
import com.nielaclag.openweather.common.helper.isDestinationInstanceOf
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.presentation.model.page.MainPage
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.ui.composable.component.core.CustomScrollableTabRow
import com.nielaclag.openweather.presentation.ui.composable.component.core.tabRowIndicatorOffset

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun MainScreenTopBar(
    navController: NavController,
    localUser: LocalUser?,
    location: String,
    onLocationChangeRequest: () -> Unit,
    showUserOptions: () -> Unit
) {
    val pages = arrayOf(
        MainPage.CurrentWeather,
        MainPage.WeatherHistory
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val screenPadding = 0.dp
    val tabMargin = AppDimension.screenPadding
    val tabEdgePadding = AppDimension.screenPadding - (tabMargin / 2)
    val minWidth = (screenWidth - (screenPadding * 2) - (tabEdgePadding * 2)) / pages.size
    val currentPageIndex by remember(navBackStackEntry) {
        derivedStateOf {
            pages.indexOfFirst { page ->
                navBackStackEntry?.isDestinationInstanceOf(page) == true
            }.let { if (it == -1) 0 else it }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures {
                }
            }
            .padding(
                top = WindowInsets
                    .systemBars
                    .asPaddingValues()
                    .calculateTopPadding() + AppDimension.screenPadding
            )
    ) {
        TextField(
            value = location,
            onValueChange = {
            },
            placeholder = {
                Text(
                    text = "Enter your location",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(
                    painter = rememberAsyncImagePainter(R.drawable.ic_location),
                    contentDescription = "Location",
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = showUserOptions,
                    modifier = Modifier
                        .clip(CircleShape)
                ) {
                    AsyncImage(
                        model = localUser?.image,
                        placeholder = rememberAsyncImagePainter(R.drawable.ic_user),
                        error = rememberAsyncImagePainter(R.drawable.ic_user),
                        contentDescription = "User image",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = colorScheme.primary,
                focusedContainerColor = colorScheme.primary,
                focusedLeadingIconColor = colorScheme.onPrimary,
                unfocusedLeadingIconColor = colorScheme.onPrimary,
                unfocusedTextColor = colorScheme.onPrimary,
                focusedTextColor = colorScheme.onPrimary,
                focusedPlaceholderColor = colorScheme.onPrimary.copy(alpha = .5f),
                unfocusedPlaceholderColor = colorScheme.onPrimary.copy(alpha = .5f)
            ),
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppDimension.screenPadding
                )
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        onLocationChangeRequest()
                    }
                }
        )

        Spacer(modifier = Modifier.height(AppDimension.itemSpaceLarge))
        CustomScrollableTabRow(
            selectedTabIndex = currentPageIndex,
            minimumTabWidth = minWidth,
            edgePadding = tabEdgePadding,
            tabBaseBackground = {
            },
            backgroundIndicator = { tabPositions ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .tabRowIndicatorOffset(
                            tabPositions[currentPageIndex]
                        )
                        .fillMaxHeight()
                        .padding(
                            horizontal = tabMargin / 2
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(colorScheme.secondary)
                    )
                }
            },
            foregroundIndicator = {
            },
            bottomDivider = {
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val selectedContentColor: Color = LocalContentColor.current
            pages.forEachIndexed { index, mainPage ->
                val active = index == currentPageIndex
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(
                            horizontal = tabMargin / 2
                        )
                        .height(AppDimension.barHeight)
                        .clip(CircleShape)
                        .selectable(
                            selected = active,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(
                                bounded = true,
                                color = selectedContentColor
                            ),
                            role = Role.Tab
                        ) {
                            navController.navigate(mainPage) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                ) {
                    Text(
                        text = when (mainPage) {
                            MainPage.CurrentWeather -> "Current Weather"
                            MainPage.WeatherHistory -> "History"
                            else -> ""
                        },
                        color = if (active) colorScheme.onSecondary else colorScheme.secondary,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}