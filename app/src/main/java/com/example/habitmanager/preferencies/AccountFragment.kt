package com.example.habitmanager.preferencies

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.habitmanagerkt.R
import java.io.IOException
import java.security.GeneralSecurityException

class AccountFragment : PreferenceFragmentCompat() {
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        private const val FILE_NAME = "encriptation_shared_prefs"
        private const val KEY_PASSWORD = "key_password"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey)
        initPreferenciasEmail()
        try {
            initEcnriptacion()
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        initPreferenciasPassword()
    }

    private fun initPreferenciasPassword() {
        val edPassword: EditTextPreference =
            preferenceManager.findPreference(getString(R.string.key_password))!!
        edPassword.setOnBindEditTextListener { editText: EditText ->
            edPassword.text = sharedPreferences!!.getString(
                KEY_PASSWORD,
                ""
            )
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.selectAll()
        }
        edPassword.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
                sharedPreferences!!.edit()
                    .putString(KEY_PASSWORD, newValue as String?)
                    .commit()
                true
            }
    }

    @Throws(GeneralSecurityException::class, IOException::class)
    private fun initEcnriptacion() {
        val mainKey = MasterKey.Builder(requireContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        sharedPreferences = EncryptedSharedPreferences
            .create(
                requireContext(),
                FILE_NAME,
                mainKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }

    private fun initPreferenciasEmail() {
        val edEmail: EditTextPreference =
            preferenceManager.findPreference(getString(R.string.key_email))!!
        edEmail.setOnBindEditTextListener { editText: EditText? ->
            edEmail.text = UserPrefManager().getUserEmail()
        }
        edEmail.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
                UserPrefManager().changeEmail(newValue as String?)
                true
            }
    }
}