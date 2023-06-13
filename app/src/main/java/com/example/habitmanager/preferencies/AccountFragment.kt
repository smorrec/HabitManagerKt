package com.example.habitmanager.preferencies

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.habitmanager.data.user.repository.UserRepository
import com.example.habitmanagerkt.R
import org.koin.java.KoinJavaComponent.get
import java.io.IOException
import java.security.GeneralSecurityException

class AccountFragment : PreferenceFragmentCompat() {
    private var sharedPreferences: SharedPreferences? = null
    private val userRepository: UserRepository = get(UserRepository::class.java)

    companion object {
        private const val FILE_NAME = "encriptation_shared_prefs"
        private const val KEY_PASSWORD = "key_password"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey)
        initPreferenciasEmail()
        initPreferenciasPassword()
    }

    private fun initPreferenciasPassword() {
        val edPassword: EditTextPreference =
            preferenceManager.findPreference(getString(R.string.key_password))!!
        edPassword.setOnBindEditTextListener { editText: EditText ->
            edPassword.text = ""
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.selectAll()
        }
        edPassword.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any? ->
                Log.d("AAAAAAAAAAAAAA", "CALED")
                userRepository.updatePassword(newValue as String)
                true
            }
    }

    private fun initPreferenciasEmail() {
        val edEmail: EditTextPreference =
            preferenceManager.findPreference(getString(R.string.key_email))!!
        edEmail.setOnBindEditTextListener {
            edEmail.text = userRepository.getEmail() as String?
        }
        edEmail.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
                Log.d("AAAAAAAAAAAAAA", "CALED" + newValue as String)
                userRepository.updateEmail(newValue as String)
                true
            }
    }
}