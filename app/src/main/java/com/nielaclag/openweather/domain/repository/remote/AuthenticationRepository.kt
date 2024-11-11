package com.nielaclag.openweather.domain.repository.remote

import com.nielaclag.openweather.common.util.CustomError
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.data.remote.dto.UserAuthenticationDto

/**
 * Created by Niel on 11/7/2024.
 */
interface AuthenticationRepository {

    suspend fun signIn(
        email: String,
        password: String
    ): DataResponse<UserAuthenticationDto, CustomError>

    suspend fun signUp(
        email: String,
        password: String
    ): DataResponse<UserAuthenticationDto, CustomError>

    suspend fun signOut(): DataResponse<Nothing, CustomError>

    suspend fun getCurrentUser(): DataResponse<UserAuthenticationDto, CustomError>

}