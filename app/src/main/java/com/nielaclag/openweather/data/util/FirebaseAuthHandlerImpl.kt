package com.nielaclag.openweather.data.util

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.nielaclag.openweather.common.helper.toJson
import com.nielaclag.openweather.common.util.CustomError
import com.nielaclag.openweather.data.remote.dto.UserAuthenticationDto
import com.nielaclag.openweather.domain.util.FirebaseAuthHandler
import com.squareup.moshi.Moshi
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by Niel on 11/8/2024.
 */
class FirebaseAuthHandlerImpl(
    private val firebaseAuth: FirebaseAuth,
    private val moshi: Moshi
) : FirebaseAuthHandler {

    override suspend fun signIn(
        email: String,
        password: String
    ): Response<UserAuthenticationDto> {
        return try {
            val task = firebaseAuth.signInWithEmailAndPassword(email, password)
            val result = task.await()
            val firebaseUser = result.user
            if (task.isSuccessful && firebaseUser != null) {
                Response.success(
                    UserAuthenticationDto(
                        email = firebaseUser.email,
                        name = firebaseUser.displayName,
                        image = firebaseUser.photoUrl?.toString()
                    )
                )
            } else {
                val error = CustomError(
                    message = "Sign in unsuccessful.",
                    code = HttpURLConnection.HTTP_INTERNAL_ERROR
                )
                Response.error(
                    error.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
                    moshi.toJson(error).toResponseBody("application/json".toMediaType())
                )
            }
        } catch (e: FirebaseException) {
            val error = CustomError(
                message = e.message ?: "Couldn't reach server. Check your internet connection.",
                code = HttpURLConnection.HTTP_INTERNAL_ERROR
            )
            Response.error(
                error.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
                moshi.toJson(error).toResponseBody("application/json".toMediaType())
            )
        } catch (e: FirebaseAuthException) {
            val error = CustomError(
                message = e.message ?: "An authentication error occurred.",
                code = HttpURLConnection.HTTP_INTERNAL_ERROR
            )
            Response.error(
                error.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
                moshi.toJson(error).toResponseBody("application/json".toMediaType())
            )
        } catch (e: Exception) {
            val error = CustomError(
                message = e.message ?: "An unexpected error occurred.",
                code = HttpURLConnection.HTTP_INTERNAL_ERROR
            )
            Response.error(
                error.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
                moshi.toJson(error).toResponseBody("application/json".toMediaType())
            )
        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): Response<UserAuthenticationDto> {
        return try {
            val task = firebaseAuth.createUserWithEmailAndPassword(email, password)
            val result = task.await()
            val firebaseUser = result.user
            if (task.isSuccessful && firebaseUser != null) {
                Response.success(
                    UserAuthenticationDto(
                        email = firebaseUser.email,
                        name = firebaseUser.displayName,
                        image = firebaseUser.photoUrl?.toString()
                    )
                )
            } else {
                val error = CustomError(
                    message = "Sign up unsuccessful.",
                    code = HttpURLConnection.HTTP_INTERNAL_ERROR
                )
                Response.error(
                    error.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
                    moshi.toJson(error).toResponseBody("application/json".toMediaType())
                )
            }
        } catch (e: FirebaseException) {
            val error = CustomError(
                message = e.message ?: "Couldn't reach server. Check your internet connection.",
                code = HttpURLConnection.HTTP_INTERNAL_ERROR
            )
            Response.error(
                error.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
                moshi.toJson(error).toResponseBody("application/json".toMediaType())
            )
        } catch (e: FirebaseAuthException) {
            val error = CustomError(
                message = e.message ?: "An authentication error occurred.",
                code = HttpURLConnection.HTTP_INTERNAL_ERROR
            )
            Response.error(
                error.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
                moshi.toJson(error).toResponseBody("application/json".toMediaType())
            )
        } catch (e: Exception) {
            val error = CustomError(
                message = e.message ?: "An unexpected error occurred.",
                code = HttpURLConnection.HTTP_INTERNAL_ERROR
            )
            Response.error(
                error.code ?: HttpURLConnection.HTTP_INTERNAL_ERROR,
                moshi.toJson(error).toResponseBody("application/json".toMediaType())
            )
        }
    }

    override suspend fun signOut(): Response<Nothing> {
        firebaseAuth.signOut()
        return Response.success(null)
    }

    override suspend fun getCurrentUser(): Response<UserAuthenticationDto> {
        return Response.success(
            firebaseAuth.currentUser?.let { firebaseUser ->
                UserAuthenticationDto(
                    email = firebaseUser.email,
                    name = firebaseUser.displayName,
                    image = firebaseUser.photoUrl?.toString()
                )
            }
        )
    }

}