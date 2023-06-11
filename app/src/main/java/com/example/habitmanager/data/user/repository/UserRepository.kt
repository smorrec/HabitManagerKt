package com.example.habitmanager.data.user.repository

import android.net.Uri
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.user.dao.UserDao
import com.example.habitmanager.data.user.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class UserRepository(private val userDao: UserDao) {
    private var user: User = User()
    private val _userLogged = MutableStateFlow(false)
    val userLogged = _userLogged.asStateFlow()
    init{
        FirebaseAuth.getInstance().currentUser?.let{
            setFirebaseUser(it)
        }
    }

    fun getUser(): User {
        return user
    }

    fun prepareUser(name:String){
        setFirebaseUser(FirebaseAuth.getInstance().currentUser!!)

        val pis = HabitManagerApplication.applicationContext().assets.open("usuario.png")
        val picture: File = createTempFile()

        pis.use { input ->
            picture.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        user.firebaseUser!!.updateProfile(
            userProfileChangeRequest {
                displayName = name
                photoUri = Uri.fromFile(picture)
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _userLogged.update { true }
            }
        }

    }

    fun setFirebaseUser(firebaseUser: FirebaseUser){
        user.firebaseUser = firebaseUser
        _userLogged.update {
            true
        }
    }

    fun getDisplayName(): CharSequence? {
        return user.firebaseUser!!.displayName
    }

    fun getEmail(): CharSequence? {
        return user.firebaseUser!!.email
    }

    fun getProfilePicture(): Uri? {
        return  user.firebaseUser!!.photoUrl
    }

    fun updatePicture(uri: Uri) {
        user.firebaseUser!!.updateProfile(
            userProfileChangeRequest {
                photoUri = uri
            })
    }

    fun logOut() {
        user.firebaseUser = null
        _userLogged.update { false }
    }

    fun updatePassword(newValue: String) {
        user.firebaseUser!!.updatePassword(newValue)
    }

    fun updateEmail(newValue: String) {
        user.firebaseUser!!.updateEmail(newValue).addOnCompleteListener {
            _userLogged.update { true }
        }
    }

    fun consumeFlow() {
        _userLogged.update {
            false
        }
    }
}