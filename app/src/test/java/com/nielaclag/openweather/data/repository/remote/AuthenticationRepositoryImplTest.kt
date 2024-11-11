package com.nielaclag.openweather.data.repository.remote

import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.common.helper.toJson
import com.nielaclag.openweather.common.util.CustomError
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.data.mapper.toDto
import com.nielaclag.openweather.data.model.moshiadapter.JsonObjectAdapter
import com.nielaclag.openweather.data.remote.dto.UserAuthenticationDto
import com.nielaclag.openweather.domain.model.UserAuthentication
import com.nielaclag.openweather.domain.repository.remote.AuthenticationRepository
import com.nielaclag.openweather.domain.util.FirebaseAuthHandler
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
 * Created by Niel on 11/9/2024.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var firebaseAuthHandler: FirebaseAuthHandler
    private lateinit var moshi: Moshi
    private lateinit var authenticationRepository: AuthenticationRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockKAnnotations.init(this)

        moshi = Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .add(JsonObjectAdapter())
            .build()
        authenticationRepository = AuthenticationRepositoryImpl(firebaseAuthHandler, moshi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // signIn

    @Test
    fun signIn_should_return_localUser_when_repository_call_is_successful() = runTest {
        val email = "user@email.com"
        val password = "password"

        val mockResponse = UserAuthentication(
            name = "userName",
            email = email,
            image = "https://user_profile_image"
        )
        val apiResponse = Response.success(mockResponse.toDto())
        val expectedResponse = DataResponse.Success<UserAuthenticationDto, CustomError>(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            message = apiResponse.message()
        )

        coEvery {
            firebaseAuthHandler.signIn(email = email, password = password)
        }.returns(apiResponse)

        val result = authenticationRepository.signIn(
            email = email,
            password = password
        )

        coVerify {
            firebaseAuthHandler.signIn(
                email = email,
                password = password
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Success::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
        assertThat(result.message).isEqualTo(expectedResponse.message)
        assertThat(result.data).isEqualTo(expectedResponse.data)
        assertThat(result.error).isEqualTo(expectedResponse.error)
    }

    @Test
    fun signIn_should_return_error_when_repository_call_is_unsuccessful() = runTest {
        val email = "user@email.com"
        val password = "password"

        val apiError = CustomError(
            message = "Sign in unsuccessful.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val apiResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )
        val expectedResponse = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = apiError,
            message = apiError.message
        )

        coEvery {
            firebaseAuthHandler.signIn(email = email, password = password)
        }.returns(apiResponse)

        val result = authenticationRepository.signIn(
            email = email,
            password = password
        )

        coVerify {
            firebaseAuthHandler.signIn(
                email = email,
                password = password
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
        assertThat(result.message).isEqualTo(expectedResponse.message)
        assertThat(result.data).isEqualTo(expectedResponse.data)
        assertThat(result.error).isEqualTo(expectedResponse.error)
    }

    // signUp

    @Test
    fun signUp_should_return_localUser_when_repository_call_is_successful() = runTest {
        val email = "user@email.com"
        val password = "password"

        val mockResponse = UserAuthentication(
            name = "userName",
            email = email,
            image = "https://user_profile_image"
        )
        val apiResponse = Response.success(mockResponse.toDto())
        val expectedResponse = DataResponse.Success<UserAuthenticationDto, CustomError>(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            message = apiResponse.message()
        )

        coEvery {
            firebaseAuthHandler.signUp(email = email, password = password)
        }.returns(apiResponse)

        val result = authenticationRepository.signUp(
            email = email,
            password = password
        )

        coVerify {
            firebaseAuthHandler.signUp(
                email = email,
                password = password
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Success::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
        assertThat(result.message).isEqualTo(expectedResponse.message)
        assertThat(result.data).isEqualTo(expectedResponse.data)
        assertThat(result.error).isEqualTo(expectedResponse.error)
    }

    @Test
    fun signup_should_return_error_when_repository_call_is_unsuccessful() = runTest {
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
        val expectedResponse = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = apiError,
            message = apiError.message
        )

        coEvery {
            firebaseAuthHandler.signUp(email = email, password = password)
        }.returns(apiResponse)

        val result = authenticationRepository.signUp(
            email = email,
            password = password
        )

        coVerify {
            firebaseAuthHandler.signUp(
                email = email,
                password = password
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
        assertThat(result.message).isEqualTo(expectedResponse.message)
        assertThat(result.data).isEqualTo(expectedResponse.data)
        assertThat(result.error).isEqualTo(expectedResponse.error)
    }

    // signOut

    @Test
    fun signOut_should_return_localUser_when_repository_call_is_successful() = runTest {
        val apiResponse = Response.success(null)

        val expectedResponse = DataResponse.Success<Nothing, CustomError>(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            message = apiResponse.message()
        )

        coEvery {
            firebaseAuthHandler.signOut()
        }.returns(apiResponse)

        val result = authenticationRepository.signOut()

        coVerify {
            firebaseAuthHandler.signOut()
        }
        assertThat(result).isInstanceOf(DataResponse.Success::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
        assertThat(result.message).isEqualTo(expectedResponse.message)
        assertThat(result.error).isEqualTo(expectedResponse.error)
    }

    @Test
    fun signOut_should_return_error_when_repository_call_is_unsuccessful() = runTest {
        val apiError = CustomError(
            message = "Sign out unsuccessful.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val apiResponse = Response.error<Nothing>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )
        val expectedResponse = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = apiError,
            message = apiError.message
        )

        coEvery {
            firebaseAuthHandler.signOut()
        }.returns(apiResponse)

        val result = authenticationRepository.signOut()

        coVerify {
            firebaseAuthHandler.signOut()
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
        assertThat(result.message).isEqualTo(expectedResponse.message)
        assertThat(result.error).isEqualTo(expectedResponse.error)
    }

    // getCurrentUser

    @Test
    fun getCurrentUser_should_return_localUser_when_repository_call_is_successful() = runTest {
        val mockResponse = UserAuthentication(
            name = "userName",
            email = "user@email.com",
            image = "https://user_profile_image"
        )
        val apiResponse = Response.success(mockResponse.toDto())

        val expectedResponse = DataResponse.Success<UserAuthenticationDto, CustomError>(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            message = apiResponse.message()
        )

        coEvery {
            firebaseAuthHandler.getCurrentUser()
        }.returns(apiResponse)

        val result = authenticationRepository.getCurrentUser()

        coVerify {
            firebaseAuthHandler.getCurrentUser()
        }
        assertThat(result).isInstanceOf(DataResponse.Success::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
        assertThat(result.message).isEqualTo(expectedResponse.message)
        assertThat(result.data).isEqualTo(expectedResponse.data)
        assertThat(result.error).isEqualTo(expectedResponse.error)
    }

    @Test
    fun getCurrentUser_should_return_null_when_repository_call_is_successful_with_no_active_log_in() = runTest {
        val apiResponse = Response.success<UserAuthenticationDto>(null)

        val expectedResponse = DataResponse.Success<UserAuthenticationDto, CustomError>(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            message = apiResponse.message()
        )

        coEvery {
            firebaseAuthHandler.getCurrentUser()
        }.returns(apiResponse)

        val result = authenticationRepository.getCurrentUser()

        coVerify {
            firebaseAuthHandler.getCurrentUser()
        }
        assertThat(result).isInstanceOf(DataResponse.Success::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
        assertThat(result.message).isEqualTo(expectedResponse.message)
        assertThat(result.data).isEqualTo(expectedResponse.data)
        assertThat(result.error).isEqualTo(expectedResponse.error)
    }

    @Test
    fun getCurrentUser_should_return_error_when_repository_call_is_unsuccessful() = runTest {
        val apiError = CustomError(
            message = "Get current user unsuccessful.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val apiResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )
        val expectedResponse = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = apiError,
            message = apiError.message
        )

        coEvery {
            firebaseAuthHandler.getCurrentUser()
        }.returns(apiResponse)

        val result = authenticationRepository.getCurrentUser()

        coVerify {
            firebaseAuthHandler.getCurrentUser()
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
        assertThat(result.message).isEqualTo(expectedResponse.message)
        assertThat(result.data).isEqualTo(expectedResponse.data)
        assertThat(result.error).isEqualTo(expectedResponse.error)
    }

}