package com.nielaclag.openweather.presentation.ui.composable.component.dialog

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.nielaclag.openweather.presentation.theme.AppDimension

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun CustomAlertDialog(
    title: String? = null,
    message: String,
    cancelable: Boolean = false,
    onDismissRequest: () -> Unit,
    option1: String = "OK",
    option2: String? = null,
    action1: () -> Unit = { onDismissRequest() },
    action2: () -> Unit = {}
) {
    CustomAlertDialog(
        title = title?.let { AnnotatedString(title) },
        message = AnnotatedString(message),
        cancelable = cancelable,
        onDismissRequest = onDismissRequest,
        option1 = option1,
        option2 = option2,
        action1 = action1,
        action2 = action2
    )
}

@Composable
fun CustomAlertDialog(
    title: String? = null,
    message: AnnotatedString,
    cancelable: Boolean = false,
    onDismissRequest: () -> Unit,
    option1: String = "OK",
    option2: String? = null,
    action1: () -> Unit = { onDismissRequest() },
    action2: () -> Unit = {}
) {
    CustomAlertDialog(
        title = title?.let { AnnotatedString(it) },
        message = message,
        cancelable = cancelable,
        onDismissRequest = onDismissRequest,
        option1 = option1,
        option2 = option2,
        action1 = action1,
        action2 = action2
    )
}

@Composable
fun CustomAlertDialog(
    title: AnnotatedString?,
    message: String,
    cancelable: Boolean = false,
    onDismissRequest: () -> Unit,
    option1: String = "OK",
    option2: String? = null,
    action1: () -> Unit = { onDismissRequest() },
    action2: () -> Unit = {}
) {
    CustomAlertDialog(
        title = title,
        message = AnnotatedString(message),
        cancelable = cancelable,
        onDismissRequest = onDismissRequest,
        option1 = option1,
        option2 = option2,
        action1 = action1,
        action2 = action2
    )
}

@Composable
fun CustomAlertDialog(
    title: AnnotatedString?,
    message: AnnotatedString,
    cancelable: Boolean = false,
    onDismissRequest: () -> Unit,
    option1: String = "OK",
    option2: String? = null,
    action1: () -> Unit = { onDismissRequest() },
    action2: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        title = {
            title?.let {
                Text(
                    text = it,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Text(
                text = message,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        },
        confirmButton = {
            Button(
                onClick = action1,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(
                    horizontal = AppDimension.itemSpaceLarge,
                    vertical = AppDimension.itemSpaceSmall
                ),
                modifier = Modifier
                    .height(AppDimension.inputAndButtonHeight)
            ) {
                Text(
                    text = option1,
                    fontSize = 16.sp,
                    color = colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = option2?.let {
            {
                Button(
                    onClick = action2,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = CircleShape,
                    contentPadding = PaddingValues(
                        horizontal = AppDimension.itemSpaceLarge,
                        vertical = AppDimension.itemSpaceSmall
                    ),
                    modifier = Modifier
                        .height(AppDimension.inputAndButtonHeight)
                ) {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable,
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier
            .padding(
                horizontal = AppDimension.screenPadding
            )
    )
}