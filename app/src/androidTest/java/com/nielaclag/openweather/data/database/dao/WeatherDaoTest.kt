package com.nielaclag.openweather.data.database.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.data.database.AppDatabase
import com.nielaclag.openweather.data.mapper.toEntity
import com.nielaclag.openweather.domain.model.weather.Clouds
import com.nielaclag.openweather.domain.model.weather.Coordinate
import com.nielaclag.openweather.domain.model.weather.Rain
import com.nielaclag.openweather.domain.model.weather.Sys
import com.nielaclag.openweather.domain.model.weather.Temperature
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.model.weather.WeatherInfo
import com.nielaclag.openweather.domain.model.weather.Wind
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Niel on 10/27/2024.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: AppDatabase
    private lateinit var dao: WeatherDao

    private val weathers = mutableListOf(
        Weather(
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
        ),
        Weather(
            id = 2,
            coordinate = Coordinate(
                latitude = 14.5605,
                longitude = 121.0764
            ),
            weatherInfo = listOf(
                WeatherInfo(
                    id = 500,
                    main = "Rain",
                    description = "light rain",
                    icon = "10d"
                )
            ),
            base = "stations",
            temperature = Temperature(
                temp = 29.99f,
                feelsLike = 36.54f,
                tempMin = 28.97f,
                tempMax = 30.07f,
                pressure = 1006,
                humidity = 76,
                seaLevel = 1006,
                groundLevel = 1005
            ),
            visibility = 10000,
            wind = Wind(
                speed = 6.69f,
                degrees = 270,
                gust = null
            ),
            rain = Rain(
                oneHour = 0.22f
            ),
            clouds = Clouds(
                all = 20
            ),
            snow = null,
            dt = 1730014729,
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
        ),
        Weather(
            id = 3,
            coordinate = Coordinate(
                latitude = 14.5605166,
                longitude = 121.0764343
            ),
            weatherInfo = listOf(
                WeatherInfo(
                    id = 501,
                    main = "Rain",
                    description = "moderate rain",
                    icon = "10d"
                )
            ),
            base = "stations",
            temperature = Temperature(
                temp = 30.28f,
                feelsLike = 37.28f,
                tempMin = 29.44f,
                tempMax = 31.01f,
                pressure = 1011,
                humidity = 81,
                seaLevel = 1011,
                groundLevel = 1010
            ),
            visibility = 10000,
            wind = Wind(
                speed = 3.13f,
                degrees = 102,
                gust = 3.13f
            ),
            rain = Rain(
                oneHour = 2.71f
            ),
            clouds = Clouds(
                all = 20
            ),
            snow = null,
            dt = 1730424680,
            sys = Sys(
                type = 2,
                id = 2083945,
                message = null,
                country = "PH",
                sunrise = 1730411461,
                sunset = 1730453244
            ),
            timezone = 28800,
            cityId = 1694403,
            cityName = "Pateros",
            cod = 200
        )
    )
        .map { it.toEntity() }
        .toMutableList()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        hiltRule.inject()

        dao = database.weatherDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    @Throws(IOException::class)
    fun insertData_should_insert_the_data() = runTest {
        val data = weathers.first()
        dao.insertData(data)

        val fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(data)
    }

    @Test
    @Throws(IOException::class)
    fun insertData_should_replace_the_data_with_the_same_id() = runTest {
        val data = weathers.first()
        dao.insertData(data)

        var fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(data)

        val data2 = weathers[1].copy(
            id = data.id
        )
        dao.insertData(data2)

        fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(data2)
    }

    @Test
    @Throws(IOException::class)
    fun getLatestData_should_fetch_the_last_inserted_data() = runTest {
        weathers.take(weathers.size - 1).forEach { weatherEntity ->
            dao.insertData(weatherEntity)
        }
        var fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers.take(weathers.size - 1).last())

        dao.insertData(weathers.last())
        fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers.last())
    }

    @Test
    @Throws(IOException::class)
    fun getLatestDataFlow_should_fetch_the_last_inserted_data() = runTest {
        weathers.take(weathers.size - 1).forEach { weatherEntity ->
            dao.insertData(weatherEntity)
        }
        var fetchedData = dao.getLatestDataFlow().first()
        assertThat(fetchedData).isEqualTo(weathers.take(weathers.size - 1).last())

        dao.insertData(weathers.last())
        fetchedData = dao.getLatestDataFlow().first()
        assertThat(fetchedData).isEqualTo(weathers.last())
    }

    @Test
    @Throws(IOException::class)
    fun updateData_should_update_data() = runTest {
        val data = weathers.first()
        dao.insertData(data)

        var fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(data)

        val data2 = weathers[1].copy(
            id = data.id
        )
        dao.updateData(data2)

        fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(data2)
    }

    @Test
    @Throws(IOException::class)
    fun deleteData_should_delete_the_data() = runTest {
        weathers.forEach { weatherEntity ->
            dao.insertData(weatherEntity)
        }
        var fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers.last())

        dao.deleteData(weathers.last())
        fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers[weathers.lastIndex - 1])
    }

    @Test
    @Throws(IOException::class)
    fun deleteAllData_should_delete_all_data() = runTest {
        weathers.forEach { weather ->
            dao.insertData(weather)
        }
        var fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers.last())

        dao.deleteAllData()
        fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(null)
    }

    @Test
    @Throws(IOException::class)
    fun setNewData_should_clear_and_set_new_data() = runTest {
        weathers.forEach { weather ->
            dao.insertData(weather)
        }
        var fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers.last())

        dao.setNewData(weathers.first())
        fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers.first())
    }

}