package com.example.habitmanager.data.calendar.model

import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanagerkt.R
import com.google.firebase.database.Exclude
import java.util.Calendar
import java.util.Objects

data class CalendarItem(
    var day: Int = 0,
    var weekDay: String = "",
    var month: String = "",
    var fullName: String = "",
    var calendar: Long = 0L
    ) {

    constructor(calendar: Calendar) : this() {
        this.day = calendar.get(Calendar.DATE)
        this.weekDay = setWeekDayName(calendar)
        this.month = setMonthName(calendar)
        this.fullName = day.toString() + weekDay + month
        this.calendar = calendar.timeInMillis
    }
    @Exclude
    fun isCurrentDay(): Boolean{
        return this == CalendarItem(Calendar.getInstance())
    }

    private fun setWeekDayName(calendar: Calendar): String {
        var name = 0
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
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

    private fun setMonthName(calendar: Calendar): String {
        var name = 0
        when (calendar.get(Calendar.MONTH)) {
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
        return day == that.day && weekDay == that.weekDay && month == that.month
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}