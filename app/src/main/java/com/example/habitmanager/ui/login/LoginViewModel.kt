package com.example.habitmanager.ui.login

import androidx.lifecycle.ViewModel
import com.example.habitmanager.data.user.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.java.KoinJavaComponent


class LoginViewModel: ViewModel() {
    private val userRepository: UserRepository = KoinJavaComponent.get(UserRepository::class.java)

    private val _loginResult = MutableStateFlow(LoginResult.STARTED)
    val loginResult = _loginResult.asStateFlow()

    val _email = MutableStateFlow("qwerty@qwerty.com")
    val email = _email.asStateFlow()
    var emailErr: Boolean = false

    val _password = MutableStateFlow("qwerty")
    val password = _password.asStateFlow()
    var passwordErr: Boolean = false

    private var auth: FirebaseAuth = Firebase.auth

    private fun noErrorActive(): Boolean{
        return !emailErr && !passwordErr
    }

    fun login(){
        if(noErrorActive()) {
            auth.signInWithEmailAndPassword(
                email.value,
                password.value
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    userRepository.setFirebaseUser(Firebase.auth.currentUser!!)
                    _loginResult.update { LoginResult.SUCCESS }
                } else {
                    _loginResult.update { LoginResult.FAILURE }
                }
            }
        }
    }

    fun consumeLoginFlow(){
        _loginResult.update { LoginResult.STARTED }
    }

    fun resetPass(){
        if(!emailErr) {
            userRepository.resetPass(email.value)
        }
    }
}