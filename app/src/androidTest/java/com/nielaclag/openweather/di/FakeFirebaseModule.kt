package com.nielaclag.openweather.di

import com.google.firebase.auth.FirebaseAuth
import com.nielaclag.openweather.data.util.FirebaseAuthHandlerImpl
import com.nielaclag.openweather.domain.util.FirebaseAuthHandler
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

/**
 * Created by Niel on 11/8/2024.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FirebaseModule::class]
)
class FakeFirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return mockk(relaxed = true)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthHandler(firebaseAuth: FirebaseAuth, moshi: Moshi): FirebaseAuthHandler {
        return FirebaseAuthHandlerImpl(firebaseAuth, moshi)
    }

}