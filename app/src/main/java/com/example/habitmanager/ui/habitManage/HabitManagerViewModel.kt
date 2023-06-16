package com.example.habitmanager.ui.habitManage

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.data.habit.repository.HabitRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class HabitManagerViewModel : ViewModel() {
    val resultMutableLiveData: MutableLiveData<HabitManagerResult> =
        MutableLiveData<HabitManagerResult>()
    private val habitRepository: HabitRepository = get(HabitRepository::class.java)
    private val habitEventRepository: HabitEventRepository = get(HabitEventRepository::class.java)


    fun validateHabitName(habit: Habit): Boolean {
        var valido = false
        if (TextUtils.isEmpty(habit.name)) {
            resultMutableLiveData.setValue(HabitManagerResult.NAMEEMPTY)
        } else {
            valido = true
        }
        return valido
    }

    fun validateHabitStartDate(habit: Habit): Boolean {
        var valido = false
        if (TextUtils.isEmpty(habit.startDateString)) {
            resultMutableLiveData.setValue(HabitManagerResult.STARTDATEEMPTY)
        } else {
            valido = true
        }
        return valido
    }

    fun addHabit(habit: Habit, category: Int) {
        if (validateHabitName(habit) && validateHabitStartDate(habit)) {
            viewModelScope.launch {
                if (habitRepository.addHabit(habit, category)) {
                    resultMutableLiveData.setValue(HabitManagerResult.SUCCESS)
                } else {
                    resultMutableLiveData.setValue(HabitManagerResult.FAILURE)
                }
            }
        }
    }

    fun editHabit(habit: Habit, category: Int) {
        if (validateHabitName(habit) && validateHabitStartDate(habit)) {
            viewModelScope.launch {
                if (habitRepository.editHabit(habit, category)) {
                    resultMutableLiveData.setValue(HabitManagerResult.SUCCESS_EDIT)
                } else {
                    resultMutableLiveData.setValue(HabitManagerResult.FAILURE)
                }
            }

        }
    }
}