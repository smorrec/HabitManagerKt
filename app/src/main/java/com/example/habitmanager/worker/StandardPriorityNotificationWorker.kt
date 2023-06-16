package com.example.habitmanager.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanager.preferencies.NotificationPreferencies
import com.example.habitmanagerkt.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent
import java.lang.StringBuilder
import java.util.Calendar

class StandardPriorityNotificationWorker(val context: Context,
                                         workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val habitEventRepository: HabitEventRepository =
            KoinJavaComponent.get(HabitEventRepository::class.java)
        val habitRepository: HabitRepository = KoinJavaComponent.get(HabitRepository::class.java)
        var result = Result.success()

        if (NotificationPreferencies().isActive()) {
            habitEventRepository.prepareDaos()
            habitRepository.prepareDaos()
            runBlocking {
                HabitManagerApplication.scope().launch {
                    val today = CalendarItem(Calendar.getInstance())
                    val events = habitEventRepository.getEvents(today, habitRepository.getList())
                    val eventString = StringBuilder("")
                    for (event in events) {
                        if (!event.isCompleted) {
                            eventString.append(event.habitName)
                            eventString.append(", ")
                        }
                        Log.d("EVENT", event.habitName.toString())
                    }
                    eventString.append(context.getString(R.string.andOthers))
                    val builder: NotificationCompat.Builder =
                        NotificationCompat.Builder(
                            context,
                            HabitManagerApplication.channelId())
                            .setSmallIcon(R.drawable.splashicon)
                            .setContentTitle(context.getString(R.string.eventAvailable))
                            .setStyle(NotificationCompat.BigTextStyle()
                                .bigText(context.getString(
                                    R.string.eventAvailableMsg,
                                    eventString.toString()
                                )))
                            .setContentText(
                                context.getString(
                                    R.string.eventAvailableMsg,
                                    eventString.toString()
                                )
                            )
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
                    notificationManagerCompat.notify(3, builder.build())
                }.join()
            }
        }
        return result
    }
}