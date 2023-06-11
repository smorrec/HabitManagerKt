package com.example.habitmanager.data.event.repository

import com.example.habitmanager.data.base.BaseRepositpory
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.calendar.repository.CalendarRepository
import com.example.habitmanager.data.event.dao.HabitEventDao
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.data.event.model.HabitEvent
import org.koin.java.KoinJavaComponent.get

class HabitEventRepository(
    private val habitEventDao: HabitEventDao
    ): BaseRepositpory() {
    private val calendarRepository: CalendarRepository = get(CalendarRepository::class.java)

    fun deleteEvent(habit: Habit) {
        habitEventDao.deleteWhereHabit(habit.name!!)
    }

    fun update(event: HabitEvent) {
        habitEventDao.update(event)
    }

    suspend fun getEvents(selectedCalendar: CalendarItem, list: ArrayList<Habit>): ArrayList<HabitEvent> {
        val events = ArrayList<HabitEvent>()
            for(habit in list){
                if(habit.hasTask(selectedCalendar)) {
                    if (selectedCalendar.isCurrentDay())
                        events.add(habitEventDao.getEvent(selectedCalendar, habit))
                    else
                        events.add(HabitEvent(habit, selectedCalendar))
                }
            }
        return events
    }
}