package com.example.habitmanager.preferencies

import android.content.Context
import com.example.habitmanager.HabitManagerApplication

class NotificationPreferencies{

    companion object {
        private const val PREFS_NAME = "notificationPref"
        private const val KEY_NOTIFICATION = "KEY_MOTIFICATION"
    }

    fun setState(value: Boolean?) {
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_NOTIFICATION, value!!)
        editor.commit()
    }

    fun isActive(): Boolean{
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_NOTIFICATION, true)
    }
}