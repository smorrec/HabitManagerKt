package com.example.habitmanager.ui.calendar

import androidx.lifecycle.ViewModel
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.event.dao.HabitEventDao
import com.example.habitmanager.data.event.model.HabitEvent
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.dao.HabitDao
import com.example.habitmanager.data.habit.repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.java.KoinJavaComponent.get
import java.util.ArrayList

class MainViewModel: ViewModel() {
    private val habitEventRepository: HabitEventRepository = get(HabitEventRepository::class.java)
    private val habitRepository: HabitRepository = get(HabitRepository::class.java)

    private val _emptyList = MutableStateFlow(false)
    val emptyList = _emptyList.asStateFlow()

    fun preareDaos() {
        val habitDao: HabitDao = get(HabitDao::class.java)
        habitDao.prepareDao()
        val habitEventDao: HabitEventDao = get(HabitEventDao::class.java)
        habitEventDao.prepareDao()
    }

    suspend fun getEvents(selectedCalendar: CalendarItem): ArrayList<HabitEvent> {
        val list = habitEventRepository.getEvents(selectedCalendar, habitRepository.getList())
        if(list.isEmpty())
            _emptyList.update { true }
        else
            _emptyList.update { false }

        return list

    }

    suspend fun getEventsCompleted(selectedCalendar: CalendarItem): ArrayList<HabitEvent> {

        val list = habitEventRepository.getEvents(selectedCalendar, habitRepository.getListCompleted())
        if(list.isEmpty())
            _emptyList.update { true }
        else
            _emptyList.update { false }

        return list
    }

    suspend fun getEventsCurrent(selectedCalendar: CalendarItem): ArrayList<HabitEvent> {
        val list = habitEventRepository.getEvents(selectedCalendar, habitRepository.getListCurrent())
        if(list.isEmpty())
            _emptyList.update { true }
        else
            _emptyList.update { false }

        return list
    }
}