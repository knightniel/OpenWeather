package com.nielaclag.openweather.domain.util

import com.nielaclag.openweather.data.remote.dto.UserAuthenticationDto
import retrofit2.Response

/**
 * Created by Niel on 11/8/2024.
 */
interface FirebaseAuthHandler {

    suspend fun signIn(
        email: String,
        password: String
    ): Response<UserAuthenticationDto>

    suspend fun signUp(
        email: String,
        password: String
    ): Response<UserAuthenticationDto>

    suspend fun signOut(): Response<Nothing>

    suspend fun getCurrentUser(): Response<UserAuthenticationDto>

}