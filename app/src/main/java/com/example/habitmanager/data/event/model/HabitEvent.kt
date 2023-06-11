package com.example.habitmanager.data.event.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.habit.model.Habit
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.Calendar
@IgnoreExtraProperties
data class HabitEvent(
    var idTask: Int? = null,
    var habitName: String? = null,
    var calendar: CalendarItem? = null,
    var isCompleted: Boolean = false)
{

    constructor(habit: Habit, calendar: CalendarItem?) : this() {
        habitName = habit.name
        this.calendar = calendar
    }
    @Exclude
    fun isCurrentDay(): Boolean{
        return calendar!!.equals(CalendarItem(Calendar.getInstance()))
    }

    override fun equals(other: Any?): Boolean {
        return if(other is HabitEvent){
            habitName == other.habitName && calendar == other.calendar
        }else {
            false
        }
    }
    override fun toString(): String {
        return "HabitTask{" +
                "habit=" + habitName +
                ", calendar=" + calendar +
                ", completed=" + isCompleted +
                '}'
    }

    override fun hashCode(): Int {
        var result = idTask ?: 0
        result = 31 * result + (habitName?.hashCode() ?: 0)
        result = 31 * result + (calendar?.hashCode() ?: 0)
        result = 31 * result + isCompleted.hashCode()
        return result
    }
}