package com.example.habitmanager.data.user.dao

import com.example.habitmanager.data.user.model.User

interface UserDao {

    fun create(user: User)

    fun update(user: User): Boolean

    fun delete(user: User): Boolean
}