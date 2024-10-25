package com.nielaclag.openweather.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Niel on 10/21/2024.
 */
@HiltWorker
class SeedDatabaseWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
//    private val localUserDaoUseCases: LocalUserDaoUseCases
) : CoroutineWorker(appContext, workerParameters) {

    companion object {
        fun seedDatabase(
            context: Context
        ) {
            val workManager = WorkManager.getInstance(context.applicationContext)
            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                .setConstraints(
                    Constraints
                        .Builder()
                        .build()
                )
                .build()
            workManager.enqueue(request)
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
//            localUserDaoUseCases.init()
            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }

}