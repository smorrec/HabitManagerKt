package com.example.habitmanager.broadcastReceiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.event.model.HabitEvent
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanagerkt.R
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.util.Calendar
import java.util.Date

class HighPriorityNotificationReceiver: BroadcastReceiver() {
    private val habitRepository: HabitRepository = get(HabitRepository::class.java)
    private val habitEventRepository: HabitEventRepository = get(HabitEventRepository::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("NOTI", "RECEIVED")
        val calendar = Calendar.getInstance()
        val curdate = Date()
        calendar.time = curdate
        val today = CalendarItem(calendar)

        HabitManagerApplication.scope().launch {
            for(event in habitEventRepository.getEvents(today, habitRepository.getList())){
                sendNotification(context!!, event)
                Log.i("NOTI", "SEND")
            }
        }
    }

    private fun sendNotification(context: Context, event: HabitEvent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, HabitManagerApplication.channelId())
                    .setSmallIcon(R.drawable.splashicon)
                    .setContentTitle(context.getString(R.string.addHabitNotTitle))
                    .setContentText(context.getString(R.string.addHabitNotTxt, event.habitName))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)

            val notificationManagerCompat: NotificationManagerCompat =
                NotificationManagerCompat.from(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            }
            notificationManagerCompat.notify(0, builder.build())
        }
    }
}