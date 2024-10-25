package com.nielaclag.openweather.presentation.ui.composable.component.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.nielaclag.openweather.common.constants.Constants
import com.nielaclag.openweather.common.helper.findActivity
import com.nielaclag.openweather.common.helper.log
import kotlin.random.Random

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun rememberGoogleAuthRequestState(): GoogleAuthRequest {
    val context = LocalContext.current.findActivity()!!
    val credentialManager = remember {
        CredentialManager.create(context)
    }

    return rememberMutableGoogleAuthRequestState(
        context = context,
        credentialManager = credentialManager
    )
}

@Composable
internal fun rememberMutableGoogleAuthRequestState(
    context: Context,
    credentialManager: CredentialManager
): MutableGoogleAuthRequestState {
    val mutableGoogleAuthRequest = remember {
        MutableGoogleAuthRequestState(
            context = context,
            credentialManager = credentialManager
        )
    }
    return mutableGoogleAuthRequest
}

interface GoogleAuthRequest {

    suspend fun signIn(filterByAuthorizedAccounts: Boolean = false, onSuccess: (googleIdTokenCredential: GoogleIdTokenCredential, googleIdToken: String, authorized: Boolean) -> Unit, onFailure: (message: String) -> Unit = {})
    suspend fun signUp(onSuccess: (googleIdTokenCredential: GoogleIdTokenCredential, googleIdToken: String, authorized: Boolean) -> Unit, onFailure: (message: String) -> Unit = {})
    suspend fun signOut()

}

@Stable
public class MutableGoogleAuthRequestState(
    private val context: Context,
    private val credentialManager: CredentialManager
) : GoogleAuthRequest {

    private var nonce: String = ""

    override suspend fun signIn(
        filterByAuthorizedAccounts: Boolean,
        onSuccess: (googleIdTokenCredential: GoogleIdTokenCredential, googleIdToken: String, authorized: Boolean) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        signOut()
        beginSignInRequest(
            filterByAuthorizedAccounts = filterByAuthorizedAccounts,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun signUp(
        onSuccess: (googleIdTokenCredential: GoogleIdTokenCredential, googleIdToken: String, authorized: Boolean) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        nonce = Random(System.nanoTime()).nextLong().toString()
        signOut()
        beginSignInRequest(
            filterByAuthorizedAccounts = false,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    override suspend fun signOut() {
        try {
            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )
        } catch (_: Exception) {
        }
    }

    private suspend fun beginSignInRequest(filterByAuthorizedAccounts: Boolean, onSuccess: (googleIdTokenCredential: GoogleIdTokenCredential, googleIdToken: String, authorized: Boolean) -> Unit, onFailure: (message: String) -> Unit = {}) {
        nonce = Random(System.nanoTime()).nextLong().toString()

        val googleIdOption = GetGoogleIdOption
            .Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .setAutoSelectEnabled(false)
            .setServerClientId(Constants.GOOGLE_OAUTH_SERVER_CLIENT_ID)
//            .setNonce(nonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest
            .Builder()
            .addCredentialOption(googleIdOption)
            .build()
        try {
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )
            handleSignIn(
                result = result,
                filterByAuthorizedAccounts = filterByAuthorizedAccounts,
                onSuccess = onSuccess,
                onFailure = {}
            )
        } catch (e: GetCredentialException) {
            log(
                tag = "GoogleAuthClient",
                message = "GetCredentialException ${ if (filterByAuthorizedAccounts) "Resigning In" else "SignIn Error [Get Credential Exception]" }",
                throwable = e
            )
            if (e is GetCredentialCancellationException) {
                onFailure("Google Authorization Cancelled.")
            } else {
                if (filterByAuthorizedAccounts) {
                    beginSignInRequest(
                        filterByAuthorizedAccounts = false,
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )
                } else {
                    onFailure("Something went wrong.")
                }
            }
        } catch (e: GoogleIdTokenParsingException) {
            log(
                tag = "GoogleAuthClient",
                message = "GoogleIdTokenParsingException ${ if (filterByAuthorizedAccounts) "Resigning In" else "SignIn Error [Get Credential Exception]" }",
                throwable = e
            )
            onFailure("Something went wrong.")
        } catch (e: Exception) {
            log(
                tag = "GoogleAuthClient",
                message = "SignIn Error",
                throwable = e
            )
            onFailure("Something went wrong.")
        }
    }

    private fun handleSignIn(result: GetCredentialResponse, filterByAuthorizedAccounts: Boolean, onSuccess: (googleIdTokenCredential: GoogleIdTokenCredential, googleIdToken: String, authorized: Boolean) -> Unit, onFailure: (message: String) -> Unit) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse on your server to
                // validate and authenticate
                val responseJson = credential.authenticationResponseJson
                log(tag = "GoogleAuthClient", message = "RESULT PublicKeyCredential: $responseJson")
            }

            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
                log(tag = "GoogleAuthClient", message = "RESULT PasswordCredential: username: $username, password: $password")
            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        log(tag = "GoogleAuthClient", message = "google id token response\n" +
                                "username: $googleIdTokenCredential\n" +
                                "id: ${ googleIdTokenCredential.id }\n" +
                                "idToken: ${ googleIdTokenCredential.idToken }\n" +
                                "idToken: ${ googleIdTokenCredential.displayName }\n" +
                                "credential: $credential")
                        onSuccess(
                            googleIdTokenCredential,
                            googleIdTokenCredential.idToken,
                            filterByAuthorizedAccounts
                        )
                    } catch (e: GoogleIdTokenParsingException) {
                        log(tag = "GoogleAuthClient", message = "Received an invalid google id token response", throwable = e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    log(tag = "GoogleAuthClient", message = "CustomCredential Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                log(tag = "GoogleAuthClient", message = "Unexpected type of credential")
                onFailure("Something went wrong.")
            }
        }
    }

}