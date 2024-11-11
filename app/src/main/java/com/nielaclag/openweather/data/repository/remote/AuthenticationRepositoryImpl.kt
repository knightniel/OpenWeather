package com.nielaclag.openweather.data.repository.remote

import com.nielaclag.openweather.common.helper.toJsonObject
import com.nielaclag.openweather.common.util.CustomError
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.data.remote.dto.UserAuthenticationDto
import com.nielaclag.openweather.domain.repository.remote.AuthenticationRepository
import com.nielaclag.openweather.domain.util.FirebaseAuthHandler
import com.squareup.moshi.Moshi

/**
 * Created by Niel on 11/7/2024.
 */
class AuthenticationRepositoryImpl(
    private val firebaseAuthHandler: FirebaseAuthHandler,
    private val moshi: Moshi
) : AuthenticationRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): DataResponse<UserAuthenticationDto, CustomError> {
        val response = firebaseAuthHandler.signIn(email, password)
        return if (response.isSuccessful) {
            DataResponse.Success(
                data = response.body(),
                statusCode = response.code(),
                message = response.message()
            )
        } else {
            val responseError = response.errorBody()?.toJsonObject<CustomError>(moshi)
            DataResponse.Error(
                data = response.body(),
                statusCode = response.code(),
                error = responseError,
                message = responseError?.message
            )
        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): DataResponse<UserAuthenticationDto, CustomError> {
        val response = firebaseAuthHandler.signUp(email, password)
        return if (response.isSuccessful) {
            DataResponse.Success(
                data = response.body(),
                statusCode = response.code(),
                message = response.message()
            )
        } else {
            val responseError = response.errorBody()?.toJsonObject<CustomError>(moshi)
            DataResponse.Error(
                data = response.body(),
                statusCode = response.code(),
                error = responseError,
                message = responseError?.message
            )
        }
    }

    override suspend fun signOut(): DataResponse<Nothing, CustomError> {
        val response = firebaseAuthHandler.signOut()
        return if (response.isSuccessful) {
            DataResponse.Success(
                data = null,
                statusCode = response.code(),
                message = response.message()
            )
        } else {
            val responseError = response.errorBody()?.toJsonObject<CustomError>(moshi)
            DataResponse.Error(
                statusCode = response.code(),
                error = responseError,
                message = responseError?.message
            )
        }
    }

    override suspend fun getCurrentUser(): DataResponse<UserAuthenticationDto, CustomError> {
        val response = firebaseAuthHandler.getCurrentUser()
        return if (response.isSuccessful) {
            DataResponse.Success(
                data = response.body(),
                statusCode = response.code(),
                message = response.message()
            )
        } else {
            val responseError = response.errorBody()?.toJsonObject<CustomError>(moshi)
            DataResponse.Error(
                data = response.body(),
                statusCode = response.code(),
                error = responseError,
                message = responseError?.message
            )
        }
    }

}