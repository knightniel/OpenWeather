package com.nielaclag.openweather.domain.usecase.dao.localuserdao

import com.nielaclag.openweather.data.mapper.toEntity
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.repository.dao.LocalUserDaoRepository
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class SetNewLocalUser @Inject constructor(
    private val repository: LocalUserDaoRepository
) {
    suspend operator fun invoke(localUser: LocalUser): Long {
        return repository.setNewData(localUser.toEntity())
    }
}