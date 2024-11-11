package com.nielaclag.openweather.domain.usecase.remote.openweather

import app.cash.turbine.test
import com.google.common.truth.Truth.*
import com.nielaclag.openweather.common.networkutil.interceptor.ConnectivityInterceptor
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.data.mapper.toDto
import com.nielaclag.openweather.data.remote.dto.OpenWeatherErrorDto
import com.nielaclag.openweather.data.remote.dto.WeatherDto
import com.nielaclag.openweather.domain.model.weather.Clouds
import com.nielaclag.openweather.domain.model.weather.Coordinate
import com.nielaclag.openweather.domain.model.weather.Sys
import com.nielaclag.openweather.domain.model.weather.Temperature
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.model.weather.WeatherInfo
import com.nielaclag.openweather.domain.model.weather.Wind
import com.nielaclag.openweather.domain.repository.remote.OpenWeatherRepository
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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

/**
 * Created by Niel on 11/6/2024.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentWeatherTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var openWeatherRepository: OpenWeatherRepository

    private lateinit var getCurrentWeather: GetCurrentWeather

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        getCurrentWeather = GetCurrentWeather(openWeatherRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun invoke_should_return_locationInfos_when_repository_call_is_successful() = runTest {
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
        val repositoryResponse = DataResponse.Success<WeatherDto, OpenWeatherErrorDto>(
            data = mockResponse.toDto(),
            statusCode = HttpURLConnection.HTTP_OK,
            message = "OK"
        )
        val expectedResult = Resource.Success(
            code = repositoryResponse.statusCode,
            data = repositoryResponse.data?.toDomain(),
            message = repositoryResponse.message
        )

        coEvery {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(repositoryResponse)

        getCurrentWeather.invoke(
            latitude = latitude,
            longitude = longitude,
            units = units
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Success::class.java)
            assertThat(resource.code).isEqualTo(expectedResult.code)
            assertThat(resource.data).isEqualTo(expectedResult.data)
            assertThat(resource.message).isEqualTo(expectedResult.message)
            awaitComplete()
        }
        coVerify {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful_with_error_400() = runTest {
        val latitude = 91.0
        val longitude = -181.0
        val units = "metric"

        val repositoryError = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_BAD_REQUEST,
            message = "wrong latitude"
        )
        val repositoryResponse = DataResponse.Error<WeatherDto, OpenWeatherErrorDto>(
            data = null,
            statusCode = repositoryError.code,
            error = repositoryError,
            message = repositoryError.message
        )
        val expectedResult = Resource.Error<WeatherDto>(
            code = repositoryResponse.statusCode,
            message = repositoryResponse.message ?: "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(repositoryResponse)

        getCurrentWeather.invoke(
            latitude = latitude,
            longitude = longitude,
            units = units
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Error::class.java)
            assertThat(resource.code).isEqualTo(expectedResult.code)
            assertThat(resource.data).isEqualTo(expectedResult.data)
            assertThat(resource.message).isEqualTo(expectedResult.message)
            awaitComplete()
        }
        coVerify {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful_with_error_401() = runTest {
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val repositoryError = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_UNAUTHORIZED,
            message = "Invalid API key. Please see https://openweathermap.org/faq#error401 for more info."
        )
        val repositoryResponse = DataResponse.Error<WeatherDto, OpenWeatherErrorDto>(
            data = null,
            statusCode = repositoryError.code,
            error = repositoryError,
            message = repositoryError.message
        )
        val expectedResult = Resource.Error<WeatherDto>(
            code = repositoryResponse.statusCode,
            message = repositoryResponse.message ?: "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(repositoryResponse)

        getCurrentWeather.invoke(
            latitude = latitude,
            longitude = longitude,
            units = units
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Error::class.java)
            assertThat(resource.code).isEqualTo(expectedResult.code)
            assertThat(resource.data).isEqualTo(expectedResult.data)
            assertThat(resource.message).isEqualTo(expectedResult.message)
            awaitComplete()
        }
        coVerify {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful_with_error_404() = runTest {
        val latitude = 91.0
        val longitude = -181.0
        val units = "metric"

        val repositoryError = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_NOT_FOUND,
            message = "not found"
        )
        val repositoryResponse = DataResponse.Error<WeatherDto, OpenWeatherErrorDto>(
            data = null,
            statusCode = repositoryError.code,
            error = repositoryError,
            message = repositoryError.message
        )
        val expectedResult = Resource.Error<WeatherDto>(
            code = repositoryResponse.statusCode,
            message = repositoryResponse.message ?: "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(repositoryResponse)

        getCurrentWeather.invoke(
            latitude = latitude,
            longitude = longitude,
            units = units
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Error::class.java)
            assertThat(resource.code).isEqualTo(expectedResult.code)
            assertThat(resource.data).isEqualTo(expectedResult.data)
            assertThat(resource.message).isEqualTo(expectedResult.message)
            awaitComplete()
        }
        coVerify {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful_with_error_500() = runTest {
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val repositoryError = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
            message = "internal error"
        )
        val repositoryResponse = DataResponse.Error<WeatherDto, OpenWeatherErrorDto>(
            data = null,
            statusCode = repositoryError.code,
            error = repositoryError,
            message = repositoryError.message
        )
        val expectedResult = Resource.Error<WeatherDto>(
            code = repositoryResponse.statusCode,
            message = repositoryResponse.message ?: "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.returns(repositoryResponse)

        getCurrentWeather.invoke(
            latitude = latitude,
            longitude = longitude,
            units = units
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Error::class.java)
            assertThat(resource.code).isEqualTo(expectedResult.code)
            assertThat(resource.data).isEqualTo(expectedResult.data)
            assertThat(resource.message).isEqualTo(expectedResult.message)
            awaitComplete()
        }
        coVerify {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_throws_a_SocketTimeoutException() = runTest {
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val repositoryError = SocketTimeoutException()
        val expectedResult = Resource.Error<WeatherDto>(
            message = repositoryError.message ?: "Couldn't reach server. Check your internet connection."
        )

        coEvery {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.answers {
            throw repositoryError
        }

        getCurrentWeather.invoke(
            latitude = latitude,
            longitude = longitude,
            units = units
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Error::class.java)
            assertThat(resource.code).isEqualTo(expectedResult.code)
            assertThat(resource.data).isEqualTo(expectedResult.data)
            assertThat(resource.message).isEqualTo(expectedResult.message)
            awaitComplete()
        }
        coVerify {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_throws_a_NoConnectivityException() = runTest {
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val repositoryError = ConnectivityInterceptor.NoConnectivityException()
        val expectedResult = Resource.Error<WeatherDto>(
            message = repositoryError.message
        )

        coEvery {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.answers {
            throw repositoryError
        }

        getCurrentWeather.invoke(
            latitude = latitude,
            longitude = longitude,
            units = units
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Error::class.java)
            assertThat(resource.code).isEqualTo(expectedResult.code)
            assertThat(resource.data).isEqualTo(expectedResult.data)
            assertThat(resource.message).isEqualTo(expectedResult.message)
            awaitComplete()
        }
        coVerify {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_throws_an_Exception() = runTest {
        val latitude = 51.5073219
        val longitude = -0.1276474
        val units = "metric"

        val repositoryError = Exception("Other exception.")
        val expectedResult = Resource.Error<WeatherDto>(
            message = "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }.answers {
            throw repositoryError
        }

        getCurrentWeather.invoke(
            latitude = latitude,
            longitude = longitude,
            units = units
        ).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val resource = awaitItem()
            assertThat(resource).isInstanceOf(Resource.Error::class.java)
            assertThat(resource.code).isEqualTo(expectedResult.code)
            assertThat(resource.data).isEqualTo(expectedResult.data)
            assertThat(resource.message).isEqualTo(expectedResult.message)
            awaitComplete()
        }
        coVerify {
            openWeatherRepository.getCurrentWeather(
                appId = any(),
                latitude = latitude,
                longitude = longitude,
                units = units
            )
        }
    }

}