package com.nielaclag.openweather.presentation.ui.composable.component.core

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
fun rememberMultiplePermissionRequestState(
    permissions: List<String>,
    rationaleTitle: String = "Permission/s",
    permanentlyDeclinedTitle: String = "Permission/s",
    rationaleMessage: String = "We need permission/s to function properly.\nPlease grant the permissions.",
    permanentlyDeclinedMessage: String = "Permission/s has been declined.\nPlease grant the permission/s in the system settings.",
    onPermissionResult: (List<Pair<String, Boolean>>) -> Unit
): MultiplePermissionRequest {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var permissionAskedAt: Long? by remember {
        mutableStateOf(null)
    }
    var permissionRequestMessage: PermissionRequestMessage? by remember {
        mutableStateOf(null)
    }
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = permissions,
        onPermissionsResult = { result ->
            log(
                message = "onPermissionResult: $result",
                tag = "permissionUtil"
            )
            permissionAskedAt?.let { askedAt ->
                val notGranted = result.toList().firstOrNull { !it.second }
                if (notGranted != null) {
                    val dateDiff = Date(askedAt).dateDiffInMilliSeconds(Date())!!
                    if (dateDiff <= 300) {
                        log(
                            message = "onPermissionResult: not granted dateDiff: $dateDiff, permanentlyDeclined",
                            tag = "permissionUtil"
                        )
                        permissionRequestMessage = PermissionRequestMessage(
                            permission = notGranted.first,
                            title = permanentlyDeclinedTitle,
                            message = permanentlyDeclinedMessage,
                            type = PermissionRequestMessage.PermissionMessageType.PermanentlyDeclined
                        )
                    } else {
                        log(
                            message = "onPermissionResult: not granted dateDiff: $dateDiff",
                            tag = "permissionUtil"
                        )
                        onPermissionResult(result.toList())
                    }
                } else {
                    onPermissionResult(result.toList())
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
                    multiplePermissionsState.launchMultiplePermissionRequest()
                }
                permissionRequestMessage = null
            },
            action2 = {
                permissionRequestMessage = null
                onPermissionResult(
                    multiplePermissionsState.permissions.map { permission ->
                        Pair(permission.permission, permission.status.isGranted)
                    }
                )
            }
        )
    }
    return rememberMutableMultiplePermissionRequestState(
        multiplePermissionsState = multiplePermissionsState,
        onGranted = {
            log(
                message = "onGranted",
                tag = "permissionUtil"
            )
            onPermissionResult(
                multiplePermissionsState.permissions.map { permission ->
                    Pair(permission.permission, permission.status.isGranted)
                }
            )
        },
        onShowRationale = { permissions_ ->
            log(
                message = "onShowRationale",
                tag = "permissionUtil"
            )
            permissionRequestMessage = PermissionRequestMessage(
                permission = permissions_.firstOrNull()?.permission ?: "Permission",
                title = rationaleTitle,
                message = rationaleMessage,
                type = PermissionRequestMessage.PermissionMessageType.Rationale
            )
        },
        onLaunchPermissions = {
            log(
                message = "onLaunchPermissions",
                tag = "permissionUtil"
            )
            permissionAskedAt = System.currentTimeMillis()
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
interface MultiplePermissionRequest {
    val permissions: List<PermissionState>
    val allPermissionsGranted: Boolean
    fun launchMultiplePermissionRequest()
    fun launchMultiplePermissionRequestNormally()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun rememberMutableMultiplePermissionRequestState(
    multiplePermissionsState: MultiplePermissionsState,
    onGranted: () -> Unit,
    onShowRationale: (permissions: List<PermissionState>) -> Unit,
    onLaunchPermissions: () -> Unit
): MutableMultiplePermissionRequestState {
    val permissionRequester = remember(multiplePermissionsState, onGranted, onShowRationale, onLaunchPermissions) {
        MutableMultiplePermissionRequestState(
            multiplePermissionsState = multiplePermissionsState,
            onGranted = onGranted,
            onShowRationale = onShowRationale,
            onLaunchPermissions = onLaunchPermissions
        )
    }
    return permissionRequester
}

@OptIn(ExperimentalPermissionsApi::class)
@Stable
private class MutableMultiplePermissionRequestState(
    private val multiplePermissionsState: MultiplePermissionsState,
    private val onGranted: () -> Unit,
    private val onShowRationale: (permissions: List<PermissionState>) -> Unit,
    private val onLaunchPermissions: () -> Unit
) : MultiplePermissionRequest {

    override val permissions = multiplePermissionsState.permissions
    override val allPermissionsGranted = multiplePermissionsState.allPermissionsGranted

    override fun launchMultiplePermissionRequest() {
        if (allPermissionsGranted) {
            onGranted()
        } else if (multiplePermissionsState.shouldShowRationale) {
            onShowRationale(
                multiplePermissionsState.permissions.filter { it.status.shouldShowRationale }
            )
        } else {
            onLaunchPermissions()
        }
    }

    override fun launchMultiplePermissionRequestNormally() {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

}