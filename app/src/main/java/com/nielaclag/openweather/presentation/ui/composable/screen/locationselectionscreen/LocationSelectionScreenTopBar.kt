package com.nielaclag.openweather.presentation.ui.composable.screen.locationselectionscreen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.nielaclag.openweather.R
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.ui.composable.component.core.rememberMultipleEventsCutter

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun LocationSelectionScreenTopBar(
    initialLocation: String?,
    localUser: LocalUser?,
    gettingDeviceLocation: Boolean,
    onLocationTextChanged: (newLocation: String) -> Unit,
    onUseCurrentLocation: () -> Unit,
    showUserOptions: () -> Unit
) {
    val backHandler = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
    }
    val multipleEventsCutter = rememberMultipleEventsCutter()

    var locationString by remember {
        mutableStateOf(initialLocation ?: "")
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
            .padding(
                top = WindowInsets
                    .systemBars
                    .asPaddingValues()
                    .calculateTopPadding() + AppDimension.screenPadding
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppDimension.screenPadding - AppDimension.clickablePaddingMedium,
                    end = AppDimension.screenPadding
                )
//                .defaultMinSize(
//                    minHeight = TextFieldDefaults.MinHeight
//                )
                .height(IntrinsicSize.Max)
        ) {
            if (initialLocation != null) {
                IconButton(
                    onClick = {
                        backHandler?.onBackPressed()
                    },
                    modifier = Modifier
//                        .size(TextFieldDefaults.MinHeight)
//                        .addBorder()
                        .clip(CircleShape)
                ) {
                    Icon(
                        painter = rememberAsyncImagePainter(R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = colorScheme.primary,
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(AppDimension.clickablePaddingMedium))
            }

            TextField(
                value = locationString,
                onValueChange = { value ->
                    locationString = value
                    onLocationTextChanged(value)
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
                        contentDescription = "Location"
                    )
                },
//                trailingIcon = {
//                    IconButton(
//                        onClick = showUserOptions,
//                        modifier = Modifier
////                        .size(TextFieldDefaults.MinHeight)
//                            .clip(CircleShape)
//                    ) {
//                        Icon(
//                            painter = rememberAsyncImagePainter(R.drawable.ic_user),
//                            contentDescription = "Back",
//                            tint = colorScheme.onPrimary
//                        )
//                    }
//                },
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
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = colorScheme.primary,
                    focusedContainerColor = colorScheme.primary,
                    focusedLeadingIconColor = colorScheme.onPrimary,
                    unfocusedLeadingIconColor = colorScheme.onPrimary,
                    focusedTrailingIconColor = colorScheme.onPrimary,
                    unfocusedTrailingIconColor = colorScheme.onPrimary,
                    unfocusedTextColor = colorScheme.onPrimary,
                    focusedTextColor = colorScheme.onPrimary,
                    focusedPlaceholderColor = colorScheme.onPrimary.copy(alpha = .5f),
                    unfocusedPlaceholderColor = colorScheme.onPrimary.copy(alpha = .5f)
                ),
                shape = CircleShape,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
            )

//            Spacer(modifier = Modifier.width(AppDimension.itemSpaceMedium))
//            IconButton(
//                onClick = onOptionClicked,
//                modifier = Modifier
//                    .clip(CircleShape)
//            ) {
//                Icon(
//                    painter = rememberAsyncImagePainter(R.drawable.ic_user),
//                    contentDescription = "Back",
//                    tint = colorScheme.primary
//                )
//            }
        }

        Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
//        Spacer(modifier = Modifier.height(AppDimension.screenPadding))

        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppDimension.screenPadding
                )
        ) {
            Button(
                onClick = {
                    multipleEventsCutter {
                        locationString = ""
                        onUseCurrentLocation()
                    }
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.tertiary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = AppDimension.containerElevation
                ),
                contentPadding = PaddingValues(
                    horizontal = AppDimension.clickablePaddingMedium,
                    vertical = AppDimension.itemSpaceSmall
                )
            ) {
                Crossfade(
                    targetState = gettingDeviceLocation,
                    label = "Use device location",
                    modifier = Modifier
                        .size(24.dp)
                ) { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            color = colorScheme.onTertiary,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    } else {
                        Icon(
                            painter = rememberAsyncImagePainter(R.drawable.ic_my_location),
                            contentDescription = "Location",
                            tint = colorScheme.onTertiary,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(AppDimension.itemSpaceMedium))
                Text(
                    text = "Use device location",
                    color = colorScheme.onTertiary,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}