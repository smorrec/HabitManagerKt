package com.example.habitmanager.ui.habitList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.data.habit.repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class HabitListViewModel : ViewModel() {
    val liveDataList: MutableLiveData<ArrayList<Habit>> = MutableLiveData()
    val deletedHabit: MutableLiveData<Habit> = MutableLiveData()
    private val habitRepository: HabitRepository = get(HabitRepository::class.java)
    private val habitEventRepository: HabitEventRepository = get(HabitEventRepository::class.java)
    var isUndoEnabled = true

    private val _emptyList = MutableStateFlow(false)
    val emptyList = _emptyList.asStateFlow()

    fun getList(){
        viewModelScope.launch {
            val list: ArrayList<Habit> = habitRepository.getList()
            synchronized(list){
                val listToShow = ArrayList<Habit>()
                if (list.isNotEmpty()) {
                    for (habit in list) {
                        if (habit.hasFinished()) {
                            habit.isFinished = true
                            habitRepository.editHabit(habit, habit.categoryId!!)
                        }
                        if (!habit.isFinished)
                            listToShow.add(habit)

                    }
                    if(listToShow.isEmpty())
                        _emptyList.update { true }
                    else
                        _emptyList.update { false }

                    liveDataList.postValue(listToShow)
                }
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
        }

    }

    fun edit(item: Habit) {
        habitRepository.editHabit(item, item.categoryId!!)
    }
}