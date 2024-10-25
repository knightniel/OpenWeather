package com.nielaclag.openweather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.usecase.dao.locationinfodao.LocationInfoDaoUseCases
import com.nielaclag.openweather.domain.usecase.remote.auth.AuthUseCases
import com.nielaclag.openweather.domain.viewmodel.DatabaseViewModelScope
import com.nielaclag.openweather.presentation.model.page.BasePage
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
@HiltViewModel
class ParentScreenViewModel @Inject constructor(
    private val locationInfoDaoUseCases: LocationInfoDaoUseCases,
    private val databaseViewModelScope: DatabaseViewModelScope,
    private val authUseCases: AuthUseCases,
    val moshi: Moshi
) : ViewModel() {

    private val _signOut = MutableSharedFlow<Resource<Any>?>()
    val signOut = _signOut.asSharedFlow()

    fun getScreen(): BasePage {
        return runBlocking(Dispatchers.IO) {
            val localUser = databaseViewModelScope.localUser.value
            if (localUser == null) {
                BasePage.UserAuthentication
            } else {
                BasePage.Main
            }
        }
    }

    fun setUserLocation(locationInfo: LocationInfo) {
        viewModelScope.launch {
            locationInfoDaoUseCases.setNewLocationInfo(locationInfo)
        }
    }

    fun signOut() {
        authUseCases
            .userSignOut()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _signOut.emit(result)
                    }
                    is Resource.Error -> {
                        _signOut.emit(result)
                    }
                    is Resource.Success -> {
                        databaseViewModelScope.signOut()
                        _signOut.emit(result)
                    }
                }
            }.launchIn(viewModelScope)
    }

    init {
//        val firebaseDatabase = Firebase.auth
//        val userTableRef = firebaseDatabase.getReference("users")
    }

}