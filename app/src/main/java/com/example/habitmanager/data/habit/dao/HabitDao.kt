package com.example.habitmanager.data.habit.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.habitmanager.data.habit.model.Habit

interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit): Long

    @Update
    suspend fun update(habit: Habit): Int

    @Delete
    suspend fun delete(habit: Habit)

    @Query("DELETE FROM category")
    suspend fun deleteAll()

    @Query("SELECT * FROM habit ORDER BY habitName ASC")
    suspend fun selectAll(): List<Habit>

    @Query("SELECT * FROM habit WHERE habitName=:name")
    suspend fun selectByName(name: String): Habit
}