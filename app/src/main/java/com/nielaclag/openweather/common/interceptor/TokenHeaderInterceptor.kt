package com.nielaclag.openweather.common.interceptor

import com.nielaclag.openweather.domain.usecase.dao.localuserdao.LocalUserDaoUseCases
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class TokenHeaderInterceptor @Inject constructor(private val localUserDaoUseCases: LocalUserDaoUseCases) :
    Interceptor {

    /**
     * Interceptor class for setting of the dynamic headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
//        val access_token = authRepository.selectUserOnce()?.access_token
//        val userToken = runBlocking {
//            localUserDaoUseCases.getLocalUser()?.userToken
//        }
//
//        if (userToken != null) {
//            request = request.newBuilder().apply {
//                addHeader("Authorization", "${ userToken.tokenType } ${ userToken.accessToken }")
//            }.build()
//        }
        return chain.proceed(request)
    }

}