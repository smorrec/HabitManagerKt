package com.example.habitmanager.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habitmanager.data.user.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel: ViewModel() {
    val loginResult: MutableLiveData<LoginResult> = MutableLiveData()
    private var auth: FirebaseAuth = Firebase.auth

    fun signUp(user: User){
        auth.createUserWithEmailAndPassword(
            user.email!!,
            user.password!!)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loginResult.postValue(LoginResult.SUCCESS)
                } else {
                    loginResult.postValue(LoginResult.FAILURE)
                }
            }
    }


    fun login(user: User){
        auth.signInWithEmailAndPassword(
            user.email!!,
            user.password!!
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                loginResult.postValue(LoginResult.SUCCESS)
            } else {
                loginResult.postValue(LoginResult.FAILURE)
            }
        }
    }
}