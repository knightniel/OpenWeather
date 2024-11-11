package com.nielaclag.openweather.common.util

/**
 * Created by Niel on 11/7/2024.
 */
sealed class DataResponse<out DATA, out ERROR>(
    val statusCode: Int?,
    val message: String?,
    val data: DATA?,
    val error: ERROR?
) {

    class Success<DATA, ERROR>(
        data: DATA?,
        statusCode: Int?,
        message: String? = null
    ) : DataResponse<DATA, ERROR>(
        statusCode = statusCode,
        message = message,
        data = data,
        error = null
    )

    class Error<DATA, ERROR>(
        data: DATA? = null,
        error: ERROR?,
        statusCode: Int?,
        message: String? = null
    ) : DataResponse<DATA, ERROR>(
        statusCode = statusCode,
        message = message,
        data = data,
        error = error
    )

}