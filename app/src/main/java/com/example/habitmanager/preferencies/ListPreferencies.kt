package com.example.habitmanager.preferencies

import android.content.Context
import com.example.habitmanager.HabitManagerApplication

class ListPreferencies() {

    companion object {
        private const val PREFS_NAME = "listPref"
        private const val KEY_LIST = "KEY_LIST"
    }

    fun getOrder(): String? {
            val sharedPreferences = HabitManagerApplication.applicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_LIST, "")
    }

    fun setOrder(value: String) {
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LIST, value)
        editor.commit()
    }
}