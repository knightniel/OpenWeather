package com.nielaclag.openweather.data.util

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.android.gms.tasks.Task
import com.google.common.truth.Truth.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.nielaclag.openweather.common.helper.toJson
import com.nielaclag.openweather.common.util.CustomError
import com.nielaclag.openweather.data.remote.dto.UserAuthenticationDto
import com.nielaclag.openweather.domain.util.FirebaseAuthHandler
import com.squareup.moshi.Moshi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.MockKAnnotations
import io.mockk.verify
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.Response
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * Created by Niel on 11/10/2024.
 */
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class FirebaseAuthHandlerImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var moshi: Moshi
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var firebaseAuthHandler: FirebaseAuthHandler

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    // signIn

    @Test
    fun signIn_should_return_UserAuthenticationDto_when_repository_call_is_successful() = runBlocking {
        val userEmail = "user@email.com"
        val userPassword = "password"
        val userDisplayName = "userName"
        val userPhotoUrl = Uri.parse("userPhotoUrl")

        val firebaseUser = mockk<FirebaseUser>(relaxed = false).apply {
            every { email }.returns(userEmail)
            every { displayName }.returns(userDisplayName)
            every { photoUrl }.returns(userPhotoUrl)
        }
        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(firebaseUser)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(true)
            every { isComplete }.returns(true)
            every { exception }.returns(null)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val expectedResponse = Response.success<UserAuthenticationDto>(
            UserAuthenticationDto(
                email = firebaseUser.email,
                name = firebaseUser.displayName,
                image = firebaseUser.photoUrl?.toString()
            )
        )

        val response = firebaseAuthHandler.signIn(
            email = userEmail,
            password = userPassword
        )

        verify { 
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun signIn_should_return_error_when_repository_call_is_unsuccessful() = runBlocking {
        val userEmail = "user@email.com"
        val userPassword = "password"

        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(null)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(false)
            every { isComplete }.returns(true)
            every { exception }.returns(null)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val apiError = CustomError(
            message = "Sign in unsuccessful.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val expectedResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )

        val response = firebaseAuthHandler.signIn(
            email = userEmail,
            password = userPassword
        )

        verify {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.message()).isEqualTo(expectedResponse.message())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun signIn_should_return_error_when_repository_call_is_unsuccessful_with_FirebaseException() = runTest {
        val userEmail = "wrongUser@email.com"
        val userPassword = "wrongPassword"

        val repositoryError = FirebaseNetworkException("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")

        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(null)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(false)
            every { isComplete }.returns(true)
            every { exception }.returns(repositoryError)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val apiError = CustomError(
            message = repositoryError.message ?: "Couldn't reach server. Check your internet connection.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val expectedResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )

        val response = firebaseAuthHandler.signIn(
            email = userEmail,
            password = userPassword
        )

        verify {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.message()).isEqualTo(expectedResponse.message())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun signIn_should_return_error_when_repository_call_is_unsuccessful_with_FirebaseAuthException() = runTest {
        val userEmail = "wrongUser@email.com"
        val userPassword = "wrongPassword"

        val repositoryError = FirebaseAuthException("errorCode", "The supplied auth credential is incorrect, malformed or has expired.")

        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(null)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(false)
            every { isComplete }.returns(true)
            every { exception }.returns(repositoryError)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val apiError = CustomError(
            message = repositoryError.message ?: "An authentication error occurred.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val expectedResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )

        val response = firebaseAuthHandler.signIn(
            email = userEmail,
            password = userPassword
        )

        verify {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.message()).isEqualTo(expectedResponse.message())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun signIn_should_return_error_when_repository_call_is_unsuccessful_with_Exception() = runTest {
        val userEmail = "wrongUser@email.com"
        val userPassword = "wrongPassword"

        val repositoryError = Exception("Other exception.")

        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(null)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(false)
            every { isComplete }.returns(true)
            every { exception }.returns(repositoryError)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val apiError = CustomError(
            message = repositoryError.message ?: "An unexpected error occurred.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val expectedResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )

        val response = firebaseAuthHandler.signIn(
            email = userEmail,
            password = userPassword
        )

        verify {
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.message()).isEqualTo(expectedResponse.message())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    // signUp

    @Test
    fun signUp_should_return_UserAuthenticationDto_when_repository_call_is_successful() = runBlocking {
        val userEmail = "user@email.com"
        val userPassword = "password"
        val userDisplayName = "userName"
        val userPhotoUrl = Uri.parse("userPhotoUrl")

        val firebaseUser = mockk<FirebaseUser>(relaxed = false).apply {
            every { email }.returns(userEmail)
            every { displayName }.returns(userDisplayName)
            every { photoUrl }.returns(userPhotoUrl)
        }
        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(firebaseUser)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(true)
            every { isComplete }.returns(true)
            every { exception }.returns(null)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val expectedResponse = Response.success<UserAuthenticationDto>(
            UserAuthenticationDto(
                email = firebaseUser.email,
                name = firebaseUser.displayName,
                image = firebaseUser.photoUrl?.toString()
            )
        )

        val response = firebaseAuthHandler.signUp(
            email = userEmail,
            password = userPassword
        )

        verify {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun signUp_should_return_error_when_repository_call_is_unsuccessful() = runBlocking {
        val userEmail = "user@email.com"
        val userPassword = "password"

        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(null)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(false)
            every { isComplete }.returns(true)
            every { exception }.returns(null)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val apiError = CustomError(
            message = "Sign up unsuccessful.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val expectedResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )

        val response = firebaseAuthHandler.signUp(
            email = userEmail,
            password = userPassword
        )

        verify {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.message()).isEqualTo(expectedResponse.message())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun signUp_should_return_error_when_repository_call_is_unsuccessful_with_FirebaseException() = runTest {
        val userEmail = "wrongUser@email.com"
        val userPassword = "wrongPassword"

        val repositoryError = FirebaseNetworkException("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")

        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(null)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(false)
            every { isComplete }.returns(true)
            every { exception }.returns(repositoryError)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val apiError = CustomError(
            message = repositoryError.message ?: "Couldn't reach server. Check your internet connection.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val expectedResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )

        val response = firebaseAuthHandler.signUp(
            email = userEmail,
            password = userPassword
        )

        verify {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.message()).isEqualTo(expectedResponse.message())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun signUp_should_return_error_when_repository_call_is_unsuccessful_with_FirebaseAuthException() = runTest {
        val userEmail = "wrongUser@email.com"
        val userPassword = "wrongPassword"

        val repositoryError = FirebaseAuthException("errorCode", "The supplied auth credential is incorrect, malformed or has expired.")

        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(null)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(false)
            every { isComplete }.returns(true)
            every { exception }.returns(repositoryError)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val apiError = CustomError(
            message = repositoryError.message ?: "An authentication error occurred.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val expectedResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )

        val response = firebaseAuthHandler.signUp(
            email = userEmail,
            password = userPassword
        )

        verify {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.message()).isEqualTo(expectedResponse.message())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun signUp_should_return_error_when_repository_call_is_unsuccessful_with_Exception() = runTest {
        val userEmail = "wrongUser@email.com"
        val userPassword = "wrongPassword"

        val repositoryError = Exception("Other exception.")

        val authResult = mockk<AuthResult>(relaxed = false).apply {
            every { user }.returns(null)
        }
        val task = mockk<Task<AuthResult>>(relaxed = false).apply {
            every { isSuccessful }.returns(false)
            every { isComplete }.returns(true)
            every { exception }.returns(repositoryError)
            every { isCanceled }.returns(false)
            every { result }.returns(authResult)
        }
        every {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }.returns(task)

        val apiError = CustomError(
            message = repositoryError.message ?: "An unexpected error occurred.",
            code = HttpURLConnection.HTTP_INTERNAL_ERROR
        )
        val expectedResponse = Response.error<UserAuthenticationDto>(
            apiError.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
            moshi.toJson(apiError).toResponseBody("application/json".toMediaType())
        )

        val response = firebaseAuthHandler.signUp(
            email = userEmail,
            password = userPassword
        )

        verify {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
        }
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.message()).isEqualTo(expectedResponse.message())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    // signOut

    @Test
    fun signIn_should_return_success_when_repository_call_is_successful() = runBlocking {
        val expectedResponse = Response.success<Nothing>(null)

        val response = firebaseAuthHandler.signOut()

        verify {
            firebaseAuth.signOut()
        }
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
    }

    // getCurrentUser

    @Test
    fun getCurrentUser_should_return_UserAuthenticationDto_when_repository_call_is_successful_with_active_log_in() = runBlocking {
        val userEmail = "user@email.com"
        val userPassword = "password"
        val userDisplayName = "userName"
        val userPhotoUrl = Uri.parse("userPhotoUrl")

        val firebaseUser = mockk<FirebaseUser>(relaxed = false).apply {
            every { email }.returns(userEmail)
            every { displayName }.returns(userDisplayName)
            every { photoUrl }.returns(userPhotoUrl)
        }

        every {
            firebaseAuth.currentUser
        }.returns(firebaseUser)

        val expectedResponse = Response.success<UserAuthenticationDto>(
            UserAuthenticationDto(
                email = firebaseUser.email,
                name = firebaseUser.displayName,
                image = firebaseUser.photoUrl?.toString()
            )
        )

        val response = firebaseAuthHandler.getCurrentUser()

        verify {
            firebaseAuth.currentUser
        }
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getCurrentUser_should_return_null_when_repository_call_is_successful_with_no_active_log_in() = runBlocking {
        val firebaseUser: FirebaseUser? = null

        every {
            firebaseAuth.currentUser
        }.returns(firebaseUser)

        val expectedResponse = Response.success<UserAuthenticationDto>(null)

        val response = firebaseAuthHandler.getCurrentUser()

        verify {
            firebaseAuth.currentUser
        }
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

}