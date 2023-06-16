package com.example.habitmanager.preferencies

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.habitmanagerkt.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        initPreferenciesNotification()
        initPreferenciesTheme()
        initPreferenciesList()
        //initPreferenciesLanguage()
    }

/*
    private fun initPreferenciesLanguage() {
        val listPreference =
            preferenceManager.findPreference<Preference>(getString(R.string.key_language))
        listPreference!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
                LanguagePreferencies().setLanguage(
                    (newValue as String?)!!
                )
                true
            }
    }
 */

    private fun initPreferenciesList() {
        val listPreference =
            preferenceManager.findPreference<Preference>(getString(R.string.key_list))
        listPreference!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
                ListPreferencies().setOrder((newValue as String?)!!)
                true
            }
    }

    private fun initPreferenciesTheme() {
        val switchPreferenceCompat =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.key_night))
        switchPreferenceCompat!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any ->
                ThemePreferencies().setState(newValue as Boolean)
                if (newValue) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                true
            }
    }

    private fun initPreferenciesNotification() {
        val switchPreferenceCompat =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.key_activate_notification))
        switchPreferenceCompat!!.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
                NotificationPreferencies().setState(newValue as Boolean?)
                true
            }
    }
}