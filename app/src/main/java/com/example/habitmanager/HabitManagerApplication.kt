package com.example.habitmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.example.habitmanager.preferencies.ThemePreferencies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HabitManagerApplication : Application() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    init{
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        //startKoin{
            //androidContext(this@HabitManagerApplication)
            //modules(
                //dbModule,
                //repositoryModule
            //)
        //}
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

}