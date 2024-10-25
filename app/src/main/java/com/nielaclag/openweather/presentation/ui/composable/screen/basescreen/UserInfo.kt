package com.nielaclag.openweather.presentation.ui.composable.screen.basescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.nielaclag.openweather.R
import com.nielaclag.openweather.presentation.theme.AppDimension
import com.nielaclag.openweather.presentation.theme.Shapes
import com.nielaclag.openweather.presentation.viewmodel.DatabaseViewModel

/**
 * Created by Niel on 10/24/2024.
 */
@Composable
fun UserInfo(
    databaseViewModel: DatabaseViewModel = hiltViewModel(),
    onSignOut: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val localUser by databaseViewModel.localUser.collectAsState()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.medium)
                .background(colorScheme.background)
                .padding(AppDimension.screenLargePadding)
        ) {
            AsyncImage(
                model = localUser?.image,
                placeholder = rememberAsyncImagePainter(R.drawable.ic_user),
                error = rememberAsyncImagePainter(R.drawable.ic_user),
                contentDescription = "User image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(AppDimension.screenPadding))

            Text(
                text = localUser?.name ?: localUser?.email ?: "",
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(AppDimension.screenLargePadding))
            Button(
                onClick = onSignOut,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.tertiary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = AppDimension.containerElevation
                )
            ) {
                Text(
                    text = "Sign out",
                    color = colorScheme.onTertiary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}