package com.example.habitmanager.preferencies

import android.content.Context
import com.example.habitmanager.HabitManagerApplication

class ThemePreferencies{

    companion object {
        private const val PREFS_NAME = "themePref"
        private const val KEY_THEME = "KEY_THEME"
    }

    fun setState(value: Boolean?) {
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_THEME, value!!)
        editor.commit()
    }

    fun isNightActive(): Boolean{
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_THEME, true)
    }
}