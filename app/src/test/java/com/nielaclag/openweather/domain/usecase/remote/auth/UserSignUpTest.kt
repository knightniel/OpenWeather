package com.nielaclag.openweather.domain.usecase.remote.auth

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.common.helper.toJson
import com.nielaclag.openweather.common.util.CustomError
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.data.mapper.toDto
import com.nielaclag.openweather.data.mapper.toLocalUser
import com.nielaclag.openweather.data.model.moshiadapter.JsonObjectAdapter
import com.nielaclag.openweather.data.remote.dto.UserAuthenticationDto
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.model.UserAuthentication
import com.nielaclag.openweather.domain.model.type.AuthenticationType
import com.nielaclag.openweather.domain.repository.remote.AuthenticationRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by Niel on 11/8/2024.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UserSignUpTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var userSignUp: UserSignUp
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        userSignUp = UserSignUp(authenticationRepository)
        moshi = Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .add(JsonObjectAdapter())
            .build()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun invoke_should_return_localUser_when_repository_call_is_successful() = runTest {
        val email = "user@email.com"
        val password = "password"

        val mockResponse = UserAuthentication(
            name = "userName",
            email = email,
            image = "https://user_profile_image"
        )
        val repositoryResponse = DataResponse.Success<UserAuthenticationDto, CustomError>(
            data = mockResponse.toDto(),
            statusCode = HttpURLConnection.HTTP_OK,
            message = "OK"
        )
        val expectedResponse = Resource.Success(
            code = repositoryResponse.statusCode,
            data = repositoryResponse.data?.toDomain()?.toLocalUser(AuthenticationType.EMAIL),
            message = repositoryResponse.message
        )

        coEvery {
            authenticationRepository.signUp(email = email, password = password)
        }.returns(repositoryResponse)

        userSignUp.invoke(
            email = email,
            password = password
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Success::class.java)
            assertThat(resource.code).isEqualTo(expectedResponse.code)
            assertThat(resource.data).isEqualTo(expectedResponse.data)
            assertThat(resource.message).isEqualTo(expectedResponse.message)
            awaitComplete()
        }
        coVerify {
            authenticationRepository.signUp(
                email = email,
                password = password
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful() = runTest {
        val email = "user@email.com"
        val password = "password"

        val apiError = CustomError(
            message = "Sign up unsuccessful.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val apiResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )
        val repositoryResponse = DataResponse.Error<UserAuthenticationDto, CustomError>(
            data = null,
            statusCode = apiResponse.code(),
            error = apiError,
            message = apiResponse.message()
        )
        val expectedResponse = Resource.Error<LocalUser>(
            code = repositoryResponse.statusCode,
            message = repositoryResponse.message ?: "An unexpected error occurred."
        )

        coEvery {
            authenticationRepository.signUp(email = email, password = password)
        }.returns(repositoryResponse)

        userSignUp.invoke(
            email = email,
            password = password
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Error::class.java)
            assertThat(resource.code).isEqualTo(expectedResponse.code)
            assertThat(resource.data).isEqualTo(expectedResponse.data)
            assertThat(resource.message).isEqualTo(expectedResponse.message)
            awaitComplete()
        }
        coVerify {
            authenticationRepository.signUp(
                email = email,
                password = password
            )
        }
    }

}