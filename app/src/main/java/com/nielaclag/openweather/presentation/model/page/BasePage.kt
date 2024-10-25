package com.nielaclag.openweather.presentation.model.page

import kotlinx.serialization.Serializable

/**
 * Created by Niel on 10/21/2024.
 */
@Serializable
open class BasePage {

    @Serializable
    data object UserAuthentication : BasePage()

    @Serializable
    data object Main : BasePage()

    @Serializable
    data object LocationSelection : BasePage()

}