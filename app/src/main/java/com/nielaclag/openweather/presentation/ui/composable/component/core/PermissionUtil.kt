package com.nielaclag.openweather.presentation.ui.composable.component.core

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.nielaclag.openweather.common.helper.dateDiffInMilliSeconds
import com.nielaclag.openweather.common.helper.log
import com.nielaclag.openweather.presentation.model.core.PermissionRequestMessage
import com.nielaclag.openweather.presentation.ui.composable.component.dialog.CustomAlertDialog
import java.util.Date

/**
 * Created by Niel on 10/21/2024.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberPermissionRequestState(
    permission: String,
    rationaleTitle: String = "Permission",
    permanentlyDeclinedTitle: String = "Permission",
    rationaleMessage: String = "We need permission to function properly.\nPlease grant the permission.",
    permanentlyDeclinedMessage: String = "Permission has been declined.\nPlease grant the permission in the system settings.",
    onPermissionResult: (granted: Boolean) -> Unit
): PermissionRequest {
    val context = LocalContext.current
    var permissionAskedAt: Long? by remember {
        mutableStateOf(null)
    }
    var permissionRequestMessage: PermissionRequestMessage? by remember {
        mutableStateOf(null)
    }
    val permissionState = rememberPermissionState(
        permission = permission,
        onPermissionResult = { granted ->
            log(
                message = "onPermissionResult: $granted",
                tag = "permissionUtil"
            )
            permissionAskedAt?.let { askedAt ->
                if (!granted) {
//                    val permanentlyDenied = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
                    val dateDiff = Date(askedAt).dateDiffInMilliSeconds(Date())!!
                    if (dateDiff <= 300) {
                        log(
                            message = "onPermissionResult: not granted dateDiff: $dateDiff, permanentlyDeclined",
                            tag = "permissionUtil"
                        )
                        permissionRequestMessage = PermissionRequestMessage(
                            permission = permission,
                            title = permanentlyDeclinedTitle,
                            message = permanentlyDeclinedMessage,
                            type = PermissionRequestMessage.PermissionMessageType.PermanentlyDeclined
                        )
                    } else {
                        log(
                            message = "onPermissionResult: not granted dateDiff: $dateDiff",
                            tag = "permissionUtil"
                        )
                        onPermissionResult(false)
                    }
                } else {
                    onPermissionResult(true)
                }
            }
        }
    )

    permissionRequestMessage?.let {
        CustomAlertDialog(
            title = it.title,
            message = it.message,
            onDismissRequest = {
                permissionRequestMessage = null
            },
            option1 = if (it.type == PermissionRequestMessage.PermissionMessageType.PermanentlyDeclined) "Open Settings" else "Proceed",
            option2 = if (it.type == PermissionRequestMessage.PermissionMessageType.PermanentlyDeclined) "Dismiss" else null,
            action1 = {
                if (it.type == PermissionRequestMessage.PermissionMessageType.PermanentlyDeclined) {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + context.packageName)
                    )
                    context.startActivity(intent)
                } else {
                    permissionAskedAt = System.currentTimeMillis()
                    permissionState.launchPermissionRequest()
                }
                permissionRequestMessage = null
            },
            action2 = {
                permissionRequestMessage = null
                onPermissionResult(false)
            }
        )
    }
    return rememberMutablePermissionRequestState(
        permissionState = permissionState,
        onGranted = {
            log(
                message = "onGranted",
                tag = "permissionUtil"
            )
            onPermissionResult(true)
        },
        onShowRationale = {
            log(
                message = "onShowRationale",
                tag = "permissionUtil"
            )
            permissionRequestMessage = PermissionRequestMessage(
                permission = permission,
                title = rationaleTitle,
                message = rationaleMessage,
                type = PermissionRequestMessage.PermissionMessageType.Rationale
            )
        },
        onLaunchPermission = {
            log(
                message = "onGranted",
                tag = "permissionUtil"
            )
            permissionAskedAt = System.currentTimeMillis()
            permissionState.launchPermissionRequest()
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
interface PermissionRequest {
    val permission: String
    val status: PermissionStatus
    fun launchPermissionRequest()
    fun launchPermissionRequestNormally()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun rememberMutablePermissionRequestState(
    permissionState: PermissionState,
    onGranted: () -> Unit,
    onShowRationale: () -> Unit,
    onLaunchPermission: () -> Unit
): MutablePermissionRequestState {
    val permissionRequester = remember(permissionState) {
        MutablePermissionRequestState(
            permissionState = permissionState,
            onGranted = onGranted,
            onShowRationale = onShowRationale,
            onLaunchPermission = onLaunchPermission
        )
    }
    return permissionRequester
}

@OptIn(ExperimentalPermissionsApi::class)
@Stable
private class MutablePermissionRequestState(
    private val permissionState: PermissionState,
    private val onGranted: () -> Unit,
    private val onShowRationale: () -> Unit,
    private val onLaunchPermission: () -> Unit
) : PermissionRequest {

    override val permission = permissionState.permission
    override val status = permissionState.status

    override fun launchPermissionRequest() {
        if (permissionState.status.isGranted) {
            onGranted()
        } else if (permissionState.status.shouldShowRationale) {
            onShowRationale()
        } else {
            onLaunchPermission()
        }
    }

    override fun launchPermissionRequestNormally() {
        permissionState.launchPermissionRequest()
    }

}