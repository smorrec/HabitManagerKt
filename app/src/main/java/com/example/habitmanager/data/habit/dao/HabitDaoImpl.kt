package com.example.habitmanager.data.habit.dao

import android.content.ContentValues
import android.util.Log
import com.example.habitmanager.data.category.model.Category
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.data.user.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class HabitDaoImpl: HabitDao {
    private lateinit var userDbRef: DatabaseReference
    private lateinit var habitDbRef: DatabaseReference
    private val list: ArrayList<Habit> = ArrayList()
    private val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            list.clear()

            for(snapShot in dataSnapshot.children){
                val habit = snapShot.getValue(Habit::class.java)
                list.add(habit!!)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    override fun prepareDao(){
        userDbRef = Firebase.database.getReference(Firebase.auth.currentUser!!.uid)
        habitDbRef = userDbRef.child("habits")
    }

    override fun insert(habit: Habit): Boolean {
        return if(!list.contains(habit)){
            habitDbRef
                .child(habit.name!!).setValue(habit)
            true
        }else{
            false
        }
    }

    override fun update(habit: Habit): Boolean{
        habitDbRef
            .child(habit.name!!)
            .setValue(habit)
        return true
    }

    override fun delete(habit: Habit) {
        habitDbRef
            .child(habit.name!!)
            .removeValue()
    }

    override fun deleteAll() {
        habitDbRef
            .removeValue()
    }

    override suspend fun selectAll(): List<Habit> {
        val list = ArrayList<Habit>()
        val task = habitDbRef.get()
        task.await()
        task.result.children.forEach{
            list.add(it.getValue(Habit::class.java)!!)
        }
        return list
    }

    override suspend fun selectAllCurrent(): List<Habit> {
        val list = ArrayList<Habit>()
        val task = habitDbRef.get()
        task.await()
        task.result.children.forEach{
            val habit = it.getValue(Habit::class.java)!!
            if(!habit.isFinished)
                list.add(habit)
        }
        return list
    }

    override suspend fun selectAllCompleted(): List<Habit> {
        val list = ArrayList<Habit>()
        val task = habitDbRef.get()
        task.await()
        task.result.children.forEach{
            val habit = it.getValue(Habit::class.java)!!
            if(habit.isFinished)
                list.add(habit)
        }
        return list
    }

    override suspend fun selectByName(name: String): Habit? {
        var habit :Habit? = null
        selectAll().forEach {
            if(it.name == name){
                habit = it
            }
        }
        return habit
    }
}