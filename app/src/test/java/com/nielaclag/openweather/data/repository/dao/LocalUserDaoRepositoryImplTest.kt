package com.nielaclag.openweather.data.repository.dao

import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.data.database.dao.LocalUserDao
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.data.mapper.toEntity
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.model.type.AuthenticationType
import com.nielaclag.openweather.domain.repository.dao.LocalUserDaoRepository
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
 * Created by Niel on 11/1/2024.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LocalUserDaoRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK(relaxed = true)
    private lateinit var dao: LocalUserDao
    private lateinit var localUserDaoRepository: LocalUserDaoRepository

    private val testUser = LocalUser(
        id = 1,
        name = "Test User",
        email = "test@gmail.com",
        image = "test.png",
        authenticationType = AuthenticationType.OTHER
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        localUserDaoRepository = LocalUserDaoRepositoryImpl(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertData_should_call_insertData_on_dao() = runTest {
        coEvery {
            dao.insertData(testUser.toEntity())
        }.returns(testUser.id)

        val result = localUserDaoRepository.insertData(testUser.toEntity())

        coVerify {
            dao.insertData(testUser.toEntity())
        }
        assertThat(result).isEqualTo(testUser.id)
    }

    @Test
    fun getData_should_call_getData_on_dao() = runTest {
        coEvery {
            dao.getData()
        }.returns(testUser.toEntity())

        val result = localUserDaoRepository.getData()?.toDomain()

        coVerify {
            dao.getData()
        }
        assertThat(result).isEqualTo(testUser)
    }

    @Test
    fun getDataFlow_should_call_getDataFlow_on_dao() = runTest {
        coEvery {
            dao.getDataFlow()
        }.returns(flowOf(testUser.toEntity()))

        val result = localUserDaoRepository.getDataFlow().first()?.toDomain()

        coVerify {
            dao.getDataFlow()
        }
        assertThat(result).isEqualTo(testUser)
    }

    @Test
    fun updateData_should_call_updateData_on_dao() = runTest {
        localUserDaoRepository.updateData(testUser.toEntity())

        coVerify {
            dao.updateData(testUser.toEntity())
        }
    }

    @Test
    fun deleteAllData_should_call_deleteAllData_on_dao() = runTest {
        localUserDaoRepository.deleteAllData()

        coVerify {
            dao.deleteAllData()
        }
    }

    @Test
    fun setNewData_should_call_setNewData_on_dao() = runTest {
        coEvery {
            dao.setNewData(testUser.toEntity())
        }.returns(testUser.id)

        val result = localUserDaoRepository.setNewData(testUser.toEntity())

        coVerify {
            dao.setNewData(testUser.toEntity())
        }
        assertThat(result).isEqualTo(testUser.id)
    }

}