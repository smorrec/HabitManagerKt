package com.example.habitmanager.ui.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class LoginViewModel: ViewModel() {
    private val _loginResult = MutableStateFlow(LoginResult.STARTED)
    val loginResult = _loginResult.asStateFlow()

    val _email = MutableStateFlow("qwerty@qwerty.com")
    val email = _email.asStateFlow()

    val _password = MutableStateFlow("qwerty")
    val password = _password.asStateFlow()

    private var auth: FirebaseAuth = Firebase.auth

    fun login(){
        auth.signInWithEmailAndPassword(
            email.value,
            password.value
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                _loginResult.update { LoginResult.SUCCESS }
            } else {
                _loginResult.update { LoginResult.FAILURE }
            }
        }
    }
}