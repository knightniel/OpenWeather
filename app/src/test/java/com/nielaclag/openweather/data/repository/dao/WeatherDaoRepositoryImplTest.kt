package com.nielaclag.openweather.data.repository.dao

import com.google.common.truth.Truth.*
import com.nielaclag.openweather.data.database.dao.WeatherDao
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.data.mapper.toEntity
import com.nielaclag.openweather.domain.model.weather.Clouds
import com.nielaclag.openweather.domain.model.weather.Coordinate
import com.nielaclag.openweather.domain.model.weather.Sys
import com.nielaclag.openweather.domain.model.weather.Temperature
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.model.weather.WeatherInfo
import com.nielaclag.openweather.domain.model.weather.Wind
import com.nielaclag.openweather.domain.repository.dao.WeatherDaoRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Niel on 11/3/2024.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherDaoRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK(relaxed = true)
    private lateinit var dao: WeatherDao
    private lateinit var weatherDaoRepository: WeatherDaoRepository

    private val testWeather = Weather(
        id = 1,
        coordinate = Coordinate(
            latitude = 14.5605166,
            longitude = 121.0764343
        ),
        weatherInfo = listOf(
            WeatherInfo(
                id = 801,
                main = "Clouds",
                description = "few clouds",
                icon = "02d"
            )
        ),
        base = "stations",
        temperature = Temperature(
            temp = 30.2f,
            feelsLike = 36.85f,
            tempMin = 30.01f,
            tempMax = 30.63f,
            pressure = 1006,
            humidity = 75,
            seaLevel = 1006,
            groundLevel = 1005
        ),
        visibility = 10000,
        wind = Wind(
            speed = 4.63f,
            degrees = 260,
            gust = null
        ),
        clouds = Clouds(
            all = 20
        ),
        rain = null,
        snow = null,
        dt = 1730010687,
        sys = Sys(
            type = 2,
            id = 2083945,
            message = null,
            country = "PH",
            sunrise = 1729979368,
            sunset = 1730021365
        ),
        timezone = 28800,
        cityId = 1694403,
        cityName = "Pateros",
        cod = 200
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        weatherDaoRepository = WeatherDaoRepositoryImpl(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertData_should_call_insertData_on_dao() = runTest {
        coEvery {
            dao.insertData(testWeather.toEntity())
        }.returns(testWeather.id)

        val result = weatherDaoRepository.insertData(testWeather.toEntity())

        coVerify {
            dao.insertData(testWeather.toEntity())
        }
        assertThat(result).isEqualTo(testWeather.id)
    }

    @Test
    fun getLatestData_should_call_getLatestData_on_dao() = runTest {
        coEvery {
            dao.getLatestData()
        }.returns(testWeather.toEntity())

        val result = weatherDaoRepository.getLatestData()?.toDomain()

        coVerify {
            dao.getLatestData()
        }
        assertThat(result).isEqualTo(testWeather)
    }

    @Test
    fun getLatestDataFlow_should_call_getLatestDataFlow_on_dao() = runTest {
        coEvery {
            dao.getLatestDataFlow()
        }.returns(flowOf(testWeather.toEntity()))

        val result = weatherDaoRepository.getLatestDataFlow().first()?.toDomain()

        coVerify {
            dao.getLatestDataFlow()
        }
        assertThat(result).isEqualTo(testWeather)
    }

    @Test
    fun updateData() = runTest {
        weatherDaoRepository.updateData(testWeather.toEntity())

        coVerify {
            dao.updateData(testWeather.toEntity())
        }
    }

    @Test
    fun deleteAllData() = runTest {
        weatherDaoRepository.deleteAllData()

        coVerify {
            dao.deleteAllData()
        }
    }

    @Test
    fun deleteData()= runTest {
        weatherDaoRepository.deleteData(testWeather.toEntity())

        coVerify {
            dao.deleteData(testWeather.toEntity())
        }
    }
    @Test
    fun setNewData() = runTest {
        coEvery {
            dao.setNewData(testWeather.toEntity())
        }.returns(testWeather.id)

        val result = weatherDaoRepository.setNewData(testWeather.toEntity())

        coVerify {
            dao.setNewData(testWeather.toEntity())
        }
        assertThat(result).isEqualTo(testWeather.id)
    }
}