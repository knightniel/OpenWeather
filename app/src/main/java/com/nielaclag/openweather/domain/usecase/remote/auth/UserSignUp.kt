package com.nielaclag.openweather.domain.usecase.remote.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.model.type.AuthenticationType
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by Niel on 10/24/2024.
 */
class UserSignUp @Inject constructor(
    private val moshi: Moshi
) {
    operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<LocalUser>> = callbackFlow {
        send(Resource.Loading())
        try {
            val auth = Firebase.auth
            auth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            val localUser = LocalUser(
                                id = 0,
                                email = user.email,
                                name = user.displayName,
                                image = user.photoUrl?.toString(),
                                authenticationType = AuthenticationType.EMAIL
                            )
                            trySend(Resource.Success(data = localUser))
                        } else {
                            trySend(Resource.Error(data = null, message = "An unexpected error occurred."))
                        }
                    } else {
                        trySend(Resource.Error(message = task.exception?.message ?: "An unexpected error occurred."))
                    }
                }
        } catch (e: Exception) {
            send(Resource.Error(message = "An unexpected error occurred."))
        } finally {
            awaitClose {
            }
        }
    }
}