package com.example.habitmanager.data.habit.repository

import com.example.habitmanager.data.habit.dao.HabitDao
import com.example.habitmanager.data.habit.model.Habit
import java.lang.Exception

class HabitRepository(
    private val habitDao: HabitDao
) {

    suspend fun getList(): ArrayList<Habit> {
             return habitDao.selectAll() as ArrayList<Habit>
    }

    suspend fun addHabit(habit: Habit, category: Int): Boolean {
        return try{
            habit.categoryId = category
            habitDao.insert(habit) > 0
        }catch (e: Exception){
            false
        }
    }

    suspend fun editHabit(habit: Habit, category: Int): Boolean {
        return try {
            habit.categoryId = category
            habitDao.update(habit)> 0
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteHabit(habit: Habit?) {
        habitDao.delete(habit!!)
    }

    suspend fun undo(habit: Habit?) {
        habitDao.insert(habit!!)
    }

    suspend fun selectByName(name: String?): Habit {
        return habitDao.selectByName(name!!)
    }
}