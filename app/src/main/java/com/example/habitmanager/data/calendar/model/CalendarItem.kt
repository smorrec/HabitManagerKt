package com.example.habitmanager.data.calendar.model

import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanagerkt.R
import java.util.Calendar
import java.util.Objects

data class CalendarItem(val calendar: Calendar) {
    val day = calendar.get(Calendar.DATE)
    val weekDay = setWeekDayName()
    val month = setMonthName()

    private fun setWeekDayName(): String{
        var name = 0
        when (calendar.get(Calendar.DAY_OF_WEEK)){
            1 -> name = R.string.sunday
            2 -> name = R.string.monday
            3 -> name = R.string.tuesday
            4 -> name = R.string.wednesday
            5 -> name = R.string.thursday
            6 -> name = R.string.friday
            7 -> name = R.string.saturday
        }
        return HabitManagerApplication.applicationContext()
            .resources.getString(name)
    }

    private fun setMonthName(): String{
        var name = 0
        when (calendar.get(Calendar.MONTH)){
            0 -> name = R.string.january
            1 -> name = R.string.february
            2 -> name = R.string.march
            3 -> name = R.string.april
            4 -> name = R.string.may
            5 -> name = R.string.june
            6 -> name = R.string.july
            7 -> name = R.string.august
            8 -> name = R.string.september
            9 -> name = R.string.october
            10 -> name = R.string.november
            11 -> name = R.string.december
        }
        return HabitManagerApplication.applicationContext()
            .resources.getString(name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this.javaClass != other.javaClass) return false
        val that = other as CalendarItem
        return day == that.day && weekDay.equals(that.weekDay) && month.equals(that.month)
    }

    override fun hashCode(): Int {
        return Objects.hash(calendar)
    }
}