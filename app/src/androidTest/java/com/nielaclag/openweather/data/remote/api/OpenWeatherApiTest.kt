package com.nielaclag.openweather.data.remote.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.common.helper.toJson
import com.nielaclag.openweather.common.helper.toUrlEncoded
import com.nielaclag.openweather.data.mapper.toDto
import com.nielaclag.openweather.data.remote.dto.LocationInfoDto
import com.nielaclag.openweather.data.remote.dto.OpenWeatherErrorDto
import com.nielaclag.openweather.data.remote.dto.WeatherDto
import com.nielaclag.openweather.di.qualifier.OpenWeather
import com.nielaclag.openweather.domain.model.weather.Clouds
import com.nielaclag.openweather.domain.model.weather.Coordinate
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.model.weather.Sys
import com.nielaclag.openweather.domain.model.weather.Temperature
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.model.weather.WeatherInfo
import com.nielaclag.openweather.domain.model.weather.Wind
import com.squareup.moshi.Moshi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Niel on 11/3/2024.
 */
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
@LargeTest
class OpenWeatherApiTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

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
        try {
            mockWebServer.start() // MockWebServer could've been started via hilt injection's mockWebServer.url("/")
        } catch (_: Exception) {
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // getLocationsByName

    @Test
    fun getLocationsByName_returns_success() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val query = "${ cityName.toUrlEncoded() },${ stateCode.toUrlEncoded() },${ countryCode.toUrlEncoded() }"

        val mockResponse = arrayOf(
            LocationInfo(
                name = "London",
                latitude = 51.5073219,
                longitude = -0.1276474,
                country = "GB",
                state = "England"
            ),
            LocationInfo(
                name = "City of London",
                latitude = 51.5156177,
                longitude = -0.0919983,
                country = "GB",
                state = "England"
            ),
            LocationInfo(
                name = "London",
                latitude = 42.9832406,
                longitude = -81.243372,
                country = "CA",
                state = "Ontario"
            ),
            LocationInfo(
                name = "Chelsea",
                latitude = 51.4875167,
                longitude = -0.1687007,
                country = "GB",
                state = "England"
            ),
            LocationInfo(
                name = "London",
                latitude = 37.1289771,
                longitude = -84.0832646,
                country = "US",
                state = "Kentucky"
            )
        )
        val expectedResponse = Response.success(
            mockResponse.map { it.toDto() }.toTypedArray()
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
            .throttleBody(1024, 200L, TimeUnit.MILLISECONDS)
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = 10
        )
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByName_returns_error_400_caused_by_wrong_query() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val query = ","
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_BAD_REQUEST,
            message = "bad query"
        )
        val expectedResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = limit
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByName_returns_error_401_caused_by_wrong_AppId() = runTest {
        val openWeatherAppId = "WrongOpenWeatherTestAppId"
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val query = "${ cityName.toUrlEncoded() },${ stateCode.toUrlEncoded() },${ countryCode.toUrlEncoded() }"
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_UNAUTHORIZED,
            message = "Invalid API key. Please see https://openweathermap.org/faq#error401 for more info."
        )
        val expectedResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = limit
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByName_returns_error_404_caused_by_wrong_query() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val cityName = ""
        val stateCode = ""
        val countryCode = ""
        val query = "${ cityName.toUrlEncoded() },${ stateCode.toUrlEncoded() },${ countryCode.toUrlEncoded() }"
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_NOT_FOUND,
            message = "not found"
        )
        val expectedResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = limit
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByName_returns_error_500_server_error() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val query = "${ cityName.toUrlEncoded() },${ stateCode.toUrlEncoded() },${ countryCode.toUrlEncoded() }"
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
            message = "internal server error"
        )
        val expectedResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = limit
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByName_fails_with_timeout() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val query = "${ cityName.toUrlEncoded() },${ stateCode.toUrlEncoded() },${ countryCode.toUrlEncoded() }"
        val limit = 10

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        mockWebServer.enqueue(mockedResponse)

        try {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
            fail("Expected SocketTimeoutException")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(SocketTimeoutException::class.java)
        }
    }

    // getLocationsByCoordinates

    @Test
    fun getLocationsByCoordinate_returns_success() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val limit = 10

        val mockResponse = arrayOf(
            LocationInfo(
                name = "London",
                latitude = 51.5073219,
                longitude = -0.1276474,
                country = "GB",
                state = "England"
            )
        )
        val expectedResponse = Response.success(
            mockResponse.map { it.toDto() }.toTypedArray()
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
            .throttleBody(1024, 200L, TimeUnit.MILLISECONDS)
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByCoordinates_returns_error_400_caused_by_wrong_query() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 91.0
        val longitude = -181.0
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_BAD_REQUEST,
            message = "wrong latitude"
        )
        val expectedResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(mockResponse.code)
            .setBody(moshi.toJson(mockResponse))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByCoordinates_returns_error_401_caused_by_wrong_AppId() = runTest {
        val openWeatherAppId = "WrongOpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_UNAUTHORIZED,
            message = "Invalid API key. Please see https://openweathermap.org/faq#error401 for more info."
        )
        val expectedResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByCoordinates_returns_error_404_caused_by_wrong_query() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 91.0
        val longitude = -181.0
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_NOT_FOUND,
            message = "not found"
        )
        val expectedResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByCoordinate_returns_error_500_server_error() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
            message = "internal server error"
        )
        val expectedResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getLocationsByCoordinate_fails_with_timeout() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val limit = 10

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        mockWebServer.enqueue(mockedResponse)

        try {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
            fail("Expected SocketTimeoutException")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(SocketTimeoutException::class.java)
        }
    }

    // getCurrentWeather

    @Test
    fun getCurrentWeather_returns_success() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val mockResponse = Weather(
            id = 0,
            coordinate = Coordinate(
                latitude = 51.5073,
                longitude = -0.1276
            ),
            weatherInfo = listOf(
                WeatherInfo(
                    id = 804,
                    main = "Clouds",
                    description = "overcast clouds",
                    icon = "04d"
                )
            ),
            base = "stations",
            temperature = Temperature(
                temp = 12.42f,
                feelsLike = 11.83f,
                tempMin = 11.16f,
                tempMax = 13.43f,
                pressure = 1029,
                humidity = 81,
                seaLevel = 1029,
                groundLevel = 1026
            ),
            visibility = 10000,
            wind = Wind(
                speed = 3.09f,
                degrees = 70,
                gust = null
            ),
            clouds = Clouds(
                all = 100
            ),
            rain = null,
            snow = null,
            dt = 1730627806,
            sys = Sys(
                type = 2,
                id = 2075535,
                message = null,
                country = "GB",
                sunrise = 1730617083,
                sunset = 1730651401
            ),
            timezone = 0,
            cityId = 2643743,
            cityName = "London",
            cod = 200
        )
        val expectedResponse = Response.success(
            mockResponse.toDto()
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
            .throttleBody(1024, 200L, TimeUnit.MILLISECONDS)
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getCurrentWeather_returns_error_400_caused_by_wrong_query() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 91.0
        val longitude = -181.0
        val units = "metric"

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_BAD_REQUEST,
            message = "wrong latitude"
        )
        val expectedResponse = Response.error<WeatherDto>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getCurrentWeather_returns_error_401_caused_by_wrong_AppId() = runTest {
        val openWeatherAppId = "WrongOpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_UNAUTHORIZED,
            message = "Invalid API key. Please see https://openweathermap.org/faq#error401 for more info."
        )
        val expectedResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getCurrentWeather_returns_error_404_caused_by_wrong_query() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 91.0
        val longitude = -181.0
        val units = "metric"

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_NOT_FOUND,
            message = "not found"
        )
        val expectedResponse = Response.error<WeatherDto>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getCurrentWeather_returns_error_500_server_error() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
            message = "Internal server error."
        )
        val expectedResponse = Response.error<WeatherDto>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(expectedResponse.code())
            .setBody(moshi.toJson(expectedResponse.body()))
        mockWebServer.enqueue(mockedResponse)

        val response = openWeatherApi.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(expectedResponse.code())
        assertThat(response.body()).isEqualTo(expectedResponse.body())
    }

    @Test
    fun getCurrentWeather_fails_with_timeout() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val mockedResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        mockWebServer.enqueue(mockedResponse)

        try {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
            fail("Expected SocketTimeoutException")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(SocketTimeoutException::class.java)
        }
    }

}