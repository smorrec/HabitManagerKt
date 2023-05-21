package com.example.habitmanager.data.user.model

import java.util.Objects

class User() {
    var email: String? = null
    var password: String? = null
    var name: String? = null
    var lastName: String? = null
    var profilePicture = 0

    constructor(email: String?, password: String?) : this() {
        this.email = email
        this.password = password
    }

    constructor(
        email: String?,
        password: String?,
        name: String?,
        lastName: String?,
        profilePicture: Int
    ) : this() {
        this.email = email
        this.password = password
        this.name = name
        this.lastName = lastName
        this.profilePicture = profilePicture
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is User) return false
        val user = o
        return email.equals(user.email) && password.equals(user.password)
    }

    override fun hashCode(): Int {
        return Objects.hash(
            email,
            password,
            name,
            lastName,
            profilePicture
        )
    }
}