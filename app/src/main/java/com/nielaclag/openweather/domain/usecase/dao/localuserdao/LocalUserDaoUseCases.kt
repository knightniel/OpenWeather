package com.nielaclag.openweather.domain.usecase.dao.localuserdao

import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
data class LocalUserDaoUseCases @Inject constructor(
    val getLocalUserFlow: GetLocalUserFlow,
    val getLocalUser: GetLocalUser,
    val insertLocalUser: InsertLocalUser,
    val updateLocalUser: UpdateLocalUser,
    val deleteLocalUser: DeleteLocalUser,
    val setNewLocalUser: SetNewLocalUser
)