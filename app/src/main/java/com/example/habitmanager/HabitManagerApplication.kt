package com.example.habitmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
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
            val name = "HabitManagerCh"
            val description = "HabitManager Notification Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
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


}