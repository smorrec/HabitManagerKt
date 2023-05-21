package com.example.habitmanager.data.event.repository

import com.example.habitmanager.data.base.BaseRepositpory
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.calendar.repository.CalendarRepository
import com.example.habitmanager.data.event.dao.HabitEventDao
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.data.task.model.HabitEvent
import org.koin.java.KoinJavaComponent.get

class HabitEventRepository(
    private val habitEventDao: HabitEventDao
    ): BaseRepositpory() {
    private val calendarRepository: CalendarRepository = get(CalendarRepository::class.java)

    suspend fun getList(): ArrayList<HabitEvent?> {
        return habitEventDao.selectAll() as ArrayList<HabitEvent?>
    }

    suspend fun addEvent(habit: Habit) {
        val calendarObjects: ArrayList<CalendarItem> = calendarRepository.getList()
        for (calendarObject in calendarObjects) {
            if (habit.hasTask(calendarObject)) {
                habitEventDao.insert(
                    HabitEvent(
                        habit,
                        calendarObject
                    )
                )
            }
        }
    }

    suspend fun deleteEvent(habit: Habit) {
        habitEventDao.deleteWhereHabit(habit.name)
    }

    suspend fun undo(value: Habit) {
        addEvent(value)
    }

    suspend fun update(event: HabitEvent?) {
        habitEventDao.update(event)
    }
}