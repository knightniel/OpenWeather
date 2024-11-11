package com.nielaclag.openweather.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nielaclag.openweather.data.util.FirebaseAuthHandlerImpl
import com.nielaclag.openweather.domain.util.FirebaseAuthHandler
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Niel on 11/6/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthHandler(firebaseAuth: FirebaseAuth, moshi: Moshi): FirebaseAuthHandler {
        return FirebaseAuthHandlerImpl(firebaseAuth, moshi)
    }

}