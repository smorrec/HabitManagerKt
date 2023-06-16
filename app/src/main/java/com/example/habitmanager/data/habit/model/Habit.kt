package com.example.habitmanager.data.habit.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.google.firebase.database.IgnoreExtraProperties
import java.util.Calendar
import java.util.Objects

@IgnoreExtraProperties
data class Habit(
    var name: String? = null,
    var description: String? = null,
    var startDate: Long? = null,
    var startDateString: String? = null,
    var endDate: Long? = null,
    var endDateString: String? = null,
    var categoryId: Int? = 0,
    var currentDaysCount: Int = 0,
    var completedDaysCount: Int = 0,
    var isFinished: Boolean = false
) : Parcelable, Comparable<Habit?> {

    constructor(
        name: String,
        description: String?,
        startDate: Calendar,
        endDate: Calendar?,
        categoryId: Int
    ) : this() {
        this.name = name
        this.description = description
        this.startDate = startDate.timeInMillis
        this.endDate = endDate?.timeInMillis
        this.categoryId = categoryId
        currentDaysCount = 0
        completedDaysCount = 0
    }

    constructor(habit: Habit) : this() {
        name = habit.name
        description = habit.description
        startDate = habit.startDate
        startDateString = habit.startDateString
        endDate = habit.endDate
        endDateString = habit.endDateString
        categoryId = habit.categoryId
        currentDaysCount = habit.currentDaysCount
        completedDaysCount = habit.completedDaysCount
    }

    fun clone(): Habit {
        return Habit(this)
    }

    protected constructor(`in`: Parcel) : this() {
        name = `in`.readString()!!
        description = `in`.readString()
        currentDaysCount = `in`.readInt()
        completedDaysCount = `in`.readInt()
    }

    override fun compareTo(other: Habit?): Int {
        return name!!.compareTo(other!!.name!!, ignoreCase = true)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val habit = o as Habit
        return name == habit.name
    }

    override fun hashCode(): Int {
        return Objects.hash(
            name,
            description,
            startDate,
            endDate,
            categoryId,
            currentDaysCount,
            completedDaysCount
        )
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(currentDaysCount)
        parcel.writeInt(completedDaysCount)
    }

    fun hasTask(calendarItem: CalendarItem): Boolean {
        return if(endDate != null)
                (startDate!! <= calendarItem.calendar) && ((calendarItem.calendar - endDate!!) < (1000 * 60 * 60 * 24))
        else
            (startDate!! <= calendarItem.calendar)
    }

    fun calculateDaysCount() {
        val startDateC = Calendar.getInstance()
        startDateC.timeInMillis = startDate!!
        startDateC.set(Calendar.HOUR_OF_DAY, 0)
        startDateC.set(Calendar.MINUTE, 0)
        startDateC.set(Calendar.SECOND, 0)

        val lastDay = Calendar.getInstance()
        if(isFinished && endDate != null)
            lastDay.timeInMillis = endDate!!
        lastDay.set(Calendar.HOUR_OF_DAY, 0)
        lastDay.set(Calendar.MINUTE, 0)
        lastDay.set(Calendar.SECOND, 0)

        val daysMillis = lastDay.timeInMillis - startDateC.timeInMillis
        currentDaysCount = (daysMillis / (1000 * 60 * 60 * 24)).toInt()
        if((daysMillis % (1000 * 60 * 60 * 24)) != 0L){
            currentDaysCount++
        }
    }

    fun hasFinished(): Boolean {
        if(endDate == null)
            return false
        val endDateC = Calendar.getInstance()
        endDateC.timeInMillis = endDate!!
        endDateC.set(Calendar.HOUR_OF_DAY, 0)
        endDateC.set(Calendar.MINUTE, 0)
        endDateC.set(Calendar.SECOND, 0)

        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)

        return (today.timeInMillis - endDateC.timeInMillis) > (1000 * 60 * 60 * 24)
    }

    companion object CREATOR : Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }

        const val KEY = "habit"
    }
}