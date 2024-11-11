package com.nielaclag.openweather.domain.usecase.remote.auth

import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.data.mapper.toLocalUser
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.model.type.AuthenticationType
import com.nielaclag.openweather.domain.repository.remote.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by Niel on 10/24/2024.
 */
class UserSignIn @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<LocalUser>> = callbackFlow {
        send(Resource.Loading())
        val dataResponse = repository.signIn(
            email = email,
            password = password
        )
        if (dataResponse is DataResponse.Success) {
            send(
                Resource.Success(
                    code = dataResponse.statusCode,
                    data = dataResponse.data?.toDomain()?.toLocalUser(AuthenticationType.EMAIL),
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