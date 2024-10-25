package com.nielaclag.openweather.domain.usecase.remote.auth

import javax.inject.Inject

/**
 * Created by Niel on 10/24/2024.
 */
data class AuthUseCases @Inject constructor(
    val userSignIn: UserSignIn,
    val userSignUp: UserSignUp,
    val userSignOut: UserSignOut
)