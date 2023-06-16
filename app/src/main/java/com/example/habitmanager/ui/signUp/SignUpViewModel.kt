package com.example.habitmanager.ui.signUp

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel: ViewModel() {
    private val _signUpResult = MutableStateFlow(SignUpResult.STARTED)
    val signUpResult = _signUpResult.asStateFlow()

    private var auth: FirebaseAuth = Firebase.auth
    val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    var emailErr: Boolean = false

    val _displayName = MutableStateFlow("")
    val displayName = _displayName.asStateFlow()
    var displayNameErr: Boolean = false

    val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    var passwordErr: Boolean = false

    val _repeatedPass = MutableStateFlow("")
    val repeatedPass = _repeatedPass.asStateFlow()
    var repeatedPassErr: Boolean = false

    private fun noErrorActive(): Boolean{
        return !emailErr && !passwordErr && !repeatedPassErr && !displayNameErr
    }

    fun signUp(){
        if(noErrorActive()) {
            auth.createUserWithEmailAndPassword(
                email.value,
                password.value
            ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _signUpResult.update { SignUpResult.SUCCESS }
                    } else {
                        _signUpResult.update { SignUpResult.FAILURE }
                    }
                }
        }
    }

}