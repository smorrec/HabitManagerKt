package com.example.habitmanager.data.task.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.habit.model.Habit
import java.util.Calendar

@Entity(
    foreignKeys = [ForeignKey(
        entity = Habit::class,
        parentColumns = ["habitName"],
        childColumns = ["habit_Name"],
        onDelete = 5
    )]
)
data class HabitEvent(
    @PrimaryKey(autoGenerate = true)
    var idTask: Int? = null,
    @ColumnInfo(name = "habit_Name")
    var habitName: String? = null,
    @Embedded
    var calendar: CalendarItem? = null,
    var isCompleted: Boolean = false)
{

    constructor(habit: Habit, calendar: CalendarItem?) : this() {
        habitName = habit.name
        this.calendar = calendar
    }

    val isCurrentDay: Boolean
        get() = calendar!!.equals(CalendarItem(Calendar.getInstance()))

    override fun toString(): String {
        return "HabitTask{" +
                "habit=" + habitName +
                ", calendar=" + calendar +
                ", completed=" + isCompleted +
                '}'
    }
}