package com.nielaclag.openweather.data.repository.dao

import com.google.common.truth.Truth.assertThat
import com.nielaclag.openweather.data.database.dao.LocationInfoDao
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.data.mapper.toEntity
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.repository.dao.LocationInfoDaoRepository
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
class LocationInfoDaoRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK(relaxed = true)
    private lateinit var dao: LocationInfoDao
    private lateinit var locationInfoDaoRepository: LocationInfoDaoRepository

    private val testLocationInfo = LocationInfo(
        name = "London",
        latitude = 51.5073219,
        longitude = -0.1276474,
        country = "GB",
        state = "England"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        locationInfoDaoRepository = LocationInfoDaoRepositoryImpl(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertData_should_call_insertData_on_dao() = runTest {
        coEvery { dao.insertData(testLocationInfo.toEntity()) }.returns(Unit)

        locationInfoDaoRepository.insertData(testLocationInfo.toEntity())

        coVerify {
            dao.insertData(testLocationInfo.toEntity())
        }
    }

    @Test
    fun getData_should_call_getData_on_dao() = runTest {
        coEvery {
            dao.getData()
        }.returns(testLocationInfo.toEntity())

        val result = locationInfoDaoRepository.getData()?.toDomain()

        coVerify {
            dao.getData()
        }
        assertThat(result).isEqualTo(testLocationInfo)
    }

    @Test
    fun getDataFlow_should_call_getDataFlow_on_dao() = runTest {
        coEvery {
            dao.getDataFlow()
        }.returns(flowOf(testLocationInfo.toEntity()))

        val result = locationInfoDaoRepository.getDataFlow().first()?.toDomain()

        coVerify {
            dao.getDataFlow()
        }
        assertThat(result).isEqualTo(testLocationInfo)
    }

    @Test
    fun updateData_should_call_updateData_on_dao() = runTest {
        locationInfoDaoRepository.updateData(testLocationInfo.toEntity())

        coVerify {
            dao.updateData(testLocationInfo.toEntity())
        }
    }

    @Test
    fun deleteAllData_should_call_deleteAllData_on_dao() = runTest {
        locationInfoDaoRepository.deleteAllData()

        coVerify {
            dao.deleteAllData()
        }
    }

    @Test
    fun setNewData_should_call_setNewData_on_dao() = runTest {
        locationInfoDaoRepository.setNewData(testLocationInfo.toEntity())

        coVerify {
            dao.setNewData(testLocationInfo.toEntity())
        }
    }
    
}