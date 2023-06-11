package com.example.habitmanager

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.habitmanager.broadcastReceiver.DailyResetReceiver
import com.example.habitmanager.broadcastReceiver.HighPriorityNotificationReceiver
import com.example.habitmanager.di.dbModule
import com.example.habitmanager.di.repositoryModule
import com.example.habitmanager.preferencies.ThemePreferencies
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import java.util.Calendar


class HabitManagerApplication : Application() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    init{
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        Firebase.database.setPersistenceEnabled(true)

        startKoin{
            androidContext(this@HabitManagerApplication)
            modules(
                dbModule,
                repositoryModule
            )
        }
        createNotificationChannel()
        initTheme()
        initAlarm()
    }

    companion object{
        private var instance: HabitManagerApplication? = null
        private val CHANNEL_ID = "notification"

        fun applicationContext(): Context{
            return instance!!.applicationContext
        }

        fun scope(): CoroutineScope {
            return instance!!.scope
        }

        fun channelId(): String{
            return CHANNEL_ID
        }
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CreateHabitCh"
            val descirption = "Ha creado un hábito"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = descirption
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initTheme(){
        val themePref = ThemePreferencies()
        if(themePref.isNightActive()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun initAlarm(){
        val broadcastReceiver = DailyResetReceiver()
        val intentFilter = IntentFilter()
        registerReceiver(broadcastReceiver, intentFilter)

        Log.i("APP", "Called")
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis() + 10*1000
        }

        val notifyIntent = Intent(
            applicationContext,
            DailyResetReceiver::class.java
        )
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            2,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = applicationContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            (1000*60*60*24),
            pendingIntent
        )

        Log.i("APP", "Ended")
    }

}