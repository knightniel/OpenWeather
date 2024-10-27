package com.nielaclag.openweather.data.database.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.data.database.AppDatabase
import com.nielaclag.openweather.data.database.converter.LocalUserConverter
import com.nielaclag.openweather.data.database.converter.WeatherConverter
import com.nielaclag.openweather.data.mapper.toEntity
import com.nielaclag.openweather.data.model.moshiadapter.JsonObjectAdapter
import com.nielaclag.openweather.domain.model.weather.Clouds
import com.nielaclag.openweather.domain.model.weather.Coordinate
import com.nielaclag.openweather.domain.model.weather.Rain
import com.nielaclag.openweather.domain.model.weather.Sys
import com.nielaclag.openweather.domain.model.weather.Temperature
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.model.weather.WeatherInfo
import com.nielaclag.openweather.domain.model.weather.Wind
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Niel on 10/27/2024.
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrentWeatherDaoTest {

    private lateinit var moshi: Moshi
    private lateinit var database: AppDatabase
    private lateinit var dao: CurrentWeatherDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        moshi = Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .add(JsonObjectAdapter())
            .build()

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .addTypeConverter(LocalUserConverter(moshi = moshi))
            .addTypeConverter(WeatherConverter(moshi = moshi))
            .build()

        dao = database.currentWeatherDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetLatestData() = runTest {
        val weathers = listOf(
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
                    temp = 29.99f,
                    feelsLike = 36.27f,
                    tempMin = 28.97f,
                    tempMax = 30.56f,
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
                dt = 1730011630,
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
            )
        ).map { it.toEntity() }

        weathers.forEachIndexed { index, weatherEntity ->
            if (index != weathers.lastIndex) {
                dao.insertData(weatherEntity)
            }
        }
        var fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers[weathers.lastIndex - 1])

        dao.insertData(weathers.last())
        fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers.last())
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetLatestDataFlow() = runTest {
        val weathers = listOf(
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
                    temp = 29.99f,
                    feelsLike = 36.27f,
                    tempMin = 28.97f,
                    tempMax = 30.56f,
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
                dt = 1730011630,
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
            )
        ).map { it.toEntity() }

        weathers.forEachIndexed { index, weatherEntity ->
            if (index != weathers.lastIndex) {
                dao.insertData(weatherEntity)
            }
        }
        var fetchedData = dao.getLatestDataFlow().last()
        assertThat(fetchedData).isEqualTo(weathers[weathers.lastIndex - 1])

        dao.insertData(weathers.last())
        fetchedData = dao.getLatestDataFlow().last()
        assertThat(fetchedData).isEqualTo(weathers.last())
    }

    @Test
    @Throws(IOException::class)
    fun updateData() = runTest {
        val weathers = listOf(
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
                    temp = 29.99f,
                    feelsLike = 36.27f,
                    tempMin = 28.97f,
                    tempMax = 30.56f,
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
                dt = 1730011630,
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
            )
        ).map { it.toEntity() }

        weathers.forEachIndexed { index, weatherEntity ->
            if (index != weathers.lastIndex) {
                dao.insertData(weatherEntity)
            }
        }
        var fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers[weathers.lastIndex - 1])

        dao.updateData(weathers.last())
        fetchedData = dao.getLatestData()
        assertThat(fetchedData).isEqualTo(weathers.last())
    }

    @Test
    @Throws(IOException::class)
    fun deleteData() = runTest {
        val weathers = listOf(
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
                    temp = 29.99f,
                    feelsLike = 36.27f,
                    tempMin = 28.97f,
                    tempMax = 30.56f,
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
                dt = 1730011630,
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
            )
        ).map { it.toEntity() }

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
    fun deleteAllData() = runTest {
        val weathers = listOf(
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
                    temp = 29.99f,
                    feelsLike = 36.27f,
                    tempMin = 28.97f,
                    tempMax = 30.56f,
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
                dt = 1730011630,
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
            )
        ).map { it.toEntity() }

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
    fun setNewData() = runTest {
        val weathers = listOf(
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
                    temp = 29.99f,
                    feelsLike = 36.27f,
                    tempMin = 28.97f,
                    tempMax = 30.56f,
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
                dt = 1730011630,
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
            )
        ).map { it.toEntity() }

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