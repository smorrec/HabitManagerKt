package com.example.habitmanager.data.event.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.habitmanager.data.task.model.HabitEvent

@Dao
interface HabitEventDao {
    @Insert
    suspend fun insert(habitEvent: HabitEvent?): Long

    @Update
    suspend fun update(habitEvent: HabitEvent?)

    @Delete
    suspend fun delete(habitEvent: HabitEvent?)

    @Query("DELETE FROM habitevent")
    suspend fun deleteAll()

    @Query("DELETE FROM habitevent WHERE habit_Name=:habitname")
    suspend fun deleteWhereHabit(habitname: String?)

    @Query("SELECT * FROM habitevent ORDER BY idTask ASC")
    suspend fun selectAll(): List<HabitEvent?>?

    @Query("SELECT * FROM habitevent WHERE idTask=:id")
    suspend fun selectById(id: Int): HabitEvent?
}