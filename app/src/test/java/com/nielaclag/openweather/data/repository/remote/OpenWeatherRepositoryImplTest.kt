package com.nielaclag.openweather.data.repository.remote

import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.common.helper.toJson
import com.nielaclag.openweather.common.helper.toUrlEncoded
import com.nielaclag.openweather.common.networkutil.interceptor.ConnectivityInterceptor.NoConnectivityException
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.data.mapper.toDto
import com.nielaclag.openweather.data.model.moshiadapter.JsonObjectAdapter
import com.nielaclag.openweather.data.remote.api.OpenWeatherApi
import com.nielaclag.openweather.data.remote.dto.LocationInfoDto
import com.nielaclag.openweather.data.remote.dto.OpenWeatherErrorDto
import com.nielaclag.openweather.data.remote.dto.WeatherDto
import com.nielaclag.openweather.domain.model.weather.Clouds
import com.nielaclag.openweather.domain.model.weather.Coordinate
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.model.weather.Sys
import com.nielaclag.openweather.domain.model.weather.Temperature
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.model.weather.WeatherInfo
import com.nielaclag.openweather.domain.model.weather.Wind
import com.nielaclag.openweather.domain.repository.remote.OpenWeatherRepository
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
import java.net.SocketTimeoutException

/**
 * Created by Niel on 11/3/2024.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class OpenWeatherRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK(relaxed = true)
    private lateinit var openWeatherApi: OpenWeatherApi
    private lateinit var moshi: Moshi
    private lateinit var openWeatherRepository: OpenWeatherRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        moshi = Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .add(JsonObjectAdapter())
            .build()
        openWeatherRepository = OpenWeatherRepositoryImpl(openWeatherApi, moshi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // getLocationsByName

    @Test
    fun getLocationsByName_returns_success() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val query = "${ cityName.toUrlEncoded() },${ stateCode.toUrlEncoded() },${ countryCode.toUrlEncoded() }"
        val limit = 10

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
            .map { it.toDto() }
            .toTypedArray()
        val apiResponse = Response.success(mockResponse)
        val expectedResult = DataResponse.Success<Array<LocationInfoDto>, OpenWeatherErrorDto>(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            message = apiResponse.message()
        )

        coEvery {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Success::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
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
        val apiResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
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
        val apiResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
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
        val apiResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
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
        val apiResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByName(
            appId = openWeatherAppId,
            query = query,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
    }

    @Test
    fun getLocationsByName_fails_with_timeout() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val query = "${ cityName.toUrlEncoded() },${ stateCode.toUrlEncoded() },${ countryCode.toUrlEncoded() }"
        val limit = 10
        var thrownException: Exception? = null

        val apiResponse = SocketTimeoutException()

        coEvery {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }.answers {
            throw apiResponse
        }

        try {
            openWeatherRepository.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        } catch (e: Exception) {
            thrownException = e
        }

        coVerify {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }
        assertThat(thrownException).isInstanceOf(apiResponse::class.java)
    }

    @Test
    fun getLocationsByName_returns_error_caused_by_no_connection() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val query = "${ cityName.toUrlEncoded() },${ stateCode.toUrlEncoded() },${ countryCode.toUrlEncoded() }"
        val limit = 10
        var thrownException: Exception? = null

        val apiResponse = NoConnectivityException()

        coEvery {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }.answers {
            throw apiResponse
        }

        try {
            openWeatherRepository.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        } catch (e: Exception) {
            thrownException = e
        }

        coVerify {
            openWeatherApi.getLocationsByName(
                appId = openWeatherAppId,
                query = query,
                limit = limit
            )
        }
        assertThat(thrownException).isInstanceOf(apiResponse::class.java)
    }

    // getLocationsByCoordinates

    @Test
    fun getLocationsByCoordinates_returns_success() = runTest {
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
            .map { it.toDto() }
            .toTypedArray()
        val apiResponse = Response.success(mockResponse)
        val expectedResult = DataResponse.Success<Array<LocationInfoDto>, OpenWeatherErrorDto>(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            message = apiResponse.message()
        )

        coEvery {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Success::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
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
        val apiResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
    }

    @Test
    fun getLocationsByCoordinate_returns_error_401_caused_by_wrong_AppId() = runTest {
        val openWeatherAppId = "WrongOpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_UNAUTHORIZED,
            message = "Invalid API key. Please see https://openweathermap.org/faq#error401 for more info."
        )
        val apiResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
    }

    @Test
    fun getLocationsByCoordinate_returns_error_404_caused_by_wrong_query() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 91.0
        val longitude = -181.0
        val limit = 10

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_NOT_FOUND,
            message = "not found"
        )
        val apiResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
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
        val apiResponse = Response.error<Array<LocationInfoDto>>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getLocationsByCoordinate(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            limit = limit
        )

        coVerify {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
    }

    @Test
    fun getLocationsByCoordinate_fails_with_timeout() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val limit = 10
        var thrownException: Exception? = null

        val apiResponse = SocketTimeoutException()

        coEvery {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }.answers {
            throw apiResponse
        }

        try {
            openWeatherRepository.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        } catch (e: Exception) {
            thrownException = e
        }

        coVerify {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }
        assertThat(thrownException).isInstanceOf(apiResponse::class.java)
    }

    @Test
    fun getLocationsByCoordinate_returns_error_caused_by_no_connection() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val limit = 10
        var thrownException: Exception? = null

        val apiResponse = NoConnectivityException()

        coEvery {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }.answers {
            throw apiResponse
        }

        try {
            openWeatherRepository.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        } catch (e: Exception) {
            thrownException = e
        }

        coVerify {
            openWeatherApi.getLocationsByCoordinate(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
        }
        assertThat(thrownException).isInstanceOf(apiResponse::class.java)
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
        ).toDto()
        val apiResponse = Response.success(mockResponse)
        val expectedResult = DataResponse.Success<WeatherDto, OpenWeatherErrorDto>(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            message = apiResponse.message()
        )

        coEvery {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )

        coVerify {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Success::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
    }

    @Test
    fun getCurrentWeather_returns_error_400_caused_by_wrong_query() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 91.0
        val longitude = -181.0
        val units = "metric"

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_BAD_REQUEST,
            message = "bad query"
        )
        val apiResponse = Response.error<WeatherDto>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )

        coVerify {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
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
        val apiResponse = Response.error<WeatherDto>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )

        coVerify {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
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
        val apiResponse = Response.error<WeatherDto>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )

        coVerify {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
        assertThat(result).isInstanceOf(DataResponse.Error::class.java)
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
    }

    @Test
    fun getCurrentWeather_returns_error_500_server_error() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val mockResponse = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
            message = "internal server error"
        )
        val apiResponse = Response.error<WeatherDto>(
            mockResponse.code,
            moshi.toJson(mockResponse).toResponseBody("application/json".toMediaType())
        )
        val expectedResult = DataResponse.Error(
            data = apiResponse.body(),
            statusCode = apiResponse.code(),
            error = mockResponse,
            message = mockResponse.message
        )

        coEvery {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(apiResponse)

        val result = openWeatherRepository.getCurrentWeather(
            appId = openWeatherAppId,
            latitude = latitude,
            longitude = longitude,
            units = units
        )

        coVerify {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
        assertThat(result.statusCode).isEqualTo(expectedResult.statusCode)
        assertThat(result.message).isEqualTo(expectedResult.message)
        assertThat(result.data).isEqualTo(expectedResult.data)
        assertThat(result.error).isEqualTo(expectedResult.error)
    }

    @Test
    fun getCurrentWeather_fails_with_timeout() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"
        var thrownException: Exception? = null

        val apiResponse = SocketTimeoutException()

        coEvery {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.answers {
            throw apiResponse
        }

        try {
            openWeatherRepository.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        } catch (e: Exception) {
            thrownException = e
        }

        coVerify {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
        assertThat(thrownException).isInstanceOf(apiResponse::class.java)
    }

    @Test
    fun getCurrentWeather_returns_error_caused_by_no_connection() = runTest {
        val openWeatherAppId = "OpenWeatherTestAppId"
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"
        var thrownException: Exception? = null

        val apiResponse = NoConnectivityException()

        coEvery {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.answers {
            throw apiResponse
        }

        try {
            openWeatherRepository.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        } catch (e: Exception) {
            thrownException = e
        }

        coVerify {
            openWeatherApi.getCurrentWeather(
                appId = openWeatherAppId,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
        assertThat(thrownException).isInstanceOf(apiResponse::class.java)
    }

}