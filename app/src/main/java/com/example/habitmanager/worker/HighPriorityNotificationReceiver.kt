package com.example.habitmanager.broadcastReceiver

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class HighPriorityNotificationReceiver(context: Context,
                                       workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }

}