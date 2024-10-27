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
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.model.type.AuthenticationType
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Niel on 10/25/2024.
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class LocalUserDaoTest {

    private lateinit var moshi: Moshi
    private lateinit var database: AppDatabase
    private lateinit var dao: LocalUserDao

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

        dao = database.localUserDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetData() = runTest {
        val data = LocalUser(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            image = "test.png",
            authenticationType = AuthenticationType.OTHER
        ).toEntity()
        dao.insertData(data)

        val fetchedData = dao.getData()
        assertThat(fetchedData).isEqualTo(data)
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetDataFlow() = runTest {
        val data = LocalUser(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            image = "test.png",
            authenticationType = AuthenticationType.OTHER
        ).toEntity()
        dao.insertData(data)

        val fetchedData = dao.getDataFlow().first()
        assertThat(fetchedData).isEqualTo(data)
    }

    @Test
    @Throws(IOException::class)
    fun updateData() = runTest {
        val data = LocalUser(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            image = "test.png",
            authenticationType = AuthenticationType.OTHER
        ).toEntity()
        dao.insertData(data)
        var fetchedData = dao.getData()
        assertThat(fetchedData).isEqualTo(data)

        val data2 = LocalUser(
            id = 1,
            name = "Test User 2",
            email = "test@gmail2.com",
            image = "test2.png",
            authenticationType = AuthenticationType.EMAIL
        ).toEntity()
        dao.updateData(data2)

        fetchedData = dao.getData()
        assertThat(fetchedData).isNotEqualTo(data)
        assertThat(fetchedData).isEqualTo(data2)
    }

    @Test
    @Throws(IOException::class)
    fun setNewData() = runTest {
        val data = LocalUser(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            image = "test.png",
            authenticationType = AuthenticationType.OTHER
        ).toEntity()
        dao.insertData(data)

        var fetchedData = dao.getData()
        assertThat(fetchedData).isEqualTo(data)

        val data2 = LocalUser(
            id = 2,
            name = "Test User 2",
            email = "test@gmail2.com",
            image = "test2.png",
            authenticationType = AuthenticationType.EMAIL
        ).toEntity()
        dao.setNewData(data2)

        fetchedData = dao.getData()
        assertThat(fetchedData).isNotEqualTo(data)
        assertThat(fetchedData).isEqualTo(data2)
    }

    @Test
    @Throws(IOException::class)
    fun deleteData() = runTest {
        val data = LocalUser(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            image = "test.png",
            authenticationType = AuthenticationType.OTHER
        ).toEntity()
        dao.insertData(data)
        var fetchedData = dao.getData()
        assertThat(fetchedData).isEqualTo(data)


        dao.deleteAllData()

        fetchedData = dao.getData()
        assertThat(fetchedData).isEqualTo(null)
    }

}