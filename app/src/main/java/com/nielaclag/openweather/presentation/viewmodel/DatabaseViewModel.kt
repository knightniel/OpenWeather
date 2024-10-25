package com.nielaclag.openweather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nielaclag.openweather.domain.viewmodel.DatabaseViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val scope: DatabaseViewModelScope
) : ViewModel() {

    val showSplash = scope.showSplash

    val localUser = scope.localUser
    val locationInfo = scope.locationInfo

    fun signOut() {
        viewModelScope.launch {
            scope.signOut()
        }
    }

}