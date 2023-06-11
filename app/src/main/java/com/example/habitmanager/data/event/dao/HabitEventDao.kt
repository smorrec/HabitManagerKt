package com.example.habitmanager.data.event.dao

import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.event.model.HabitEvent
import com.example.habitmanager.data.habit.model.Habit

interface HabitEventDao {
    fun prepareDao()

    fun insert(habitEvent: HabitEvent)

    suspend fun getEvent(caledarItem: CalendarItem, habit: Habit): HabitEvent

    fun update(habitEvent: HabitEvent)

    fun delete(habitEvent: HabitEvent)

    fun deleteAll()

    fun deleteWhereHabit(habitname: String)

    fun selectAll(): List<HabitEvent?>?
}