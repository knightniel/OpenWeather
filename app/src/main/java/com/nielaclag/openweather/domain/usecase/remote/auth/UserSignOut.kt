package com.nielaclag.openweather.domain.usecase.remote.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nielaclag.openweather.common.helper.log
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.domain.model.LocalUser
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by Niel on 10/24/2024.
 */
class UserSignOut @Inject constructor(
    private val moshi: Moshi
) {
    operator fun invoke(): Flow<Resource<Any>> = callbackFlow {
        send(Resource.Loading())
        log("UserSignOut: loading")
        try {
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                Firebase.auth.signOut()
                log("UserSignOut: current user signed out")
            } else {
                log("UserSignOut: no signed in user")
            }
            send(Resource.Success(null))
        } catch (e: Exception) {
            log("UserSignOut: error", throwable = e)
            send(Resource.Error(message = e.message ?: "An unexpected error occurred."))
        } finally {
            log("UserSignOut: awaitClose")
            awaitClose {
            }
        }
    }
}