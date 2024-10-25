package com.nielaclag.openweather.common.constants

/**
 * Created by Niel on 10/21/2024.
 */
object UtilConstants {

    const val DEFAULT_REQUEST_TIMEOUT = 35L

    const val EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"

}