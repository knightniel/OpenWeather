package com.nielaclag.openweather.presentation.ui.composable.screen.userauthenticationscreen.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.nielaclag.openweather.R
import com.nielaclag.openweather.common.helper.log
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.domain.model.type.AuthenticationType
import com.nielaclag.openweather.domain.model.type.TimePeriod
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.ui.composable.component.core.rememberMultipleEventsCutter
import com.nielaclag.openweather.presentation.ui.composable.component.util.GoogleAuthRequest
import com.nielaclag.openweather.presentation.viewmodel.UserAuthenticationViewModel
import kotlinx.coroutines.launch

/**
 * Created by Niel on 10/24/2024.
 */
@Composable
fun SignUpPage(
    googleAuthRequest: GoogleAuthRequest,
    contentPadding: PaddingValues,
    userAuthenticationViewModel: UserAuthenticationViewModel = hiltViewModel(),
    onMessage: (message: String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val multipleEventsCutter = rememberMultipleEventsCutter()
    val timePeriod by userAuthenticationViewModel.timePeriod

    val signIn by userAuthenticationViewModel.signIn.collectAsState(initial = null)

    var signingInWithGoogle by remember {
        mutableStateOf(false)
    }
    val isSigningIn by remember(signIn, signingInWithGoogle) {
        derivedStateOf {
            signIn is Resource.Loading || signingInWithGoogle
        }
    }
    val authenticationType by remember(signIn) {
        derivedStateOf {
            signIn?.extra?.let {
                if (it is AuthenticationType) it else null
            }
        }
    }

    var email by remember {
        mutableStateOf("")
    }
    var password1 by remember {
        mutableStateOf("")
    }
    var password2 by remember {
        mutableStateOf("")
    }
    var password1Visible by remember {
        mutableStateOf(false)
    }
    var password2Visible by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight - (contentPadding.calculateTopPadding() + contentPadding.calculateBottomPadding()))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    AsyncImage(
                        model = R.drawable.ic_logo,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(.4f)
                            .aspectRatio(1f)
                    )

                    Spacer(modifier = Modifier.height(AppDimension.screenPadding))
                    Text(
                        text = "Good ${
                            when (timePeriod) {
                                TimePeriod.EarlyMorning, TimePeriod.Morning -> "Morning"
                                TimePeriod.Noon -> "Noon"
                                TimePeriod.Afternoon -> "Afternoon"
                                TimePeriod.Evening, TimePeriod.Midnight -> "Evening"
                            }
                        }!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(AppDimension.screenPadding))
                    Text(
                        text = "Please sign up to continue.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorScheme.onSecondaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(AppDimension.screenPadding))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { value ->
                            email = value
                        },
                        placeholder = {
                            Text(
                                text = "Email",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        singleLine = true,
                        readOnly = isSigningIn,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .5f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .5f)
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                    )

                    Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
                    OutlinedTextField(
                        value = password1,
                        onValueChange = { value ->
                            password1 = value
                        },
                        placeholder = {
                            Text(
                                text = "Password",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        singleLine = true,
                        readOnly = isSigningIn,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        visualTransformation = if (password1Visible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    password1Visible = !password1Visible
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .clip(CircleShape)
                            ) {
                                Icon(
                                    painter = rememberAsyncImagePainter(if (password1Visible) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off),
                                    contentDescription = "Password visibility"
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .5f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .5f)
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                    )

                    Spacer(modifier = Modifier.height(AppDimension.itemSpaceMedium))
                    OutlinedTextField(
                        value = password2,
                        onValueChange = { value ->
                            password2 = value
                        },
                        placeholder = {
                            Text(
                                text = "Confirm password",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        singleLine = true,
                        readOnly = isSigningIn,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        visualTransformation = if (password2Visible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    password2Visible = !password2Visible
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .clip(CircleShape)
                            ) {
                                Icon(
                                    painter = rememberAsyncImagePainter(if (password2Visible) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off),
                                    contentDescription = "Password visibility"
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .5f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .5f)
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                    )

                    Spacer(modifier = Modifier.height(AppDimension.screenPadding))
                    Button(
                        onClick = {
                            multipleEventsCutter {
                                when {
                                    email.isEmpty() -> {
                                        onMessage("Please enter your email.")
                                    }
                                    password1.isEmpty() -> {
                                        onMessage("Please enter your password.")
                                    }
                                    password1 != password2 -> {
                                        onMessage("Passwords do not match.")
                                    }
                                    else -> {
                                        focusManager.clearFocus()
                                        password1Visible = false
                                        password2Visible = false
                                        userAuthenticationViewModel.signUpWithEmail(
                                            email = email,
                                            password = password1
                                        )
                                    }
                                }
                            }
                        },
                        enabled = !isSigningIn,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
//                        containerColor = colorScheme.background
                            containerColor = colorScheme.tertiary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = AppDimension.containerElevation
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .height(AppDimension.inputAndButtonHeight)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(AppDimension.itemSpaceXSmall)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(
                                        ratio = 1f,
                                        matchHeightConstraintsFirst = true
                                    )
                            )
                            Text(
                                text = "Sign up",
                                color = colorScheme.onTertiary,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(
                                        ratio = 1f,
                                        matchHeightConstraintsFirst = true
                                    )
                            ) {
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = isSigningIn && authenticationType == AuthenticationType.EMAIL
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimension.screenPadding))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                    ) {
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                        )
                        Spacer(modifier = Modifier.width(AppDimension.itemSpaceSmall))
                        Text(
                            text = "or",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.width(AppDimension.itemSpaceSmall))
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(AppDimension.screenPadding))
                    Button(
                        onClick = {
                            multipleEventsCutter {
                                scope.launch {
                                    signingInWithGoogle = true
                                    googleAuthRequest.signUp(
                                        onSuccess = { googleIdTokenCredential, googleIdToken, authorized ->
                                            userAuthenticationViewModel.signInUserWithGoogle(googleIdTokenCredential)
                                            signingInWithGoogle = false
                                            log("googleSignInData: ${googleIdTokenCredential.profilePictureUri}")
                                        },
                                        onFailure = { message ->
                                            signingInWithGoogle = false
                                            onMessage(message)
                                        }
                                    )
                                }
                            }
                        },
                        enabled = !isSigningIn,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.background
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = AppDimension.containerElevation
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .height(AppDimension.inputAndButtonHeight)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(AppDimension.itemSpaceXSmall)
                        ) {
                            AsyncImage(
                                model = R.drawable.ic_google,
                                contentDescription = "Google",
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(
                                        ratio = 1f,
                                        matchHeightConstraintsFirst = true
                                    )
                            )
                            Spacer(modifier = Modifier.width(AppDimension.itemSpaceSmall))
                            Text(
                                text = "Sign up with Google",
                                color = colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .weight(1f)
                            )
                            Spacer(modifier = Modifier.width(AppDimension.itemSpaceSmall))
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(
                                        ratio = 1f,
                                        matchHeightConstraintsFirst = true
                                    )
                            ) {
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = isSigningIn && authenticationType == AuthenticationType.GOOGLE || signingInWithGoogle
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}