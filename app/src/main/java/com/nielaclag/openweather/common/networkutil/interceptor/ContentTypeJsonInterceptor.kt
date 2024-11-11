package com.nielaclag.openweather.common.networkutil.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class ContentTypeJsonInterceptor : Interceptor {

    /**
     * Interceptor class for setting of the dynamic headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().apply {
            addHeader("Content-Type", "application/json")
            addHeader("Accept", "application/json")
        }.build()
        return chain.proceed(request)
    }

}