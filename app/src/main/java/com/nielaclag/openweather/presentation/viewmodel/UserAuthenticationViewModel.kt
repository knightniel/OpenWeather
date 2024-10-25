package com.nielaclag.openweather.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.model.type.AuthenticationType
import com.nielaclag.openweather.domain.usecase.dao.localuserdao.LocalUserDaoUseCases
import com.nielaclag.openweather.domain.model.type.TimePeriod
import com.nielaclag.openweather.domain.usecase.remote.auth.AuthUseCases
import com.nielaclag.openweather.domain.usecase.util.UtilUseCases
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
@HiltViewModel
class UserAuthenticationViewModel @Inject constructor(
    private val localUserDaoUseCases: LocalUserDaoUseCases,
    private val authUseCases: AuthUseCases,
    private val utilUseCases: UtilUseCases,
    val moshi: Moshi
) : ViewModel() {

    private val _timePeriod = mutableStateOf(TimePeriod.Morning)
    val timePeriod: State<TimePeriod> = _timePeriod

    private val _signIn = MutableSharedFlow<Resource<LocalUser>?>()
    val signIn = _signIn.asSharedFlow()

    init {
        getLocalTime()
    }

    private fun getLocalTime() {
        viewModelScope.launch {
            while (true) {
                _timePeriod.value = utilUseCases.getTimePeriod(
                    timeMillis = System.currentTimeMillis()
                )
                delay(1000 * 15)
            }
        }
    }

    fun signInUserWithGoogle(
        googleIdTokenCredential: GoogleIdTokenCredential
    ) {
        viewModelScope.launch {
            _signIn.emit(Resource.Loading(
                extra = AuthenticationType.GOOGLE
            ))
            val newUser = LocalUser(
                id = 0,
                name = googleIdTokenCredential.displayName,
                email = googleIdTokenCredential.id,
                image = googleIdTokenCredential.profilePictureUri?.toString(),
                authenticationType = AuthenticationType.GOOGLE
            )
            localUserDaoUseCases.setNewLocalUser(newUser)
            _signIn.emit(
                Resource.Success(
                    data = newUser,
                    extra = AuthenticationType.GOOGLE
                )
            )
        }
    }

    fun signInWithEmail(email: String, password: String) {
        authUseCases.userSignIn(
            email = email,
            password = password
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _signIn.emit(
                        Resource.Loading(
                            extra = AuthenticationType.EMAIL
                        )
                    )
                }
                is Resource.Error -> {
                    _signIn.emit(
                        Resource.Error(
                            message = result.message,
                            data = result.data,
                            code = result.code,
                            extra = AuthenticationType.EMAIL
                        )
                    )
                }
                is Resource.Success -> {
                    val newUser = LocalUser(
                        id = 0,
                        name = null,
                        email = email,
                        image = null,
                        authenticationType = AuthenticationType.EMAIL
                    )
                    localUserDaoUseCases.setNewLocalUser(newUser)
                    _signIn.emit(
                        Resource.Success(
                            message = result.message,
                            data = result.data,
                            code = result.code,
                            extra = AuthenticationType.EMAIL
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun signUpWithEmail(email: String, password: String) {
        authUseCases.userSignUp(
            email = email,
            password = password
        ).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _signIn.emit(
                        Resource.Loading(
                            extra = AuthenticationType.EMAIL
                        )
                    )
                }
                is Resource.Error -> {
                    _signIn.emit(
                        Resource.Error(
                            message = result.message,
                            data = result.data,
                            code = result.code,
                            extra = AuthenticationType.EMAIL
                        )
                    )
                }
                is Resource.Success -> {
                    val newUser = LocalUser(
                        id = 0,
                        name = null,
                        email = email,
                        image = null,
                        authenticationType = AuthenticationType.EMAIL
                    )
                    localUserDaoUseCases.setNewLocalUser(newUser)
                    _signIn.emit(
                        Resource.Success(
                            message = result.message,
                            data = result.data,
                            code = result.code,
                            extra = AuthenticationType.EMAIL
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun resetSignIn() {
        viewModelScope.launch {
            _signIn.emit(null)
            authUseCases.userSignOut()
        }
    }

}