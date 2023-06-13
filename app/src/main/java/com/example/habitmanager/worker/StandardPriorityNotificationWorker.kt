package com.example.habitmanager.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanagerkt.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent
import java.util.Calendar

class StandardPriorityNotificationWorker(val context: Context,
                                         workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val habitEventRepository: HabitEventRepository =
            KoinJavaComponent.get(HabitEventRepository::class.java)
        val habitRepository: HabitRepository = KoinJavaComponent.get(HabitRepository::class.java)
        var result = Result.success()
        val GROUP = "eventNotificationGroup"

        habitEventRepository.prepareDaos()
        habitRepository.prepareDaos()
        runBlocking {
            HabitManagerApplication.scope().launch {
                val today = CalendarItem(Calendar.getInstance())
                val events = habitEventRepository.getEvents(today, habitRepository.getList())
                for((id, event) in events.withIndex()){
                    if(!event.isCompleted) {
                        val builder: NotificationCompat.Builder =
                            NotificationCompat.Builder(context, HabitManagerApplication.channelId())
                                .setSmallIcon(R.drawable.splashicon)
                                .setContentTitle(context.getString(R.string.eventAvailable))
                                .setContentText(
                                    context.getString(
                                        R.string.eventAvailableMsg,
                                        event.habitName
                                    )
                                )
                                .setGroup(GROUP)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true)
                        val notificationManagerCompat: NotificationManagerCompat =
                            NotificationManagerCompat.from(context)
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            result = Result.failure()
                        }
                        notificationManagerCompat.notify(id, builder.build())
                    }
                }

            }.join()
        }
        return result
    }
}