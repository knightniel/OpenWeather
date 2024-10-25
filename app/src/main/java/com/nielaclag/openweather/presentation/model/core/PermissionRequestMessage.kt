package com.nielaclag.openweather.presentation.model.core

/**
 * Created by Niel on 10/21/2024.
 */
data class PermissionRequestMessage(
    val permission: String,
    val title: String,
    val message: String,
    val type: PermissionMessageType
) {
    sealed class PermissionMessageType {
        data object PermanentlyDeclined : PermissionMessageType()
        data object Rationale : PermissionMessageType()
    }
}