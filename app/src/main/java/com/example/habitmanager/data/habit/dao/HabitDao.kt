package com.example.habitmanager.data.habit.dao

import com.example.habitmanager.data.habit.model.Habit

interface HabitDao {
    fun prepareDao()

    fun insert(habit: Habit): Boolean

    fun update(habit: Habit): Boolean

    fun delete(habit: Habit)

    fun deleteAll()

    suspend fun selectAll(): List<Habit>

    suspend fun selectByName(name: String): Habit?

}