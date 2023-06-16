package com.example.habitmanager.data.user.dao

import com.example.habitmanager.data.user.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserDaoImpl: UserDao {
    private lateinit var userDbRef: DatabaseReference

    override fun create(user: User){
        userDbRef = Firebase.database.getReference(user.firebaseUser!!.uid)
    }

    override fun update(user: User): Boolean {
        return false
    }

    override fun delete(user: User): Boolean {
        return false
    }
}