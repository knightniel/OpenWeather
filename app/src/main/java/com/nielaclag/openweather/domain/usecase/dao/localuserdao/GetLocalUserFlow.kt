package com.nielaclag.openweather.domain.usecase.dao.localuserdao

import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.domain.model.LocalUser
import com.nielaclag.openweather.domain.repository.dao.LocalUserDaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class GetLocalUserFlow @Inject constructor(
    private val repository: LocalUserDaoRepository
) {
    operator fun invoke(): Flow<LocalUser?> {
        return repository.getDataFlow().map { it?.toDomain() }
    }
}