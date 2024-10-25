package com.nielaclag.openweather.domain.usecase.dao.localuserdao

import com.nielaclag.openweather.domain.repository.dao.LocalUserDaoRepository
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class DeleteLocalUser @Inject constructor(
    private val repository: LocalUserDaoRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllData()
    }
}