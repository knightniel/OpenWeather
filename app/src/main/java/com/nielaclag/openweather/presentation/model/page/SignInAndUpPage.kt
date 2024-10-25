package com.nielaclag.openweather.presentation.model.page

import kotlinx.serialization.Serializable

/**
 * Created by Niel on 10/24/2024.
 */
@Serializable
open class SignInAndUpPage {

    @Serializable
    data object SignIn : SignInAndUpPage()

    @Serializable
    data object SignUp : SignInAndUpPage()

}