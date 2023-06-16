package com.example.habitmanager.data.user.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.user.dao.UserDao
import com.example.habitmanager.data.user.model.User
import com.example.habitmanagerkt.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception

class UserRepository {
    private var user: User = User()
    private val _userLogged = MutableStateFlow(false)
    val userLogged = _userLogged.asStateFlow()

    private val _userPrepared = MutableStateFlow(false)
    val userPrepared = _userPrepared.asStateFlow()

    private val storageRef = Firebase.storage.reference
    private var imageRef: StorageReference? = storageRef.child("images")
    var profilePictureRef: StorageReference? = null

    init{
        FirebaseAuth.getInstance().currentUser?.let{
            setFirebaseUser(it)
            profilePictureRef = storageRef.child(user.firebaseUser!!.uid)
            profilePictureRef
        }
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

        val uri = Uri.fromFile(picture)
        val bitMap = MediaStore.Images.Media.getBitmap(HabitManagerApplication.applicationContext().contentResolver, uri)



        user.firebaseUser!!.updateProfile(
            userProfileChangeRequest {
                displayName = name
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updatePicture(bitMap)
                _userPrepared.update { true }
            }
        }

    }

    fun setFirebaseUser(firebaseUser: FirebaseUser){
        user.firebaseUser = firebaseUser
        profilePictureRef = storageRef.child(user.firebaseUser!!.uid)
        _userLogged.update {
            true
        }
    }

    fun getDisplayName(): CharSequence? {
        return user.firebaseUser?.displayName
    }

    fun getEmail(): CharSequence? {
        return user.firebaseUser?.email
    }

    suspend fun getProfilePicture(): ByteArray? {
        return try{
            var imageBytes: ByteArray?
            val ONE_MEGABYTE: Long = 1024 * 1024 * 5

            imageBytes = profilePictureRef!!.getBytes(ONE_MEGABYTE).await()

            imageBytes!!
        }catch (e:Exception){
            null
        }

    }

    fun updatePicture(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        profilePictureRef!!.putBytes(data).addOnCompleteListener{
            _userPrepared.update { true }
        }
        /*
        uploadTask.addOnSuccessListener {
            profilePictureRef!!.downloadUrl.addOnCompleteListener { task ->
                user.firebaseUser!!.updateProfile(
                    userProfileChangeRequest {
                        photoUri = task.result
                    })
            }
        }
         */
    }

    fun logOut() {
        user.firebaseUser = null
        _userLogged.update { false }
        Firebase.auth.signOut()
    }

    fun updatePassword(newValue: String) {
        user.firebaseUser!!.updatePassword(newValue)
    }

    fun updateEmail(newValue: String) {
        user.firebaseUser!!.updateEmail(newValue).addOnCompleteListener {
            if(it.isSuccessful) {
                user.firebaseUser!!.reload()
                _userPrepared.update { true }
            }
        }
    }

    fun consumeFlowPrepared() {
        _userPrepared.update { false }
    }

    fun resetPass(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(HabitManagerApplication.applicationContext(),
                        HabitManagerApplication.applicationContext().getText(R.string.emailSend),
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    fun updateDisplayName(newValue: String) {
        user.firebaseUser!!.updateProfile(
            userProfileChangeRequest{
                displayName = newValue
            }
        ).addOnCompleteListener {
            if(it.isSuccessful) {
                user.firebaseUser!!.reload()
                _userPrepared.update { true }
            }
        }
    }
}