package com.example.habitmanager.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class DailyResetWorker(val context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {

    override fun doWork(): Result {
        val delay: Long =  (7 * 60 * 60 * 1000) + (30 * 60 * 1000)

        val workManager = WorkManager.getInstance(context)
        val request = PeriodicWorkRequestBuilder<StandardPriorityNotificationWorker>(5, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork("NotificationWorker", ExistingPeriodicWorkPolicy.UPDATE, request)
        return Result.success()
    }
}