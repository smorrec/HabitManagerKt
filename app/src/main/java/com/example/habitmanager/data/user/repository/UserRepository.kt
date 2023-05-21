package com.example.habitmanager.data.user.repository

import com.example.habitmanager.data.user.model.User
import com.example.habitmanagerkt.R

class UserRepository(
    var list: ArrayList<User>? = ArrayList()
){

    init {
        initialize()
    }

    fun addUser(user: User) {
        list!!.add(user)
    }

    fun login(email: String?, password: String?): Boolean {
        val user = User(email, password)
        return list!!.contains(user)
    }

    private fun initialize() {
        list = ArrayList()
        list!!.add(User("sergio@sergio.com", "sergio", "Sergio", "Morales", R.drawable.profile))
        list!!.add(User("aaaa@aaaaa.com", "A+a12345678", "aaaaaaaaaaaa", "aaaaaaaaaaaa", 1))
        list!!.add(User("bbbb@bbbbb.com", "bbbbbbbbbbbb", "bbbbbbbbbbbb", "bbbbbbbbbbbb", 2))
        list!!.add(User("cccc@ccccc.com", "cccccccccccc", "cccccccccccc", "cccccccccccc", 3))
        list!!.add(User("dddd@ddddd.com", "dddddddddddd", "dddddddddddd", "dddddddddddd", 4))
        list!!.add(User("eeee@eeeee.com", "eeeeeeeeeeee", "eeeeeeeeeeee", "eeeeeeeeeeee", 5))
        list!!.add(User("ffff@fffff.com", "ffffffffffff", "ffffffffffff", "ffffffffffff", 6))
        list!!.add(User("gggg@ggggg.com", "gggggggggggg", "gggggggggggg", "gggggggggggg", 7))
        list!!.add(User("hhhh@hhhhh.com", "hhhhhhhhhhhh", "hhhhhhhhhhhh", "hhhhhhhhhhhh", 8))
        list!!.add(User("iiii@iiiii.com", "iiiiiiiiiiii", "iiiiiiiiiiii", "iiiiiiiiiiii", 9))
        list!!.add(User("jjjj@jjjjj.com", "jjjjjjjjjjjj", "jjjjjjjjjjjj", "jjjjjjjjjjjj", 10))
        list!!.add(User("kkkk@kkkkk.com", "kkkkkkkkkkkk", "kkkkkkkkkkkk", "kkkkkkkkkkkk", 11))
        list!!.add(User("llll@lllll.com", "llllllllllll", "llllllllllll", "llllllllllll", 12))
        list!!.add(User("mmmm@mmmmm.com", "mmmmmmmmmmmm", "mmmmmmmmmmmm", "mmmmmmmmmmmm", 13))
    }

    fun getUser(email: String): User? {
        for (user in list!!) {
            if (user.email == email) {
                return user
            }
        }
        return null
    }
}