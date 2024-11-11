package com.nielaclag.openweather.domain.usecase.remote.openweather

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.common.networkutil.interceptor.ConnectivityInterceptor.NoConnectivityException
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.data.mapper.toDto
import com.nielaclag.openweather.data.remote.dto.LocationInfoDto
import com.nielaclag.openweather.data.remote.dto.OpenWeatherErrorDto
import com.nielaclag.openweather.domain.model.weather.LocationInfo
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
class GetLocationsByNameTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var openWeatherRepository: OpenWeatherRepository

    private lateinit var getLocationsByName: GetLocationsByName

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        getLocationsByName = GetLocationsByName(openWeatherRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun invoke_should_return_locationInfos_when_repository_call_is_successful() = runTest {
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
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
        val repositoryResponse = DataResponse.Success<Array<LocationInfoDto>, OpenWeatherErrorDto>(
            data = mockResponse.map { it.toDto() }.toTypedArray(),
            statusCode = HttpURLConnection.HTTP_OK,
            message = "OK"
        )
        val expectedResult = Resource.Success(
            code = repositoryResponse.statusCode,
            data = repositoryResponse.data?.map { it.toDomain() }?.toTypedArray(),
            message = repositoryResponse.message
        )

        coEvery {
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }.returns(repositoryResponse)

        getLocationsByName.invoke(
            cityName = cityName,
            stateCode = stateCode,
            countryCode = countryCode,
            limit = limit
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
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful_with_error_400() = runTest {
        val cityName = ""
        val stateCode = ""
        val countryCode = ""
        val limit = 10

        val repositoryError = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_BAD_REQUEST,
            message = "bad query"
        )
        val repositoryResponse = DataResponse.Error<Array<LocationInfoDto>, OpenWeatherErrorDto>(
            data = null,
            statusCode = repositoryError.code,
            error = repositoryError,
            message = repositoryError.message
        )
        val expectedResult = Resource.Error<Array<LocationInfoDto>>(
            code = repositoryResponse.statusCode,
            message = repositoryResponse.message ?: "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }.returns(repositoryResponse)

        getLocationsByName.invoke(
            cityName = cityName,
            stateCode = stateCode,
            countryCode = countryCode,
            limit = limit
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
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful_with_error_401() = runTest {
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val limit = 10

        val repositoryError = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_UNAUTHORIZED,
            message = "Invalid API key. Please see https://openweathermap.org/faq#error401 for more info."
        )
        val repositoryResponse = DataResponse.Error<Array<LocationInfoDto>, OpenWeatherErrorDto>(
            data = null,
            statusCode = repositoryError.code,
            error = repositoryError,
            message = repositoryError.message
        )
        val expectedResult = Resource.Error<Array<LocationInfoDto>>(
            code = repositoryResponse.statusCode,
            message = repositoryResponse.message ?: "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }.returns(repositoryResponse)

        getLocationsByName.invoke(
            cityName = cityName,
            stateCode = stateCode,
            countryCode = countryCode,
            limit = limit
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
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful_with_error_404() = runTest {
        val cityName = ""
        val stateCode = ""
        val countryCode = ""
        val limit = 10

        val repositoryError = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_NOT_FOUND,
            message = "not found"
        )
        val repositoryResponse = DataResponse.Error<Array<LocationInfoDto>, OpenWeatherErrorDto>(
            data = null,
            statusCode = repositoryError.code,
            error = repositoryError,
            message = repositoryError.message
        )
        val expectedResult = Resource.Error<Array<LocationInfoDto>>(
            code = repositoryResponse.statusCode,
            message = repositoryResponse.message ?: "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }.returns(repositoryResponse)

        getLocationsByName.invoke(
            cityName = cityName,
            stateCode = stateCode,
            countryCode = countryCode,
            limit = limit
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
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful_with_error_500() = runTest {
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val limit = 10

        val repositoryError = OpenWeatherErrorDto(
            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
            message = "internal error"
        )
        val repositoryResponse = DataResponse.Error<Array<LocationInfoDto>, OpenWeatherErrorDto>(
            data = null,
            statusCode = repositoryError.code,
            error = repositoryError,
            message = repositoryError.message
        )
        val expectedResult = Resource.Error<Array<LocationInfoDto>>(
            code = repositoryResponse.statusCode,
            message = repositoryResponse.message ?: "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }.returns(repositoryResponse)

        getLocationsByName.invoke(
            cityName = cityName,
            stateCode = stateCode,
            countryCode = countryCode,
            limit = limit
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
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_throws_a_SocketTimeoutException() = runTest {
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val limit = 10

        val repositoryError = SocketTimeoutException()
        val expectedResult = Resource.Error<Array<LocationInfo>>(
            message = repositoryError.message ?: "Couldn't reach server. Check your internet connection."
        )

        coEvery {
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }.answers {
            throw repositoryError
        }

        getLocationsByName.invoke(
            cityName = cityName,
            stateCode = stateCode,
            countryCode = countryCode,
            limit = limit
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
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_is_unsuccessful_with_NoConnectivityException() = runTest {
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val limit = 10

        val repositoryError = NoConnectivityException()
        val expectedResult = Resource.Error<Array<LocationInfo>>(
            message = repositoryError.message
        )

        coEvery {
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }.answers {
            throw repositoryError
        }

        getLocationsByName.invoke(
            cityName = cityName,
            stateCode = stateCode,
            countryCode = countryCode,
            limit = limit
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
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }
    }

    @Test
    fun invoke_should_return_error_when_repository_call_throws_an_Exception() = runTest {
        val cityName = "london"
        val stateCode = ""
        val countryCode = ""
        val limit = 10

        val repositoryError = Exception("Other exception.")
        val expectedResult = Resource.Error<Array<LocationInfo>>(
            message = "An unexpected error occurred."
        )

        coEvery {
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }.answers {
            throw repositoryError
        }

        getLocationsByName.invoke(
            cityName = cityName,
            stateCode = stateCode,
            countryCode = countryCode,
            limit = limit
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
            openWeatherRepository.getLocationsByName(
                appId = any(),
                query = any(),
                limit = limit
            )
        }
    }

}