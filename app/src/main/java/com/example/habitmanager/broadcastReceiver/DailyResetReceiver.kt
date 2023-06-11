package com.example.habitmanager.broadcastReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Calendar


class DailyResetReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("DAILYRESET", "RECEIVED")
        initHighPriorityAlarm(context)
    }

    private fun initHighPriorityAlarm(context: Context?){
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 25)
        }

        val notifyIntent = Intent(
            context,
            HighPriorityNotificationReceiver::class.java
        )
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            (1000*10),
            pendingIntent
        )
        Log.i("DAILYRESET", "NOTIFY")
    }

    private fun initStandardPriorityAlarm(context: Context?){
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 30)
        }

        val notifyIntent = Intent(
            context,
            StandarPriorityNotificationReceiver::class.java
        )
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            (1000*60*450),
            pendingIntent
        )
    }
}