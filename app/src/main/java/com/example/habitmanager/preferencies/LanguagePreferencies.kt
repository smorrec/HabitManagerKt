package com.example.habitmanager.preferencies

import android.content.Context
import com.example.habitmanager.HabitManagerApplication

class LanguagePreferencies {

    companion object {
        private const val PREFS_NAME = "languagePref"
        private const val KEY_LANGUAGE = "KEY_LANGUAGE"
    }

    fun getLanguage(): String? {
            val sharedPreferences = HabitManagerApplication.applicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_LANGUAGE, "es")
        }

    fun setLanguage(value: String) {
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LANGUAGE, value)
        editor.commit()
    }
}