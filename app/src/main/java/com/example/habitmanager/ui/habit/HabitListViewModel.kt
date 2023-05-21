package com.example.habitmanager.ui.habit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.data.habit.repository.HabitRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class HabitListViewModel : ViewModel() {
    val liveDataList: MutableLiveData<ArrayList<Habit>> = MutableLiveData()
    val deletedHabit: MutableLiveData<Habit> = MutableLiveData()
    private val habitRepository: HabitRepository = get(HabitRepository::class.java)
    private val habitEventRepository: HabitEventRepository = get(HabitEventRepository::class.java)
    var isUndoEnabled = true

    fun getList(){
        HabitManagerApplication.scope().launch {
            val list: ArrayList<Habit> = habitRepository.getList()
            if (list.isNotEmpty()) {
                liveDataList.postValue(list!!)
            }
        }
    }

    fun delete(habit: Habit) {
        HabitManagerApplication.scope().launch {
            habitRepository.deleteHabit(habit)
            habitEventRepository.deleteEvent(habit)
            isUndoEnabled = true
            deletedHabit.setValue(habit)
        }

    }

    fun undo() {
        HabitManagerApplication.scope().launch {
            habitRepository.undo(deletedHabit.value)
            habitEventRepository.undo(deletedHabit.value!!)
        }

    }
}