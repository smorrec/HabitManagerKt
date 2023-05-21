package com.example.habitmanager.preferencies

import android.content.Context
import com.example.habitmanager.HabitManagerApplication

/**
 * Esta clase se encrga de leer o escribir cualquier información del usuario como preferencia del
 * sistema. Cualquier Fragment/Activity qeu necesite información del usuario utilizará una instancia
 * de esta clase.
 */
class UserPrefManager() {

    companion object {
        private const val PREFS_NAME = "userpref"
        private const val KEY_EMAIL = "email"
    }

    fun login(email: String?) {
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, email)
        editor.commit()
    }

    fun isUserLogged(): Boolean{
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return !sharedPreferences.getString(KEY_EMAIL, "")!!.isEmpty()
    }

    fun logOut() {
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_EMAIL)
        editor.commit()
    }

    fun getUserEmail(): String? {
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_EMAIL, "")
    }

    fun changeEmail(email: String?) {
        val sharedPreferences = HabitManagerApplication.applicationContext()
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, email)
        editor.commit()
    }
}