package com.example.habitmanager.data.habit.repository

import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.event.model.HabitEvent
import com.example.habitmanager.data.habit.dao.HabitDao
import com.example.habitmanager.data.habit.model.Habit
import kotlinx.coroutines.launch
import java.lang.Exception

class HabitRepository(
    private val habitDao: HabitDao
) {

    suspend fun getList(): ArrayList<Habit> {
        return habitDao.selectAll() as ArrayList<Habit>
    }

    suspend fun getListCompleted(): ArrayList<Habit> {
        return habitDao.selectAllCompleted() as ArrayList<Habit>
    }

    suspend fun getListCurrent(): ArrayList<Habit> {
        return habitDao.selectAllCurrent() as ArrayList<Habit>
    }

    fun addHabit(habit: Habit, category: Int): Boolean {
            habit.categoryId = category
            return habitDao.insert(habit)
    }

    fun editHabit(habit: Habit, category: Int): Boolean {
        return try {
            habit.categoryId = category
            habitDao.update(habit)
        } catch (e: Exception) {
            false
        }
    }

    fun deleteHabit(habit: Habit?) {
        habitDao.delete(habit!!)
    }

    fun undo(habit: Habit?) {
        habitDao.insert(habit!!)
    }

    suspend fun selectByName(name: String?): Habit? {
        return habitDao.selectByName(name!!)
    }

    fun modifiedCompletedDays(event: HabitEvent) {
        HabitManagerApplication.scope().launch {
            val habit = selectByName(event.habitName)!!
            if(event.isCompleted)
                habit.completedDaysCount++
            else
                habit.completedDaysCount--

            editHabit(habit, habit.categoryId!!)
        }
    }

    fun prepareDaos() {
        habitDao.prepareDao()
    }
}