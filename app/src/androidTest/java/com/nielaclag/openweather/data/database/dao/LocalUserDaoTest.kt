package com.nielaclag.openweather.data.database.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.data.database.AppDatabase
import com.nielaclag.openweather.data.mapper.toEntity
import com.nielaclag.openweather.data.model.room.supportmodel.AuthenticationTypePersistence
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.model.type.AuthenticationType
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
 * Created by Niel on 10/25/2024.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalUserDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: AppDatabase
    private lateinit var dao: LocalUserDao

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        hiltRule.inject()

        dao = database.localUserDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    @Throws(IOException::class)
    fun insertData_and_getData_should_insert_and_get_the_data() = runTest {
        val data = LocalUser(
            id = 1,
            name = "Test User",
            email = "test@gmail.com",
            image = "test.png",
            authenticationType = AuthenticationType.OTHER
        ).toEntity()
        val id = dao.insertData(data)

        val fetchedData = dao.getData()
        assertThat(fetchedData).isEqualTo(data)
        assertThat(id).isEqualTo(data.id)
    }

    @Test
    @Throws(IOException::class)
    fun insertData_should_replace_the_data_with_the_same_id() = runTest {
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

        val data2 = data.copy(
            name = "Test User 2",
            email = "test2@gmail.com",
            image = "test2.png",
            authenticationType = AuthenticationTypePersistence.GOOGLE
        )
        dao.insertData(data2)

        fetchedData = dao.getData()
        assertThat(fetchedData).isEqualTo(data2)
    }

    @Test
    @Throws(IOException::class)
    fun getDataFlow_should_get_the_data() = runTest {
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
    fun updateData_should_update_the_data() = runTest {
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

        val data2 = data.copy(
            name = "Test User 2",
            email = "test2@gmail.com",
            image = "test2.png",
            authenticationType = AuthenticationTypePersistence.GOOGLE
        )
        dao.updateData(data2)

        fetchedData = dao.getData()
        assertThat(fetchedData).isEqualTo(data2)
    }

    @Test
    @Throws(IOException::class)
    fun deleteAllData_should_remove_all_data() = runTest {
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

    @Test
    @Throws(IOException::class)
    fun setNewData_should_clear_and_set_new_data() = runTest {
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
        assertThat(fetchedData).isEqualTo(data2)
    }

}