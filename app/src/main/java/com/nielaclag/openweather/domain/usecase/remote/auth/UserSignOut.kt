package com.nielaclag.openweather.domain.usecase.remote.auth

import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.domain.repository.remote.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by Niel on 10/24/2024.
 */
class UserSignOut @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(): Flow<Resource<Nothing>> = callbackFlow {
        send(Resource.Loading())
        val dataResponse = repository.signOut()
        if (dataResponse is DataResponse.Success) {
            send(
                Resource.Success(
                    code = dataResponse.statusCode,
                    data = null,
                    message = dataResponse.message
                )
            )
        } else {
            send(
                Resource.Error(
                    code = dataResponse.statusCode,
                    message = dataResponse.message ?: "An unexpected error occurred."
                )
            )
        }
        close()
    }
}