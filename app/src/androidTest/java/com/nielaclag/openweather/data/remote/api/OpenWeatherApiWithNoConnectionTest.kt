package com.nielaclag.openweather.data.remote.api

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.nielaclag.openweather.common.helper.asAndroidX
import com.nielaclag.openweather.common.helper.log
import com.nielaclag.openweather.common.helper.toUrlEncoded
import com.nielaclag.openweather.common.networkutil.interceptor.ConnectivityInterceptor.NoConnectivityException
import com.nielaclag.openweather.common.networkutil.interceptor.ContentTypeJsonInterceptor
import com.nielaclag.openweather.di.InterceptorModule
import com.nielaclag.openweather.di.qualifier.Connectivity
import com.nielaclag.openweather.di.qualifier.ContentTypeJson
import com.nielaclag.openweather.di.qualifier.HttpLogging
import com.nielaclag.openweather.di.qualifier.OpenWeather
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Niel on 11/5/2024.
 */
@UninstallModules(InterceptorModule::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
@LargeTest
class OpenWeatherApiWithNoConnectionTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var testIdlingResource: IdlingResource

    @Module
    @InstallIn(SingletonComponent::class)
    object FakeInterceptorModule {

        @Provides
        @HttpLogging
        fun provideHttpLoggingInterceptor(): Interceptor {
            return HttpLoggingInterceptor { message ->
                log(message = message, tag = "OkHttp3")
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        @Provides
        @Connectivity
        fun provideConnectivityInterceptor(): Interceptor {
            return Interceptor { throw NoConnectivityException() }
        }

        @Provides
        @ContentTypeJson
        fun provideContentTypeJsonInterceptor(): Interceptor {
            return ContentTypeJsonInterceptor()
        }

    }

    @Inject
    @OpenWeather
    lateinit var okHttpClient: OkHttpClient
    @Inject
    lateinit var moshi: Moshi
    @Inject
    lateinit var openWeatherApi: OpenWeatherApi
    @Inject
    lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        hiltRule.inject()
        testIdlingResource = OkHttp3IdlingResource.create("OkHttp", okHttpClient).asAndroidX()
        IdlingRegistry.getInstance().register(testIdlingResource)
        try {
            mockWebServer.start() // MockWebServer could've been started via hilt injection's mockWebServer.url("/")
        } catch (_: Exception) {
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(testIdlingResource)
    }

    @Test
    fun getLocationsByName_returns_error_caused_by_no_connection() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val query = "${ cityName.toUrlEncoded() },${ stateCode.toUrlEncoded() },${ countryCode.toUrlEncoded() }"
        val limit = 10

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{}")
            .throttleBody(1024, 200L, TimeUnit.MILLISECONDS)
        mockWebServer.enqueue(mockedResponse)

        try {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
            fail("Expected NoConnectivityException")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(NoConnectivityException::class.java)
        }
    }

    @Test
    fun getLocationsByCoordinates_returns_error_caused_by_no_connection() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val limit = 10

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{}")
            .throttleBody(1024, 200L, TimeUnit.MILLISECONDS)
        mockWebServer.enqueue(mockedResponse)

        try {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
            fail("Expected NoConnectivityException")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(NoConnectivityException::class.java)
        }
    }

    @Test
    fun getCurrentWeather_returns_error_caused_by_no_connection() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{}")
            .throttleBody(1024, 200L, TimeUnit.MILLISECONDS)
        mockWebServer.enqueue(mockedResponse)

        try {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
            fail("Expected NoConnectivityException")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(NoConnectivityException::class.java)
        }
    }

}