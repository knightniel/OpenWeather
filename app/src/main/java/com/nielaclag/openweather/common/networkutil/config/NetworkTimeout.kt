package com.nielaclag.openweather.common.networkutil.config

import java.util.concurrent.TimeUnit

/**
 * Created by Niel on 11/5/2024.
 */
class NetworkTimeout(
    val readTimeout: Timeout,
    val connectTimeout: Timeout,
    val writeTimeout: Timeout
) {

    class Timeout(
        val duration: Long,
        val unit: TimeUnit
    )

}